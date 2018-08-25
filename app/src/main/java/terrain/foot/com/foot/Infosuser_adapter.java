package terrain.foot.com.foot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Infosuser_adapter extends ArrayAdapter<String  > {




        String name;


        ArrayList<String > users = new ArrayList<>();
        public Infosuser_adapter(Context context,ArrayList<String >  users) {
            super(context, 0, users);
            this.users=users;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.userinfos_item, parent, false);
            }

            TextView text = (TextView) convertView.findViewById(R.id.info);
            TextView type = (TextView) convertView.findViewById(R.id.type);

            ImageView state=(ImageView)convertView.findViewById(R.id.iconn);
            String text1 = users.get(position);
            text.setText(text1);
            if(position==0)
{
    state.setImageResource(R.drawable.phone);
   type.setText("Mobile");

}else {
                state.setImageResource(R.drawable.progress_clock);
                type.setText("Temps de réservation");

            }




            return convertView;
        }
    }


