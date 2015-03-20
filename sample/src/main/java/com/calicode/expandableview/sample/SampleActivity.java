package com.calicode.expandableview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calicode.expendableview.ExpandableView;
import com.calicode.expendableview.ExpandableView.OnExpandableViewInflatedListener;


public class SampleActivity extends Activity {

    private LinearLayout mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        mList = (LinearLayout) findViewById(R.id.expandViewList);

        String[] texts = {
                "ASdaodinw oadn aoiwdn ainwd aoASdaodinw oadn aoiwdn ainwd aoiwdn aoiwndawodnaoiwiASdaodinw oadn aoiwdn ainwd aoiwdn aoiwndawodnaoiwwdn aoiwndawodnaoiw loppu",
                "testeion adsaoi adiASdaodinw oadn aoiwdn ainwd aoiwdn aoiwndawodnaoiwASdaodinw oadn aoiwdn ainwd aoiwdn aoiwndawodnaoiwn waoniw Loppu",
                "wandoaid",
                "asoASdaodinw oadn aoiwdn ainwd aoiwdn aoiwndawodnaoiwASdaodinw oadn aoiwdn ainwd aoiwdn aoiwndawodnaoiwASdaodinw oadn aoiwdn ainwd aoiwdn aoiwndawodnaoiwidnawionadw Loppu"};

        for (int i = 0; i < texts.length; ++i) {
            final String headerText = "Message " + (i+1);
            final String expandText = texts[i];

            ExpandableView view = new ExpandableView(this);

            view.setListener(new OnExpandableViewInflatedListener() {
                @Override
                public void headerInflated(View headerView) {
                    ((TextView) headerView.findViewById(R.id.headerTextView)).setText(headerText);
                }

                @Override
                public void expandAreaInflated(View expandView) {
                    ((TextView) expandView.findViewById(R.id.dynamicTextView)).setText(expandText);
                }
            });

            view.setHeaderLayoutId(R.layout.sample_header);
            view.setExpandLayoutId(R.layout.sample_expand_area_item);

            mList.addView(view);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.expandAll) {
            expandAll();
            return true;
        } else if (id == R.id.collapseAll) {
            collapseAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void expandAll() {
        for (int i = 0; i < mList.getChildCount(); ++i) {
            View child = mList.getChildAt(i);
            if (child instanceof ExpandableView) {
                ExpandableView expandView = (ExpandableView) child;
                expandView.expand();
            }
        }
    }

    private void collapseAll() {
        for (int i = 0; i < mList.getChildCount(); ++i) {
            View child = mList.getChildAt(i);
            if (child instanceof ExpandableView) {
                ExpandableView expandView = (ExpandableView) child;
                expandView.collapse();
            }
        }
    }
}
