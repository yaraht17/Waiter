package com.buoistudio.waiter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.buoistudio.adapter.DishAdapter;
import com.buoistudio.connect.APIConnection;
import com.buoistudio.connect.ParseJSON;
import com.buoistudio.connect.VolleyCallback;
import com.buoistudio.data.Constants;
import com.buoistudio.model.DishOrder;
import com.buoistudio.model.ExtraFeeItem;
import com.buoistudio.model.TableItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TableActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView listDish;
    private ArrayList<DishOrder> dishItems = new ArrayList();
    private DishAdapter dishAdapter;
    private Button btnMenu, btnNotification;
    private Typeface font_awesome;

    private Button btnConfirm, btnExtraFee;
    private TextView txtTableName, txtTotal;
    private TableItem table;
    private SharedPreferences sharedPreferences;
    private String accessToken;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        sharedPreferences = getSharedPreferences(Constants.WAITER_PREFERENCES, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        table = (TableItem) getIntent().getSerializableExtra(Constants.TABLE_EXTRA);
        createViewTopBar();
        listDish = (ListView) findViewById(R.id.listDish);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnExtraFee = (Button) findViewById(R.id.btnExtraFee);
        txtTableName = (TextView) findViewById(R.id.txtTableName);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtTableName.setText(table.getName());
        getOrderOfTable();

        btnConfirm.setOnClickListener(this);
        btnExtraFee.setOnClickListener(this);
        listDish.setOnItemClickListener(this);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.GONE);
                refresh();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
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

    private void refresh() {
        progressBar.setVisibility(View.VISIBLE);
        dishAdapter.clear();
        getOrderOfTable();

    }

    private double totalDishOrder = 0;
    private double totalExtraFee = 0;

    private void getOrderOfTable() {
        String url = Constants.URL_GET_ORDER_OF_TABLE;
        url = url.replace(Constants.TABLE_ID_REPLACE, table.getId());
        Log.d("buoistudio", "URL: " + url);
        //get Dish Order for table
        APIConnection.request(this, url, accessToken, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {

                try {
                    String status = response.getString(Constants.STATUS);
                    if (status.equals(Constants.SUCCESS)) {
                        JSONArray result = response.getJSONArray(Constants.RESULT);
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject jsonObject = result.getJSONObject(i);
                            JSONArray dishes = jsonObject.getJSONArray(Constants.DISHES);
                            for (int j = 0; j < dishes.length(); j++) {
                                DishOrder dishOrder = ParseJSON.parseDishOrder(dishes.getJSONObject(j));
                                dishItems.add(dishOrder);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast(getString(R.string.request_error));
                }
                totalDishOrder = getTotal(dishItems);
                //get Extra Fee for table
                APIConnection.getExtraFee(getApplicationContext(), table, accessToken, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        swipeContainer.setRefreshing(false);
                        try {
                            String status = response.getString(Constants.STATUS);
                            double total = 0;
                            if (status.equals(Constants.SUCCESS)) {
                                JSONArray result = response.getJSONArray(Constants.RESULT);
                                for (int i = 0; i < result.length(); i++) {
                                    ExtraFeeItem extraFee = ParseJSON.parseExtraFee(result.getJSONObject(i));
                                    total += extraFee.getPrice();
                                    DishOrder dishOrder = new DishOrder(extraFee, true);
                                    dishItems.add(dishOrder);
                                }
                                totalExtraFee = total;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast(getString(R.string.request_error));
                        }
                        dishAdapter = new DishAdapter(getApplicationContext(), -1, dishItems);
                        listDish.setAdapter(dishAdapter);
                        txtTotal.setText(String.valueOf(totalDishOrder + totalExtraFee));
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        swipeContainer.setRefreshing(false);
                        showToast(getString(R.string.request_error));
                    }
                });
            }

            @Override
            public void onError(VolleyError error) {
                swipeContainer.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                showToast(getString(R.string.request_error));
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnExtraFee:
                extraFee();
                break;
            case R.id.btnMenu:
                finish();
                break;
            case R.id.btnConfirm:
                confirm();
                break;
            default:
                break;
        }
    }

    private void extraFee() {
        final Dialog dialog = new Dialog(TableActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_extrafee);
        Button btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
        final EditText edtExtraFeeName = (EditText) dialog.findViewById(R.id.edtExtraFeeName);
        final EditText edtExtraFeePrice = (EditText) dialog.findViewById(R.id.edtExtraFeePrice);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtExtraFeeName.getText().toString();
                String priceText = edtExtraFeePrice.getText().toString();
                if (name.equals("") || priceText.equals("")) {
                    showToast(getString(R.string.extra_fee_alert));
                    return;
                }
                double price = Double.parseDouble(priceText);
                ExtraFeeItem extraFee = new ExtraFeeItem(name, price);
                progressBar.setVisibility(View.VISIBLE);
                try {
                    APIConnection.addExtraFee(getApplicationContext(), extraFee, table, accessToken, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            dialog.dismiss();
                            refresh();
                        }

                        @Override
                        public void onError(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            showToast(getString(R.string.request_error));
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }

    private void confirm() {
        if (dishAdapter == null) return;
        try {
            APIConnection.updateDishOrder(this, dishAdapter.getDishOrder(), accessToken, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        String status = response.getString(Constants.STATUS);
                        if (status.equals(Constants.SUCCESS)) {
                            refresh();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast(getString(R.string.request_error));
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    showToast(getString(R.string.request_error));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void update(DishOrder dish, int qty) {
        try {
            APIConnection.updateDishQty(getApplicationContext(), dish, table, qty, accessToken, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    refresh();
                }

                @Override
                public void onError(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    showToast(getString(R.string.request_error));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            showToast(getString(R.string.request_error));
        }
    }

    private void remove(DishOrder dish) {
        APIConnection.removeDishOrder(getApplicationContext(), dish, table, accessToken, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                refresh();
            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                showToast(getString(R.string.request_error));
            }
        });
    }

    private double getTotal(ArrayList<DishOrder> list) {
        double total = 0;
        for (DishOrder dishOrder : list) {
            total += dishOrder.getDish().getPrice() * dishOrder.getCount();
        }
        return total;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("buoistudio", "Click Item");
        final DishOrder dishOrder = (DishOrder) dishItems.get(position);
        if (!dishOrder.isExtraFee()) {
            final Dialog dialog = new Dialog(TableActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_update_dish);
            TextView txtSub, txtPlus, txtDishName;
            Button btnUpdate, btnRemove;
            final EditText edQty;
            btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
            btnRemove = (Button) dialog.findViewById(R.id.btnRemove);
            edQty = (EditText) dialog.findViewById(R.id.edQty);
            txtDishName = (TextView) dialog.findViewById(R.id.txtDishName);
            txtSub = (TextView) dialog.findViewById(R.id.txtSub);
            txtPlus = (TextView) dialog.findViewById(R.id.txtPlus);
            txtDishName.setText(dishOrder.getDish().getName());

            edQty.setText(String.valueOf(dishOrder.getCount()));
            txtSub.setTypeface(font_awesome);
            txtPlus.setTypeface(font_awesome);
            txtSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty = Integer.parseInt(edQty.getText().toString());
                    if (qty > 1) {
                        qty--;
                    }
                    edQty.setText(qty + "");
                }
            });
            txtPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty = Integer.parseInt(edQty.getText().toString());
                    if (qty < 100) {
                        qty++;
                    }
                    edQty.setText(qty + "");
                }
            });
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty = Integer.parseInt(edQty.getText().toString());
                    dialog.dismiss();
                    update(dishOrder, qty);
                }
            });
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);
                    builder.setMessage("\n" + getString(R.string.remove_question) + "\n");
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder.setNegativeButton(R.string.remove, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            remove(dishOrder);
                        }
                    });
                    builder.create().show();

                }
            });
            dialog.show();
        } else {
            //show dialog delete
            setDialog(dishOrder.getExtraFee());
        }
    }


    public void setDialog(final ExtraFeeItem extraFee) {
        String[] items = {getString(R.string.delete)};
        AlertDialog.Builder dialog = new Builder(this);
        dialog.setTitle(extraFee.getName());
        dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);
                                builder.setMessage("\n" + getString(R.string.delete_question) + "\n");
                                builder.setPositiveButton(getString(R.string.cancel),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                builder.setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                APIConnection.deleteExtraFee(getApplicationContext(), table, extraFee, accessToken, new VolleyCallback() {
                                                    @Override
                                                    public void onSuccess(JSONObject response) {
                                                        refresh();
                                                    }

                                                    @Override
                                                    public void onError(VolleyError error) {
                                                        showToast(getString(R.string.request_error));
                                                    }
                                                });
                                            }
                                        }
                                );
                                builder.create().show();
                                break;
                        }
                    }
                }
        );
        dialog.show();
    }

    private void showToast(String error) {
        Toast.makeText(getApplicationContext(), error,
                Toast.LENGTH_LONG).show();
    }
}