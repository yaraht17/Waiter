package com.buoistudio.waiter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.buoistudio.adapter.CustomDrawerAdapter;
import com.buoistudio.adapter.TableAdapter;
import com.buoistudio.connect.APIConnection;
import com.buoistudio.connect.ParseJSON;
import com.buoistudio.connect.VolleyCallback;
import com.buoistudio.data.Constants;
import com.buoistudio.model.DrawerItem;
import com.buoistudio.model.TableItem;
import com.buoistudio.model.UserItem;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private GridView tableList;
    private ArrayList<TableItem> tableItems = new ArrayList<>();
    private TableAdapter tableAdapter;

    private int screenHeight, screenWidth, itemSize;

    private Button btnMenu, btnNotification;
    private Typeface font_awesome;
    private TextView txtWaiterName;
    //drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CustomDrawerAdapter drawerAdapter;
    private List<DrawerItem> drawerDataList = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private String accessToken;
    private UserItem user;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(Constants.URL_SERVER);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(Constants.WAITER_PREFERENCES, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "");

        setContentView(R.layout.activity_home);
        tableList = (GridView) findViewById(R.id.gridview);
        txtWaiterName = (TextView) findViewById(R.id.txtWaiterName);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        createViewTopBar();

        //get size screen of device
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
        itemSize = (screenWidth - 150) / 3;

        setupDrawer();
        mSocket.connect();
        mSocket.on(Constants.MESSAGE, onMessage);
        getProfile();
        getTable();
        tableList.setOnItemClickListener(this);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.GONE);
                if (tableAdapter != null) {
                    tableAdapter.clear();
                }
                getProfile();
                getTable();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off(Constants.MESSAGE);

    }

    private void setupDrawer() {
        //setup drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        drawerDataList.add(new DrawerItem(getString(R.string.logout), R.string.icon_logout));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerAdapter = new CustomDrawerAdapter(getApplicationContext(), R.layout.custom_drawer_item,
                drawerDataList);
        mDrawerList.setAdapter(drawerAdapter);
    }

    private void createViewTopBar() {
        //define font
        font_awesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        btnMenu = (Button) findViewById(R.id.btnMenu);
        btnNotification = (Button) findViewById(R.id.btnNotification);
        btnMenu.setTypeface(font_awesome);
        btnNotification.setTypeface(font_awesome);
        btnMenu.setOnClickListener(this);
        btnNotification.setOnClickListener(this);
    }


    //class of event for drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent;
            switch (position) {
                case 0:
                    //logout
                    logout();
                    break;

                default:
                    break;
            }
        }
    }

    private void getProfile() {
        APIConnection.request(this, Constants.URL_GET_PROFILE, accessToken, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString(Constants.STATUS);
                    if (status.equals(Constants.SUCCESS)) {
                        JSONObject userInfo = response.getJSONObject(Constants.RESULT);
                        user = ParseJSON.parseUser(userInfo);

                        //send username
                        JSONObject userJSON = new JSONObject();
                        userJSON.put(Constants.USERNAME, user.getUsername());
                        mSocket.emit(Constants.WAITER_SOCKET, userJSON);
                        txtWaiterName.setText(user.getName());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast(getString(R.string.request_error));
                }
            }

            @Override
            public void onError(VolleyError error) {
                showToast(getString(R.string.request_error));
            }
        });
    }

    private void getTable() {
        APIConnection.request(this, Constants.URL_GET_TABLE, accessToken, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
                try {
                    String status = response.getString(Constants.STATUS);
                    if (status.equals(Constants.SUCCESS)) {
                        JSONArray listTable = response.getJSONArray(Constants.RESULT);
                        for (int i = 0; i < listTable.length(); i++) {
                            JSONObject jsonObject = listTable.getJSONObject(i);
                            TableItem table = ParseJSON.parseTable(jsonObject);
                            tableItems.add(table);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast(getString(R.string.request_error));
                }
                tableAdapter = new TableAdapter(getApplicationContext(), itemSize, tableItems);
                tableList.setAdapter(tableAdapter);
            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
                showToast(getString(R.string.request_error));
            }
        });

    }

    private ProgressDialog pDialog;

    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("\n" + getString(R.string.logout_question) + "\n");
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(R.string.logout, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //logout here
                pDialog = new ProgressDialog(HomeActivity.this);
                pDialog.setMessage("Logout...");
                pDialog.show();
                APIConnection.request(getApplicationContext(), Constants.URL_LOGOUT, accessToken, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(Constants.ACCESS_TOKEN);
                        editor.commit();
                        pDialog.cancel();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        pDialog.cancel();
                        showToast(getString(R.string.request_error));
                    }
                });
            }
        });
        builder.create().show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(HomeActivity.this, TableActivity.class);
        intent.putExtra(Constants.TABLE_EXTRA, tableItems.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMenu:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.btnNotification:
                if (!hasNotif) {
                    tableCall = tableCallCache;
                }
                Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
                intent.putExtra(Constants.LIST_NOTIFICATION_EXTRA, tableCall);
                startActivity(intent);

                if (hasNotif) {
                    btnNotification.setTextColor(Color.WHITE);
                    tableCallCache = (ArrayList<TableItem>) tableCall.clone();
                    tableCall.clear();
                    hasNotif = false;
                } else {

                }

                break;
            default:
                break;
        }
    }

    private ArrayList<TableItem> tableCall = new ArrayList<>();
    private ArrayList<TableItem> tableCallCache = new ArrayList<>();
    private boolean hasNotif = false;
    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    hasNotif = true;
                    tableCallCache.clear();
                    Log.d("buoistudio", "onMessage: " + data.toString());
                    btnNotification.setTextColor(Color.RED);
                    try {
                        JSONObject tableJSON = data.getJSONObject(Constants.TABLE);
                        long timeCall = System.currentTimeMillis();
                        TableItem tableItem = ParseJSON.parseTable(tableJSON);
                        tableItem.setTimeCall(timeCall);
                        tableCall.add(0, tableItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    };

    private void showToast(String error) {
        Toast.makeText(getApplicationContext(), error,
                Toast.LENGTH_LONG).show();
    }
}
