package loginpage.pradeep.loginpage;

/**
 * Created by Anoop Jatavallabha on 3/21/2017.
 */

public class Name_Review {
    private String title;
    private double rating;
    private String description;

    public Name_Review(String title, double rating, String description) {
        super();
        this.title = title;
        this.description = description;
        this.rating = rating;
    }

    public String getTitle(){
        return this.title;
    }
    public double getRating(){
        return this.rating;
    }

    public String getDescription(){
        return this.description;
    }
}
