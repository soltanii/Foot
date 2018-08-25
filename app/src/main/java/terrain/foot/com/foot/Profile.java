package terrain.foot.com.foot;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static terrain.foot.com.foot.Tab1Fragment.p;
import static terrain.foot.com.foot.Tab1Fragment.value1;


public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    View include1,include2;
    private SectionsPageAdapter mSectionsPageAdapter;
    String id,Email;
    private ViewPager mViewPager;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    static  StadeModel stade=null;
    ImageView refrech,wifi;
    TabLayout tabLayout;
    int number=-1,numberi=-1;
    boolean existcnn=false;
    ValueEventListener valuee,valuei;
    MediaPlayer mp;
    Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);     //  Fixed Portrait orientation

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Email=intent.getStringExtra("Email");


        include1=findViewById(R.id.incl1);
        include2=include1.findViewById(R.id.incl2);
        wifi=(ImageView)include2.findViewById(R.id.wifi);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.googleevent);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(isNetworkAvailable()){
           wifi.setVisibility(View.GONE);
           existcnn=true;

             }





        refrech=(ImageView)findViewById(R.id.refresh);
        refrech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refrech.setColorFilter(R.color.colorPrimaryDark);
                Intent intent = new Intent(Profile.this,Profile.class);
                intent.putExtra("id", id);
                intent.putExtra("Email", Email);
                p.clear();
                myRef.child(id).child("numberres").setValue(0);
                myRef.child(id).child("numberfeedback").setValue(0);
                startActivity(intent);
                finish();



            }
        });


        tabs();
        myRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stade= new StadeModel(0,dataSnapshot.child("firstName").getValue().toString(),dataSnapshot.child("localisation").getValue().toString(),id,dataSnapshot.child("profilePic").getValue().toString(),Integer.parseInt(dataSnapshot.child("phoneNumber").getValue().toString()));


                remplissage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        valuee=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                number=Integer.parseInt(dataSnapshot.getValue().toString());
                if(number>0){
                    tabLayout.getTabAt(0).setCustomView(null);
                    tabLayout.getTabAt(0).setCustomView(R.layout.badged_tab);
                    v.vibrate(900);
                    mp.start();
                    addnotifcation(number,0);}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.child(id).child("numberres").addValueEventListener(valuee);
        valuei=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numberi=Integer.parseInt(dataSnapshot.getValue().toString());
                if(numberi>0){
                    tabLayout.getTabAt(1).setCustomView(null);
                    tabLayout.getTabAt(1).setCustomView(R.layout.badged_tab);
                    v.vibrate(900);
                        mp.start();
                    addnotifcation(numberi,1);}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.child(id).child("numberfeedback").addValueEventListener(valuei);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id1 = item.getItemId();
if(existcnn) {
    if (id1 == R.id.nav_camera) {

        Intent intent = new Intent(Profile.this, Image.class);
        intent.putExtra("id", id);
        intent.putExtra("Email", Email);
        myRef.child(id).child("Newreservation").removeEventListener(value1);
        myRef.child(id).child("numberres").removeEventListener(valuee);
        myRef.child(id).child("numberres").setValue(0);
        myRef.child(id).child("numberfeedback").setValue(0);
        p.clear();
        startActivity(intent);
        finish();
    } else if (id1 == R.id.calendrier) {
        Intent intent = new Intent(Profile.this, Calendrierr.class);
        intent.putExtra("id", id);
        intent.putExtra("Email", Email);
        myRef.child(id).child("Newreservation").removeEventListener(value1);
        myRef.child(id).child("numberres").removeEventListener(valuee);
        myRef.child(id).child("numberres").setValue(0);
        myRef.child(id).child("numberfeedback").setValue(0);
        p.clear();
        startActivity(intent);
        finish();
    } else if (id1 == R.id.logout) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().remove("id").commit();
        prefs.edit().remove("Email").commit();
        removefile("id");
        removefile("Email");
        myRef.child(id).child("numberres").setValue(0);
        myRef.child(id).child("numberfeedback").setValue(0);
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Profile.this, Login.class);
        myRef.child(id).child("Newreservation").removeEventListener(value1);
        myRef.child(id).child("numberres").removeEventListener(valuee);
        myRef.child(id).child("numberres").removeEventListener(valuei);

        p.clear();
        startActivity(intent);
        finish();

    } else if (id1 == R.id.nav_manage) {
        if(stade!=null){
        Intent intent = new Intent(Profile.this, Parametre.class);
        intent.putExtra("id", id);
        intent.putExtra("Email", Email);
        myRef.child(id).child("Newreservation").removeEventListener(value1);
        myRef.child(id).child("numberres").removeEventListener(valuee);
        p.clear();
        myRef.child(id).child("numberres").removeEventListener(valuei);
            myRef.child(id).child("numberres").setValue(0);
            myRef.child(id).child("numberfeedback").setValue(0);
        startActivity(intent);
        finish();}
    } else if (id1 == R.id.tournoi) {
        Intent intent = new Intent(Profile.this, Evenement.class);
        intent.putExtra("id", id);
        intent.putExtra("Email", Email);
        myRef.child(id).child("Newreservation").removeEventListener(value1);
        myRef.child(id).child("numberres").removeEventListener(valuee);
        myRef.child(id).child("numberres").removeEventListener(valuei);
        p.clear();
        myRef.child(id).child("numberres").setValue(0);
        myRef.child(id).child("numberfeedback").setValue(0);
        startActivity(intent);
        finish();
    }
}
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
          SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
          adapter.addFragment(new Tab1Fragment(id), "");
          adapter.addFragment(new Tab2Fragment(id), "");
          viewPager.setAdapter(adapter);
    }
    public  void remplissage(){
        NavigationView nv;
        View head;
        TextView t;
        TextView t1;
        nv = (NavigationView)findViewById(R.id.nav_view);
        head = nv.getHeaderView(0);
        t=(TextView)head.findViewById(R.id.tv);
        t1=(TextView)head.findViewById(R.id.textView);
        t.setText(stade.getFirstName());
        t1.setText(Email);
    }
    public void addnotifcation(int number,int index){
        if(tabLayout.getTabAt(index) != null && tabLayout.getTabAt(index).getCustomView() != null) {
            TextView b = (TextView) tabLayout.getTabAt(index).getCustomView().findViewById(R.id.badge);
            if(b != null) {
                b.setText( number+ "");

            }
            View v = tabLayout.getTabAt(index).getCustomView().findViewById(R.id.badgeCotainer);
            if(v != null) {
                v.setVisibility(View.VISIBLE);
            }
        }
    }
    public void tabs(){
        tabLayout = (TabLayout) include2.findViewById(R.id.tabs);
        tabLayout.setSelected(true);
        tabLayout.setTabTextColors(getResources().getColor(R.color.gris1),getResources().getColor(R.color.vert2));
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) include2.findViewById(R.id.container);
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(1).setIcon(R.drawable.messagealertoutline);
        tabLayout.getTabAt(0).setIcon(R.drawable.playlist_plus);
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#388e3c"), PorterDuff.Mode.SRC_IN);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#388e3c"), PorterDuff.Mode.SRC_IN);
                    tab.setCustomView(null);
                    myRef.child(id).child("numberres").setValue(0);
                    myRef.child(id).child("numberfeedback").setValue(0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
        // Do nothing or catch the keys you want to block
        return false;
    }
    public void removefile(String name){
        String dir = getFilesDir().getAbsolutePath();
        File f0 = new File(dir, name);
        f0.delete();
    }
    private boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
