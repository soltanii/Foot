package terrain.foot.com.foot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;



@SuppressLint("ValidFragment")
public class Tab2Fragment extends Fragment {
    ImageButton back;
    ImageView refresh,addpub;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    DatabaseReference myRefsimpleuser = database.getReference("simpleusers");
    ListView publications;
    String id;
    Button button;
    ViewGroup footer=null;
    ValueEventListener valuea;
    int nombre=7;
    ArrayList<Feedback_item> pub= new ArrayList<>();
    ArrayAdapter<Feedback_item> adapter;
    final String[] name = new String[1];

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab2_fragment,container,false);

        publications = view.findViewById(R.id.pubb);
        adapter = new FeedBackAdapter(getActivity(), pub);
pub.clear();
        if(pub.isEmpty()){
            remplipub(7,view,inflater);
        }
        else {
            publications.setAdapter(adapter);
        }
        if(pub.size()>=7&& footer==null){
            footer = (ViewGroup)inflater.inflate(R.layout.footer,publications,false);
            publications.addFooterView(footer,null,true);
            button=(Button)view.findViewById(R.id.ajouter);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nombre=nombre+7;
                    remplipub(nombre,view,inflater);
                    adapter.notifyDataSetChanged();
                }
            });}
        if(pub.size()<7 && footer!=null) {publications.removeFooterView(footer);}
        return view;
    }
    public void remplipub(int nbr, final View view, final LayoutInflater inflater){
        Query query = myRef.child(id).child("feedback").orderByKey()
               .limitToLast(nbr);
        valuea= new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pub.clear();
                if(dataSnapshot.exists()) {

                    for (final DataSnapshot data : dataSnapshot.getChildren()) {
                        if(data.hasChild("text") && data.hasChild("img")) {
                            pub.add(new Feedback_item(data.child("text").getValue().toString(), data.child("img").getValue().toString(),data.child("name").getValue().toString()+" "+data.child("lastname").getValue().toString(),data.getKey(),data.child("profilephoto").getValue().toString()));
                        }else if(!data.hasChild("text")){
                            pub.add(new Feedback_item("", data.child("img").getValue().toString(), data.child("name").getValue().toString()+" "+data.child("lastname").getValue().toString(),data.getKey(),data.child("profilephoto").getValue().toString()));
                        }else if(!data.hasChild("img")){
                            pub.add(new Feedback_item(data.child("text").getValue().toString(),"",  data.child("name").getValue().toString()+" "+data.child("lastname").getValue().toString(),data.getKey(),data.child("profilephoto").getValue().toString()));
                        }

                        if(data.hasChild("text") && data.hasChild("img")) {
                            pub.add(new Feedback_item(data.child("text").getValue().toString(), data.child("img").getValue().toString(),data.child("name").getValue().toString()+" "+data.child("lastname").getValue().toString(),data.getKey(),data.child("profilephoto").getValue().toString()));
                        }else if(!data.hasChild("text")){
                            pub.add(new Feedback_item("", data.child("img").getValue().toString(), data.child("name").getValue().toString()+" "+data.child("lastname").getValue().toString(),data.getKey(),data.child("profilephoto").getValue().toString()));
                        }else if(!data.hasChild("img")){
                            pub.add(new Feedback_item(data.child("text").getValue().toString(),"",  data.child("name").getValue().toString()+" "+data.child("lastname").getValue().toString(),data.getKey(),data.child("profilephoto").getValue().toString()));
                        }

                        if(data.hasChild("text") && data.hasChild("img")) {
                            pub.add(new Feedback_item(data.child("text").getValue().toString(), data.child("img").getValue().toString(),data.child("name").getValue().toString()+" "+data.child("lastname").getValue().toString(),data.getKey(),data.child("profilephoto").getValue().toString()));
                        }else if(!data.hasChild("text")){
                            pub.add(new Feedback_item("", data.child("img").getValue().toString(), data.child("name").getValue().toString()+" "+data.child("lastname").getValue().toString(),data.getKey(),data.child("profilephoto").getValue().toString()));
                        }else if(!data.hasChild("img")){
                            pub.add(new Feedback_item(data.child("text").getValue().toString(),"",  data.child("name").getValue().toString()+" "+data.child("lastname").getValue().toString(),data.getKey(),data.child("profilephoto").getValue().toString()));
                        }

                    }
                    Collections.reverse(pub);
                }

                if(pub.size()>=7 && footer==null){
                    footer = (ViewGroup) inflater.inflate(R.layout.footer,publications,false);
                    publications.addFooterView(footer,null,true);
                    button=(Button)view.findViewById(R.id.ajouter);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nombre=nombre+7;
                            remplipub(nombre,view,inflater);
                            adapter.notifyDataSetChanged();
                        }
                    });}
                if(pub.size()<nombre && footer!=null) {publications.removeFooterView(footer);
                    footer=null;
                }
                publications.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        query.addListenerForSingleValueEvent(valuea);
    }
    @SuppressLint("ValidFragment")
    public Tab2Fragment(String id){
        this.id=id;
    }

}
