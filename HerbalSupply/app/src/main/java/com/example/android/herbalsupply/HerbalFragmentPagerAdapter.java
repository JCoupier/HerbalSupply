package com.example.android.herbalsupply;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Herbal Supply created by JCoupier on 10/07/2017.
 */
public class HerbalFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public HerbalFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HydrolatFragment();
        } else if (position == 1){
           return new HuileVegeFragment();
        } else if (position == 2) {
            return new HuileEssFragment();
        } else if (position == 3) {
            return new ExtraitFragment();
        } else if (position == 4) {
            return new PoudreFragment();
        } else if (position == 5){
            return new ActifFragment();
        } else if (position == 6) {
            return new DiversFragment();
        } else {
            return new ContenFragment();
        }
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.category_hydrolats);
        } else if (position == 1) {
            return mContext.getString(R.string.category_huile_vegetales);
        } else if (position == 2) {
            return mContext.getString(R.string.category_huile_essentielles);
        } else if (position == 3) {
            return mContext.getString(R.string.category_extraits);
        } else if (position == 4) {
            return mContext.getString(R.string.category_poudre);
        } else if (position == 5) {
            return mContext.getString(R.string.category_actif);
        } else if (position == 6) {
            return mContext.getString(R.string.category_divers);
        } else {
            return mContext.getString(R.string.category_contenants);
        }
    }
}
