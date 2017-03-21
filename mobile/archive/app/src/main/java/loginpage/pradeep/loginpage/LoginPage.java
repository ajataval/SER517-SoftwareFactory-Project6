package loginpage.pradeep.loginpage;




import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;



public class LoginPage extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,  View.OnClickListener
{



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

    private SQLiteDatabase db ;



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

//            signOutButton = (Button) findViewById(R.id.signOutButton);
//            signOutButton.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Context context = getApplicationContext();
        String s = String.valueOf(context.getDatabasePath("Anoop.db"));
        db = getApplicationContext().openOrCreateDatabase(s,MODE_APPEND,null);
        login = (Button) findViewById(R.id.button);

        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                username = (EditText) findViewById(R.id.username);
                password = (EditText) findViewById(R.id.password);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                String uname = username.getText().toString();
                String pword = password.getText().toString();



                editor.putString("Name",uname);
                editor.commit();

                boolean loginStatus = openDatabase(uname,pword);
                onEncry();
                if(loginStatus)
                {
                    Log.d("U have entered",uname);
                    Log.d("U have entered", pword);
                    contentPage(uname);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
                }
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
            case R.id.signOutButton:
                signOut();
                break;
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


    public void onEncry(){
        Intent intent = new Intent(this,EncryptDec.class);
        startActivity(intent);
    }
    private boolean openDatabase(String username, String password ){
        String SELECT_SQL = "SELECT EmailID,Password FROM SIGNINFO WHERE EmailID="+"'"+username+"' "+"AND Password="+"'"+password+"'";
        String itemname =null;
        String itmepass = null;
        c = db.rawQuery(SELECT_SQL,null);


        if(c.moveToFirst())
        {

            itemname =  c.getString(c.getColumnIndex("EmailID"));
            itmepass = c.getString(c.getColumnIndex("Password"));
            return  true;
        }
//         System.out.print("Password"+itmepass);
//         System.out.print("Name"+itemname);
        return false;
    }
}


