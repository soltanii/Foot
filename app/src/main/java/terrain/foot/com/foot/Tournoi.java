package terrain.foot.com.foot;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static terrain.foot.com.foot.Tab1Fragment.p;

public class Tournoi extends AppCompatActivity {
    Button validd, addph;
    String id, Email,Img;
    ImageView back,img,refrech;
    EditText publication,title;
    boolean rempli=false;

    private final int PICK_iMAGE_REQUEST = 71;
    FirebaseStorage storage;
    private Uri filePath = null;
    StorageReference storagereference;
    StorageReference ref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    Publication publicat=new Publication(null,null,null,null);

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpublication);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       /* back = (ImageView) findViewById(R.id.back10);
        addph = (Button) findViewById(R.id.addph);
        validd=(Button)findViewById(R.id.validd);
        publication = (EditText) findViewById(R.id.publicationn);
        title = (EditText) findViewById(R.id.titlee);

        img=(ImageView)findViewById(R.id.img);
        storage = FirebaseStorage.getInstance();
        storagereference = storage.getReference();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Email = intent.getStringExtra("Email");
        refrech=(ImageView)findViewById(R.id.refresh2);
        refrech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tournoi.this,Tournoi.class);
                intent.putExtra("id", id);
                intent.putExtra("Email", Email);
                p.clear();
                startActivity(intent);
                finish();



            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setColorFilter(R.color.colorPrimaryDark);
                Intent intent = new Intent(Tournoi.this, Evenement.class);
                intent.putExtra("id", id);
                intent.putExtra("Email", Email);
                startActivity(intent);
                finish();

            }
        });
        addph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/+");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent, "Select Picture"), PICK_iMAGE_REQUEST);


            }
        });
       validd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String titre=title.getText().toString();
              String text= publication.getText().toString();
              if(titre.length()!=0){
                  publicat.setTitre(titre);
              if(text.length()==0 && !rempli){
                  Toast.makeText(Tournoi.this, "ajouter quelque chose à publier", Toast.LENGTH_SHORT).show();

              }
              else {
                  if(rempli){
                  publicat.setText(text);
                  uploadImag();


              }else {


                  publicat.setText(text);
                  myRef.child(id).child("publication").push().setValue(publicat);
                  Intent intent = new Intent(Tournoi.this, Tournoi.class);
                  intent.putExtra("id", id);
                  intent.putExtra("Email", Email);
                  startActivity(intent);
                  finish();
              }

              }}else {
                  Toast.makeText(Tournoi.this, "ajouter un titre", Toast.LENGTH_SHORT).show();
              }


           }
       });

    }
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            if (requestCode == PICK_iMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
                super.onActivityResult(requestCode, resultCode, data);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    Drawable d = new BitmapDrawable(getResources(), bitmap);
                   img.setImageBitmap(bitmap);
                   addph.setVisibility(View.GONE);
                   img.setVisibility(View.VISIBLE);
                   img.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent intent = new Intent();
                           intent.setType("image/+");
                           intent.setAction(Intent.ACTION_GET_CONTENT);
                           startActivityForResult(intent.createChooser(intent, "Select Picture"), PICK_iMAGE_REQUEST);

                       }
                   });
rempli=true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private void uploadImag () {
            if (filePath != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                ref = storagereference.child("image/" + UUID.randomUUID().toString());
                ref.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        progressDialog.dismiss();
                        Toast.makeText(Tournoi.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Tournoi.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 + taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Upload" + (int) progress + "4");
                    }
                });
                ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      Img=taskSnapshot.getDownloadUrl().toString();
                        publicat.setImg(Img);

                        myRef.child(id).child("publication").push().setValue(publicat);
                        Intent intent = new Intent(Tournoi.this, Tournoi.class);
                        intent.putExtra("id", id);
                        intent.putExtra("Email", Email);
                        startActivity(intent);
                        finish();


                    }
                });
            } else {
                Toast.makeText(Tournoi.this, "Insérer une photo", Toast.LENGTH_SHORT).show();
            }

        }
        @Override
        protected void onPause () {
            super.onPause();

            ActivityManager activityManager = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);

            activityManager.moveTaskToFront(getTaskId(), 0);
        }
        @Override
        public boolean onKeyDown ( int keyCode, KeyEvent event){
            // Do nothing or catch the keys you want to block
            return false;
        }
    }*/
    }}