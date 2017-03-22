package loginpage.pradeep.loginpage;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Anoop Jatavallabha on 3/21/2017.
 */

public class MyComparator  implements Comparator<Name_Review>{

    public int compare( Name_Review o1,Name_Review o2){
        if(o1.getDescription() > o2.getDescription()){
            return -1;
        }
        else if (o1.getDescription() < o2.getDescription()){
            return 1;
        }
        return 0;
    }

}
