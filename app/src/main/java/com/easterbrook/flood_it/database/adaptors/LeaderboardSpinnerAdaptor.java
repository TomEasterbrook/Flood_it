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
import com.easterbrook.flood_it.database.models.LeaderboardModel;

import java.util.List;

/**
 * An array adapter which enables the information retrieved from the leaderboard to be displayed
 */

public class LeaderboardSpinnerAdaptor extends ArrayAdapter<LeaderboardModel> {
    private List<LeaderboardModel> leaderboards;
   private LayoutInflater inflater;
    public LeaderboardSpinnerAdaptor(@NonNull Context context, @NonNull List objects) {
        super(context, R.layout.layout_leaderboard_entry, objects);
        leaderboards = objects;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }
    private View createView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_spinner_leaderboard,parent,false);
        }
        TextView captionLabel =  convertView.findViewById(R.id.caption_view);
        LeaderboardModel leaderboard = leaderboards.get(position);
        captionLabel.setText(leaderboard.getCaption());
        return convertView;

    }
}
