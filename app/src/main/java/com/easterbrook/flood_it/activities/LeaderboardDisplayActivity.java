package com.easterbrook.flood_it.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.easterbrook.flood_it.R;
import com.easterbrook.flood_it.database.LeaderboardDataSource;
import com.easterbrook.flood_it.database.adaptors.LeaderbordScoresListAdaptor;
import com.easterbrook.flood_it.database.models.LeaderboardModel;

import java.util.ArrayList;
import java.util.List;


public class LeaderboardDisplayActivity extends AppCompatActivity {
    private Spinner spinner;
    private ListView listView;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_display);
        context = this;
        listView = findViewById(R.id.leaderboard_list);
        spinner = findViewById(R.id.Leaderboardspinner);

        final LeaderboardDataSource leaderboardDataSource = new LeaderboardDataSource(this);
        leaderboardDataSource.open();
        final ArrayList<LeaderboardModel> leaderboards = leaderboardDataSource.getLeaderboardsWithValues();
        ArrayList<String>captions = new ArrayList<>();
        for (LeaderboardModel leaderboard:leaderboards){
            captions.add(leaderboard.getCaption());
        }
        ArrayAdapter<String> stateNameAdaptor = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, captions);
        stateNameAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List list = leaderboardDataSource.getScoresFromLeaderboard(leaderboards.get(position));
                listView.setAdapter(new LeaderbordScoresListAdaptor(context,list));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(stateNameAdaptor);
    }
}
