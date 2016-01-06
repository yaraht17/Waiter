package com.buoistudio.connect;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.buoistudio.data.Constants;
import com.buoistudio.model.DishOrder;
import com.buoistudio.model.ExtraFeeItem;
import com.buoistudio.model.TableItem;
import com.buoistudio.model.UserItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class APIConnection {

    public static void login(Context context, final UserItem user, final VolleyCallback callback) throws JSONException {

        final JSONObject jsonBody = new JSONObject();
        jsonBody.put(Constants.USERNAME, user.getUsername());
        jsonBody.put(Constants.PASSWORD, user.getPassword());

        Log.d("buoistudio", "json send: " + jsonBody.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.URL_GET_TOKEN, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("buoistudio", "update user: " + response);
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("buoistudio", "update err: " + error.toString());
                        callback.onError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("charset", "utf-8");
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void request(Context context, String url, final String accessToken, final VolleyCallback callback) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("buoistudio", response.toString());
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("buoistudio", error.toString());
                        callback.onError(error);
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "access_token " + accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void updateDishOrder(Context context, ArrayList<DishOrder> dishOrders, final String accessToken, final VolleyCallback callback) throws JSONException {
        final JSONObject jsonBody = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        for (DishOrder dishOrder : dishOrders) {
            JSONObject dish = new JSONObject();
            dish.put(Constants.ID, dishOrder.getId());
            dish.put(Constants.STATUS, dishOrder.getStatus());
            jsonArray.put(dish);
        }
        jsonBody.put(Constants.DISHES_STATUS, jsonArray);

        Log.d("buoistudio", "json send: " + jsonBody.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, Constants.URL_UPDATE_STATUS, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("buoistudio", "update status: " + response);
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("buoistudio", "update err: " + error.toString());
                        callback.onError(error);
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "access_token " + accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void updateDishQty(Context context, DishOrder dishOrder, TableItem table, int qty, final String accessToken, final VolleyCallback callback) throws JSONException {
        final JSONObject jsonBody = new JSONObject();
        jsonBody.put(Constants.COUNT, qty);
        String url = Constants.URL_UPDATE_QTY;
        url = url.replace(Constants.TABLE_ID_REPLACE, table.getId());
        url = url.replace(Constants.DISH_ID_REPLACE, dishOrder.getId());
        Log.d("buoistudio", "URL: " + url);
        Log.d("buoistudio", "token: " + accessToken);
        Log.d("buoistudio", "json send: " + jsonBody.toString());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("buoistudio", "update status: " + response);
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("buoistudio", "update err: " + error.toString());
                        callback.onError(error);
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "access_token " + accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void removeDishOrder(Context context, DishOrder dishOrder, TableItem table, final String accessToken, final VolleyCallback callback) {
        String url = Constants.URL_REMOVE_DISH;
        url = url.replace(Constants.TABLE_ID_REPLACE, table.getId());
        url = url.replace(Constants.DISH_ID_REPLACE, dishOrder.getId());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("buoistudio", "update status: " + response);
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("buoistudio", "update err: " + error.toString());
                        callback.onError(error);
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "access_token " + accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void addExtraFee(Context context, ExtraFeeItem extraFee, TableItem table, final String accessToken, final VolleyCallback callback) throws JSONException {
        final JSONObject jsonBody = new JSONObject();
        jsonBody.put(Constants.NAME, extraFee.getName());
        jsonBody.put(Constants.PRICE, extraFee.getPrice());
        Log.d("buoistudio", "json send: " + jsonBody.toString());
        String url = Constants.URL_ADD_EXTRA_FEE;
        url = url.replace(Constants.TABLE_ID_REPLACE, table.getId());
        Log.d("buoistudio", "URL: " + url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("buoistudio", "update status: " + response);
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("buoistudio", "update err: " + error.toString());
                        callback.onError(error);
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "access_token " + accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void getExtraFee(Context context, TableItem table, final String accessToken, final VolleyCallback callback) {
        String url = Constants.URL_GET_EXTRA_FEE;
        url = url.replace(Constants.TABLE_ID_REPLACE, table.getId());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("buoistudio", "update status: " + response);
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("buoistudio", "update err: " + error.toString());
                        callback.onError(error);
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "access_token " + accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public static void deleteExtraFee(Context context, TableItem table, ExtraFeeItem extraFee, final String accessToken, final VolleyCallback callback) {
        String url = Constants.URL_DELETE_EXTRA_FEE;
        url = url.replace(Constants.TABLE_ID_REPLACE, table.getId());
        url = url.replace(Constants.EXTRA_FEE_ID_REPLACE, extraFee.getId());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("buoistudio", "update status: " + response);
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("buoistudio", "update err: " + error.toString());
                        callback.onError(error);
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "access_token " + accessToken);
                return headers;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }
}
