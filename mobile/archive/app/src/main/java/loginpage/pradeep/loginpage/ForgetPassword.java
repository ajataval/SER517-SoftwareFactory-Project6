package loginpage.pradeep.loginpage;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import com.android.volley.RequestQueue;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPassword extends AppCompatActivity {

    private Cursor c;
    private SQLiteDatabase db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
 //       getActionBar().setDisplayHomeAsUpEnabled(true);
        final Button readEmail = (Button) findViewById(R.id.readEmail);
        readEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //SEND TO SERVER AND GET DATA, GO TO NEXT SCREEN

                EditText email = (EditText) findViewById(R.id.recoveryemail);
                String emailID = email.getText().toString();
                String serverUrl = "http://hungrymeser.herokuapp.com/app/users/"+emailID;
                checkEmailId(emailID,serverUrl);

//                else
//                {
//                    Toast.makeText(getApplicationContext(), "Invalid emailID", Toast.LENGTH_LONG).show();
//                }


            }
        });
    }

    public void checkEmailId(String emailId,String serverUrl){

        final RequestQueue requestQueue = Volley.newRequestQueue(ForgetPassword.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, serverUrl, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                System.out.println("EMAIL RESPONSE" + response);
                if(response.length()==0){
                    Toast.makeText(getApplicationContext(), "Invalid emailID", Toast.LENGTH_LONG).show();
                }
                else{

                    try{
                        String emailID = response.getString("username");
                        String secQues = response.getString("securityQuest");
                        goToSecurityScreen(emailID,secQues);
                    }catch (JSONException e){

                    }
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("ERROR in getting JSON object");
                // TODO Auto-generated method stub

            }
        });
        requestQueue.add(jsonObjectRequest);

    }
    public void goToSecurityScreen(String emailID,String secuQuest)
    {
        // SEND SECURITY QUESTION ALSO
        Intent intent = new Intent(this,SecurityQuestion.class);
        intent.putExtra("email",emailID);
        intent.putExtra("securityQuestion",secuQuest);
        startActivity(intent);
    }



}
