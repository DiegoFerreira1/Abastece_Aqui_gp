package com.example.abasteceaqui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.abasteceaqui.tools.JSONResourceReader;
import com.example.abasteceaqui.tools.PostoInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity_Map extends AppCompatActivity {

    private MapView mapView;
    private MyLocationNewOverlay myLocationOverlay;
    private ImageButton bt_busca_cidade;
    private ImageView bt_exit;
    private Button bt_go, bt_favorite;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FirebaseAuth mAuth;
    private TextView gasolina_comum, gasolina_aditivada, etanol, diesel_comum, diesel_s10, gas_natural, nome_posto;

    private JSONArray postosArray;


    FloatingActionButton floatingActionButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_map);

        iniciarComponentes();
        carregarDadosEAdicionarMarcadores();

        //Botao de buscar cidade
        bt_busca_cidade = findViewById(R.id.imageButton);
        bt_busca_cidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarCidade();
            }
        });

        //Botao de sair
        mAuth = FirebaseAuth.getInstance();
        bt_exit = findViewById(R.id.id_sair);
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity_Map.this, MainActivity_login.class);
                startActivity(intent);
                finish();
            }
        });


        // Inicializar o mapa
        Configuration.getInstance().load(getApplicationContext(), getPreferences(MODE_PRIVATE));
        mapView = findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Configurar o MyLocationNewOverlay para mostrar a localização atual
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        myLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(myLocationOverlay);


        //Botão de localização
        floatingActionButton = findViewById(R.id.id_focus_location);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar permissão de localização
                if (ContextCompat.checkSelfPermission(MainActivity_Map.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Se a permissão foi concedida, obter a localização atual
                    Location location = myLocationOverlay.getLastFix();
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        // Criar um GeoPoint com a localização atual
                        GeoPoint currentLocation = new GeoPoint(latitude, longitude);


                        // Centralizar o mapa na localização atual
                        IMapController mapController = mapView.getController();
                        mapController.setCenter(currentLocation);
                        mapController.setZoom(18.0); // Ajuste o nível de zoom conforme necessário
                    } else {
                        Toast.makeText(MainActivity_Map.this, "Localização não encontrada.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Se a permissão não foi concedida, solicitar permissão
                    ActivityCompat.requestPermissions(MainActivity_Map.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                }

            }
        });

        // Suponha que você já adicionou os marcadores usando o método adicionarMarcadorNoMapa

        for (Overlay overlay : mapView.getOverlays()) {
            if (overlay instanceof Marker) {
                final Marker marker = (Marker) overlay;

                // Evento para exibir informações do posto
                marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                    // Capture as informações do marcador
                    @Override
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        marker.showInfoWindow();

                        // Capture as informações do marcador
                        String title = marker.getTitle();
                        nome_posto.setText(title);

                        PostoInfo postoInfo = (PostoInfo) marker.getRelatedObject();

                        if (postoInfo != null) {

                            if (postoInfo.getNomeFantasia().equals(title)) {
                                // O nome do posto associado ao marcador é igual ao nome obtido do título do marcador
                                // Agora você pode acessar as informações do posto e atualizar os campos de texto
                                String precoGasolinaComum = postoInfo.getPrecoGasolinaComum();
                                String precoGasolinaAditivada = postoInfo.getPrecoGasolinaAditivada();
                                String precoEtanol = postoInfo.getPrecoEtanol();
                                String precoDieselComum = postoInfo.getPrecoDieselComum();
                                String precoDieselS10 = postoInfo.getPrecoDieselS10();
                                String precoGasNatural = postoInfo.getPrecoGasNatural();

                                // Atualize os campos de texto com as informações do posto
                                gasolina_comum.setText(precoGasolinaComum);
                                gasolina_aditivada.setText(precoGasolinaAditivada);
                                etanol.setText(precoEtanol);
                                diesel_comum.setText(precoDieselComum);
                                diesel_s10.setText(precoDieselS10);
                                gas_natural.setText(precoGasNatural);
                            }
                        }


                        //Botao para iniciar navegação
                        bt_go = findViewById(R.id.button_go);
                        // Defina o OnClickListener para o botão "Go" apos pegar pontos de origem e destino
                        bt_go.setOnClickListener(new View.OnClickListener() {
                            final double markerLatitude = marker.getPosition().getLatitude();
                            final double markerLongitude = marker.getPosition().getLongitude();

                            @Override
                            public void onClick(View v) {
                                // Quando o botão "Go" é pressionado, crie um Intent para abrir a MainActivity_navigation
                                Intent intent = new Intent(MainActivity_Map.this, MainActivity_navigation.class);
                                // Adicione as coordenadas de origem (sua localização) e destino (marcador) como extras no Intent

                                intent.putExtra("destinationLatitude", markerLatitude);
                                intent.putExtra("destinationLongitude", markerLongitude);

                                Log.d("pontoLat", String.valueOf(markerLatitude));
                                Log.d("pontoLong", String.valueOf(markerLongitude));


                                Location location = myLocationOverlay.getLastFix();
                                if (location != null) {
                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();

                                    // Adicione as coordenadas de origem (latitude e longitude) como extras no Intent
                                    intent.putExtra("originLatitude", latitude);
                                    intent.putExtra("originLongitude", longitude);

                                }
                                // Inicie a MainActivity_navigation para exibir a WebView para navegação
                                startActivity(intent);
                            }

                        });

                        return true; // Retorna true para indicar que o evento foi tratado
                    }
                });
            }

        }//Fim do clique no marcador


    }//Fim do metodo OnCreate

    // Permissão para usar localização
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão de localização concedida, clique no botão novamente para obter a localização atual
                floatingActionButton.performClick();
            } else {
                Toast.makeText(this, "Permissão de localização negada.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Metodo de busca da cidade
    private void buscarCidade() {
        // Obtenha o EditText onde o usuário digitou o nome da cidade
        EditText editTextCidade = findViewById(R.id.edit_buscaCidade);


        // Obtenha o nome da cidade digitado pelo usuário
        String nomeCidade = editTextCidade.getText().toString();

        // Verifique se o campo de texto não está vazio
        if (!TextUtils.isEmpty(nomeCidade)) {

            editTextCidade.setText("");
        } else {
            // Caso o campo de texto esteja vazio, exiba uma mensagem para o usuário
            Toast.makeText(this, "Digite o nome da cidade", Toast.LENGTH_SHORT).show();
        }

        // Exemplo (usando o Google Geocoding API):
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(nomeCidade, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address cidadeEncontrada = addresses.get(0);
                double latitude = cidadeEncontrada.getLatitude();
                double longitude = cidadeEncontrada.getLongitude();
                String nome = cidadeEncontrada.getFeatureName();

                // Adicione um marcador no mapa com as coordenadas da cidade
                adicionarMarcadorNoMapa(latitude, longitude, nome,null);
            } else {
                // Trate o caso em que a cidade não foi encontrada
                Toast.makeText(this, "Cidade não encontrada", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo para ser chamado no metodo de buscar cidade
    private void adicionarMarcadorNoMapa(double latitude, double longitude, String nomeLocal, PostoInfo postoInfo) {
        // Configure o mapa (substitua "mapView" pelo seu objeto MapView)
        MapView mapView = findViewById(R.id.mapView);
        IMapController mapController = mapView.getController();
        mapController.setCenter(new GeoPoint(latitude, longitude));
        mapController.setZoom(10.0); // Ajuste o nível de zoom conforme necessário

        // Adicione um marcador com as coordenadas da cidade
        Marker localMarker = new Marker(mapView);
        localMarker.setPosition(new GeoPoint(latitude, longitude));
        localMarker.setTitle(nomeLocal);

        // Associe o objeto PostoInfo ao marcador
        localMarker.setRelatedObject(postoInfo);

        mapView.getOverlays().add(localMarker);

        // Atualize o mapa para exibir o marcador
        mapView.invalidate();
    }


    //Metodo carregar os marcadores
    private void carregarDadosEAdicionarMarcadores() {
        try {
            String json = JSONResourceReader.readJSONResource(this, R.raw.fuelstations);
            JSONArray postosArray = new JSONArray(json);

            for (int i = 0; i < postosArray.length(); i++) {
                JSONObject posto = postosArray.getJSONObject(i);
                double latitude = posto.getDouble("latitude");
                double longitude = posto.getDouble("longitude");
                String nomeFantasia = posto.getString("nome fantasia");
                String precoGasolinaComum = posto.getString("Gasolina Comum");
                String precoGasolinaAditivada = posto.getString("Gasolina Aditivada");
                String precoEtanol = posto.getString("Etanol");
                String precoDieselComum = posto.getString("Diesel Comum");
                String precoDieselS10 = posto.getString("Diesel - s10");
                String precoGasNatural = posto.getString("gas natural");

                // Crie um objeto PostoInfo com as informações do posto
                PostoInfo postoInfo = new PostoInfo(nomeFantasia, precoGasolinaComum, precoGasolinaAditivada, precoEtanol,
                        precoDieselComum, precoDieselS10, precoGasNatural);

                // Adicione um marcador no mapa com as coordenadas do posto e associe as informações
                adicionarMarcadorNoMapa(latitude, longitude, nomeFantasia, postoInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void iniciarComponentes() {
        gasolina_comum = findViewById(R.id.id_valor_comum);
        gasolina_aditivada = findViewById(R.id.id_valor_adt);
        diesel_comum = findViewById(R.id.id_valor_diesel_comum);
        diesel_s10 = findViewById(R.id.id_valor_diesel_s10);
        etanol = findViewById(R.id.id_valor_etanol);
        gas_natural = findViewById(R.id.id_valor_gasNatural);
        nome_posto = findViewById(R.id.id_nome_posto);

    }
}


