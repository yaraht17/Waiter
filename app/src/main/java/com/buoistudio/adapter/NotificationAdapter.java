package com.buoistudio.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.buoistudio.model.TableItem;
import com.buoistudio.waiter.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends ArrayAdapter<TableItem> {
    private int resource;
    private Context context;
    private ViewHolder viewHolder;
    private LayoutInflater inflater;
    private List<TableItem> notifList = new ArrayList<TableItem>();

    public NotificationAdapter(Context context, int resource, ArrayList<TableItem> tables) {
        super(context, resource, tables);
        this.context = context;
        this.resource = resource;
        this.notifList = tables;
    }


    @Override
    public int getCount() {
        return notifList.size();
    }

    @Override
    public TableItem getItem(int position) {
        return notifList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder();
        final TableItem item = getItem(position);
        View row = convertView;
        if (row == null) {
            inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.item_notification, parent, false);
            viewHolder.content = (TextView) row.findViewById(R.id.txtContent);
            viewHolder.tableNumber = (TextView) row.findViewById(R.id.tableNumber);
            viewHolder.time = (TextView) row.findViewById(R.id.txtTime);
            row.setTag(viewHolder);

            String contentString = context.getString(R.string.table) + " " + "<font color='#EE0000'>"
                    + item.getName() + "</font>" + " " + context.getString(R.string.call_waiter);
            viewHolder.content.setText(Html.fromHtml(contentString));
            viewHolder.tableNumber.setText(item.getName());
            long current = System.currentTimeMillis();
            long timeWait = current - item.getTimeCall();
            long timeSeconds = timeWait / 1000;
            //set time
            String time = "";
            if (timeSeconds < 60) {
                time = "now";
            } else if (timeSeconds > 60 && timeSeconds < 3600) {
                long minutes = timeSeconds / 60;
                time = minutes + " " + context.getString(R.string.minutes_ago);
            } else if (timeSeconds > 3600) {
                long hour = timeSeconds / 3600;
                time = hour + " " + context.getString(R.string.hours_ago);
            }
            viewHolder.time.setText(String.valueOf(time));

        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        return row;
    }


    private class ViewHolder {
        TextView content, tableNumber, time;

    }


}
