package loginpage.pradeep.loginpage;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class Resturant_Detail extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

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
    String lat;
    String lon;
    String appUserName;
    //JSONObject obj;

    //for maps

    String placeId;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SigninActivity";
    Double dlat;
    Double dlong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant__detail);
        Intent intent = getIntent();
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("long");
        //  System.out.println("TOKEN"  + IID_TOKEN);
        usernameR = intent.getStringExtra("username");
        appUserName = intent.getStringExtra("username");
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        //google map

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();;


        boolean flagRatingBar = false;
        //final CheckBox fav;
        //fav = (CheckBox) findViewById(R.id.fav);
        // final RatingBar fav;
        // fav = (RatingBar) fin
        //
        // dViewById(R.id.fav);
        final ImageView fav;
        fav = (ImageView) findViewById(R.id.fav);
        context = getApplicationContext();
        //s= String.valueOf(context.getDatabasePath("Anoop.db"));

        try{
            String flag = intent.getStringExtra("FLAG");
            JSONObject obj = new JSONObject(intent.getStringExtra("JSON") );
            placeId = obj.getString("place_id");

            if(flag.equals("MENU")){
                //CLEARS FAV LIST AND FILLS IT WITH INTENT OBJECT FROM MENU DETAILS PAGE
                ArrayList<String> temp = intent.getStringArrayListExtra("favListM");
                favList.clear();
                if(temp.size()>0){
                    for(int i=0;i<temp.size();i++){
                        favList.add(temp.get(i));
                    }
                }

            }
            else{ // BEST  WAY IS TO SEND A GET HERE FOR FAV
                //CLEARS FAV LIST AND FILLS IT WITH INTENT OBEJCT FROM ACTIVITY PAGE
                favList.clear();
                favList = intent.getStringArrayListExtra("favList");
                System.out.println("LIST IN ONCREATE" + favList );
            }


            // System.out.println(obj + " REST");
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
            //fav.setChecked(true);
            //fav.setRating(1.0f);
            fav.setImageResource(R.drawable.fav);
            //fav.setEnabled(true);
            favRest = true;
        }
        else{
            System.out.println("CHECKING FALSE");
            //fav.setChecked(false);
            //fav.setRating(0.0f);
            fav.setImageResource(R.drawable.unfav);
            //fav.setEnabled(true);
            favRest = false;
        }




//        fav.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//
//                    fav.setRating(1.0f);
//                    addFav(uname,hotelName);
//
//                }
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//
//                    fav.setRating(0f);
//                    System.out.println("CRASH + " + hotelName );
//                    removeFav(uname,hotelName);
//
//                }
//
//                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
//                    v.setPressed(false);
//                }
//
//
//
//
//                return true;
//            }});



//        fav.setOnTouchListener(new View.OnTouchListener() {
//                                   @Override
//                                   public boolean onTouch(View v, MotionEvent event) {
//                                       favRest = !favRest;
//                                       if(favRest)
//                                        {
//                                            fav.setRating(1f);
//                                            fav.setEnabled(true);
//                                           // TODO perform your action here
//                                           addFav(uname,hotelName);
//
//                                       }
//                                       else {
//
//
//                                           fav.setRating(0f);
//                                           fav.setEnabled(true);
//                                           System.out.println("CRASH + " + hotelName );
//                                           removeFav(uname,hotelName);
//
//
//                                       }
//                                       return true;
//                                   }
//                               });



