package com.example.abasteceaqui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class MainActivity_navigation extends AppCompatActivity {

    WebView webView;
    String url;

    @SuppressLint({"SetJavaScriptEnabled", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);

        webView = findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            double originLatitude = intent.getDoubleExtra("originLatitude", 0.0);
            double originLongitude = intent.getDoubleExtra("originLongitude", 0.0);

            double destinationLatitude = intent.getDoubleExtra("destinationLatitude", 0.0);
            double destinationLongitude = intent.getDoubleExtra("destinationLongitude", 0.0);

            Log.d("origem_lat", String.valueOf(originLatitude));
            Log.d("origem_long", String.valueOf(originLatitude));

            Log.d("destino_lat", String.valueOf(destinationLatitude));
            Log.d("destino_long", String.valueOf(destinationLatitude));


            // Construir a URL da WebView com as coordenadas recebidas
            url = "https://www.google.com/maps/dir/" + originLatitude + "," + originLongitude + "/" + destinationLatitude + "," + destinationLongitude;

            // Carregar a WebView com a URL atualizada
            webView.loadUrl(url);
        }
    }
}