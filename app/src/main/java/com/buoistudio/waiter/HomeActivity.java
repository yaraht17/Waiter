package com.buoistudio.waiter;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.buoistudio.adapter.TableAdapter;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private GridView tableList;
    private ArrayList<String> tableItems = new ArrayList<>();
    private TableAdapter tableAdapter;

    private int screenHeight, screenWidth;

    private Button btnMenu, btnNotification;
    private Typeface font_awesome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tableList = (GridView) findViewById(R.id.gridview);


        createViewTopBar();

        //get size screen of device
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
        int itemSize = (screenWidth - 150) / 3;

        createDemo();
        tableAdapter = new TableAdapter(this, itemSize, tableItems);
        tableList.setAdapter(tableAdapter);
        tableList.setOnItemClickListener(this);
    }

    public void createDemo() {
        for (int i = 1; i <= 10; i++) {
            String item = i + "";
            tableItems.add(item);
        }
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(HomeActivity.this, TableActivity.class);
        startActivity(intent);
    }
}
