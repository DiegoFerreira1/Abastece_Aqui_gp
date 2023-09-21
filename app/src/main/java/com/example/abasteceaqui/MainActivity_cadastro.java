package com.example.abasteceaqui;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abasteceaqui.tools.FieldValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity_cadastro extends AppCompatActivity {

    private EditText edit_nome, edit_email, edit_senha, edit_senha2, edit_endereco;
    private Button bt_cadastrar;

    private ImageView ver_senha_cad;

    private TextView text_buscar_cep;

    private FirebaseAuth mAuth;
    String[] mensagens = {"Preencha todos os campos", "Cadastro Realizado com Sucesso"};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cadastro);

        IniciarComponentes();

        bt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = edit_nome.getText().toString();
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();
                String senha2 = edit_senha2.getText().toString();
                String endereco = edit_endereco.getText().toString();


                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || senha2.isEmpty() || endereco.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    CadastrarUsuario(v);
                }

            }
        });
        ver_senha_cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_senha.getTransformationMethod() instanceof PasswordTransformationMethod){
                    edit_senha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edit_senha2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edit_senha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edit_senha2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });



    }

    private void IniciarComponentes() {

        FirebaseAuth.getInstance();

        edit_nome = findViewById(R.id.edit_nome_cad);
        edit_email = findViewById(R.id.edit_mail_cad);
        edit_senha = findViewById(R.id.edit_pass_cad);
        edit_senha2 = findViewById(R.id.edit_pass_cad2);
        edit_endereco = findViewById(R.id.edit_address_cad);
        bt_cadastrar = findViewById(R.id.id_button_cad);
        text_buscar_cep = findViewById(R.id.id_text_busca_cep);
        ver_senha_cad = findViewById(R.id.id_ver_senha_cad);
    }

    private void CadastrarUsuario(View v) {

        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();
        String senha2 = edit_senha2.getText().toString();

        if(senha.equals(senha2)){


                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    snackbar.dismiss();
                                    Intent intent = new Intent(MainActivity_cadastro.this, MainActivity_login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 2000); // Atraso de 2000 milissegundos (2 segundos)
                        }

                        else {

                            String erro;
                            try {
                                if (!FieldValidator.validate(senha, FieldValidator.TYPE_PASSWORD)) {
                                    throw new Exception("A senha deve conter pelo menos um carácter minúsculo, um maiúsculo, um número e um carácter especial.");
                                }

                                // Tente criar o usuário ou alterar a senha no Firebase Auth aqui
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthUserCollisionException e) {
                                erro = "Email ja cadastrado.";
                                Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "Email inválido.";
                                Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.show();
                            } catch (Exception e) {
                                erro = "A senha deve conter pelo menos um carácter minúsculo, um maiúsculo, um número e um carácter especial!";
                                Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.show();
                            }
                        }

                    }
                }); // Esta é a chave de fechamento correta para o método addOnCompleteListener



            }else {
            String erro = "As senhas não conferem!";
            Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        }


    } // Esta é a chave de fechamento correta para o método CadastrarUsuarios

    public void SalvarDadosUsuario(){
        String nome = edit_nome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usuarios = new HashMap<>();

        usuarios.put("nome", nome);

        usuarioID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);

        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Log.d("db", "Sucesso ao salvar os dados");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("db_error","Erro ao salvar os dados" + e.toString());

                    }
                });


    }

}

