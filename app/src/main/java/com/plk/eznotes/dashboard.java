package com.plk.eznotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class dashboard extends AppCompatActivity {
    FloatingActionButton newNote;
    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirestoreRecyclerAdapter<ModelClass, NoteViewHolder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        newNote = findViewById(R.id.newNote);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        newNote.setOnClickListener(view -> startActivity(new Intent(dashboard.this, noteCreate.class)));
        Query query = firestore.collection("NotesDB").document(firebaseUser.getUid()).collection("myNotes").orderBy("title",Query.Direction.DESCENDING); firestore.collection("SchemesDB").document("schemesDB").collection("schemes").document();
        FirestoreRecyclerOptions<ModelClass> allNotes =
                new FirestoreRecyclerOptions.Builder<ModelClass>()
                        .setQuery(query, ModelClass.class)
                        .build();
        noteAdapter = new FirestoreRecyclerAdapter<ModelClass, NoteViewHolder>(allNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull ModelClass model) {
                holder.noteTitle.setText(model.getTitle());
                holder.noteContent.setText(model.getContent());
                holder.itemView.setOnClickListener(view -> {
                    String noteId1 =noteAdapter.getSnapshots().getSnapshot(position).getId();
                    DocumentReference dr = firestore.collection("NotesDB").document(firebaseUser.getUid()).collection("myNotes").document(noteId1);
                    Toast.makeText(dashboard.this, ""+ noteId1, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(dashboard.this);
                    builder.setMessage("What you wanna do?");
                    builder.setTitle("Alert");
                    builder.setCancelable(false);

                    builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> dialog.cancel());
                    builder.setPositiveButton("Edit",(dialog,which) ->{
                        Intent intent = new Intent(dashboard.this,noteUpdate.class);
                        intent.putExtra("noteId",noteId1);
                        intent.putExtra("noteTitle",model.getTitle());
                        intent.putExtra("noteContent",model.getContent());
                       startActivity(intent);
                    });

//                    builder.setPositiveButton("Delete", (dialog, which) -> {
//                        dr.delete().addOnSuccessListener(unused -> Toast.makeText(view.getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(view.getContext(), "Failed to Delete", Toast.LENGTH_SHORT).show());
//                        dialog.dismiss();
//                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                });
                holder.itemView.setOnLongClickListener(view -> {
                    String noteId12 =noteAdapter.getSnapshots().getSnapshot(position).getId();
                    DocumentReference dr = firestore.collection("NotesDB").document(firebaseUser.getUid()).collection("myNotes").document(noteId12);

                    AlertDialog.Builder builder = new AlertDialog.Builder(dashboard.this);
                    builder.setMessage("Do uhh wanna Delete?");
                    builder.setTitle("Alert");
                    builder.setCancelable(false);
                    builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> dialog.cancel());
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        dr.delete().addOnSuccessListener(unused -> Toast.makeText(view.getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(view.getContext(), "Failed to Delete", Toast.LENGTH_SHORT).show());
                        dialog.dismiss();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                    return true;
                });
            }
            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notes,parent, false );
                return new NoteViewHolder(view);
            }
        };
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter( noteAdapter);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder{
        private final TextView noteTitle;
        private final TextView noteContent;
        LinearLayout myNote;

        public NoteViewHolder(@NonNull View itemview){
            super(itemview);
            noteTitle = itemview.findViewById(R.id.title);
            noteContent = itemview.findViewById(R.id.content);
            myNote = itemview.findViewById(R.id.note);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Logout");
        menu.add("Change Password");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Logout")){
            firebaseAuth.signOut();
            finish();
            Toast.makeText(this, "Signout Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(dashboard.this,MainActivity.class));
        }
        else if(item.getTitle().equals("Change Password")){
            startActivity(new Intent(dashboard.this,ForgotPassword.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.startListening();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(dashboard.this);
        builder.setMessage("Do uhh wanna Exit?");
        builder.setTitle("Alert");
        builder.setCancelable(false);
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> dialog.cancel());
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> finish());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}