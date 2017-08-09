package belizenearme.infoservices.lue.belize.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import belizenearme.infoservices.lue.belize.fragments.EighthFragment;
import belizenearme.infoservices.lue.belize.fragments.FifthFragment;
import belizenearme.infoservices.lue.belize.fragments.FirstFragment;
import belizenearme.infoservices.lue.belize.fragments.SecondFragment;
import belizenearme.infoservices.lue.belize.fragments.SeventhFragment;
import belizenearme.infoservices.lue.belize.fragments.ThirdFragment;
import belizenearme.infoservices.lue.belize.fragments.fourthFragment;
import belizenearme.infoservices.lue.belize.fragments.sixthFragment;


public class ScrollPageAdapter extends FragmentPagerAdapter {


    private final int PAGE_COUNT = 8;

    public ScrollPageAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FirstFragment();
                break;
            case 1:
                fragment = new SecondFragment();
                break;
            case 2:
                fragment = new ThirdFragment();
                break;
            case 3:
                fragment = new fourthFragment();
                break;
            case 4:
                fragment = new FifthFragment();
                break;
            case 5:
                fragment = new sixthFragment();
                break;
            case 6:
                fragment = new SeventhFragment();
                break;
            case 7:
                fragment = new EighthFragment();
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
