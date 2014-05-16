package de.bitninja.net.app.pullviewtest;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TextView;

import de.bitninja.net.pulloutview.lib.PullOutView;

public class MainActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;
    private PullOutView pullOutView;
    private String currentTabId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent );

        setupTab(ListFragment.class,
                getResources().getString(R.string.tab_fruits),
                getResources().getStringArray(R.array.fruits)
        );
        setupTab(ListFragment.class,
                getResources().getString(R.string.tab_cars),
                getResources().getStringArray(R.array.cars)
        );
        setupTab(ListFragment.class,
                getResources().getString(R.string.tab_computers),
                getResources().getStringArray(R.array.computers)
        );

        pullOutView = (PullOutView) findViewById(R.id.pullOut1);

        currentTabId = mTabHost.getCurrentTabTag();
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

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

            View widget = mTabHost.getTabWidget().getChildAt(i);

//            widget.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//
//                    String tabId = mTabHost.getCurrentTabTag();
//
//                    if(event.getAction() == MotionEvent.ACTION_UP){
//
//                        Log.e(MainActivity.class.getName(), "TabId " + currentTabId);
//
//                        if(currentTabId != tabId) {
//                            currentTabId = tabId;
//                        }else {
//                            if(!pullOutView.isOpened())
//                                pullOutView.openLayer(true);
//                            else
//                                pullOutView.closeLayer(true);
//                        }
//                    }
//                    return false;
//                }
//            });


            final TextView tv = (TextView) widget.findViewById(android.R.id.title);
            if (tv == null)
                continue;
            else
                tv.setTextColor(0xFFFFFFFF);
        }
    }
}
