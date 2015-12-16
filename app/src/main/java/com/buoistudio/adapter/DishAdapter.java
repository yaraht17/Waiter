package com.buoistudio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.buoistudio.model.DishItem;
import com.buoistudio.waiter.R;

import java.util.ArrayList;
import java.util.List;

public class DishAdapter extends ArrayAdapter<DishItem> {
    private int resource;
    private Context context;
    private ViewHolder viewHolder;
    private LayoutInflater inflater;
    private List<DishItem> dishList = new ArrayList<DishItem>();

    public DishAdapter(Context context, int resource, ArrayList<DishItem> dishs) {
        super(context, resource, dishs);
        this.context = context;
        this.resource = resource;
        this.dishList = dishs;
    }


    @Override
    public int getCount() {
        return dishList.size();
    }

    @Override
    public DishItem getItem(int position) {
        return dishList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder();
        DishItem item = getItem(position);
        View row = convertView;
        if (row == null) {
            inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_dish, parent, false);
            viewHolder.checkItem = (CheckBox) row.findViewById(R.id.checkItem);
            viewHolder.name = (TextView) row.findViewById(R.id.dishName);
            viewHolder.avatar = (ImageView) row.findViewById(R.id.dishAvatar);
            viewHolder.price = (TextView) row.findViewById(R.id.dishPrice);
            viewHolder.qty = (TextView) row.findViewById(R.id.dishQty);
            viewHolder.total = (TextView) row.findViewById(R.id.dishTotal);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        viewHolder.checkItem.setChecked(item.isStatus());
        viewHolder.name.setText(item.getName());
        viewHolder.avatar.setImageResource(R.drawable.avatar_dish);
        viewHolder.price.setText(String.valueOf(item.getPrice()));
        viewHolder.qty.setText("Qty: " + item.getQty());
        viewHolder.total.setText("$ " + item.getQty() * item.getPrice());

        return row;
    }


    private class ViewHolder {
        CheckBox checkItem;
        ImageView avatar;
        TextView name, price, qty;

        TextView total;

    }
}
