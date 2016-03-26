package com.youdao.sean.translate;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TextActivity extends AppCompatActivity {

    private EditText rawContent;
    private Button goButton;
    private TextView resultContent;

    private final String urlAddress = "http://fanyi.youdao.com/openapi.do?keyfrom=translatetion&key=660729186&type=data&doctype=json&version=1.1&q=";

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        rawContent = (EditText) findViewById(R.id.rawText);
        goButton = (Button) findViewById(R.id.goTranslate);
        resultContent = (TextView) findViewById(R.id.translate_result);

        mQueue = Volley.newRequestQueue(this);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String word = rawContent.getText().toString();

                try {
                    word = URLEncoder.encode(word, "utf-8"); //先对中文进行UTF-8编码
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String query = urlAddress + word;
                System.out.println(query);
                readResult(query);

            }
        });

    }

    public void readResult(String url) {
        JsonObjectRequest jsonObjectRequest = new CharsetJsonRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String resp = "";
                                try {
                                    resp = URLDecoder.decode(response.toString(), "UTF-8");
                                    System.out.println(resp);
                                    System.out.println(response.toString());
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                //System.out.println(resp);

                                try {
                                    String errcode = response.getString("errorCode");
                                    if (Integer.parseInt(errcode) != 0) {
                                        Toast.makeText(TextActivity.this, "error, please try again",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        String direct_translation = response.getString("translation");
                                        direct_translation = direct_translation.replace("\"","");
                                        direct_translation = direct_translation.replace("[", "");
                                        direct_translation = direct_translation.replace("]", "");
                                        resultContent.setText(direct_translation);
                                        System.out.println(direct_translation);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                    }
                });
        mQueue.add(jsonObjectRequest);
    }

    /*public String getMD5(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest();
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }*/


}
