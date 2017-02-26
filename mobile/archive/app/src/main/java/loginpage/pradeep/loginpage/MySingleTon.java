package loginpage.pradeep.loginpage;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by pradeepbalakrishnan on 2/23/17.
 */

public class MySingleTon {

    private static MySingleTon instance;
    private RequestQueue requestQueue;
    private static Context context;

    // constructor

    private MySingleTon(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if(requestQueue == null){
            //initialise reqQue

            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized  MySingleTon getInstance(Context con){

        if(instance == null){
            instance = new MySingleTon(con);
        }
        return instance;
    }

    // adding to requestQueue

    public <T>  void addToReqQue(Request<T> request){

        requestQueue.add(request);
        requestQueue.start();
    }
}
