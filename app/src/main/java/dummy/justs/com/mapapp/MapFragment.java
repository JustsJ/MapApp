package dummy.justs.com.mapapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by eptron on 9/17/2015.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerDragListener {

    private LatLng mSWCorner, mNECorner;
    private GoogleMap mMap;
    private Button mButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=inflater.inflate(R.layout.activity_maps,null);

        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mButton = (Button) view.findViewById(R.id.button);
        mButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.i("mapapp", "mapready");
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(57, 27);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Nowhere"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        mMap.setOnMarkerDragListener(this);

        mSWCorner = new LatLng(57, 27);
        mNECorner = new LatLng(58, 28);

        UrlTileProvider provider=new UrlTileProvider(512,512) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                if (zoom==4 && (x>=5 && x<=12) && (y>=5 && x<=12)){
                    try {
                        return new URL("http://people.mozilla.org/~faaborg/files/shiretoko/firefoxIcon/firefox-512-noshadow.png");
                    }
                    catch (MalformedURLException e){
                        e.printStackTrace();
                        return null;
                    }
                }
                else
                    return null;
            }
        };

        TileOverlay t=mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));


        mMap.addPolygon(new PolygonOptions()
                //.add(new LatLng(56, 24), new LatLng(56, 29), new LatLng(59, 29), new LatLng(56, 24))
                .add(new LatLng(-85, -179.9f),
                        new LatLng(-85, -60), new LatLng(-85, 60), new LatLng(-85, 180),
                        new LatLng(85, 180),
                        new LatLng(85, 60), new LatLng(85, -60), new LatLng(85, -179.9f),
                        new LatLng(-85, -179.9f)
                )
                .strokeWidth(2f)
                .strokeColor(Color.TRANSPARENT)
                .addHole(Arrays.asList(new LatLng(57, 27), new LatLng(57, 28), new LatLng(58, 28), new LatLng(57, 27)))
                .fillColor(Color.argb(0x40, 0xFF, 0, 0)));

        mMap.addCircle(new CircleOptions()
                .center(new LatLng(57.25f,27.75f))
                .fillColor(Color.argb(0x80, 0, 0xFF, 0))
                .radius(10000)
                .strokeWidth(0));

    }

    private void resetMap() {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(mSWCorner, mNECorner), 20), 1000, null);
        }
    }

    @Override
    public void onClick(View v) {
        resetMap();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        marker.setTitle(marker.getPosition().latitude+" "+marker.getPosition().longitude);
    }

}
