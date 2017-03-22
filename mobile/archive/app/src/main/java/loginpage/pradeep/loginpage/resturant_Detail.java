package loginpage.pradeep.loginpage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
            uname = obj.getString("username");
            System.out.println(menu.length()+ "ANOOP" + menu.getJSONObject(0)) ;

            for(int i =0; i<menu.length() ;i++){
                String dish = (menu.getJSONObject(i).getString("name"));
                double rating =Double.parseDouble (menu.getJSONObject(i).getString("review"));
                if(rating < 1.0)
                {
                    rating = 0.0;
                }
                dishArrayList.add(new Name_Review(dish,rating));
            }
            for(int i =0; i<menu.length() ;i++){
                descList.add(menu.getJSONObject(i).getString("description"));
            }


        }catch (JSONException e){

        }


        array = new String[menuList.size()];
        for(int i=0; i<menuList.size();i++){
            array[i] = menuList.get(i);
        }

        System.out.println("BEFORE" + dishArrayList);
        Collections.sort(dishArrayList,new MyComparator());
        System.out.println("AFTER" + dishArrayList);
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
