package dummy.justs.com.mapapp;

import android.graphics.drawable.Drawable;

/**
 * Created by eptron on 9/17/2015.
 */
public class DrawerItem {
    private String mTitle;
    private int mId;

    public DrawerItem(String mTitle, int mId) {
        this.mTitle = mTitle;
        this.mId=mId;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public int getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public Drawable getIcon(){
        return null;
    }
}
