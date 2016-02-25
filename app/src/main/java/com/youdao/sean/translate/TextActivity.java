package com.youdao.sean.translate;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class TextActivity extends AppCompatActivity {

    private EditText rawContent;
    private Button goButton;
    private TextView resultContent;

    private final String urlAddress = "http://api.fanyi.baidu.com/api/trans/vip/translate?";
    private final String appid = "20160128000009646";
    private final String code = "Cwz96rkC8UzQtO7vsXeD";
    private final String autoLanguage = "auto";

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
                Random random = new Random();
                int s = random.nextInt(65536)%(65536-32768+1) + 32768;
                String salt = s+"";
                String rawSign = appid+word+salt+code;
                String sign = null;
                try {
                    sign = getMD5(rawSign.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String query = urlAddress+"q="+word+"&from="+autoLanguage+"&to="+autoLanguage+"&appid="+appid+"&salt="+salt+"&sign="+sign;
                System.out.println(query);
                readResult(query);

            }
        });

    }

    public void readResult(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println(response.toString());

                                try {
                                    JSONArray jsonArray = response.getJSONArray("trans_result");
                                    JSONObject object = jsonArray.getJSONObject(0);
                                    String result = object.getString("dst");

                                    System.out.println(result);

                                    resultContent.setText(result);
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

    public static String getMD5(byte[] source) {
        String s = null;
        char [] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        final int temp = 0xf;
        final int arraySize = 32;
        final int strLen = 16;
        final int offset = 4;
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(source);
            byte [] tmp = md.digest();
            char [] str = new char[arraySize];
            int k = 0;
            for (int i = 0; i < strLen; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> offset & temp];
                str[k++] = hexDigits[byte0 & temp];
            }
            s = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

}
