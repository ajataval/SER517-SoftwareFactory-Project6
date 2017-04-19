package loginpage.pradeep.loginpage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by pradeepbalakrishnan on 4/1/17.
 */

public class LoginRequest extends AsyncTask<String, Integer, Void> {

    // API values (API will only grab values from Arizona, to reduce information overload)
    private static final String loginUrl ="http://hungrymeser.herokuapp.com/app/login?";
    //private static final String STATE_CODE = "&stateCode=AZ";


    private LoginPage loginPage;
    private String username;
    private String password;
    private String resultString = "false";
    private String flag = "false";


    private ProgressDialog dialog;

    public LoginRequest(LoginPage resultClass, String username, String password) {
        this.loginPage = resultClass;
        this.username = username;
        this.password = EncryptDec.encrypt(password);
    }

    @Override
    protected void onPreExecute() {

        dialog = new ProgressDialog(loginPage);
        dialog.setMessage("Loging in..");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false); // this was deprecated as of SDK 23

        dialog.show();
    }

    /**
     * @param params - we do not use this
     *
     * @return jsonObject - holds search results from TicketMaster API
     *                    - null if results were not returned
     */
    @Override
    protected Void doInBackground(String... params) {

        // check if the app has access to the internet
        if (ContextCompat.checkSelfPermission(
                loginPage.getContext(), android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            return null;
        }


        String urlString = loginUrl +"username="+username +"&password="+password;

//        String encodedSearch;
//        String urlString;
//        URL url;
//
//        HttpURLConnection connection = null;
//        InputStreamReader stream;
//        BufferedReader buffer;
//
//        try {
//
//            // format the search query to be used in a URL
//
//
//            urlString = loginUrl +"username="+username +"&password="+password;
//            url = new URL(urlString);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.connect();
//
//            // confirm that the connection returned a response code of 200 (OK)
//            // TicketMaster API returns response code 401 when the key is invalid
//            if (connection.getResponseCode() == 200) {
//
//                // convert connection results to String
//                stream = new InputStreamReader(connection.getInputStream());
//                buffer = new BufferedReader(stream);
//
//                resultString = "";
//                String line;
//                while ((line = buffer.readLine()) != null) {
//                    resultString += line;
//                }
//
//            } else if (connection.getResponseCode() == 401) {
//                dialog.setMessage("Invalid API key");
//            } else {
//                dialog.setMessage("Error Getting Data From Server");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//
//            if (connection != null) {
//                try {
//                    connection.disconnect();
//                } catch (Exception e) {
//                    // disconnect error
//                }
//            }
//        }


        final RequestQueue requestQueue = Volley.newRequestQueue(loginPage);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlString, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {

                if(response.length()==0){
                    resultString = "false";
                    loginPage.dataBaseStatus(resultString,flag);
                }
                else{

                    try{


                        if(response.getString("result").equalsIgnoreCase("true")){
                            resultString = "true";
                            flag = "true";
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                                }
                            loginPage.dataBaseStatus(resultString, flag);
                        }
                        else {
                            flag = "true";
                            resultString = "false";
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            loginPage.dataBaseStatus(resultString, flag);
                        }
                    }catch (JSONException e){
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                       resultString = "false";
                        loginPage.dataBaseStatus(resultString, "true");
                    }
                }

            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        Log.d("Response Header",response.headers.toString());
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
                else if (response.statusCode == 401)
                {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    resultString = "false";
                    loginPage.dataBaseStatus(resultString, "true");
                }

            }




        });
        requestQueue.add(jsonObjectRequest);


        return null;
    }

//    @Override
//    protected void onPostExecute(String resultString) {
//
//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
//
//
//
//        loginPage.dataBaseStatus(resultString,flag);
//    }
}




