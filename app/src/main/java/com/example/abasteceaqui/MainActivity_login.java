package com.example.abasteceaqui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity_login extends AppCompatActivity {

    String[] mensagens = {"Preencha todos os campos!", "Usuário não cadastrado!"};
    boolean salvarLogin = false; // Variável de controle para salvar o login

    private EditText edt_mail, edt_senha;
    private Button btn_login, btn_cadastro;
    private ImageView ver_senha;
    private CheckBox check_login;
    private ProgressBar LoginProgressBar;

    private FirebaseAuth mAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_login);

        IniciarComponentes();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginmail = edt_mail.getText().toString();
                String loginsenha = edt_senha.getText().toString();

                if (loginmail.isEmpty() || loginsenha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                }else {
                    AutenticarUsuario(v, loginmail, loginsenha);

                }

            }
        });


        btn_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity_login.this, MainActivity_cadastro.class );
                startActivity(intent);
            }
        });

        ver_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_senha.getTransformationMethod() instanceof PasswordTransformationMethod){
                    edt_senha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edt_senha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }

        });


    }


    private void AutenticarUsuario(View v, String loginmail, String loginsenha) {
        if(!TextUtils.isEmpty(loginmail) || !TextUtils.isEmpty(loginsenha)){

            LoginProgressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(loginmail, loginsenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){
                        LoginProgressBar.setVisibility(View.VISIBLE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                abrirTelaPrincipal();
                            }
                        }, 1500);

                    }else {
                        Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                        LoginProgressBar.setVisibility(View.INVISIBLE);

                    }

                }
            });

        }
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(MainActivity_login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void IniciarComponentes(){
        mAuth = FirebaseAuth.getInstance();
        edt_mail = findViewById(R.id.edit_mail);
        edt_senha = findViewById(R.id.edit_pass);
        btn_login = findViewById(R.id.id_button_login);
        ver_senha = findViewById(R.id.id_ver_senha);
        check_login = findViewById(R.id.id_checkbox);
        LoginProgressBar = findViewById(R.id.id_bar);
        btn_cadastro = findViewById(R.id.id_button_cadastro);
    }

}