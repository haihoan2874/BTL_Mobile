package com.example.signuplogin_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.signuplogin_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText dk_email, dk_password;
    private Button dk_button;
    private TextView dn_redirect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        dk_email=findViewById(R.id.dk_email);
        dk_password=findViewById(R.id.dk_password);
        dk_button=findViewById(R.id.dk_button);
        dn_redirect=findViewById(R.id.dn_redirect);

        dk_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=dk_email.getText().toString().trim();
                String pass=dk_password.getText().toString().trim();

                if(user.isEmpty()){
                    dk_email.setError("Email không được bỏ trống");
                }
                if(pass.isEmpty()){
                    dk_password.setError("Password không được bổ trống");
                }else{
                    auth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            }else{
                                Toast.makeText(SignUpActivity.this, "Đăng ký không thành công" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        dn_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });
    }
}