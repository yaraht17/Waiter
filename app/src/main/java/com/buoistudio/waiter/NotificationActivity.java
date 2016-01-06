package com.buoistudio.waiter;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.buoistudio.adapter.NotificationAdapter;
import com.buoistudio.data.Constants;
import com.buoistudio.model.TableItem;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnMenu, btnNotification;
    private Typeface font_awesome;

    private NotificationAdapter notificationAdapter;
    private ArrayList<TableItem> tableItems;
    private ListView listNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        createViewTopBar();

        listNotification = (ListView) findViewById(R.id.listNotification);
        tableItems = (ArrayList<TableItem>) getIntent().getSerializableExtra(Constants.LIST_NOTIFICATION_EXTRA);
//        if (tableItems.size() > 0) {
//            tableItems = mergeNotification(tableItems);
//        }
        notificationAdapter = new NotificationAdapter(this, -1, tableItems);
        listNotification.setAdapter(notificationAdapter);
    }

    private void createViewTopBar() {
        //define font
        font_awesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        btnMenu = (Button) findViewById(R.id.btnMenu);
        btnNotification = (Button) findViewById(R.id.btnNotification);
        btnMenu.setText(getString(R.string.icon_back));
        btnMenu.setTypeface(font_awesome);
        btnNotification.setTypeface(font_awesome);
        btnMenu.setOnClickListener(this);
        btnNotification.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnMenu:
                finish();
                break;

            default:
                break;
        }
    }

    private ArrayList<TableItem> mergeNotification(ArrayList<TableItem> tables) {

        ArrayList<TableItem> list = new ArrayList<>();
        list.add(tables.get(0));
        boolean kt = false;
        for (int b = 1; b < tables.size(); b++) {
            kt = false;
            for (int i = 0; i < list.size(); i++) {
                if (tables.get(b).getId() == list.get(i).getId()) {
                    kt = true;
                }
            }
            if (!kt) {
                list.add(tables.get(b));
            }
        }
        return list;
    }
}
