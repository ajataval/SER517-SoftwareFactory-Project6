package loginpage.pradeep.loginpage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class resturant_Detail extends AppCompatActivity {

    String hotelName ;
    String distance ;
    String address ;
    String openNow;
    JSONArray menu;
    ArrayList<String> menuList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant__detail);
        Intent intent = getIntent();

        try{
            JSONObject obj = new JSONObject(intent.getStringExtra("JSON") );
            System.out.println(obj + " REST");
            hotelName = obj.getString("hotelname");
            distance = obj.getString("distance") + " mi";
            address = obj.getString("address");
            openNow = obj.getString("openNow");
            menu = obj.getJSONArray("menu");

            /*for(int i =0; i<menu.length() ;i++){
                menuList.set(i,menu.getJSONObject(i).getString("name"));
            }*/
        }catch (JSONException e){

        }




        TextView rDetail = (TextView) findViewById(R.id.resturantName);
        rDetail.setText(hotelName + " " + distance + "  " + address + menu);
    }
}
