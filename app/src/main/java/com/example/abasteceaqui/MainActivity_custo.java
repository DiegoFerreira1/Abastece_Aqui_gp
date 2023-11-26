package com.example.abasteceaqui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity_custo extends AppCompatActivity {

    private EditText editPrecoAlcool;
    private EditText editprecoGasolina;
    private TextView textResultado, textVoltar;
    private Button bt_resultado;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_custo);

        editPrecoAlcool = findViewById(R.id.id_alcool);
        editprecoGasolina = findViewById(R.id.id_gasolina);
        textResultado = findViewById(R.id.id_resultado);
        textVoltar = findViewById(R.id.id_voltar);

        bt_resultado = findViewById(R.id.id_bt_calcular);
        bt_resultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularpreco(v);
            }
        });

        textVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity_custo.this, MainActivity_Map.class );
                startActivity(intent);
            }
        });

    }
    public void calcularpreco(View view){

        Double precoAlcool =  Double.parseDouble(editPrecoAlcool.getText().toString());
        Double precoGasolina = Double.parseDouble(editprecoGasolina.getText().toString());

        /* Calculo: (precoAlcool / precoGasolina)
         *  Se o resultado for >= ou igual a 0.7 - melhor utilizar gasolina
         *  Se nao, é melhor utilizar o alcool. */

        Double resultado = precoAlcool/precoGasolina;

        if(resultado >= 0.7){

            textResultado.setText("Melhor utilizar Gasolina!");

        }else{
            textResultado.setText("Melhor utilizar Álcool");
        }

    }
}