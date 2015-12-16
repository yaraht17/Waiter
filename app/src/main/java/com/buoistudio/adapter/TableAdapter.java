package com.buoistudio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.buoistudio.waiter.R;

import java.util.ArrayList;


public class TableAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> items;
    int size;

    public TableAdapter(Context c, int size, ArrayList<String> items) {
        mContext = c;
        this.size = size;
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String item = items.get(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.item_table, parent, false);
            view.setLayoutParams(new GridView.LayoutParams(size, size));
        } else {
            view = convertView;
        }
        TextView textView = (TextView) view.findViewById(R.id.tableNumber);
        textView.setText(item);
        return view;
    }

}