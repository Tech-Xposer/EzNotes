package com.plk.eznotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
EditText uname, pass;
String passWord, userName;
TextView signup,forgotpswd;
private FirebaseAuth firebaseAuth;
private  FirebaseUser firebaseUser;

ImageView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uname= findViewById(R.id.uid);
        pass=findViewById(R.id.password);
        login = findViewById(R.id.register);
        signup = findViewById(R.id.signup);
        forgotpswd=findViewById(R.id.resetpswd);
        firebaseAuth  = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() !=null){
            finish();
            startActivity(new Intent(MainActivity.this, dashboard.class));
        }
        login.setOnClickListener(view -> {
            userName = uname.getText().toString().trim();
            passWord = pass.getText().toString().trim();
            if(userName.isEmpty() || passWord.isEmpty()){
                Toast.makeText(MainActivity.this, "Please Enter Credentials", Toast.LENGTH_SHORT).show();
            }
            else if(userName.length()<6){
                Toast.makeText(MainActivity.this, "Username should be valid", Toast.LENGTH_SHORT).show();
            }
            else if(passWord.length()<8){
                Toast.makeText(MainActivity.this, "Password should be 8 characters long", Toast.LENGTH_SHORT).show();
            }
            else {
                firebaseAuth.signInWithEmailAndPassword(userName, passWord).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        isUserVerified();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });
        forgotpswd.setOnClickListener(view -> {
            userName = uname.getText().toString().trim();
            if(userName.isEmpty()){
                Toast.makeText(MainActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            }else{
                firebaseAuth.sendPasswordResetEmail(userName).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Reset link sent successfully ", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        signup.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, register.class)));
    }

    private void isUserVerified() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if(Objects.requireNonNull(firebaseUser).isEmailVerified()){
            Toast.makeText(MainActivity.this, "Logged In Successfully!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, dashboard.class ));
        }
        else {
            Toast.makeText(MainActivity.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do uhh wanna Exit?");
        builder.setTitle("Alert");
        builder.setCancelable(false);
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> dialog.cancel());
        builder.setPositiveButton("Yes", (dialog, which) -> MainActivity.this.finish());
       AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
