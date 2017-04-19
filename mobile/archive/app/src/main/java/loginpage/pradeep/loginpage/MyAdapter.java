package loginpage.pradeep.loginpage;

import android.graphics.BitmapFactory;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Anoop Jatavallabha on 3/21/2017.
 */

public class MyAdapter extends ArrayAdapter<Name_Review> {
    private final Context context;
    private final ArrayList<Name_Review> itemsArrayList;
    RatingBar ratingBar;

    public MyAdapter(Context context, ArrayList<Name_Review> itemsArrayList) {

        super(context, R.layout.custom_layout_menu, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.custom_layout_menu, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.name);
        //ImageView valueView = (ImageView) rowView.findViewById(R.id.review);

        ratingBar = (RatingBar) rowView.findViewById(R.id.ratingBar);
        double rating = (itemsArrayList.get(position).getRating());
        ratingBar.setRating(Float.parseFloat(rating+""));

        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).getTitle());




        // 5. retrn rowView
        return rowView;
    }

}
