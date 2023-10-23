package com.example.abasteceaqui;

import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abasteceaqui.tools.SalvarNoBancoDeDadosTask;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.GeoPoint;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MainActivity_posto extends AppCompatActivity {

    private MapView mapView;
    private Button bt_concluir;
    private EditText nome_posto, endereco_posto, v_gasolina, v_alcool, v_diesel;


    private org.osmdroid.util.GeoPoint currentLocationPoint;

    String[] mensagens = {"Inserir nome e endereço", "Cadastro Realizado com Sucesso"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_posto);

        IniciarComponentes();


        bt_concluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePosto = nome_posto.getText().toString();
                String enderecoPosto = endereco_posto.getText().toString();
                Double preco_gasolina, preco_alcool, preco_diesel;

                // Recupere as coordenadas do Intent
                double latitude = getIntent().getDoubleExtra("latitude", 0.0);
                double longitude = getIntent().getDoubleExtra("longitude", 0.0);

                if (nomePosto.isEmpty() || enderecoPosto.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    // Execute as operações de rede em uma AsyncTask
                    SalvarNoBancoDeDadosTask task = new SalvarNoBancoDeDadosTask(nomePosto, enderecoPosto, latitude, longitude);
                    task.execute();
                    Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            snackbar.dismiss();
                            Intent intent = new Intent(MainActivity_posto.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 1500); // Atraso de 1500 milissegundos (1.5 segundos)
                }

            }
        });

    }


    private void IniciarComponentes(){
        nome_posto = findViewById(R.id.edit_nome_posto);
        endereco_posto = findViewById(R.id.edit_address_posto);
        v_gasolina = findViewById(R.id.edit_gasolina);
        v_alcool = findViewById(R.id.edit_alcool);
        v_diesel = findViewById(R.id.edit_diesel);
        bt_concluir = findViewById(R.id.id_button_posto);

    }




}