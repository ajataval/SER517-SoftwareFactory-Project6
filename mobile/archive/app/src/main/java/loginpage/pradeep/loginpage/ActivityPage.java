package loginpage.pradeep.loginpage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ActivityPage extends LoginPage {

    TextView statusTextView;
    String[] restList = new String[0];
    // server url
    String server_url = "http://hungrymeser.herokuapp.com/dummy" ;//"https://hungrymeser.herokuapp.com";
    private TextView servtext;
    private JSONArray restArray;
    ListView listView;
    //ArrayAdapter adapter;
    MyAdapterDistance adapter;  //TWO
    // String hotelName;
    //String distance;
    //String address;
    JSONObject objIntent;
    private LocationManager locationManager;
    private LocationListener listener;
    Double longitude;
    Double latitude;
    ArrayList<Name_Distance> restArrayList = new ArrayList<Name_Distance>();

    Spinner spin;
    EditText searchValue;
    String searchVal;
    String selectedSearch;





    public void setrestarray(JSONArray temp){

        restArray = temp;
    }

    protected void onCreate(final Bundle savedInstanceState) {
        Intent intent = getIntent();
        //latitude = Double.parseDouble(intent.getStringExtra("lat"));
        // longitude = Double.parseDouble(intent.getStringExtra("lon"));
        // System.out.println(latitude + "   " + longitude + " asdsad ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

//FETCHING LOCATION BEGINS

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location",""+location.getLongitude());
                longitude =location.getLongitude();
                latitude  = location.getLatitude();
                latitude = 33.42025778;
                longitude = -111.9306630;
              // String dummy = server_url + "lat=" +latitude.toString()+"&long="+longitude.toString();

               // server_url = "http://hungrymeser.herokuapp.com/search?lat=33.42025778&long=-111.9306630";
               // System.out.println(dummy.equals(server_url) + "THIS SHOUDL");


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();

        //code to set value of search type

        spin = (Spinner) findViewById(R.id.spin);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSearch = parent.getSelectedItem().toString();
            }
            @Override
            public  void onNothingSelected(AdapterView<?> parent){

            }
        });


//FETCHING LOCATION ENDS

        //fetching data from sever
        String[] ma = {" "," "};
        //setAdapter(ma);
        ArrayList<Name_Distance> temp = new ArrayList<>();
        temp.add( new Name_Distance(" Loading", "....."));
        setAdapter1(temp);


//        final RequestQueue requestQueue = Volley.newRequestQueue(ActivityPage.this);
        server_url = "http://hungrymeser.herokuapp.com/search?lat=33.42025778&long=-111.9306630";
//        JsonArrayRequest JARequest = new JsonArrayRequest(Request.Method.GET, server_url, null, new Response.Listener<JSONArray>(){
//
//            @Override
//            public void onResponse(JSONArray response) {
//                System.out.println("JSON Response" + response);
//
//
//
//                setrestarray(response);
//                restArray = response;
//
//                System.out.println("LENGTH OF JSONARRRAY IS " + " " + restArray.length());
//                System.out.println("LENGTH OF JSONARRRAY IS asdasd " + " " + restArray.length());
//                restList = new String[restArray.length()];
//                for (int i = 0; i < restArray.length(); i++) {
//                    try{
//                        JSONObject jsonobject = restArray.getJSONObject(i);
//                        String hname = jsonobject.getString("hotelname");
//                        String dist = jsonobject.getString("distance");
//                        restList[i] = hname;
//                        restArrayList.add(new Name_Distance(hname,dist));
//                        System.out.println(jsonobject);
//
//
//
//                    }
//                    catch (JSONException e){
//                        e.printStackTrace();
//                    }
//
//                }
//                callme(restArray);
//                //disp(restArray);
//
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                System.out.println("ERROR in getting JSON object");
//                // TODO Auto-generated method stub
//
//            }
//        });
//        requestQueue.add(JARequest);


        jsonMethod(server_url);



        //UNCOMMENT servtext = (TextView) findViewById(R.id.status_textview);

/*
        JSONObject jso = new JSONObject();
        try {
            jso.put("Nombre","Miguel");
            jso.put("Apellidos", "Garcia");
            jso.put("Año_nacimiento", 19.90);
            JSONArray jsa = new JSONArray();
            JSONObject jsa1 = new JSONObject();
            jsa1.put("gto","Blur");
            jsa1.put("rte","Clur");
            jsa.put(jsa1);
            jso.put("Nombres_Hijos", jsa);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            System.out.println("NO CREATED");
            e.printStackTrace();
        }
        JSONObject jso1 = new JSONObject();
        try {
            jso1.put("Nombre","Miguel");
            jso1.put("Apellidos", "Garcia");
            jso1.put("Año_nacimiento", 19.90);
            JSONArray jsa2 = new JSONArray();
            JSONObject jsa3 = new JSONObject();
            jsa3.put("gto","Blur");
            jsa3.put("rte","Clur");
            jsa2.put(jsa3);
            jso.put("Nombres_Hijos", jsa2);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            System.out.println("NO CREATED");
            e.printStackTrace();
        }
        JSONArray j = new JSONArray();
        j.put(jso);
        j.put(jso1);
        String js = j.toString();
        System.out.println("J TO S  " + js);
        try{
            JSONObject obj = new JSONObject(js);
            System.out.println("*********************");
            System.out.println("S TO J  " + obj);
            System.out.println(obj.getClass() + "JSON");
            //JSONParser parser = new JSONParser();
            //JSONObject json = (JSONObject) parser.parse(response);
        }
        catch (JSONException e){
            //obj = null;
            System.out.println("JSON ERROR");
            e.printStackTrace();
        }
*/

