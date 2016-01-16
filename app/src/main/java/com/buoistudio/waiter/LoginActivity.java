package com.buoistudio.waiter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.buoistudio.connect.APIConnection;
import com.buoistudio.connect.VolleyCallback;
import com.buoistudio.data.Constants;
import com.buoistudio.model.UserItem;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private EditText etUser, etPass;
    private ProgressDialog pDialog;


    private SharedPreferences sharedPreferences;
    private String accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);


        btnLogin = (Button) findViewById(R.id.btnLogin);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);

        sharedPreferences = getSharedPreferences(Constants.WAITER_PREFERENCES, Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, "");

        checkLogin();


        btnLogin.setOnClickListener(this);

    }

    private void checkLogin() {
        if (accessToken.equals("")) {
            //login
        } else {
            intentHome();
        }
    }

    private void intentHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            showDialog();
            String username = etUser.getText().toString();
            String password = etPass.getText().toString();
            if (username.equals("") && password.equals("")) {
                showToast(getString(R.string.login_alert));
            }
            UserItem user = new UserItem(username, password);
            try {
                APIConnection.login(this, user, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        hideDialog();
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equals(Constants.SUCCESS)) {
                                //save token
                                accessToken = response.getString(Constants.ACCESS_TOKEN);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Constants.ACCESS_TOKEN, accessToken);
                                editor.commit();
                                intentHome();
                            } else {
                                showToast(getString(R.string.login_fail));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showToast(getString(R.string.request_error));
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        hideDialog();
                        showToast(getString(R.string.request_error));
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Login...");
        pDialog.show();
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
    }

    private void hideDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void showToast(String error) {
        Toast.makeText(getApplicationContext(), error,
                Toast.LENGTH_LONG).show();
    }


}
