package loginpage.pradeep.loginpage;



import android.content.Context;
import android.content.Intent;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class SignUpPage extends AppCompatActivity {



    SQLiteDatabase db;


    private  static String firstName = null;
    private  static String lastName = null;
    private  static String emailID = null;
    private  static String password = null;
    private  static String confirmpassword = null;
    private  static String phoneNumber = null;
    private  static String securityQuest = null;

    public String storeName;

    private Context context;
    private String s;

    private EditText first_name;
    private EditText last_name;
    private EditText email;
    private EditText passwd;
    private EditText cfrmpswd;
    private EditText phone_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        context = getApplicationContext();
        s = String.valueOf(context.getDatabasePath("Anoop.db"));

        first_name = (EditText) findViewById(R.id.firstname);
        last_name = (EditText) findViewById(R.id.lastname);
        email = (EditText) findViewById(R.id.email);
        passwd = (EditText) findViewById(R.id.password);
        cfrmpswd = (EditText) findViewById(R.id.confpassword);
        phone_number = (EditText) findViewById(R.id.phoneNumber);

        Button create = (Button) findViewById(R.id.confirmsignUp);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstName = first_name.getText().toString();
                lastName = last_name.getText().toString();
                emailID = email.getText().toString();
                password = passwd.getText().toString();


                confirmpassword = cfrmpswd.getText().toString();


                phoneNumber = phone_number.getText().toString();

                EditText security_ques = (EditText) findViewById(R.id.secquest1);
                securityQuest = security_ques.getText().toString();

                boolean validemail = isEmailValid(emailID);
                boolean validpassword = password.equals(confirmpassword);

                if(!validemail ||!validpassword){
                    if(!isEmailValid(emailID))
                        email.setError("Invalid Email");

                    if(!validpassword)
                        passwd.setError("Invalid Password");
                }
                else
                {
                    if(enrtyDatabase())
                    {
                        activePage(v);
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Something went wrong Re-register", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


    }

    private void activePage(View view) {
        Intent intent = new Intent(this, ActivityPage.class);
        startActivity(intent);

    }

    private boolean isEmailValid(String emailID) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailID).matches();
    }

    private boolean enrtyDatabase() {
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
            return false;
        } finally {
            db.endTransaction();
            return true;
        }
    }



}
