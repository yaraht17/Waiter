package com.buoistudio.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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
    private ImageLoader imageLoader; //load image from server
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

        final DishOrder item = getItem(position);
        View row = convertView;
        if (row == null) {
            inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            if (imageLoader == null)
                imageLoader = MySingleton.getInstance(context).getImageLoader();
            viewHolder = new ViewHolder();
            row = inflater.inflate(R.layout.item_dish, parent, false);

            //layout item dish
            viewHolder.layoutDish = (LinearLayout) row.findViewById(R.id.layoutDish);
            viewHolder.checkItem = (CheckBox) row.findViewById(R.id.checkItem);
            viewHolder.name = (TextView) row.findViewById(R.id.dishName);
            viewHolder.image = (NetworkImageView) row.findViewById(R.id.dishAvatar);
            viewHolder.price = (TextView) row.findViewById(R.id.dishPrice);
            viewHolder.qty = (TextView) row.findViewById(R.id.dishQty);
            viewHolder.total = (TextView) row.findViewById(R.id.dishTotal);

            //layout item extra fee
            viewHolder.layoutExtraFee = (LinearLayout) row.findViewById(R.id.layoutExtraFee);
            viewHolder.extraFeeName = (TextView) row.findViewById(R.id.txtExtraFeeName);
            viewHolder.extraFeePrice = (TextView) row.findViewById(R.id.txtExtraFeePrice);
            viewHolder.extraFeeAvt = (TextView) row.findViewById(R.id.txtExtraFeeAvatar);
            viewHolder.extraFeeAvt.setTypeface(font_awesome);
            row.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        if (item.isExtraFee()) {
            //item extra fee
            viewHolder.layoutDish.setVisibility(View.GONE);
            viewHolder.layoutExtraFee.setVisibility(View.VISIBLE);
            ExtraFeeItem extraFee = item.getExtraFee();
            viewHolder.extraFeeName.setText(extraFee.getName());
            viewHolder.extraFeePrice.setText(String.valueOf(extraFee.getPrice()));
        } else {
            //set view for dish order
            viewHolder.layoutDish.setVisibility(View.VISIBLE);
            viewHolder.layoutExtraFee.setVisibility(View.GONE);
            viewHolder.checkItem.setOnCheckedChangeListener(null);
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
        }
        return row;
    }


    private class ViewHolder {
        CheckBox checkItem;
        NetworkImageView image;
        TextView name, price, qty;
        TextView total;

        TextView extraFeeName, extraFeePrice, extraFeeAvt;
        LinearLayout layoutDish, layoutExtraFee;
    }

    private boolean changeStatus(int status) {
        if (status == Constants.SERVED) return true;
        return false;
    }

    public ArrayList<DishOrder> getDishOrder() {
        return (ArrayList<DishOrder>) dishList;
    }
}

