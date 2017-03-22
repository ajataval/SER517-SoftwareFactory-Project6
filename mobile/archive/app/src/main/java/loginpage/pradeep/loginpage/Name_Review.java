package loginpage.pradeep.loginpage;

/**
 * Created by Anoop Jatavallabha on 3/21/2017.
 */

public class Name_Review {
    private String title;
    private String description;

    public Name_Review(String title, String description) {
        super();
        this.title = title;
        this.description = description;
    }

    public String getTitle(){
        return this.title;
    }
    public String getDescription(){
        return this.description;
    }
}
