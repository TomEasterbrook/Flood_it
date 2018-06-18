package com.easterbrook.flood_it.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toolbar;

import com.easterbrook.flood_it.R;


public class CustomGameSettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner gridSizePicker;
    private NumberPicker colourNumberPicker;
    private int gridSize = 15;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_game_settings);
        initialiseNumberPicker();
        initialiseSpinner();
        setTitle("Custom Game Settings");
    }
    private void initialiseSpinner(){
        gridSizePicker = (Spinner) findViewById(R.id.grid_size_spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.grid_sizes,R.layout.support_simple_spinner_dropdown_item);
        gridSizePicker.setAdapter(arrayAdapter);
        gridSizePicker.setOnItemSelectedListener(this);
    }
    private void initialiseNumberPicker(){
        colourNumberPicker = (NumberPicker)findViewById(R.id.number_colour_picker);
        colourNumberPicker.setMinValue(3);
        colourNumberPicker.setMaxValue(8);
        colourNumberPicker.setValue(4);
    }
    public void playGameButtonClick(View view){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("grid_width", gridSize);
        intent.putExtra("grid_height",gridSize);
        intent.putExtra("grid_colour_count", colourNumberPicker.getValue());
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       String selectedItem = (String) parent.getItemAtPosition(position);
       gridSize = Integer.parseInt(selectedItem.split("x")[0]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
