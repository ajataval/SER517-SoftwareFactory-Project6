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
        final String dbSecurityQuestion = intent1.getStringExtra("securityQuestion");
        goToReset.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v)
            {

                // IF SECURITY QUESTION IS CORRECT SEND EMAIL TO NEXT PAGE

                EditText securityAnswer = (EditText) findViewById(R.id.securityAnswer);
                String secAns = securityAnswer.getText().toString();


                System.out.println("email from intent" + email);
                //goToResetfunc(email);
                if(dbSecurityQuestion.equals(secAns)){
                    goToResetfunc(email);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Security answer was not correct", Toast.LENGTH_LONG).show();
                }

            }

        });
    }


    public void goToResetfunc(String email){
        Intent intent = new Intent(this,ResetPassword.class);
        intent.putExtra("email",email);
        startActivity(intent);
    }
}
