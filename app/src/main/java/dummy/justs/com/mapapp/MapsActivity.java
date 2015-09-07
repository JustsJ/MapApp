package dummy.justs.com.mapapp;

import android.app.usage.UsageEvents;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener
 {
    private LatLng mBoundLatLong;
     private double mBoundLat,mBoundLong;
     private float mBoundZoom;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


     /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(57,27);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Nowhere"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnCameraChangeListener(this);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);



        mBoundLat=57;
        mBoundLong=27;
        mBoundZoom=7;

        Polygon p = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(56, 24), new LatLng(56, 29), new LatLng(59, 29), new LatLng(56, 24))
                .strokeColor(Color.RED)
                .addHole(Arrays.asList(new LatLng(57, 27), new LatLng(57, 28), new LatLng(58, 28), new LatLng(57, 27)))
                .fillColor(Color.argb(0x40, 00, 00, 0xFF)));


    }

     @Override
     protected void onResume() {
         super.onResume();
         EventBus.getDefault().register(this);
         new Timer().scheduleAtFixedRate(new TimerTask() {
             @Override
             public void run() {
                 EventBus.getDefault().post(new ResetMapEvent());
             }
         }, 0, 1000);
     }

     @Override
     protected void onPause() {
         super.onPause();
         EventBus.getDefault().unregister(this);
     }

     public void onEventMainThread(ResetMapEvent event){
         if (mMap!=null)
             mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mBoundLat, mBoundLong), mBoundZoom));
     }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        if (cameraPosition.zoom<7){
            mBoundZoom=7;
        }
        else if (cameraPosition.zoom>9){
            mBoundZoom=9;
        }
        else
          mBoundZoom=cameraPosition.zoom;

        mBoundLat=cameraPosition.target.latitude;
        mBoundLong=cameraPosition.target.longitude;
        double maxLat=59;
        double minLat=56;
        double maxLong=29;
        double minLong=24;

        if (mBoundLat>maxLat) mBoundLat=maxLat;
        if (mBoundLat<minLat) mBoundLat=minLat;
        if (mBoundLong>maxLong) mBoundLong=maxLong;
        if (mBoundLong<minLong) mBoundLong=minLong;


    }

 }
