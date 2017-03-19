package loginpage.pradeep.loginpage;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by pradeepbalakrishnan on 3/15/17.
 */

public class EncryptDec extends Activity {
        public static String seedValue = "I AM UNBREAKABLE";
        public static String MESSAGE = "No one can read this message without decrypting me.";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            try {
                String encryptedData = AESHelper.encrypt(seedValue, MESSAGE);
                Log.v("EncryptDecrypt", "Encoded String " + encryptedData);
                String decryptedData = AESHelper.decrypt(seedValue, encryptedData);
                Log.v("EncryptDecrypt", "Decoded String " + decryptedData);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

