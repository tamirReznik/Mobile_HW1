package com.college.mobile_hw1.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.college.mobile_hw1.R;
import com.college.mobile_hw1.general.Player;
import com.college.mobile_hw1.interfaces.callbackes.RefreshMapCallback;
import com.college.mobile_hw1.utils.Utils;
import com.college.mobile_hw1.fragments.Fragment_List;
import com.college.mobile_hw1.fragments.Fragment_Map;

public class ScoreActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        initFragments();


    }

    private void initFragments() {
        Fragment_List fragment_list = new Fragment_List();
        getSupportFragmentManager().beginTransaction().add(R.id.score_LAY_list, fragment_list).commit();
        Fragment_Map fragment_map = new Fragment_Map();
        getSupportFragmentManager().beginTransaction().replace(R.id.score_LAY_map, fragment_map).commit();


        fragment_list.setRefreshMapCallback(player -> fragment_map.showMarker(player.getLat(), player.getLon()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.fullScreen(getWindow());
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            Utils.fullScreen(getWindow());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}