package com.buoistudio.connect;

import com.buoistudio.data.Constants;
import com.buoistudio.model.DishItem;
import com.buoistudio.model.DishOrder;
import com.buoistudio.model.ExtraFeeItem;
import com.buoistudio.model.TableItem;
import com.buoistudio.model.UserItem;

import org.json.JSONException;
import org.json.JSONObject;

public class ParseJSON {
    public static UserItem parseUser(JSONObject response) {
        String username = "", name = "", email = "";
        try {
            username = response.getString(Constants.USERNAME);
            name = response.getString(Constants.NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UserItem user = new UserItem(username, name, email);
        return user;
    }

    public static TableItem parseTable(JSONObject response) {
        String id = "", name = "";
        int slotNumber = 0;

        try {
            id = response.getString(Constants.ID);
            name = response.getString(Constants.NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TableItem table = new TableItem(id, name);
        return table;
    }

    public static DishOrder parseDishOrder(JSONObject response) {
        String id = "";
        int count = 0, status = 0;
        DishItem dish = new DishItem();

        try {
            id = response.getString(Constants.ID);
            dish = parseDish(response.getJSONObject(Constants.DISH));
            count = response.getInt(Constants.COUNT);
            status = response.getInt(Constants.STATUS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DishOrder dishOrder = new DishOrder(id, dish, count, status);
        return dishOrder;
    }

    public static DishItem parseDish(JSONObject response) {
        String name = "", image = "";
        double price = 0;

        try {
            name = response.getString(Constants.NAME);
            price = response.getDouble(Constants.PRICE);
            image = response.getString(Constants.IMAGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DishItem dish = new DishItem(name, price, image);
        return dish;

    }

    public static ExtraFeeItem parseExtraFee(JSONObject response) {
        String id = "", name = "";
        double price = 0;

        try {
            id = response.getString(Constants.ID);
            name = response.getString(Constants.NAME);
            price = response.getDouble(Constants.PRICE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ExtraFeeItem extraFee = new ExtraFeeItem(id, name, price);
        return extraFee;
    }
}
