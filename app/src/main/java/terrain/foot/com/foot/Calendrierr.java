package terrain.foot.com.foot;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Calendrierr  extends AppCompatActivity {
  ArrayList<liststate> list;
   ListView listview ;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    ChildEventListener value;
    String id,date,Email;
    ImageView back,refresh;
    CalendrierAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.calendrierr);
   setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

       Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Email=intent.getStringExtra("Email");
        back=(ImageView)findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setColorFilter(R.color.colorPrimaryDark);
                Intent intent = new Intent(Calendrierr.this,Profile.class);
                intent.putExtra("id", id);
                intent.putExtra("Email",Email);
                startActivity(intent);
                finish();
            }
        });
        refresh=(ImageView)findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh.setColorFilter(R.color.colorPrimaryDark);
                Intent intent = new Intent(Calendrierr.this,Calendrierr.class);
                intent.putExtra("id", id);
                intent.putExtra("Email",Email);
                startActivity(intent);
                finish();
            }
        });
listview = (ListView) findViewById(R.id.timelist);
        String[] values = new String[] { "00:00-01:30", "01:30-03:00", "03:00-04:30",
                "04:30-06:00", "06:00-07:30", "07:30-09:00", "09:00-10:30", "10:30-12:00",
                "12:00-13:30", "13:30-15:00", "15:00-16:30", "16:30-18:00", "18:00-19:30", "19:30-21:00",
                "21:00-22:30", "22:30-00:00" };
        list = new ArrayList<>();
        adapter = new CalendrierAdapter(this, list);

        for (int i = 0; i < values.length; ++i) {
            list.add(new liststate(0,values[i]));
        }
        listview.setAdapter(adapter);

        mDisplayDate = (TextView) findViewById(R.id.Date);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Calendrierr.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("Calendrier", "onDateSet: mm-dd-yyy: " + day + "-" + month + "-" + year);

                date = year+"-"+"0"+month+"-"+day;

                mDisplayDate.setText(date);
                restart();
                adapterlist();



            }
        };

        init_list();

    }
    public  void  adapterlist(){
        Query query=myRef.child(id).child("Newreservation").orderByChild("date").equalTo(date);


        value = new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.get(Integer.parseInt(dataSnapshot.child("etat").getValue().toString())).setEtat(1);

                adapter.notifyDataSetChanged();

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
               list.get(Integer.parseInt(dataSnapshot.child("etat").getValue().toString())).setEtat(0);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        query.addChildEventListener(value);

        myRef.child(id).child("tempBloquer").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        list.get(Integer.parseInt(data.getKey())).setEtat(2);

                   }adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    public  void restart(){
        for(int i=0;i<list.size();i++){
list.get(i).setEtat(0);
        }
        adapter.notifyDataSetChanged();

    }
    public void init_list()
    {
        final ImageView[] im = new ImageView[1];
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id1) {
               if(date!=null){
                if(list.get(position).getEtat()==0)
                {
                    AlertDialog.Builder builder =new AlertDialog.Builder(Calendrierr.this);
                    builder.setMessage("voulez vous Blocker ce temp").setPositiveButton("oui", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            list.get(position).setEtat(2);
                            im[0] =(ImageView)view.findViewById(R.id.check_bloc);
                            im[0].setImageResource(R.drawable.closex);
                            im[0].setBackgroundResource(R.color.gris0);

                            myRef.child(id).child("tempBloquer").child(date).child(Integer.toString(position)).setValue(position);

                        }
                    }).setNegativeButton("Cancel",null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }else if (list.get(position).getEtat()==2){
                    AlertDialog.Builder builder =new AlertDialog.Builder(Calendrierr.this);
                    builder.setMessage("voulez vous Libérer ce temp").setPositiveButton("oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            list.get(position).setEtat(0);
                            im[0] =(ImageView)view.findViewById(R.id.check_bloc);
                            im[0].setImageDrawable(null);

                            myRef.child(id).child("tempBloquer").child(date).child(Integer.toString(position)).removeValue();

                        }
                    }).setNegativeButton("Cancel",null);
                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                }else {


                }}else {           Toast.makeText(getApplicationContext(), "choisissez une date d'abord", Toast.LENGTH_SHORT).show();
               }
            }
        });


    }
}
