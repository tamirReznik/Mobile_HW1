package com.college.mobile_hw1.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.college.mobile_hw1.interfaces.callbackes.AddMarkerToMapCallback;
import com.college.mobile_hw1.utils.KEYS;
import com.college.mobile_hw1.R;
import com.college.mobile_hw1.general.Player;
import com.college.mobile_hw1.interfaces.callbackes.RefreshMapCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Stack;


public class Fragment_List extends Fragment {

    private RefreshMapCallback refreshMapCallback;


    private AddMarkerToMapCallback addMarkerToMapCallback;
    private int validRows;
    private Stack<Player> players;

    public Fragment_List() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__list, container, false);
        initList(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void initList(View view) {
        Query query = FirebaseDatabase.getInstance().getReference().child(KEYS.KEY_DB_PLAYERS_LIST).orderByChild(KEYS.KEY_SCORE).limitToLast(10);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fetchTopTenListFromDb(view, snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchTopTenListFromDb(View view, DataSnapshot snapshot) {

        HashMap<String, Object> playerFromDb;
        players = new Stack<>();


        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

            playerFromDb = (HashMap<String, Object>) dataSnapshot.getValue();

            if (playerFromDb == null)
                throw new RuntimeException("player Can't be null");

            int score = ((Long) Objects.requireNonNull(playerFromDb.get(KEYS.KEY_SCORE))).intValue();

            if (score <= 0)
                continue;

            String avatar = (String) playerFromDb.get(KEYS.AVATAR);
            String name = (String) playerFromDb.get(KEYS.NAME);
            double lat = (Double) Objects.requireNonNull(playerFromDb.get(KEYS.LAT));
            double lon = (Double) Objects.requireNonNull(playerFromDb.get(KEYS.LON));

            if (name == null || avatar == null)
                throw new RuntimeException("player can't have null properties");

            if (lat != KEYS.DEFAULT_LAT_LON_VAL && lon != KEYS.DEFAULT_LAT_LON_VAL)
                addMarkerToMapCallback.addMarker(lat, lon);

            players.push(new Player(name, lat, lon, avatar, score));

            Log.i("playerDet", "fetchTopTenListFromDb: " + players.peek().toString());
        }

        fillTopTenList(view);

    }

    // fill the list (table xml) with the top 10 players
    private void fillTopTenList(View view) {

        TableRow tableRow;
        TableLayout tableLayout = view.findViewById(R.id.score_TBL_topTen);
        ArrayList<TableRow> rows = new ArrayList<>();
//
        validRows = Math.min(tableLayout.getChildCount() - 1, players.size());

        int i;
        for (i = 1; i <= validRows; i++) {
            tableRow = (TableRow) tableLayout.getChildAt(i);
            Player player = players.elementAt(validRows - i);

            TextView name = (TextView) tableRow.getChildAt(1);
            TextView score = (TextView) tableRow.getChildAt(2);
            ImageView avatar = (ImageView) tableRow.getChildAt(3);


            name.setText(player.getName());
            score.setText(String.format(Locale.getDefault(), "%d", player.getScore()));
            avatar.setImageResource(getResources().getIdentifier(player.getAvatar(), "drawable", Objects.requireNonNull(getActivity()).getPackageName()));

            rows.add(tableRow);
            rows.get(i - 1).setOnClickListener(v -> {
                if (refreshMapCallback != null) {
                    String rank = ((TextView) ((TableRow) v).getChildAt(0)).getText().toString();
                    Player temp = players.elementAt(validRows - Integer.parseInt(rank));
                    double latitude = temp.getLat();
                    double longitude = temp.getLon();
                    if (latitude != KEYS.DEFAULT_LAT_LON_VAL && longitude != KEYS.DEFAULT_LAT_LON_VAL)
                        refreshMapCallback.refreshMap(temp.getLat(), temp.getLon());
                }
            });

        }
    }

    public void setRefreshMapCallback(RefreshMapCallback refreshMapCallback) {
        this.refreshMapCallback = refreshMapCallback;
    }

    public void setAddMarkerToMapCallback(AddMarkerToMapCallback addMarkerToMapCallback) {
        this.addMarkerToMapCallback = addMarkerToMapCallback;
    }

}