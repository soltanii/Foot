package terrain.foot.com.foot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Image extends AppCompatActivity {
    ImageView back,refresh,addphoto;
    private final int PICK_iMAGE_REQUEST = 71 ;
    private Uri filePath=null;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    ArrayList<String> keys = new ArrayList<String>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    StorageReference storagereference;
    StorageReference ref;
    FirebaseStorage storage;

    String id,Email,Img;
    ArrayList<ImageItem> imageItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Email=intent.getStringExtra("Email");
        gridView = (GridView) findViewById(R.id.gridView);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Email!=null&&id!=null){
                    back.setColorFilter(R.color.colorPrimaryDark);
                    Intent intent = new Intent(Image.this,Profile.class);
                    intent.putExtra("id", id);
                    intent.putExtra("Email",Email);
                    startActivity(intent);
                    finish();}
            }
        });
        refresh=(ImageView)findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh.setColorFilter(R.color.colorPrimaryDark);
                Intent intent = new Intent(Image.this,Image.class);
                intent.putExtra("id", id);
                intent.putExtra("Email",Email);
                startActivity(intent);
                finish();
            }
        });

        addphoto=(ImageView)findViewById(R.id.addphoto);
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageItems.size()<10) {
                    Intent intent = new Intent();
                    intent.setType("image/+");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent.createChooser(intent, "Select Picture"), PICK_iMAGE_REQUEST);
                }else{
                    Toast.makeText(Image.this, "vous devez suprimer des photos d'abord", Toast.LENGTH_SHORT).show();
                }

            }
        });
        getData();
        storage = FirebaseStorage.getInstance();
        storagereference = storage.getReference();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, final int pos, final long id1)
            {
                AlertDialog.Builder builder =new AlertDialog.Builder(Image.this);
                builder.setMessage("voulez vous suprimer cette photo").setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        myRef.child(id).child("photoScroll").child(keys.get(pos)).removeValue();
                        imageItems.remove(pos);
                        keys.remove(pos);
                        gridAdapter.notifyDataSetChanged();

                    }
                }).setNegativeButton("Non",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return false;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PICK_iMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null ){
            filePath = data.getData();
            super.onActivityResult(requestCode,resultCode,data);
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                uploadImag();

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    private void uploadImag(){

        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            ref = storagereference.child("image/"+ UUID.randomUUID().toString());
            ref.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    progressDialog.dismiss();
                    Toast.makeText(Image.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Image.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0+taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Upload"+(int)progress+"4");
                }
            });
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Img=taskSnapshot.getDownloadUrl().toString();
                    int i= imageItems.size()+1;
                    imageItems.add(new ImageItem(Img,""));
                    gridAdapter.notifyDataSetChanged();
                    myRef.child(id).child("photoScroll").push().setValue(Img);
                }
            });
        }
        else {
            Toast.makeText(Image.this, "Insérer une photo", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onPause() {

        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Do nothing or catch the keys you want to block
        return false;
    }
    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem> getData() {

        myRef.child(id).child("photoScroll").limitToFirst(15).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        imageItems.add(new ImageItem(snapshot.getValue().toString(), ""));
                        keys.add(snapshot.getKey());


                    }
                    gridAdapter = new GridViewAdapter(Image.this, R.layout.grid_item_layout, imageItems);
                    gridView.setAdapter(gridAdapter);



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return imageItems;
    }
}