package loginpage.pradeep.loginpage;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.MapFragment;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class resturant_Detail extends AppCompatActivity {

    String hotelName ;
    String distance ;
    String address ;
    String openNow;
    JSONArray menu;

    ListView listView;
    //ArrayAdapter adapter;
    MyAdapter adapter;
    ArrayList<String> menuList = new ArrayList<String>();
    ArrayList<String > descList = new ArrayList<String>();
    String array[];
    String desc;
    String uname;
    ArrayList<Name_Review> dishArrayList = new ArrayList<Name_Review>();
    SQLiteDatabase db;
    String usernameR;
    private String s;
    private Context context;
    static boolean favRest = false;
    String appuser;
    String IID_TOKEN = FirebaseInstanceId.getInstance().getToken();
    ArrayList<String> favList = new ArrayList<String>();
    //JSONObject obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant__detail);
        Intent intent = getIntent();

        usernameR = intent.getStringExtra("username");
//        getActionBar().setDisplayHomeAsUpEnabled(true);



        final CheckBox fav;
        fav = (CheckBox) findViewById(R.id.fav);
        context = getApplicationContext();
        s= String.valueOf(context.getDatabasePath("Anoop.db"));

        try{
            String flag = intent.getStringExtra("FLAG");
            JSONObject obj = new JSONObject(intent.getStringExtra("JSON") );

            if(flag.equals("MENU")){
                ArrayList<String> temp = intent.getStringArrayListExtra("favList");
                if(temp.size()>0){
                    for(int i=0;i<temp.size();i++){
                        favList.add(temp.get(i));
                    }
                }

            }
            else{ // BEST  WAY IS TO SEND A GET HERE FOR FAV
                favList = intent.getStringArrayListExtra("favList");
                System.out.println("LIST IN ONCREATE" + favList );

            }


            System.out.println(obj + " REST");
            hotelName = obj.getString("hotelname");
           // distance = obj.getString("distance") + " mi";
            address = obj.getString("address");
            //openNow = obj.getString("openNow");
            menu = obj.getJSONArray("menu");
            uname = obj.getString("username");
           // System.out.println(menu.length()+ "ANOOP" + menu.getJSONObject(0)) ;



            for(int i =0; i<menu.length() ;i++){
                String dish = (menu.getJSONObject(i).getString("name"));
                double rating =Double.parseDouble (menu.getJSONObject(i).getString("review"));
                String description = menu.getJSONObject(i).getString("description");
                if(rating < 1.0)
                {
                    rating = 0.0;
                }
                dishArrayList.add(new Name_Review(dish,rating,description));
            }

            Collections.sort(dishArrayList,new MyComparator());

            for(int i =0; i<dishArrayList.size() ;i++){
                        descList.add(dishArrayList.get(i).getDescription());
                        }


        }catch (JSONException e){

        }

        //CHECK if favList has AND SET TRUE OR FALSE

        if(isfav(uname))//PRESENT IN DB
        {
            System.out.println("CHECKING TRUE");
            fav.setChecked(true);
            favRest = true;
        }
        else{
            System.out.println("CHECKING FALSE");
            fav.setChecked(false);
            favRest = false;
        }


        fav.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Toast.makeText(getApplicationContext(),  "CLICKED",Toast.LENGTH_LONG).show();
                favRest = !favRest;

                if(fav.isChecked()){

                    addFav(uname,hotelName);

                }
                else{

                    removeFav(uname,hotelName);

                }




            }
        });

        array = new String[menuList.size()];
        for(int i=0; i<menuList.size();i++){
            array[i] = menuList.get(i);
        }

       // System.out.println("BEFORE" + dishArrayList);


        //SORT JSON OBJECT AND RECREATE THOSE LIST


      //  System.out.println("AFTER" + dishArrayList);
        adapter = new MyAdapter(this, dishArrayList);
        listView = (ListView) findViewById(R.id.menu_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                //Object o = listView.getItemAtPosition(i);


                                                //System.out.println("ANOOP" + menuList.get(i));
                                                desc = descList.get(i);
                                                String dishanme = dishArrayList.get(i).getTitle();
                                                String hname = hotelName;
                                                menuDetails(desc,dishanme,hname);

                                                //Toast.makeText(getApplicationContext(),  menuList.get(i).toString(),Toast.LENGTH_LONG).show();
//

                                            }
                                        }
        );



        TextView Hname = (TextView) findViewById(R.id.Hname);
        Hname.setText(hotelName);

        TextView HAddress = (TextView) findViewById(R.id.Address);
        HAddress.setText(address);

        HAddress.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                    maps();
                }
        });




