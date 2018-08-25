package terrain.foot.com.foot;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FeedBackAdapter extends ArrayAdapter<Feedback_item> {
    FirebaseDatabase database;
    DatabaseReference myRef;
    String name;


    ArrayList<Feedback_item> users = new ArrayList<>();
    public FeedBackAdapter(Context context, ArrayList<Feedback_item> users) {
        super(context, 0, users);
        this.users=users;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position


        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.feedbackitem, parent, false);
        }
        // Lookup view for data population
        TextView titre= (TextView) convertView.findViewById(R.id.titre);
        TextView text = (TextView) convertView.findViewById(R.id.description);
        final ImageView image = convertView.findViewById(R.id.image1);
        final ImageView profilephoto = convertView.findViewById(R.id.profilephoto);

        String text1 = users.get(position).getText();
        String titre1 = users.get(position).getTitre();
        String image1 = users.get(position).getImg();
String image2=users.get(position).getPhotoitem();
        if (image!=null) {
            Glide.with(getContext()).load(image1)
                    .into(image);
        }
        if (image2!=null) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.fitCenter();
            Glide.with(getContext()).applyDefaultRequestOptions(requestOptions).load(image2)
                    .into(profilephoto);
        }
        if (titre1!=null) {  titre.setText(titre1);}
        if (text1!=null) {  text.setText(text1);}


        return convertView;
    }

}
