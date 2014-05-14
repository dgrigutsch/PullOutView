package de.bitninja.net.app.pullviewtest;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

public class MainActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
//        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent );
//
//        setupTab(ListFragment.class, "Tab 1", getResources().getStringArray(R.array.fruits));
//        setupTab(ListFragment.class, "Tab 2", getResources().getStringArray(R.array.cars));
//        setupTab(ListFragment.class, "Tab 3",  getResources().getStringArray(R.array.computers));
    }

    private void setupTab(Class fragmentClass, final String text) {
        setupTab(fragmentClass,text,null);
    }

    private void setupTab(Class fragmentClass, final String text, String[] array) {


        Bundle args1 = null;
        if(array!=null){
            args1 = new Bundle();
            args1.putStringArray(ListFragment.DATA_ARRAY,array);

            mTabHost.addTab(
                    mTabHost.newTabSpec(text).setIndicator(text),
                    fragmentClass, args1);
        }else {
            mTabHost.addTab(
                    mTabHost.newTabSpec(text).setIndicator(text),
                    fragmentClass, null);
        }


    }
}
