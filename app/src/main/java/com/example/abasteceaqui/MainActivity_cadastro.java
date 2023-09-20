package com.example.abasteceaqui;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    }

    private void IniciarComponentes() {

        edit_nome = findViewById(R.id.edit_nome_cad);
        edit_email = findViewById(R.id.edit_mail_cad);
        edit_senha = findViewById(R.id.edit_pass_cad);
        edit_senha2 = findViewById(R.id.edit_pass_cad2);
        edit_endereco = findViewById(R.id.edit_address_cad);
        bt_cadastrar = findViewById(R.id.id_button_cad);
    }

    private void CadastrarUsuario(View v) {

        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();
        String senha2 = edit_senha2.getText().toString();

        if(senha.equals(senha2)){

            if (FieldValidator.validate(senha, FieldValidator.TYPE_PASSWORD)) {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        } else {
                            String erro;
                            class WeakPasswordException extends Exception {
                                public WeakPasswordException(String message) {
                                    super(message);
                                }
                            }

                            try {

                                // Tente criar o usuário ou alterar a senha no Firebase Auth aqui
                                throw Objects.requireNonNull(task.getException());

                            } catch (FirebaseAuthUserCollisionException e) {
                                erro = "Email ja cadastrado.";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "Email inválido.";
                            } catch (Exception e) {
                                erro = "Erro ao cadastrar o usuário!";

                                Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.show();
                            }
                        }
                    }
                }); // Esta é a chave de fechamento correta para o método addOnCompleteListener



            }else {
                String erro = "A senha deve conter pelo menos um carácter minúsculo, um maiúsculo, um número e um carácter especial.";
                Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }

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

