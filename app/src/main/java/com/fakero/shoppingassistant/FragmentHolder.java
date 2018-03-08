package com.fakero.shoppingassistant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

public class FragmentHolder extends AppCompatActivity implements ShoppingListFragment.OnItemSelectedListener {

    ShoppingListItem item;

    ViewPager mViewPager;
    ShoppingListPagerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_holder);
        mAdapter = new ShoppingListPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mAdapter);
        item = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);

    }

    public void onItemAddedToCart() {
        Fragment listFrag = mAdapter.getFragment(0);
        if (listFrag instanceof ShoppingListFragment) {
            ((ShoppingListFragment)listFrag).notifyList();
        }
    }

    public void onItemSelect(int position) {
        Fragment calcFrag = mAdapter.getFragment(1);
        if (calcFrag instanceof CalculatorFragment) {
            ((CalculatorFragment)calcFrag).setItem(position);
        }
        else {
            Log.d("FragmentHolder", "onItemSelect, calculatorFragment = null");
            /*LayoutInflater inflater = getLayoutInflater();
            ViewGroup vg =
            inflater.inflate(R.layout.fragment_calculator, this, )
            mAdapter.instantiateItem(getLayoutInflater().inflate(R.layout.fragment_calculator,))*/
        }
        mViewPager.setCurrentItem(1, true);
    }

    public class ShoppingListPagerAdapter extends FragmentStatePagerAdapter {

        SparseArray<Fragment> mFragments = new SparseArray<>();

        public ShoppingListPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            mFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getFragment(int position) {
            return mFragments.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return ShoppingListFragment.newInstance("");
                case 1: return CalculatorFragment.newInstance();
            }
            return ShoppingListFragment.newInstance("");
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.shoppingList);
                case 1:
                    return getString(R.string.priceCalculator);
            }
            return null;
        }
    }
}
