package com.buoistudio.data;


public class Constants {
    public static final String TABLE_ID_REPLACE = "TABLE_ID";
    public static final String EXTRA_FEE_ID_REPLACE = "DISH_ID";
    public static final String DISH_ID_REPLACE = "DISH_ID";

    public static final String URL_SERVER = "http://52.25.229.209:3000";

    public static final String URL_GET_TOKEN = URL_SERVER + "/access-token";
    public static final String URL_LOGOUT = URL_SERVER + "/waitter/logout";
    public static final String URL_GET_TABLE = URL_SERVER + "/waitter/tables";
    public static final String URL_GET_PROFILE = URL_SERVER + "/waitter/profile";
    public static final String URL_GET_ORDER_OF_TABLE = URL_SERVER + "/waitter/" + TABLE_ID_REPLACE + "/orders";
    public static final String URL_UPDATE_STATUS = URL_SERVER + "/waitter/orders/status";
    public static final String URL_UPDATE_QTY = URL_SERVER + "/waiter/" + TABLE_ID_REPLACE + "/order/" + DISH_ID_REPLACE;
    public static final String URL_REMOVE_DISH = URL_SERVER + "/waiter/" + TABLE_ID_REPLACE + "/order/" + DISH_ID_REPLACE;
    public static final String URL_GET_EXTRA_FEE = URL_SERVER + "/waiter/" + TABLE_ID_REPLACE + "/extrafees";
    public static final String URL_ADD_EXTRA_FEE = URL_SERVER + "/waiter/" + TABLE_ID_REPLACE + "/extrafees";
    public static final String URL_DELETE_EXTRA_FEE = URL_SERVER + "/waiter/" + TABLE_ID_REPLACE + "/extrafees/" + EXTRA_FEE_ID_REPLACE;
    public static final String URL_IMAGE = URL_SERVER + "/uploads/thumbs/";


    //user info
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";

    //table info
    public static final String ID = "id";
    public static final String SLOT_NUMBER = "slot_number";

    //json filed
    public static final String STATUS = "status";
    public static final String SUCCESS = "success";
    public static final String RESULT = "result";
    public static final String COUNT = "count";
    public static final String PRICE = "price";
    public static final String IMAGE = "image";
    public static final String DISH = "dish";
    public static final String DISHES = "dishes";
    public static final String DISHES_STATUS = "dishes_status";
    public static final String TABLE = "table";
    public static final String ACCESS_TOKEN = "access_token";

    public static final String WAITER_PREFERENCES = "waiter_preferences";

    public static final int SERVED = 1;
    public static final int UNSERVED = 0;

    //intent keyword
    public static final String TABLE_EXTRA = "TABLE";
    public static final String LIST_NOTIFICATION_EXTRA = "LIST_NOTIFICATION";

    public static final String WAITER_SOCKET = "waiter-join";
    public static final String MESSAGE = "message";


}
