package com.plk.eznotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class noteUpdate extends AppCompatActivity {
    Intent noteData;
    EditText noteTitle,noteContent;
    FloatingActionButton save;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String Title,Content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_update);
        noteData=getIntent();
        firebaseAuth=FirebaseAuth.getInstance();
        noteTitle= findViewById(R.id.title);
        noteContent = findViewById(R.id.content);
        save=findViewById(R.id.save);
        String noteId=(noteData.getStringExtra("noteId"));
        Title=(noteData.getStringExtra("noteTitle"));
        Content=(noteData.getStringExtra("noteContent"));
        firestore=FirebaseFirestore.getInstance();
        noteTitle.setText(Title);
        noteContent.setText(Content);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Title.isEmpty() || !Content.isEmpty()){

                    DocumentReference dr = firestore.collection("NotesDB").document(firebaseAuth.getCurrentUser().getUid()).collection("myNotes").document(noteId);
                    Map<String, Object> note = new HashMap<>();
                    note.put("title",noteTitle.getText().toString());
                    note.put("content",noteContent.getText().toString());
                    dr.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(noteUpdate.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(noteUpdate.this, "Failed Exception"+e , Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(noteUpdate.this, "Enter Details!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}