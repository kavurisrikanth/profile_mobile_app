package com.example.srikanth.helloworld;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AttendanceListAdapter extends ArrayAdapter {
    private final Activity context;
    private final String[] dates, stuff;

    public AttendanceListAdapter(Activity context, String[] dates, String[] stuff) {
        // TODO: Check if dates needs to be added here as another argument...
        super(context, R.layout.attendance_one_row);

        this.context = context;
        this.dates = dates;
        this.stuff = stuff;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.attendance_one_row, null, true);

        TextView dateTextField = rowView.findViewById(R.id.dateGoesHere),
                 otherStuffField = rowView.findViewById(R.id.otherStuffHere);

        dateTextField.setText(dates[position]);
        otherStuffField.setText(stuff[position]);

        return rowView;
    }
}
