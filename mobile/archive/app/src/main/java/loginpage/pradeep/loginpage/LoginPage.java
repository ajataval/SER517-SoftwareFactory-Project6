package loginpage.pradeep.loginpage;




import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URLEncoder;



public class LoginPage extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,  View.OnClickListener
{


    String resultString = null;
    private Cursor c;
    //variables used for google login
    SignInButton signInButton;
    Button signOutButton;
    TextView statusTextView;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SigninActivity";
    private static final int RC_SIGN_IN = 9001;
    Intent signInIntent;

    public static final String MyPREFERENCES = "MyPrefs" ;
    EditText username;
    EditText password;
    Button login;
    TextView fgtpassword ;
    TextView sign_up;
    SharedPreferences sharedpreferences;

    static String pword = "";
    static String uname = "";

    private SQLiteDatabase db ;

    String flag = "";
    JSONObject jsonObj = new JSONObject();
    JSONObject[] obj = {new JSONObject()};
    private LocationManager locationManager;
    private LocationListener listener;
    double longitude;
    double latitude;
    //String url = "http://hungrymeser.herokuapp.com/app/login?";



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        //UNCOMMENT statusTextView = (TextView) findViewById(R.id.status_textview);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Context context = getApplicationContext();

        login = (Button) findViewById(R.id.button);

        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                username = (EditText) findViewById(R.id.username);
                password = (EditText) findViewById(R.id.password);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                uname = username.getText().toString();
                pword = password.getText().toString();



                editor.putString("Name",uname);
                editor.commit();

                LoginRequest loginRequest = new LoginRequest(LoginPage.this,uname,pword);
                loginRequest.execute();



            }
        });



        fgtpassword = (TextView) findViewById(R.id.activity_forget_password);

        fgtpassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                forgetPassword(view);
            }
        });

        sign_up = (TextView) findViewById(R.id.sign_up);

        sign_up.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                signUpUser(view);
            }
        });

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
            /*case R.id.signOutButton:
                signOut();
                break;*/
        }
    }

    private void signIn(){
        signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            System.out.println("Email"+acct.getEmail());
            onGoogleSignin();

        }
        else{

        }
    }


    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.d(TAG, "onConnectionFailed:" + connectionResult );
    }
    private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {

            }
        });
    }

    private void contentPage(String username) {

        Intent intent = new Intent(this,ActivityPage.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    public void forgetPassword(View view) {
        Intent intent = new Intent(this,ForgetPassword.class);
        startActivity(intent);
    }

    public void signUpUser(View view) {
        Intent intent = new Intent(this, SignUpPage.class);
        startActivity(intent);
    }

    public void onGoogleSignin(){
        Intent intent = new Intent(this, ActivityPage.class);
        startActivity(intent);
    }



    protected Context getContext() {
        return getApplicationContext();
    }

    protected void  dataBaseStatus(String val, String flag)
    {
        if(val.contains("server error") && flag == "true")
        {
            Toast.makeText(getApplicationContext(), "Try later", Toast.LENGTH_LONG).show();

        }
        else if(val.contains("true") && flag == "true" )
        {
            contentPage(uname);
        }
        else if(val.contains("false") && flag == "true")
        {
            Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
        }

    }

}




