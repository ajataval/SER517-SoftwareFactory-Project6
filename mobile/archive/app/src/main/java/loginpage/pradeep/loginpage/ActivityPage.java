package loginpage.pradeep.loginpage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.location.LocationListener;

import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ActivityPage extends LoginPage implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,LocationListener {

    TextView statusTextView;
    String[] restList = new String[0];
    // server url
    String server_url = "http://hungrymeser.herokuapp.com/search?";//"https://hungrymeser.herokuapp.com";
    private TextView servtext;
    private JSONArray restArray;
    ListView listView;
    //ArrayAdapter adapter;
    MyAdapterDistance adapter;  //TWO
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    // String hotelName;
    //String distance;
    //String address;\
    boolean recflag = false;
    JSONObject objIntent;
    private LocationManager locationManager;
    private LocationListener listener;
    Double longitude = 0.0;
    Double latitude = 0.0;
    ArrayList<Name_Distance> restArrayList = new ArrayList<Name_Distance>();

    LoginPage loginPage;
    Spinner spin;
    EditText searchValue;
    String searchVal;
    String selectedSearch;
    String usernameSend;
    ArrayList<String> favList = new ArrayList<>();

    ArrayList<Name_Distance> temp = new ArrayList<>();

    private ProgressDialog dialog;


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    public void setrestarray(JSONArray temp) {

        restArray = temp;
    }

    protected void onCreate(final Bundle savedInstanceState) {
        Intent intent = getIntent();
        usernameSend = intent.getStringExtra("username");

        //latitude = Double.parseDouble(intent.getStringExtra("lat"));
        // longitude = Double.parseDouble(intent.getStringExtra("lon"));
        // System.out.println(latitude + "   " + longitude + " asdsad ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        // google location api new

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


            String url1 = "https://hungrymeser.herokuapp.com/app/users/" + usernameSend;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET,url1,null,
                            new Response.Listener<JSONObject>(){

                                @Override
                                public void onResponse(JSONObject res){

                                    System.out.println("GOT INSIDE ****");
                                    try{

                                        if(res.getJSONArray("revCuisine").length() >0){
                                            System.out.println("YES");
                                            recflag = true;
                                        }else{
                                            System.out.println("NO");
                                            recflag = false;
                                        }
                                        //JSONArray ans  = res.getJSONArray("favorite");
                                        //System.out.println("GETTING HERE INSIDE");
                                        //System.out.println(ans.getClass().getName());
                                        //System.out.println(ans.get(1).getClass().getName());
//                                        for(int i=0;i<ans.length();i++){
//                                            favList.add(ans.get(i).toString());
//                                        }
                                        System.out.println("LIST NEW IN ACTIVITY" + favList );
                                    }catch (JSONException e){
                                        System.out.println("ERR");
                                        recflag = false;

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


//FETCHING LOCATION BEGINS


//        dialog = new ProgressDialog(this);
//        dialog.setMessage("Fetching data...");
//        dialog.setCancelable(false);
//        dialog.setInverseBackgroundForced(false); // this was deprecated as of SDK 23
//
//        dialog.show();

        //FETCHING LOCATION BEGINS

//
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        listener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//                Log.d("Location", "" + location.getLongitude());
//                longitude = location.getLongitude();
//                latitude = location.getLatitude();
//
//
//                temp.add(new Name_Distance("", ""));
//                setAdapter1(temp);
//                restArrayList.clear();
//                String url = server_url + "lat=" + latitude + "&long=" + longitude;
//                jsonMethod(url);
//
//
//            }
//
//            @Override
//            public void onStatusChanged(String s, int i, Bundle bundle) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String s) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String s) {
//
//                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
//            }
//        };
//        configure_button();


        //code to set value of search type

        spin = (Spinner) findViewById(R.id.spin);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSearch = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//FETCHING LOCATION ENDS

        //fetching data from sever
        String[] ma = {" ", " "};
        //setAdapter(ma);
        ArrayList<Name_Distance> temp = new ArrayList<>();
        temp.add(new Name_Distance(" Loading", "....."));
        setAdapter1(temp);


//        final RequestQueue requestQueue = Volley.newRequestQueue(ActivityPage.this);
        // server_url = "http://hungrymeser.herokuapp.com/search?lat="+latitude+"&long="+longitude;

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


        //jsonMethod(server_url);


        //UNCOMMENT servtext = (TextView) findViewById(R.id.status_textview);

/*
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

        //  signOutButton.setOnClickListener(this);

        final ImageView rec;
        rec = (ImageView) findViewById(R.id.rec);
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "CLICKED ON REC", Toast.LENGTH_LONG).show();
                if(recflag){

                    rec.setImageResource(R.drawable.toppicks);
                    recommendMe();
                }
                else{
                    Toast.makeText(getApplicationContext(), "NO RECOMENDATION FOR YOU YET", Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    void jsonMethod(String server_url) {
        final RequestQueue requestQueue = Volley.newRequestQueue(ActivityPage.this);
        System.out.println(server_url + " LAT LONG");
        JsonArrayRequest JARequest = new JsonArrayRequest(Request.Method.GET, server_url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                // System.out.println("JSON Response" + response);


                setrestarray(response);
                restArray = response;

                //  System.out.println("LENGTH OF JSONARRRAY IS " + " " + restArray.length());
                // System.out.println("LENGTH OF JSONARRRAY IS asdasd " + " " + restArray.length());
                restList = new String[restArray.length()];

                if (restArray.length() != 0) {

                    for (int i = 0; i < restArray.length(); i++) {
                        try {
                            JSONObject jsonobject = restArray.getJSONObject(i);
                            String hname = jsonobject.getString("hotelname");
                            String dist = jsonobject.getString("distance");


                            restList[i] = hname;
                            restArrayList.add(new Name_Distance(hname, dist));
                            System.out.println(jsonobject);


                        } catch (JSONException e) {
                            System.out.println("NO REC");
                            e.printStackTrace();
                        }

                    }
                    callme(restArray);
                    //disp(restArray);
                } else {
                    Toast.makeText(getApplicationContext(), "Item not found", Toast.LENGTH_LONG).show();
                    jsonMethod("http://hungrymeser.herokuapp.com/search?" + "lat=" + latitude + "&long=" + longitude);

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




//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 10:
//                configure_button();
//                break;
//            default:
//                break;
//        }
//    }

//    void configure_button() {
//        // first check for permissions
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
//                        , 10);
//            }
//            return;
//        }
//        // this code won'textView execute IF permissions are not allowed, because in the line above there is return statement.
//
//        //noinspection MissingPermission
//        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 0, listener);
//
//
//    }


    public void callme(JSONArray r) {
//        listView.invalidate();
        //setAdapter(restList);
        setAdapter1(restArrayList);
        adapter.notifyDataSetChanged();
        System.out.println(latitude + " " + longitude + " SUCCESS ");
        try {
            //System.out.println(r.length() + " asdasdasd asdasd");
        } catch (NullPointerException e) {
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
                                                try {
                                                    //  System.out.println("ANOOP" + restArray.getJSONObject(i));
                                                    objIntent = restArray.getJSONObject(i);


                                                    callthis();


                                                    //hotelName = objIntent.getString("hotelname");
                                                    //distance = Double.toString(objIntent.getDouble("distance"));
                                                    //address = objIntent.getString("address");

                                                } catch (JSONException e) {

                                                }

                                                //String resturantName = "WORKING";

//                                            Toast.makeText(getApplicationContext(), Selecteditem, Toast.LENGTH_SHORT).show();


                                            }
                                        }
        );
    }


    public void callthis() {

        String url = "http://hungrymeser.herokuapp.com/app/users/" + usernameSend;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject res) {

                                System.out.println("Success full marked fav");
                                System.out.println(res);
                                try {
                                    JSONArray ans = res.getJSONArray("favorite");
                                    System.out.println("GETTING HERE");
                                    System.out.println(ans.getClass().getName());
                                    System.out.println(ans.get(1).getClass().getName());
                                    favList.clear();
                                    for (int i = 0; i < ans.length(); i++) {
                                        favList.add(ans.get(i).toString());
                                    }
                                    System.out.println("LIST FIRST" + favList);
                                    resturantView(objIntent, usernameSend,favList);
                                } catch (JSONException e) {
                                    System.out.println("ERR");
                                    resturantView(objIntent, usernameSend,favList);

                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("Something went wrong while geeting");
                        error.printStackTrace();

                    }
                }) {

        };
        MySingleTon.getInstance(this).addToReqQue(jsonObjectRequest);

    }

    public void resturantView(JSONObject objIntent, String username, ArrayList fav) {
        Intent intent = new Intent(this, Resturant_Detail.class);
        intent.putExtra("username", username);
        intent.putExtra("JSON", objIntent.toString());
        intent.putExtra("FLAG", "ACTIVITY");
        intent.putStringArrayListExtra("favList", fav);
        intent.putExtra("lat", latitude.toString());
        intent.putExtra("long", longitude.toString());
        intent.putExtra("appusername", username);

        // intent.putExtra("hotelName", hotelName);
        //intent.putExtra("distance", distance);
        //intent.putExtra("address", address);
        startActivity(intent);

    }

    public void searchCuisineDish(View view) {
        searchValue = (EditText) findViewById(R.id.search_bar);
        searchVal = searchValue.getText().toString();
        if (!searchVal.equals("")) {
            String url = server_url + "lat=" + latitude + "&long=" + longitude + "&" + selectedSearch + "=" + searchVal;
            restArrayList.clear();
            jsonMethod(url);
        } else if (selectedSearch.equalsIgnoreCase("cuisine")) {
            Toast.makeText(getApplicationContext(), "Enter a cuisine", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Enter a cuisine", Toast.LENGTH_LONG).show();
        }

    }

    //when clicked on banner

    public void reloadMenu(View view) {
        ImageView rec;
        rec = (ImageView) findViewById(R.id.rec);
        restArrayList.clear();
        rec.setImageResource(R.drawable.rec);
        System.out.println("lat=" + latitude + "&long=" + longitude);
        jsonMethod(server_url + "lat=" + latitude + "&long=" + longitude);

    }


    private void fetchedLocation() {
        if (latitude == 0.0 && longitude == 0.0) {
            Toast.makeText(getApplicationContext(), "Unable to fetch location", Toast.LENGTH_LONG).show();

        } else if (latitude > 0.0 && longitude > 0.0) {
            //  contentPage(uname);
        } else {
            Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
        }
    }


//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (locationManager != null) {
//            //noinspection MissingPermission
//            locationManager.removeUpdates(listener);
//        }
//    }


    //recommendation page

    public void recommendMe() {
        String urlR = "http://hungrymeser.herokuapp.com/app/users/" + usernameSend + "/recommend?lat=" + latitude + "&long=" + longitude;
        restArrayList.clear();
        jsonMethod(urlR);
    }

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    //google location api new

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onConnected(Bundle dataBundle) {
        // Get last known recent location.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null


            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
            longitude = mCurrentLocation.getLongitude();
            latitude = mCurrentLocation.getLatitude();

            temp.add(new Name_Distance("", ""));
            setAdapter1(temp);
            restArrayList.clear();
            String url = server_url + "lat=" + latitude + "&long=" + longitude;
            jsonMethod(url);
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }


    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
       // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }


}