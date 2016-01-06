package com.buoistudio.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.buoistudio.connect.MySingleton;
import com.buoistudio.data.Constants;
import com.buoistudio.model.DishOrder;
import com.buoistudio.model.ExtraFeeItem;
import com.buoistudio.waiter.R;

import java.util.ArrayList;
import java.util.List;

public class DishAdapter extends ArrayAdapter<DishOrder> {
    private int resource;
    private Context context;
    private ViewHolder viewHolder;
    private LayoutInflater inflater;
    private List<DishOrder> dishList = new ArrayList<DishOrder>();
    private ImageLoader imageLoader;
    private Typeface font_awesome;

    public DishAdapter(Context context, int resource, ArrayList<DishOrder> dishs) {
        super(context, resource, dishs);
        this.context = context;
        this.resource = resource;
        this.dishList = dishs;
        imageLoader = MySingleton.getInstance(context).getImageLoader();
        font_awesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
    }


    @Override
    public int getCount() {
        return dishList.size();
    }

    @Override
    public DishOrder getItem(int position) {
        return dishList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder();
        final DishOrder item = getItem(position);
        View row = convertView;
        if (row == null) {
            inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            if (!item.isExtraFee()) {

                if (imageLoader == null)
                    imageLoader = MySingleton.getInstance(context).getImageLoader();
                row = inflater.inflate(R.layout.item_dish, parent, false);
                viewHolder.checkItem = (CheckBox) row.findViewById(R.id.checkItem);
                viewHolder.name = (TextView) row.findViewById(R.id.dishName);
                viewHolder.image = (NetworkImageView) row.findViewById(R.id.dishAvatar);
                viewHolder.price = (TextView) row.findViewById(R.id.dishPrice);
                viewHolder.qty = (TextView) row.findViewById(R.id.dishQty);
                viewHolder.total = (TextView) row.findViewById(R.id.dishTotal);
                row.setTag(viewHolder);
                //set view for dish order
                viewHolder.checkItem.setChecked(changeStatus(item.getStatus()));
                viewHolder.name.setText(item.getDish().getName());
                viewHolder.image.setImageUrl(Constants.URL_IMAGE + item.getDish().getImage(), imageLoader);
                viewHolder.price.setText(String.valueOf(item.getDish().getPrice()));
                viewHolder.qty.setText(context.getString(R.string.qty) + ": " + item.getCount());
                viewHolder.total.setText(String.valueOf(item.getCount() * item.getDish().getPrice()));

                viewHolder.checkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            item.setStatus(Constants.SERVED);
                        } else {
                            item.setStatus(Constants.UNSERVED);
                        }
                    }
                });
            } else {
                //set extra fee
                row = inflater.inflate(R.layout.item_extra_fee, parent, false);
                viewHolder.name = (TextView) row.findViewById(R.id.txtExtraFeeName);
                viewHolder.price = (TextView) row.findViewById(R.id.txtExtraFeePrice);
                viewHolder.qty = (TextView) row.findViewById(R.id.txtExtraFeeAvatar);
                viewHolder.qty.setTypeface(font_awesome);
                row.setTag(viewHolder);
                ExtraFeeItem extraFee = item.getExtraFee();
                viewHolder.name.setText(extraFee.getName());
                viewHolder.price.setText(String.valueOf(extraFee.getPrice()));

            }
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }


        return row;
    }


    private class ViewHolder {
        CheckBox checkItem;
        NetworkImageView image;
        TextView name, price, qty;
        TextView total;

    }

    private boolean changeStatus(int status) {
        if (status == Constants.SERVED) return true;
        return false;
    }

    public ArrayList<DishOrder> getDishOrder() {
        return (ArrayList<DishOrder>) dishList;
    }
}

