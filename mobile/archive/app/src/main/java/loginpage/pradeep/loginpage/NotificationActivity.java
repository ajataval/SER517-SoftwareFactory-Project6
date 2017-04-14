package loginpage.pradeep.loginpage;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        NotificationManager notificationManager = null;


        Intent intent = getIntent();
        String notificationTitle =  intent.getStringExtra("notificationTitle");
        String body = intent.getStringExtra("body");
        String end_time = intent.getStringExtra("end_time");
        String start_time = intent.getStringExtra("start_time");
        String address = intent.getStringExtra("address");


        TextView textView = (TextView) findViewById(R.id.notificationTitle);
        textView.setText(notificationTitle);

        TextView textBody = (TextView) findViewById(R.id.body);
        textBody.setText(body);

        TextView adsView = (TextView) findViewById(R.id.address);
        adsView.setText(address);

        TextView start = (TextView) findViewById(R.id.start_time);
        start.setText(start_time);

        TextView end = (TextView) findViewById(R.id.end_time);
        end.setText(end_time);




    }
}