//        fav.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//
//            // Called when the user swipes the RatingBar
//            @Override
//            public void onRatingChanged(RatingBar fav, float rating, boolean fromUser) {
//                favRest = !favRest;
//
//
//                if(favRest)
//                { //fav.isChecked()
//                    fav.setRating(1.0f);
//                    fav.setEnabled(false);
//                    System.out.println("CRASH + " + hotelName );
//
//                    addFav(uname,hotelName);
//
//                }
//                else{
//                    System.out.println("THIS SHOUDL HAVE WORKED");
//                    fav.setRating(0.0f);
//                    fav.setEnabled(false);
//                    removeFav(uname,hotelName);
//
//                }
//            }
//        });



        fav.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Toast.makeText(getApplicationContext(),  "CLICKED",Toast.LENGTH_LONG).show();
                favRest = !favRest;


                if(favRest)
                { //fav.isChecked()
                    //fav.setRating(1f);
                    //fav.setEnabled(true);
                    fav.setImageResource(R.drawable.fav);
                    System.out.println("PRESENT IN FAV + " + hotelName );

                    addFav(uname,hotelName);

                }
                else{
                    System.out.println("NOT IN FAV");
                    //fav.setRating(0);
                    //fav.setEnabled(true);
                    fav.setImageResource(R.drawable.unfav);
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

//        HAddress.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View view){
//
//                maps();
//            }
//        });

    }





    public void maps(View view){
        try{
            Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                    .setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getStatus().isSuccess()) {
                                final Place myPlace = places.get(0);
                                LatLng queriedLocation = myPlace.getLatLng();
                                dlat  = queriedLocation.latitude;
                                dlong = queriedLocation.longitude;
                                plotMap(dlat,dlong);
                            }
                            else{
                                Log.v("LAT LONG", "" + "FAILED");

                            }
                            places.release();
                            System.out.println("IN PLACES");
                        }
                    });
        }catch (Exception e){
            System.out.println("ERROR IN PLACES");
            e.printStackTrace();

        }
    }

    public boolean isfav(String uname){
        System.out.println("INSIDE IS FAV " + uname);
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
        String url = "http://hungrymeser.herokuapp.com/app/users/" + appUserName + "/favorite";
        //String IID_TOKEN = FirebaseInstanceId.getInstance().getToken();
        JSONObject obj = new JSONObject();
        favList.add(emailId);

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
                                    // System.out.println("GETTING HERE");
                                    // System.out.println(ans.getClass().getName());
                                    // System.out.println(ans.get(1).getClass().getName());

                                    // addToList(ans);

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

//
//    public void addToList(JSONArray ans){
//        favList.clear();
//        try{
//
//            for(int i=0;i<ans.length();i++){
//                favList.add(ans.get(i).toString());
//            }
//            System.out.println("LIST after adding" + favList );
//        }catch (JSONException e){
//
//        }
//
//    }

    public void removeFav(final String emailId, String hname){
        String url = "http://hungrymeser.herokuapp.com/app/users/" + appUserName + "/favorite?hotel="+emailId+"&registrationToken="+IID_TOKEN;
        JSONObject obj = new JSONObject();
        //String IID_TOKEN = FirebaseInstanceId.getInstance().getToken();

        /*try{
            obj.put("hotel",emailId );
            obj.put("registrationToken", IID_TOKEN);

        }catch (JSONException e){

        }*/

        //System.out.println("body " + obj)  ;
        favList.remove(emailId);
        // System.out.println(IID_TOKEN);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.DELETE,url,obj,
                        new Response.Listener<JSONObject>(){

                            @Override
                            public void onResponse(JSONObject res){

                                System.out.println("Success full marked UNfav");
                                System.out.println(res);
                                try{
                                    JSONArray ans  = res.getJSONArray("favorite");
                                    //System.out.println("GETTING HERE");
                                    //System.out.println(ans.getClass().getName());
                                    //System.out.println(ans.get(1).getClass().getName());
                                    //removeFromList(ans);

                                }catch (JSONException e){

                                }


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

//    public void removeFromList(JSONArray ans){
//
//        favList.clear();
//        try {
//
//            for(int i=0;i<ans.length();i++){
//                favList.add(ans.get(i).toString());
//            }
//            System.out.println("LIST after removing" + favList );
//
//        }catch (JSONException e){
//
//        }
//    }
    public void menuDetails(String desc,String dishname,String hname){
        Intent intent = new Intent(this,Menu_Details.class);
        intent.putExtra("DESC", desc);
        intent.putExtra("DISH", dishname);
        intent.putExtra("HOTEL", hname);
        intent.putExtra("UNAME",uname);
        intent.putExtra("favList", favList);
        intent.putExtra("appusername",appUserName);
        // intent.putExtra("hotelName", hotelName);
        //intent.putExtra("distance", distance);
        //intent.putExtra("address" , address);
        startActivity(intent);
    }
    public void onConnectionFailed(ConnectionResult connectionResult){
        Log.d(TAG, "onConnectionFailed:" + connectionResult );
    }


    public void plotMap(double dlat,double dlong){
        System.out.println("INSIDE MAPS" + dlat + "" + dlong);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr="+lat+","+ lon +"&daddr=" + dlat + "," + dlong));
        System.out.println("INSIDE MAPS" + dlat + "" + dlong);
        startActivity(intent);

    }

    public void googleMaps(View view) {
        try{
            Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                    .setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getStatus().isSuccess()) {
                                final Place myPlace = places.get(0);
                                LatLng queriedLocation = myPlace.getLatLng();
                                dlat  = queriedLocation.latitude;
                                dlong = queriedLocation.longitude;
                                plotMap(dlat,dlong);
                            }
                            else{
                                Log.v("LAT LONG", "" + "FAILED");

                            }
                            places.release();
                            System.out.println("IN PLACES");
                        }
                    });
        }catch (Exception e){
            System.out.println("ERROR IN PLACES");
            e.printStackTrace();

        }
    }
}
