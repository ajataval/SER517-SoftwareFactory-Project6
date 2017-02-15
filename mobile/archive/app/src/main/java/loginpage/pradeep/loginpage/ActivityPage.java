package loginpage.pradeep.loginpage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;



public class ActivityPage extends AppCompatActivity   {


    private static final String TAG = "SigninActivity";
    private static final int RC_SIGN_IN = 9001;
    TextView statusTextView;
    GoogleApiClient mGoogleApiClient;
    Button signOutButton;

    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
//        String name = intent.getStringExtra("username");
//        Log.d("name inside nextpage",name);
//        TextView textView = new TextView(this);
//        textView.setText(name);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
    }





}
