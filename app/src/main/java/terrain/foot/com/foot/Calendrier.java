package terrain.foot.com.foot;

import android.annotation.SuppressLint;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import java.util.Vector;



public class Calendrier extends AppCompatActivity  {
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String id,date,Email;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
            Vector<Button> ids=new Vector<Button>();
    ImageView back,refresh;
    SwipeRefreshLayout swipeRefreshLayout;
    ChildEventListener value;
    Vector<Integer> tblocker = new Vector<Integer>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendrier);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Email=intent.getStringExtra("Email");
        back=(ImageView)findViewById(R.id.back);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe);

swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Calendrier.this,Calendrier.class);
                intent.putExtra("id", id);
                intent.putExtra("Email",Email);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }

});
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setColorFilter(R.color.colorPrimaryDark);
                Intent intent = new Intent(Calendrier.this,Profile.class);
                intent.putExtra("id", id);
                intent.putExtra("Email",Email);
              // myRef.child(id).child("Newreservation").removeEventListener(value);
                startActivity(intent);
                finish();
               // sendSMS("27763486", "Hi You got a message!");
            }
        });
        refresh=(ImageView)findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh.setColorFilter(R.color.colorPrimaryDark);
                Intent intent = new Intent(Calendrier.this,Calendrier.class);
                intent.putExtra("id", id);
                intent.putExtra("Email",Email);
                 startActivity(intent);
                finish();
            }
        });

        ids.add((Button)findViewById( R.id.a));
        ids.add((Button)findViewById(R.id.b));
        ids.add((Button)findViewById(R.id.c));
        ids.add((Button)findViewById(R.id.d));
        ids.add((Button)findViewById(R.id.e));
        ids.add((Button)findViewById(R.id.f));
        ids.add((Button)findViewById(R.id.g));
        ids.add((Button)findViewById(R.id.h));
        ids.add((Button)findViewById(R.id.i));
        ids.add((Button)findViewById(R.id.j));
        ids.add((Button)findViewById(R.id.k));
        ids.add((Button)findViewById(R.id.l));
        ids.add((Button)findViewById(R.id.m));
        ids.add((Button)findViewById(R.id.n));
        ids.add((Button)findViewById(R.id.o));
        ids.add((Button)findViewById(R.id.p));
        restart();


      for(int i=0;i<ids.size();i++){

        final int finalI = i;
        ids.get(i).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(date==null){
                Toast.makeText(getApplicationContext(), "choisissez un date d'abord", Toast.LENGTH_SHORT).show();

            }else{
            blocker(ids.get(finalI),finalI);}
        }
        });
}
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Calendrier.this,
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

                date = year+"-"+month+"-"+day;

                mDisplayDate.setText(date);
                restart();
                adapterlist();



            }
        };





    }
    public  void  adapterlist(){
        final Button[] b = new Button[1];
       Query query=myRef.child(id).child("Newreservation").orderByChild("date").equalTo(date);


        value = new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                b[0] = ids.get(Integer.parseInt(dataSnapshot.child("etat").getValue().toString()));

                b[0].setBackground(getResources().getDrawable(R.drawable.testlistgreen));

                b[0].setEnabled(false);
                b[0].setClickable(false);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                b[0] = ids.get(Integer.parseInt(dataSnapshot.child("etat").getValue().toString()));

                b[0].setBackgroundColor(Color.WHITE);

                b[0].setEnabled(true);
                b[0].setClickable(true);


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
 tblocker.add(Integer.parseInt(data.getKey()));
                ids.get(Integer.parseInt(data.getKey())).setBackground(getResources().getDrawable(R.drawable.testlist));
            }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public  void restart(){
        for(int i=0;i<ids.size();i++){

                ids.get(i).setBackgroundColor(Color.WHITE);

            ids.get(i).setEnabled(true);
            ids.get(i).setClickable(true);

        }
    }
    public void blocker(final Button b, final int i){

        if(tblocker.contains(i)){
        AlertDialog.Builder builder =new AlertDialog.Builder(Calendrier.this);
        builder.setMessage("voulez vous Libérer ce temp").setPositiveButton("oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                b.setBackgroundColor(Color.WHITE);
                tblocker.remove(tblocker.indexOf(i));

                myRef.child(id).child("tempBloquer").child(date).child(Integer.toString(i)).removeValue();

            }
        }).setNegativeButton("Cancel",null);
        AlertDialog alertDialog = builder.create();

        alertDialog.show();}
        else{
            AlertDialog.Builder builder =new AlertDialog.Builder(Calendrier.this);
            builder.setMessage("voulez vous Blocker ce temp").setPositiveButton("oui", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    b.setBackground(getResources().getDrawable(R.drawable.testlist));
                    tblocker.add(i);

                    myRef.child(id).child("tempBloquer").child(date).child(Integer.toString(i)).setValue(i);

                }
            }).setNegativeButton("Cancel",null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
    private void sendSMS(String phoneNumber, String message) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("smsto", phoneNumber, null));
        sendIntent.putExtra("sms_body", "text message");
        startActivity(sendIntent);
        }
}
