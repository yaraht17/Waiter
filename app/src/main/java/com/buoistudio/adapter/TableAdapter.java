package com.buoistudio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.buoistudio.model.TableItem;
import com.buoistudio.waiter.R;

import java.util.ArrayList;


public class TableAdapter extends ArrayAdapter<TableItem> {
    private Context mContext;
    private ArrayList<TableItem> items;
    int size; //size for item

    public TableAdapter(Context c, int size, ArrayList<TableItem> items) {
        super(c, size, items);
        mContext = c;
        this.size = size;
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    public TableItem getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TableItem item = items.get(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.item_table, parent, false);
            view.setLayoutParams(new GridView.LayoutParams(size, size));
        } else {
            view = convertView;
        }
        TextView textView = (TextView) view.findViewById(R.id.tableNumber);
        textView.setText(item.getName());
        return view;
    }

}