package dummy.justs.com.mapapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by eptron on 9/17/2015.
 */
public class DrawerAdapter extends BaseAdapter {

    private DrawerItem[] mItems;
    private LayoutInflater mInflater;

    public DrawerAdapter(DrawerItem[] mItems,LayoutInflater mInflater) {
        this.mInflater = mInflater;
        this.mItems=mItems;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public Object getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return mItems[position].getmId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=mInflater.inflate(R.layout.drawer_item,null);

        ImageView image=(ImageView) view.findViewById(R.id.icon);
        image.setImageDrawable(mItems[position].getIcon());

        TextView text=(TextView) view.findViewById(R.id.title);
        text.setText(mItems[position].getmTitle());

        return view;
    }


}
