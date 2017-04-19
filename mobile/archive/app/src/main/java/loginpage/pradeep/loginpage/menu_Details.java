package loginpage.pradeep.loginpage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Menu_Details extends AppCompatActivity {

    public RatingBar ratingBar;
    EditText comment;
    String rating;
    String review;
    String dish ;
    String uname;
    String appUserName;
    ArrayList<String > temp = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__details);
        Intent intent = getIntent();
        appUserName = intent.getStringExtra("appusername");
        String Description = intent.getStringExtra("DESC");
        temp = intent.getStringArrayListExtra("favList");
        TextView Desc = (TextView) findViewById(R.id.Desc);
        //Desc.setText(Description);
        Desc.setMovementMethod(new ScrollingMovementMethod());
        Desc.setText(Description);

        TextView hname = (TextView) findViewById(R.id.Hname);
        TextView dname = (TextView) findViewById(R.id.Dname);

        hname.setText(intent.getStringExtra("HOTEL"));
        dname.setText(intent.getStringExtra("DISH"));
        dish = intent.getStringExtra("DISH");
        uname = intent.getStringExtra("UNAME");

       // getActionBar().setDisplayHomeAsUpEnabled(true);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        ratingBar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                rateMe(view);


            }
        });;




        Button submit = (Button) findViewById(R.id.SumitReview);

        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                EditText comment = (EditText) findViewById(R.id.Comment);
                rating = Double.toString(ratingBar.getRating());
                review = comment.getText().toString();
                System.out.println(review + "  WRITE THIS  " + rating);
                try {
                    postToServer(review,rating);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        });;

    }
    public void rateMe(View view){


        Toast.makeText(getApplicationContext(), Double.toString(ratingBar.getRating()),
                Toast.LENGTH_LONG).show();
    }

    public void postToServer(String review,String rating) throws UnsupportedEncodingException, MalformedURLException, URISyntaxException {


       // dish.replace(" ","%20");
        String serverUrl = "http://hungrymeser.herokuapp.com/hotel/users/" + uname + "/menu/" + dish;

        URL url = null;

            url = new URL(serverUrl);

        URI uri = null;

            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());


            url = uri.toURL();

        Log.d("ServerUrl",url.toString());

        JSONObject jsonObj = new JSONObject();

        try{
            jsonObj.put("review", rating);
            jsonObj.put("comment", review);
            jsonObj.put("appUser",appUserName);

        }catch (JSONException e){

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PUT,url.toString(),jsonObj,
                        new Response.Listener<JSONObject>(){

                            @Override
                            public void onResponse(JSONObject res){

                                System.out.println("Success full entered");
                                System.out.println(res);
                                refreshRating(res);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("Something went wrong");
                        error.printStackTrace();

                    }
                }){

        };
        MySingleTon.getInstance(this).addToReqQue(jsonObjectRequest);
    }

    public void refreshRating(JSONObject res){
        Intent intent = new Intent(this,Resturant_Detail.class);
        intent.putExtra("username",uname);
        intent.putExtra("JSON", res.toString());
        intent.putExtra("FLAG", "MENU");
        intent.putExtra("favListM", temp);
        intent.putExtra("username",appUserName);
        //intent.putExtra("distance", distance);
        //intent.putExtra("address" , address);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       startActivity(intent);
    }
}
