package dummy.justs.com.mapapp;

import android.app.usage.UsageEvents;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener, View.OnClickListener {
    private float mBoundZoom;
    private LatLng mSWCorner, mNECorner;
    private GoogleMap mMap;
    private LatLng mBoundLatLng;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.i("mapapp", "mapready");
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(57, 27);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Nowhere"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnCameraChangeListener(this);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        mSWCorner = new LatLng(56, 24);
        mNECorner = new LatLng(59, 29);

        mBoundZoom = 7;


        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(56, 24), new LatLng(56, 29), new LatLng(59, 29), new LatLng(56, 24))
                .strokeWidth(2f)
                .strokeColor(Color.RED)
                .addHole(Arrays.asList(new LatLng(57, 27), new LatLng(57, 28), new LatLng(58, 28), new LatLng(57, 27)))
                .fillColor(Color.argb(0x40, 00, 00, 0xFF)));


    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        /* new Timer().scheduleAtFixedRate(new TimerTask() {
             @Override
             public void run() {
                 EventBus.getDefault().post(new ResetMapEvent());
             }
         }, 0, 1000);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void resetMap() {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(mSWCorner, mNECorner), 20), 1000, null);
        }
    }

    public void onEventMainThread(ResetMapEvent event) {
        Log.i("mapapp", "camera moved");
        if (mMap != null)
            resetMap();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        if (cameraPosition.zoom < 7) {
            mBoundZoom = 7;
        } else if (cameraPosition.zoom > 9) {
            mBoundZoom = 9;
        } else
            mBoundZoom = cameraPosition.zoom;

        double lat = cameraPosition.target.latitude;
        double lng = cameraPosition.target.longitude;
        double maxLat = 59;
        double minLat = 56;
        double maxLong = 29;
        double minLong = 24;

        /*if (mBoundLat>maxLat) mBoundLat=maxLat;
        if (mBoundLat<minLat) mBoundLat=minLat;
        if (mBoundLong>maxLong) mBoundLong=maxLong;
        if (mBoundLong<minLong) mBoundLong=minLong;
*/

        if (lat > mNECorner.latitude) lat = mNECorner.latitude;
        if (lat < mSWCorner.latitude) lat = mSWCorner.latitude;
        if (lng > mNECorner.longitude) lng = mNECorner.longitude;
        if (lng < mNECorner.longitude) lng = mNECorner.longitude;

        mBoundLatLng = new LatLng(lat, lng);
    }

    @Override
    public void onClick(View v) {
        resetMap();
    }
}
