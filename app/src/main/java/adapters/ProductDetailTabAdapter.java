package adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fragments.Dashboard;
import fragments.DescriptionFragment;
import fragments.DetailFragment;


/**
 * Created by welcome on 28-11-2017.
 */

public class ProductDetailTabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ProductDetailTabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DetailFragment tab1 = new DetailFragment();
                return tab1;
            case 1:
                DescriptionFragment tab2 = new DescriptionFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
