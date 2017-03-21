package loginpage.pradeep.loginpage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class menu_Details extends AppCompatActivity {

    public RatingBar ratingBar;
    EditText comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__details);
        Intent intent = getIntent();
        String Description = intent.getStringExtra("DESC");
        TextView Desc = (TextView) findViewById(R.id.Desc);
        //Desc.setText(Description);

        Desc.setText("asdasdasdasdsadasda" +
                "asdasdasdasdasdasdasd" +
                "asdasdasdasdasd" +
                "asdasdasdasdasd" +
                "asdasdasdasdasdasd");

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        ratingBar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                rateMe(view);

            }
        });;




        Button submit = (Button) findViewById(R.id.SumitReview);

        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                comment = (EditText) findViewById(R.id.Comment);


                String userComment = comment.getText().toString();
                System.out.println(userComment + "  WRITE THIS  " + ratingBar.getRating());

            }
        });;

    }
    public void rateMe(View view){


        Toast.makeText(getApplicationContext(), Double.toString(ratingBar.getRating()),
                Toast.LENGTH_LONG).show();
    }
}
