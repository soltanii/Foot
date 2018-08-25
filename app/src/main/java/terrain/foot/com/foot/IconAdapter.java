package terrain.foot.com.foot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class IconAdapter extends ArrayAdapter<Person> {


    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView name;
        TextView birthday;
        TextView sex;
        ImageView profilephoto;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public IconAdapter(Context context, int resource, ArrayList<Person> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String name = getItem(position).getName();
        String birthday = getItem(position).getDate();
        String sex = getItem(position).getHeure();
        String key=getItem(position).getKey();
        String etat=getItem(position).getEtat();
        String id=getItem(position).getId();
        String datederese=getItem(position).getDatederese();
        String profilephot=getItem(position).getProfilephot();
        //Create the person object with the information
        Person person = new Person(name,birthday,sex,key,etat,id,datederese,profilephot);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.textView);
            holder.birthday = (TextView) convertView.findViewById(R.id.textVieww);
            holder.sex = (TextView) convertView.findViewById(R.id.textViewww);
holder.profilephoto=(ImageView)convertView.findViewById(R.id.profilephot);
            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


       /* Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);*/
        lastPosition = position;

        holder.name.setText(person.getName());
        holder.birthday.setText(person.getDate());
        holder.sex.setText(person.getHeure());
        Glide.with(getContext()).load(person.getProfilephot())
                .into(holder.profilephoto);

        return convertView;
    }

}
