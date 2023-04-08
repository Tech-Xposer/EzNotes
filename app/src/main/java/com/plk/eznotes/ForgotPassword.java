package com.plk.eznotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassword extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    EditText pswd,cpswd;
    ImageView reset;
    String nPassword,cPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        pswd=findViewById(R.id.npswd);
        cpswd=findViewById(R.id.cpswd);
        reset=findViewById(R.id.resetpswd);
        firebaseAuth=FirebaseAuth.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nPassword=pswd.getText().toString().trim();
                cPassword=cpswd.getText().toString().trim();
                if(nPassword.length()>8){
                    if(nPassword.equals(cPassword)){
                        firebaseUser=firebaseAuth.getCurrentUser();
                        firebaseUser.updatePassword(nPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this, "Password Reset Successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(ForgotPassword.this, dashboard.class));
                                }else{
                                    Toast.makeText(ForgotPassword.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(ForgotPassword.this, "Password not match", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ForgotPassword.this, "Password must be of 8 chars", Toast.LENGTH_SHORT).show();
                }
                
                
            }
        });
        
    }

    @Override
    public void onBackPressed() {
        finish();
//        startActivity(new Intent(ForgotPassword.this, MainActivity.class));

    }
}