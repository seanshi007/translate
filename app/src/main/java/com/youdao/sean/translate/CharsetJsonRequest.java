package com.youdao.sean.translate;

/**
 * Created by sean on 16-2-28.
 */
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

//如果返回头中没有Charset，默认UTF-8
public class CharsetJsonRequest extends JsonObjectRequest {

    /*public CharsetJsonRequest(String url, JSONObject jsonRequest,
                              Listener<JSONObject> listener, ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }*/

    public CharsetJsonRequest(int method, String url, JSONObject jsonRequest,
                              Listener<JSONObject> listener, ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

}