/*
        StringRequest stringRequest = new StringRequest(Request.Method.GET, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("Response"+response.getClass());
                        System.out.println("Response"+response);
                        System.out.println("Class"+response.getClass());
                        try{
                            JSONObject obj = new JSONObject(response);
                            System.out.println("*********************");
                            System.out.println(obj);
                            System.out.println(obj.getClass() + "   JSON");
                            //JSONParser parser = new JSONParser();
                            //JSONObject json = (JSONObject) parser.parse(response);
                        }
                        catch (JSONException e){
                            //obj = null;
                            System.out.println("JSON ERROR");
                            e.printStackTrace();
                        }
                        //servtext.setText(response);
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //servtext.setText("Something went wrong");
                System.out.println("ANOOP");
                error.printStackTrace();
                requestQueue.stop();
            }
        }
        );
*/

        //LIST VIEW
        //




        // String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
        //        "WebOS","Ubuntu","Windows7","Max OS X"};




        //String[] ma = {"ma","sd"};








        // signOutButton = (Button) findViewById(R.id.signOutButton);
        //signOutButton = (Button) findViewById(R.id.gsignOut);

        //  signOutButton.setOnClickListener(this);
    }


    void jsonMethod(String server_url){
        final RequestQueue requestQueue = Volley.newRequestQueue(ActivityPage.this);
        JsonArrayRequest JARequest = new JsonArrayRequest(Request.Method.GET, server_url, null, new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response) {
                System.out.println("JSON Response" + response);



                setrestarray(response);
                restArray = response;

                System.out.println("LENGTH OF JSONARRRAY IS " + " " + restArray.length());
                System.out.println("LENGTH OF JSONARRRAY IS asdasd " + " " + restArray.length());
                restList = new String[restArray.length()];

                if(restArray.length() != 0) {

                    for (int i = 0; i < restArray.length(); i++) {
                        try {
                            JSONObject jsonobject = restArray.getJSONObject(i);
                            String hname = jsonobject.getString("hotelname");
                            String dist = jsonobject.getString("distance");
                            restList[i] = hname;
                            restArrayList.add(new Name_Distance(hname, dist));
                            System.out.println(jsonobject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    callme(restArray);
                    //disp(restArray);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Item not found", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("ERROR in getting JSON object");
                // TODO Auto-generated method stub

            }
        });
        requestQueue.add(JARequest);


    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // this code won'textView execute IF permissions are not allowed, because in the line above there is return statement.

        //noinspection MissingPermission
        locationManager.requestLocationUpdates("gps", 50000, 0, listener);


    }


    public void callme(JSONArray r){
//        listView.invalidate();
        //setAdapter(restList);
        setAdapter1(restArrayList);
        adapter.notifyDataSetChanged();
        System.out.println(latitude + " " + longitude + " SUCCESS ");
        try{
            System.out.println(r.length() + " asdasdasd asdasd");
        }catch (NullPointerException e){
            System.out.println("NULL HERE");
        }

    }

    public void setAdapter1(ArrayList<Name_Distance> array) {


        adapter = new MyAdapterDistance(this, array);
        listView = (ListView) findViewById(R.id.resturant_list);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Object o = listView.getItemAtPosition(i);
                                                //String s = Integer.toString(i);
                                                try{
                                                    System.out.println("ANOOP" + restArray.getJSONObject(i));
                                                    objIntent = restArray.getJSONObject(i);
                                                    //hotelName = objIntent.getString("hotelname");
                                                    //distance = Double.toString(objIntent.getDouble("distance"));
                                                    //address = objIntent.getString("address");

                                                }catch (JSONException e){

                                                }

                                                //String resturantName = "WORKING";

//                                            Toast.makeText(getApplicationContext(), Selecteditem, Toast.LENGTH_SHORT).show();
                                                resturantView(objIntent);

                                            }
                                        }
        );
    }

    public void resturantView(JSONObject objIntent){
        Intent intent = new Intent(this,resturant_Detail.class);
        intent.putExtra("JSON", objIntent.toString());
        // intent.putExtra("hotelName", hotelName);
        //intent.putExtra("distance", distance);
        //intent.putExtra("address", address);
        startActivity(intent);

    }

    public void searchCuisineDish(View view) {
        searchValue = (EditText) findViewById(R.id.search_bar);
        searchVal = searchValue.getText().toString();
        if(!searchVal.equals("")){
            String url = server_url+"&"+selectedSearch+"="+searchVal;
            restArrayList.clear();
            jsonMethod(url);
        }else if(selectedSearch.equalsIgnoreCase("cuisine"))
        {
            Toast.makeText(getApplicationContext(), "Enter a cuisine", Toast.LENGTH_LONG).show();
        }else
        {
            Toast.makeText(getApplicationContext(), "Enter a cuisine", Toast.LENGTH_LONG).show();
        }

    }

    //when clicked on banner

    public void reloadMenu(View view){
        restArrayList.clear();
        jsonMethod(server_url);

    }





}