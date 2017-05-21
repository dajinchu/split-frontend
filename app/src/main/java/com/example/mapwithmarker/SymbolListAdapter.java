package com.example.mapwithmarker;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dajin on 5/21/2017.
 */

public class SymbolListAdapter extends ArrayAdapter<String> {


    public SymbolListAdapter(Context context, List<String> symbols) {
        super(context, 0, symbols);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.symbol_pick, parent, false);
        }
        ((TextView)convertView.findViewById(R.id.symbol)).setText(getItem(position));
        return convertView;
    }
}