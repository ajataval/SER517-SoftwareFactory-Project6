package loginpage.pradeep.loginpage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by pradeepbalakrishnan on 4/1/17.
 */

public class LoginRequest extends AsyncTask<String, Integer, Void> {

    String username;
    String password;
    private LoginPage loginpage;
    private ProgressDialog dialog;


    public LoginRequest(LoginPage resultClass, String username, String password)
    {
        loginpage = resultClass;
        this.username = username;
        this.password = EncryptDec.encrypt(password);
        Log.d("userpassword=",this.password);
        dialog = new ProgressDialog(resultClass);
        dialog.setMessage("Login in...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false); // this was deprecated as of SDK 23

        dialog.show();

    }

//    @Override
//    protected void onPreExecute() {
//
//        username = LoginPage.uname;
//        password = LoginPage.pword;
//
//
//    }

    @Override
    protected Void doInBackground(String... params ) {



        String resultString = null;

        String encodedSearch;
        String urlString;
        URL url;
        String tr = "true";
        HttpURLConnection connection = null;
        InputStreamReader stream;
        BufferedReader buffer;

        try {




            // format the search query to be used in a URL
            // encodedSearch = URLEncoder.encode(, "UTF-8");

            String serverUrl = "http://hungrymeser.herokuapp.com/app/login?username=" + username + "&password=" + password;
            url = new URL(serverUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // confirm that the connection returned a response code of 200 (OK)
            // TicketMaster API returns response code 401 when the key is invalid
            if (connection.getResponseCode() == 200) {

                // convert connection results to String
                stream = new InputStreamReader(connection.getInputStream());
                buffer = new BufferedReader(stream);

                resultString = "";
                String line;
                while ((line = buffer.readLine()) != null) {
                    resultString += line;
                }

            } else if(connection.getResponseCode() == 401)  {

                resultString = "false";

            }else
            {
                resultString = "servererror";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e) {
                    // disconnect error
                }
            }
        }

        loginpage.flag = resultString.contains(tr)+"";

        return null;
    }



    @Override
    protected void onPostExecute(Void para) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        loginpage.dataBaseStatus();

    }

}




