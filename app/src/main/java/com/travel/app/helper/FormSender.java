package com.travel.app.helper;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.travel.app.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 15.09.2016.
 */
public class FormSender {

    private static final String KEY_APP = "app";
    private static final String VALUE_APP = "android";
    private static final String KEY_TITLE = "title";
    private static final String VALUE_TITLE_TOUR = "дополнительная информация";
    private static final String VALUE_TITLE_AGENT = "мой агент";
    private static final String VALUE_TITLE_CONTACT = "контакты";
    private static final String KEY_NAME = "fio";
    private static final String KEY_EMAIL = "mail";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_OPTIONS = "options";

    public static void sendForm(final Context context, final String name, final String email, final String phone, final String comment, final int reqCode) {

        String formUrl = context.getString(R.string.form_send_url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, formUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, context.getString(R.string.form_send_success),Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, context.getString(R.string.form_send_error),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                String mSendValue;
                if (reqCode == 0) {
                    mSendValue = VALUE_TITLE_TOUR;
                } else if (reqCode == 1) {
                    mSendValue = VALUE_TITLE_AGENT;
                } else {
                    mSendValue = VALUE_TITLE_CONTACT;
                }
                Map<String,String> params = new HashMap<>();
                params.put(KEY_APP, VALUE_APP);
                params.put(KEY_TITLE, mSendValue);
                params.put(KEY_NAME, name);
                params.put(KEY_EMAIL, email);
                params.put(KEY_PHONE, phone);
                params.put(KEY_OPTIONS, comment);
                return params;
            }
        };

        VolleyRequestQueue.getInstance(context).addToRequestQueue(stringRequest);
    }
}
