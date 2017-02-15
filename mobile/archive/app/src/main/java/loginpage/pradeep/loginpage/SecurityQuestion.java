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

public class SecurityQuestion extends AppCompatActivity {

    private Cursor c;
    private SQLiteDatabase db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);


        Button goToReset = (Button) findViewById(R.id.goToResetPage);
        Intent intent1 = getIntent();
        final String email = intent1.getStringExtra("email");
        goToReset.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v)
            {

                EditText securityAnswer = (EditText) findViewById(R.id.securityAnswer);
                String secAns = securityAnswer.getText().toString();

                String secAnsw = null;
                System.out.println("email from intent" + email);
                secAnsw = getSecurityAnswer(email);
                // query if answer is right using email

                //if yes go to reset page
                //System.out.println(email);
                System.out.println("security answer is" + secAnsw);
                //goToResetfunc(email);
                if(secAns.equals(secAnsw)){
                    goToResetfunc(email);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Security answer was not correct", Toast.LENGTH_LONG).show();
                }
                db.setTransactionSuccessful();
                db.endTransaction();


            }

        });
    }

    public String getSecurityAnswer(String email){
        System.out.print("This function is being called");
        Context context = getApplicationContext();
        String s = String.valueOf(context.getDatabasePath("Anoop.db"));
        db = getApplicationContext().openOrCreateDatabase(s,MODE_APPEND,null);
        db.beginTransaction();
        String SELECT_SQL = "SELECT Security FROM SIGNINFO WHERE EmailID="+"'"+email+"'";

        c = db.rawQuery(SELECT_SQL,null);
        if(c.moveToFirst())
            return  c.getString(c.getColumnIndex("Security"));
        else
            return "FAILED";

    }
    public void goToResetfunc(String email){
        Intent intent = new Intent(this,ResetPassword.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }
}
