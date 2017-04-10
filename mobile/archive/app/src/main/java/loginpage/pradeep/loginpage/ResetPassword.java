package loginpage.pradeep.loginpage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPassword extends AppCompatActivity {

    private Cursor c;
    private SQLiteDatabase db ;
    String result = "Asd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = getIntent();
                String email = intent.getStringExtra("email");
                String serverUrl = "http://hungrymeser.herokuapp.com/app/users/"+email;


                //using this email update password

                EditText passwod = (EditText) findViewById(R.id.resestPassword);
                EditText confirmPassword = (EditText) findViewById((R.id.confrimResetPassword));
                String pass = passwod.getText().toString();
                String confirmPass = confirmPassword.getText().toString();

                //System.out.println(confirmPass);
                if(pass.equals(confirmPass)){
                    String encryptedPassword = EncryptDec.encrypt(confirmPass);
                    System.out.println("PASS NEW" + encryptedPassword);
                    result = updatePassword(email,encryptedPassword,serverUrl);
                    Toast.makeText(getApplicationContext(), "Password reset sucessfull", Toast.LENGTH_LONG).show();
                    //GO TO MAIN PAGE
                }
                else{
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                }
                System.out.println(result);

            }
        });
        //Query using this email and update password if security question matches

    }
    public String updatePassword(String email,String encryptedPassword,String serverUrl){

        JSONObject obj = new JSONObject();
        try{
            obj.put("password",encryptedPassword);
        }catch (JSONException e){

        }
        final RequestQueue requestQueue = Volley.newRequestQueue(ResetPassword.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, serverUrl, obj, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                System.out.println("SUCESFULLY UPDATED");
                goToHome();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("ERROR in updating password");
                // TODO Auto-generated method stub

            }
        });
        requestQueue.add(jsonObjectRequest);

        return "Sucess";

    }


    public  void goToHome(){
        Intent intent = new Intent(this,LoginPage.class);
        startActivity(intent);
    }
}
