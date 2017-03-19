package loginpage.pradeep.loginpage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;



public class ActivityPage extends LoginPage {

    TextView statusTextView;

    // server url
    String server_url = "https://hungrymeser.herokuapp.com";
    private TextView servtext;

    protected void onCreate(final Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        //fetching data from sever

//        final RequestQueue requestQueue = Volley.newRequestQueue(ActivityPage.this);
//        servtext = (TextView) findViewById(R.id.status_textview);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, server_url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        servtext.setText(response);
//                        requestQueue.stop();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                servtext.setText("Something went wrong");
//                error.printStackTrace();
//                requestQueue.stop();
//            }
//        }
//        );
//        requestQueue.add(stringRequest);

        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton = (Button) findViewById(R.id.gsignOut);

        signOutButton.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()) {

            case R.id.gsignOut:
                signOut();
                break;

            case R.id.signOutButton:
                break;
        }

    }

    public void logout(View view){

        SharedPreferences sharedpreferences = getSharedPreferences(LoginPage.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
        backToHome();

    }


    public void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                backToHome();
            }
        });

    }


    private void backToHome(){
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }




}
