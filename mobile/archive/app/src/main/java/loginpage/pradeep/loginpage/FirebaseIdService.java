package loginpage.pradeep.loginpage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by pradeepbalakrishnan on 4/5/17.
 */

public class FirebaseIdService extends FirebaseInstanceIdService
{


    final String tokenPreferenceKey = "fcm_token";

    final static String infoTopicName = "Edge";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(tokenPreferenceKey, FirebaseInstanceId.getInstance().getToken()).apply();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token", token);
        System.out.print("token"+token);
        FirebaseMessaging.getInstance().subscribeToTopic(infoTopicName);
    }
}
