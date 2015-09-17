package dummy.justs.com.mapapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class MapsActivity extends Activity implements View.OnClickListener{

    private FrameLayout mContentFrame;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Fragment mCurrFragment;
    private Button mButton;

    private static final String MAP_FRAGMENT="MAP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        mContentFrame=(FrameLayout) findViewById(R.id.content_frame);
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList=(ListView) findViewById(R.id.left_drawer);

        mButton=(Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);

        mDrawerList.setAdapter(new DrawerAdapter(
                new DrawerItem[]{
                        new DrawerItem("Title1",0),
                        new DrawerItem("Title2",1),
                        new DrawerItem("Title3",2)
                }
                ,getLayoutInflater()));
        switchFragment(MAP_FRAGMENT);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_search:
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void switchFragment(String tag) {


        Fragment frag = getFragmentManager().findFragmentByTag(tag);
        if (frag == null) {
            switch (tag){
                case MAP_FRAGMENT:
                    frag=new MapFragment();
                    break;
            }
        }

        if (frag!=null && mCurrFragment != frag) {
            mCurrFragment = frag;

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, mCurrFragment, tag)
                    .commit();
        }

        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.button){
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }
}
