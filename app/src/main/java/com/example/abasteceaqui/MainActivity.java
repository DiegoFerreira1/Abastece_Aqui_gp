package com.example.abasteceaqui;
import android.location.Geocoder;
import android.location.Location;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapAdapter;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private Button bt_perto_mim, bt_busca;
    private MyLocationNewOverlay myLocationOverlay;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    FusedLocationProviderClient client;
    private FirebaseAuth mAuth;
    private ImageView ic_sair;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        IniciarComponentes();

        // Inicializar o mapa
        Configuration.getInstance().load(getApplicationContext(), getPreferences(MODE_PRIVATE));
        mapView = findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Configurar o MyLocationNewOverlay para mostrar a localização atual
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        myLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(myLocationOverlay);

        // Configurar o botão perto de mim
        bt_perto_mim = findViewById(R.id.id_bt_perto);
        bt_perto_mim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar permissão de localização
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                        Toast.makeText(MainActivity.this, "Localização não encontrada.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Se a permissão não foi concedida, solicitar permissão
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                }
            }
        });


        bt_busca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lidar com o clique no botão de busca
                buscarCidade();
            }
        });


        ic_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, MainActivity_login.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private void IniciarComponentes(){
        mAuth = FirebaseAuth.getInstance();
        ic_sair = findViewById(R.id.id_ic_sair);
        ImageView ic_postos = findViewById(R.id.id_ic_postos);
        ImageView ic_perfil = findViewById(R.id.id_ic_perfil);
        bt_busca = findViewById(R.id.id_bt_busca);
        EditText busca_cidade = findViewById(R.id.editTextCidade);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão de localização concedida, clique no botão novamente para obter a localização atual
                bt_perto_mim.performClick();
            } else {
                Toast.makeText(this, "Permissão de localização negada.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void buscarCidade() {
        // Obtenha o EditText onde o usuário digitou o nome da cidade
        EditText editTextCidade = findViewById(R.id.editTextCidade);


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

                // Adicione um marcador no mapa com as coordenadas da cidade
                adicionarMarcadorNoMapa(latitude, longitude);
            } else {
                // Trate o caso em que a cidade não foi encontrada
                Toast.makeText(this, "Cidade não encontrada", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adicionarMarcadorNoMapa(double latitude, double longitude) {
        // Configure o mapa (substitua "mapView" pelo seu objeto MapView)
        MapView mapView = findViewById(R.id.mapView);
        IMapController mapController = mapView.getController();
        mapController.setCenter(new GeoPoint(latitude, longitude));
        mapController.setZoom(10.0); // Ajuste o nível de zoom conforme necessário

        // Adicione um marcador com as coordenadas da cidade
        Marker cityMarker = new Marker(mapView);
        cityMarker.setPosition(new GeoPoint(latitude, longitude));
        mapView.getOverlays().add(cityMarker);

        // Atualize o mapa para exibir o marcador
        mapView.invalidate();
    }


    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, MainActivity_login.class);
        }

    }





}