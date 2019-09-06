package com.example.vijay.musicplayer;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    Toolbar mtoolbar;
    TabLayout mtablayout;
    TabItem currentmusic;
    TabItem allmusic;
    TabItem playlists;
    ViewPager mviewpager;
    PagerController mpagercontroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Music Player");

        mtablayout = findViewById(R.id.tabLayout);
        currentmusic = findViewById(R.id.currentmusic);
        allmusic = findViewById(R.id.currentmusic);

        mviewpager = findViewById(R.id.viewpager);

        mpagercontroller = new PagerController(getSupportFragmentManager(),mtablayout.getTabCount());
        mviewpager.setAdapter(mpagercontroller);

        mtablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mviewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mviewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mtablayout));
    }
}
