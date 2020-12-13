package com.college.mobile_hw1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.college.mobile_hw1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Fragment_Map extends Fragment implements OnMapReadyCallback {


    GoogleMap googleMap;

    public Fragment_Map() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initMap();

        return view;
    }

    private void initMap() {

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.score_FRG_map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

    }

    public void addMarker(double lat, double lon) {

        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(lat, lon);

        markerOptions.position(latLng);
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
        googleMap.addMarker(markerOptions);

    }

    public void showMarker(double lat, double lon) {
        if (googleMap == null)
            return;
        // if location doesn't exist - no zoom-in in map
        LatLng latLng = new LatLng(lat, lon);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        this.googleMap.clear();

    }
}