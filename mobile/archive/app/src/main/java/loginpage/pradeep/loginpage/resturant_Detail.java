package loginpage.pradeep.loginpage;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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

import com.google.android.gms.maps.MapFragment;

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
    //JSONObject obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant__detail);
        Intent intent = getIntent();
        usernameR = intent.getStringExtra("username");




        CheckBox fav;
        fav = (CheckBox) findViewById(R.id.fav);
        context = getApplicationContext();
        s= String.valueOf(context.getDatabasePath("Anoop.db"));

        if(selectFromDb())//PRESENT IN DB
        {
            fav.setChecked(true);
            favRest = true;
        }
        else{
            fav.setChecked(false);
            favRest = false;
        }






        try{
            String flag = intent.getStringExtra("FLAG");

            JSONObject obj = new JSONObject(intent.getStringExtra("JSON") );
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


        fav.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Toast.makeText(getApplicationContext(),  "CLICKED",Toast.LENGTH_LONG).show();
                favRest = !favRest;

                if(favRest){

                    try{
                        db = getApplicationContext().openOrCreateDatabase(s, MODE_APPEND,null);
                        db.beginTransaction();
                        String table_name = "FAV_" + usernameR.split("\\@")[0];
                        String insert = "INSERT INTO " + table_name + " (HNAME) "
                                + "VALUES ('" + hotelName + "');";
                        db.execSQL(insert);
                        Toast.makeText(getApplicationContext(), "INSERTED AFTER CLICKING" + table_name, Toast.LENGTH_LONG).show();
                        System.out.println("INSERTED AFTER CLICKING" + table_name);


                    }catch (SQLException e){
                        System.out.println("SOMETHING WENT WRONG WHILE INSERTING");
                    }finally {
                        db.endTransaction();
                    }
                }
                else{

                    try{
                        db = getApplicationContext().openOrCreateDatabase(s, MODE_APPEND,null);
                        db.beginTransaction();
                        String table_name = "FAV_" + usernameR.split("\\@")[0];

//                        db.delete(table_name, "HNAME" + "=" + hotelName, null);
                        db.execSQL("DELETE FROM " + table_name + " WHERE " + "HNAME" + "= '" + hotelName + "'");
                        Toast.makeText(getApplicationContext(), "DELETED AFTER CLICKING" + table_name, Toast.LENGTH_LONG).show();
                        System.out.println("DELETED AFTER CLICKING" + table_name);


                    }catch (SQLException e){
                        System.out.println("SOMETHING WENT WRONG WHILE DELETING");
                    }finally {
                        db.endTransaction();
                    }


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

/*
        TextView rName = (TextView) findViewById(R.id.HNAME);
        rName.setText(hotelName );

        TextView rADDRESS = (TextView) findViewById(R.id.ADDRESS);
        rADDRESS.setText(address );
        */


    }

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

    public void menuDetails(String desc,String dishname,String hname){
        Intent intent = new Intent(this,menu_Details.class);
        intent.putExtra("DESC", desc);
        intent.putExtra("DISH", dishname);
        intent.putExtra("HOTEL", hname);
        intent.putExtra("UNAME",uname);
        // intent.putExtra("hotelName", hotelName);
        //intent.putExtra("distance", distance);
        //intent.putExtra("address" , address);
        startActivity(intent);
    }
}
