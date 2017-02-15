package loginpage.pradeep.loginpage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPassword extends AppCompatActivity {

    private Cursor c;
    private SQLiteDatabase db ;
    String result = "Asd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = getIntent();
                String email = intent.getStringExtra("email");
                //using this email update password

                EditText passwod = (EditText) findViewById(R.id.resestPassword);
                EditText confirmPassword = (EditText) findViewById((R.id.confrimResetPassword));
                String pass = passwod.getText().toString();
                String confirmPass = confirmPassword.getText().toString();
                //System.out.println(confirmPass);
                if(pass.equals(confirmPass)){
                    result = updatePassword(email,confirmPass);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                }
                System.out.println(result);

            }
        });
        //Query using this email and update password if security question matches

    }
    public String updatePassword(String email,String confirmPass){
        Context context = getApplicationContext();
        String s = String.valueOf(context.getDatabasePath("Anoop.db"));
        db = getApplicationContext().openOrCreateDatabase(s,MODE_APPEND,null);
        db.beginTransaction();
        //String SELECT_SQL = "UPDATE SIGNINFO SET Password='" + confirmPass + "' WHERE EmailID=" + email + ";";
        //db.execSQL(SELECT_SQL);

        //String query = "SELECT * FROM SIGNINFO;";
        //c = db.rawQuery(query,null);
        ContentValues cv = new ContentValues();
        cv.put("Password",confirmPass);
        db.update("SIGNINFO", cv, "Emailid = "+"'"+email+"'", null);
        db.setTransactionSuccessful();
        db.endTransaction();
        /*c.moveToFirst();
        while(c.isAfterLast() == false){
            if(email.equals(c.getString(c.getColumnIndex("EmailID")))){
                break;
            }
            else{
                c.moveToNext();
            }

        }*/


        return "Sucess";

    }
}