/*
        TextView rName = (TextView) findViewById(R.id.HNAME);
        rName.setText(hotelName );

        TextView rADDRESS = (TextView) findViewById(R.id.ADDRESS);
        rADDRESS.setText(address );
        */


    }



    /*
    public boolean selectFromDb(){
        try{
            db = getApplicationContext().openOrCreateDatabase(s, MODE_APPEND,null);
            db.beginTransaction();
            String table_name = "FAV_" + usernameR.split("\\@")[0];
            db.execSQL("SELECT FROM " + table_name + " WHERE " + "HNAME" + "= '" + hotelName + "'") ;
            Toast.makeText(getApplicationContext(), "SELECTED AFTER CLICKING" + table_name, Toast.LENGTH_LONG).show();
            System.out.println("DELETED AFTER CLICKING" + table_name);
            return  true;

        }catch (SQLException e){

            System.out.println("SOMETHING WENT WRONG WHILE SELECTING");
            return false;
        }finally {
            db.endTransaction();
        }

    }
*/

    public void maps(){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=33.418330,-111.934835&daddr=33.419529,-111.919417"));
        startActivity(intent);
    }

    public boolean isfav(String uname){
        System.out.println("HOTEL NAME IS " + uname);
        System.out.println(favList);
        for(int i=0;i<favList.size();i++){
            if(favList.get(i).equals(uname)){
                System.out.println("PRESENT");
                return true;
            }

        }
return false;
    }

    public void addFav(String emailId,String appname){
        String url = "http://hungrymeser.herokuapp.com/app/users/" + usernameR + "/favorite";
        //String IID_TOKEN = FirebaseInstanceId.getInstance().getToken();
        JSONObject obj = new JSONObject();

        try{
            obj.put("hotel",emailId );
            obj.put("registrationToken", IID_TOKEN);

        }catch (JSONException e){

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PUT,url,obj,
                        new Response.Listener<JSONObject>(){

                            @Override
                            public void onResponse(JSONObject res){

                                System.out.println("Success full marked fav");
                                System.out.println(res);
                                try{
                                    JSONArray ans  = res.getJSONArray("favorite");
                                    System.out.println("GETTING HERE");
                                    System.out.println(ans.getClass().getName());
                                    System.out.println(ans.get(1).getClass().getName());
                                    for(int i=0;i<ans.length();i++){
                                        favList.add(ans.get(i).toString());
                                    }
                                    System.out.println("LIST " + favList );
                                }catch (JSONException e){
                                    System.out.println("ERR");
                                    e.printStackTrace();

                                }



                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("Something went wrong while marking");
                        error.printStackTrace();

                    }
                }){

        };
        MySingleTon.getInstance(this).addToReqQue(jsonObjectRequest);
    }

    public void removeFav(final String emailId, String hname){
        String url = "http://hungrymeser.herokuapp.com/app/users/" + usernameR + "/favorite?hotel="+emailId+"&registrationToken="+IID_TOKEN;
        JSONObject obj = new JSONObject();
        //String IID_TOKEN = FirebaseInstanceId.getInstance().getToken();

        /*try{
            obj.put("hotel",emailId );
            obj.put("registrationToken", IID_TOKEN);

        }catch (JSONException e){

        }*/

        //System.out.println("body " + obj)  ;
        System.out.println(IID_TOKEN);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.DELETE,url,obj,
                        new Response.Listener<JSONObject>(){

                            @Override
                            public void onResponse(JSONObject res){

                                System.out.println("Success full marked UNfav");
                                System.out.println(res);
                                favList.remove(emailId);


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("Something went wrong while unmarking");
                        error.printStackTrace();

                    }
                }){

        };
        MySingleTon.getInstance(this).addToReqQue(jsonObjectRequest);
    }

    public void menuDetails(String desc,String dishname,String hname){
        Intent intent = new Intent(this,menu_Details.class);
        intent.putExtra("DESC", desc);
        intent.putExtra("DISH", dishname);
        intent.putExtra("HOTEL", hname);
        intent.putExtra("UNAME",uname);
        intent.putExtra("favList", favList);
        // intent.putExtra("hotelName", hotelName);
        //intent.putExtra("distance", distance);
        //intent.putExtra("address" , address);
        startActivity(intent);
    }
}
