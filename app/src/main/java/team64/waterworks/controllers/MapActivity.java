package team64.waterworks.controllers;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import com.google.android.gms.maps.model.Marker;

import team64.waterworks.R;
import team64.waterworks.models.WPRManager;
import team64.waterworks.models.WSRManager;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
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
        try {
            mMap = googleMap;
            mMap.setOnInfoWindowClickListener(this);
            ArrayList<String> waterReports = WSRManager.viewAllSourceReports();
            LatLng location = new LatLng(50,50);
            boolean reportsExist = false;

            ArrayList<String> purityReports = WPRManager.viewAllPurityReports();

            if (waterReports != null) {
                for (int i = 0; i < waterReports.size(); i++) {
                    String[] reportsData = waterReports.get(i).split(" ", 0);
                    String idString = reportsData[0].replace("(", "").replace(")", "");
                    Long idLong = Long.parseLong(idString);
                    Double lng = WSRManager.getSourceReportByID(idLong).getLongitude();
                    Double lat = WSRManager.getSourceReportByID(idLong).getLatitude();
                    location = new LatLng(lat, lng);

                    mMap.addMarker(new MarkerOptions().position(location).title("Water " +
                            "Report: " + idLong).snippet
                            ("Lat/Long: " + lat + "/" + lng));
                }
                reportsExist = true;
            }
            if (purityReports != null){
                for (int i = 0; i < purityReports.size(); i++) {
                    String[] reportsData = purityReports.get(i).split(" ", 0);
                    String idString = reportsData[0].replace("(", "").replace(")", "");
                    Long idLong = Long.parseLong(idString);
                    Double lng = WPRManager.getPurityReportByID(idLong).getLongitude();
                    Double lat = WPRManager.getPurityReportByID(idLong).getLatitude();
                    location = new LatLng(lat, lng);

                    mMap.addMarker(new MarkerOptions().position(location).title("Water " +
                            "Report: " + idLong).snippet
                            ("Lat/Long: " + lat + "/" + lng).icon
                            (BitmapDescriptorFactory.defaultMarker
                                    (BitmapDescriptorFactory.HUE_AZURE)));
                }
                reportsExist = true;
            }

            if (!reportsExist) {
                Toast.makeText(getApplicationContext(), "No reports have been added yet!",Toast.LENGTH_SHORT).show();
            }

            // Add a marker and move camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }
}
