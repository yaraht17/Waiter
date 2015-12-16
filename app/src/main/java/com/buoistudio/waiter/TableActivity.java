package com.buoistudio.waiter;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.buoistudio.adapter.DishAdapter;
import com.buoistudio.model.DishItem;

import java.util.ArrayList;

public class TableActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listDish;
    private ArrayList dishItems = new ArrayList();
    private DishAdapter dishAdapter;
    private Button btnMenu, btnNotification;
    private Typeface font_awesome;

    private Button btnConfirm, btnExtraFee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        createViewTopBar();
        listDish = (ListView) findViewById(R.id.listDish);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnExtraFee = (Button) findViewById(R.id.btnExtraFee);
        btnExtraFee.setOnClickListener(this);
        createDemo();
        dishAdapter = new DishAdapter(this, -1, dishItems);
        listDish.setAdapter(dishAdapter);
    }

    private void createDemo() {
        dishItems.add(new DishItem("", "Chicken wings", 65, 2, false));
        dishItems.add(new DishItem("", "Salat", 20, 2, true));
        dishItems.add(new DishItem("", "Salat", 20, 2, false));
        dishItems.add(new DishItem("", "Salat", 20, 2, false));
        dishItems.add(new DishItem("", "Salat", 20, 2, true));
    }

    private void createViewTopBar() {
        //define font
        font_awesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        btnMenu = (Button) findViewById(R.id.btnMenu);
        btnNotification = (Button) findViewById(R.id.btnNotification);
        btnMenu.setTypeface(font_awesome);
        btnNotification.setTypeface(font_awesome);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnExtraFee) {
            final Dialog dialog = new Dialog(TableActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_extrafee);
            dialog.show();
        }
    }
}
