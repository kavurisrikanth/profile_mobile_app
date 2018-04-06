package com.example.srikanth.helloworld;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter {

    private final Activity context;
    private final String[] names, descriptions;

    public CustomListAdapter(Activity context, String[] names, String[] descriptions) {
        super(context, R.layout.listview_onerow, names);

        this.context = context;
        this.names = names;
        this.descriptions = descriptions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.listview_onerow, null, true);

        TextView nameTextField = (TextView)rowView.findViewById(R.id.nameTextViewID),
                 detailsTextField = (TextView)rowView.findViewById(R.id.detailsTextViewID);

        nameTextField.setText(names[position]);
        detailsTextField.setText(descriptions[position]);

        return rowView;
    }
}
