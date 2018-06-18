package com.easterbrook.flood_it.database.adaptors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.easterbrook.flood_it.R;
import com.easterbrook.flood_it.database.models.LeaderboardEntryModel;

import java.util.List;

/**
 * An array adapter which enables the information retrieved from the leaderboard to be displayed
 */

public class LeaderbordScoresListAdaptor extends ArrayAdapter {
    private List<LeaderboardEntryModel> leaderboardEntries;
    private LayoutInflater inflater;
    public LeaderbordScoresListAdaptor(@NonNull Context context, @NonNull List objects) {
        super(context, R.layout.layout_leaderboard_entry, objects);
        leaderboardEntries = objects;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_leaderboard_entry,parent,false);
        }
        TextView nameLabel = convertView.findViewById(R.id.nameText);
        TextView scoreLabel = convertView.findViewById(R.id.scoreText);
        LeaderboardEntryModel leaderboardEntry = leaderboardEntries.get(position);
        nameLabel.setText(leaderboardEntry.getPlayerName());
        scoreLabel.setText(Integer.toString(leaderboardEntry.getScore()));
        return convertView;
    }
}
