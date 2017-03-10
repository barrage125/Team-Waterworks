package team64.waterworks.controllers;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import com.google.android.gms.maps.model.Marker;

import team64.waterworks.R;
import team64.waterworks.models.ReportsManager;

public class HomeMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * getreportById(ParseDouble(report.get(i).split(" ", 0)).getLatitude()
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(this);

        ArrayList<String> waterReports = ReportsManager.viewAllReports();

        if (waterReports != null) {
            for (int i = 0; i < waterReports.size(); i++) {
                String[] reportsData = waterReports.get(i).split(" ", 0);
                String idString = reportsData[0].replace("(", "").replace(")","");
                Long idLong = Long.parseLong(idString);
                Double lng = ReportsManager.getReportByID(idLong).getLongitude();
                Double lat = ReportsManager.getReportByID(idLong).getLatitude();

                Marker temp = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                        .title("Water Report: " + ReportsManager.getReportByID(idLong).getId())
                        .snippet("Longitude: " + lng + "\nLatitude: " + lat + "\nAuthor: "
                                + ReportsManager.getReportByID(idLong).getAuthor()
                                + "\nType: " + ReportsManager.getReportByID(idLong).getType()));
            }
        }


        // Add a marker and move camera
        LatLng location = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(location).title("WaterReport").snippet("Sydney, Australia"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }
}
