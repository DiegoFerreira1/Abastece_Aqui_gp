package com.example.abasteceaqui;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity_Cep extends AppCompatActivity {

    private EditText edt_buscar_cep;

    private Button btn_inserir_endereco;

    private TextView ruaTextView, bairroTextView, cidadeTextView, estadoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cep);

        ruaTextView = findViewById(R.id.id_text_rua2);
        bairroTextView = findViewById(R.id.id_text_bairro2);
        cidadeTextView = findViewById(R.id.id_text_cidade2);
        estadoTextView = findViewById(R.id.id_text_estado2);
        btn_inserir_endereco = findViewById(R.id.id_button_insere_endereco);

        edt_buscar_cep = findViewById(R.id.edit_busca_cep);
        Button bt_buscar_cep = findViewById(R.id.id_button_cep);

        bt_buscar_cep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cep = edt_buscar_cep.getText().toString();
                String resultado = MainActivity_Cep.buscarCep(cep);

                if (resultado != null) {
                    try {

                        JSONObject jsonObject = new JSONObject(resultado);

                        String ruaStr = jsonObject.getString("logradouro");
                        String bairroStr = jsonObject.getString("bairro");
                        String cidadeStr = jsonObject.getString("localidade");
                        String estadoStr = jsonObject.getString("uf");


                        ruaTextView.setText(ruaStr);
                        bairroTextView.setText(bairroStr);
                        cidadeTextView.setText(cidadeStr);
                        estadoTextView.setText(estadoStr);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity_Cep.this, "Erro ao analisar os dados do CEP", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity_Cep.this, "Erro ao buscar CEP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_inserir_endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Codigo para verificar se o cep gerou o endereço
                //Então o botao deve inserir a strind da rua no campo rua do cadastro

                Intent intent = new Intent(MainActivity_Cep.this, MainActivity_cadastro.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public static String buscarCep(String cep) {
        try {
            String urlStr = "https://viacep.com.br/ws/" + cep + "/json/";
            URL url = new URL(urlStr);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}