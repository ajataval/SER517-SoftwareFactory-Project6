package loginpage.pradeep.loginpage;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignUpPage extends AppCompatActivity {



    SQLiteDatabase db;


    private  String firstName = null;
    private  String lastName = null;
    private  String emailID = null;
    private  String password = null;
    private  String confirmpassword = null;
    private  String phoneNumber = null;
    private  String securityQuest = null;
    public String storeName;

    private Context context;
    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

       context = getApplicationContext();
        s = String.valueOf(context.getDatabasePath("Anoop.db"));

        Button create = (Button) findViewById(R.id.confirmsignUp);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                EditText first_name = (EditText) findViewById(R.id.firstname);
                firstName = first_name.getText().toString();
                EditText last_name = (EditText) findViewById(R.id.lastname);
                lastName = last_name.getText().toString();
                EditText email = (EditText) findViewById(R.id.email);
                emailID = email.getText().toString();
                EditText passwd = (EditText) findViewById(R.id.password);
                password = passwd.getText().toString();

                EditText cfrmpswd = (EditText) findViewById(R.id.confpassword);
                confirmpassword = cfrmpswd.getText().toString();

                EditText phone_number = (EditText) findViewById(R.id.phoneNumber);
                phoneNumber = phone_number.getText().toString();

                EditText security_ques = (EditText) findViewById(R.id.secquest1);
                securityQuest = security_ques.getText().toString();

                if (!password.equals(confirmpassword)) {
                    Toast.makeText(getApplicationContext(), "Re-enter password", Toast.LENGTH_LONG).show();
                }
                else
                {
                    enrtyDatabase();
                }




            }
        });


    }

    private void enrtyDatabase() {
        try{
            db = getApplicationContext().openOrCreateDatabase(s, MODE_APPEND, null);
            db.beginTransaction();
            try {


                String query3 = "CREATE TABLE if not exists " + "SIGNINFO" + " ("
                        + "FirstName TEXT,"
                        + "LastName TEXT,"
                        + "EmailID TEXT,"
                        + "Password TEXT,"
                        + "Phone TEXT,"
                        + "Security TEXT);";
                ;
                //perform your database operations here ...

                db.execSQL(query3);
                System.out.println("TABLE CREATED signup storage");
                db.setTransactionSuccessful();

            } catch (SQLiteException e) {

                System.out.println("********SOME THING WRONG WHILE CREATING******");
            } finally {
                db.endTransaction();
            }


        } catch (SQLException e) {
            System.out.println("********SOME THING WRONG OUTER WHILE OPENING******");
            //Toast.makeText(CVSStore.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


        try {

            db = getApplicationContext().openOrCreateDatabase(s, MODE_APPEND, null);
            db.beginTransaction();

            String query5 = "INSERT INTO SIGNINFO" +
                    " (FirstName,LastName,EmailID,Password,Phone,Security) "
                    + "VALUES ('"
                    + firstName + "','"
                    + lastName + "','"
                    + emailID + "','"
                    + password + "','"
                    + phoneNumber + "','"
                    + securityQuest + "');";


            db.execSQL(query5);

            System.out.println("value inserted");

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            System.out.println("SOMETHING WRONG WHILE INSERTING");
        } finally {
            db.endTransaction();
        }
    }



}
