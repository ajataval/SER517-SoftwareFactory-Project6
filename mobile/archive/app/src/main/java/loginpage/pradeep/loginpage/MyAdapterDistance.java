package loginpage.pradeep.loginpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anoop Jatavallabha on 3/21/2017.
 */

public class MyAdapterDistance extends ArrayAdapter<Name_Distance> {
    private final Context context;
    private final ArrayList<Name_Distance> itemsArrayList;

    public MyAdapterDistance(Context context, ArrayList<Name_Distance> itemsArrayList) {

        super(context, R.layout.custom_layout, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.custom_layout, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.name);
        TextView valueView = (TextView) rowView.findViewById(R.id.review);

        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).getTitle());
        valueView.setText(itemsArrayList.get(position).getDescription());

        // 5. retrn rowView
        return rowView;
    }


}
