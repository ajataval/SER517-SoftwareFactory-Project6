package loginpage.pradeep.loginpage;

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

public class ForgetPassword extends AppCompatActivity {

    private Cursor c;
    private SQLiteDatabase db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        final Button readEmail = (Button) findViewById(R.id.readEmail);
        readEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText email = (EditText) findViewById(R.id.recoveryemail);
                String emailID = email.getText().toString();
                boolean flag = checkEmailId(emailID);
                if(flag){
                    goToSecurityScreen(emailID);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid emailID", Toast.LENGTH_LONG).show();
                }
                db.setTransactionSuccessful();
                db.endTransaction();


            }
        });
    }

    public boolean checkEmailId(String emailId){
        Context context = getApplicationContext();
        String s = String.valueOf(context.getDatabasePath("Anoop.db"));
        db = getApplicationContext().openOrCreateDatabase(s,MODE_APPEND,null);
        db.beginTransaction();
        String SELECT_SQL = "SELECT Security FROM SIGNINFO WHERE EmailID="+"'"+emailId+"'";
        c = db.rawQuery(SELECT_SQL,null);
        if(c.moveToFirst()){

            return  true;
        }
        else{
            return false;
        }


    }
    public void goToSecurityScreen(String emailID)
    {
        Intent intent = new Intent(this,SecurityQuestion.class);
        intent.putExtra("email",emailID);
        startActivity(intent);
    }



}
