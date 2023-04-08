package com.plk.eznotes;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class noteCreate extends AppCompatActivity {
    EditText title, content;
    FloatingActionButton save;
    String Title, Content;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_create);
        save=findViewById(R.id.save);
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Title = title.getText().toString().trim();
                Content = content.getText().toString().trim();
                if(Title.isEmpty()||Content.isEmpty()){
                    Toast.makeText(noteCreate.this, "Enter Both Fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    DocumentReference dr = firestore.collection("NotesDB").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String, Object> note = new HashMap<>();
                    note.put("title",Title);
                    note.put("content",Content);
                    dr.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(noteCreate.this, "Created Successfully", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(noteCreate.this, "Failed Exception"+e , Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }


}