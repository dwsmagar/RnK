package com.susankya.rnk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private String[] objects;
    private Context context;

    public CustomSpinnerAdapter(Context context, int resourceId,
                                String[] objects) {
        super(context, resourceId, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View v = getCustomView(position, convertView, parent);
        ImageView iv = v.findViewById(R.id.dropdowniv);
        iv.setVisibility(View.INVISIBLE);
        return v;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_custom_layout, parent, false);
        TextView label = row.findViewById(R.id.textOfSpinner);
        label.setText(objects[position]);
        return row;
    }
}
