package terrain.foot.com.foot;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by User on 2/28/2017.
 */

@SuppressLint("ValidFragment")
public class Tab1Fragment extends Fragment {
    private static final String TAG = "Tab1Fragment";
    Dialog myDialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    DatabaseReference myRefsimpleuser = database.getReference("simpleusers");
    ListView mListView;
   static ArrayList<Person> p = new ArrayList<Person>();
    static ValueEventListener value1;
    Button button;
    String id;
    ViewGroup footer=null;
    int nombre=10;
    IconAdapter adapter;
    Infosuser_adapter adapterr;
    LayoutInflater inflater1;
    ListView listres;
    ImageView im;
    TextView nom;
    ArrayList<String > users = new ArrayList<>();
    @SuppressLint("ValidFragment")
public Tab1Fragment(String id){
    this.id=id;
}

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab1_fragment,container,false);
        mListView=(ListView)view.findViewById(R.id.listView);
        myDialog = new Dialog(getContext());

        adapter = new IconAdapter(getActivity(), R.layout.adapter_view_layout,p);
       inflater1 = getLayoutInflater();

        if(p.isEmpty()) {
            reservation(10,inflater,view);
        }
        else {
            mListView.setAdapter(adapter);
        }

        if(p.size()>=10&& footer==null){
            footer = (ViewGroup)inflater.inflate(R.layout.footer,mListView,false);
            mListView.addFooterView(footer,null,true);
            button=(Button)view.findViewById(R.id.ajouter);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nombre=nombre+10;
                    reservation(nombre,inflater,view);
                    adapter.notifyDataSetChanged();
                    myRef.child(id).child("numberres").setValue(0);
                }
            });}
        if(p.size()<10 && footer!=null) {mListView.removeFooterView(footer);}
         mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                myDialog.setContentView(R.layout.reservation);
                listres=(ListView)myDialog.findViewById(R.id.listres);
                im=(ImageView)myDialog.findViewById(R.id.photores);
                nom=(TextView)myDialog.findViewById(R.id.nom);
                 String id1=p.get(position).getId();
                 myRefsimpleuser.child(id1).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               users.clear();
               RequestOptions myOptions = new RequestOptions().override(1000, 1000);
               Glide.with(getActivity()).load(dataSnapshot.child("_filephoto").getValue()).apply(myOptions).into(im);
nom.setText(dataSnapshot.child("_lastname").getValue().toString()+" "+dataSnapshot.child("_name").getValue().toString());
              users.add(dataSnapshot.child("Mobile").getValue().toString());
              users.add(p.get(position).getDatederese());
              adapterr = new Infosuser_adapter(getActivity(),users);
              listres.setAdapter(adapterr);
           }
          @Override
            public void onCancelled(DatabaseError databaseError) {
             }
            });


                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();

            }
        });

        return view;

    }
    public void reservation(int nbr, final LayoutInflater inflater, final View view){

        Query query = myRef.child(id).child("Newreservation").orderByKey()
        .limitToLast(nbr);

       value1=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                p.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    p.add(new Person("",snapshot.child("date").getValue().toString(),snapshot.child("heure").getValue().toString(),snapshot.getKey(),snapshot.child("etat").getValue().toString(),snapshot.child("idTerrain").getValue().toString(),snapshot.child("dateDeDemande").getValue().toString(),snapshot.child("profilephoto").getValue().toString()));
                }
                Collections.reverse(p);
                if(p.size()>=10 && footer==null){
                    footer = (ViewGroup)inflater.inflate(R.layout.footer,mListView,false);
                    mListView.addFooterView(footer,null,true);

                    button=(Button)view.findViewById(R.id.ajouter);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nombre=nombre+10;
                            reservation(nombre,inflater,view);
                            //  myRef.child(id).child("Newreservation").limitToFirst(10)
                            adapter.notifyDataSetChanged();
                        }
                    });}
                    if(p.size()<nombre && footer!=null) {mListView.removeFooterView(footer);
                footer=null;
                }
                mListView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        query.addValueEventListener(value1);
    }

}
