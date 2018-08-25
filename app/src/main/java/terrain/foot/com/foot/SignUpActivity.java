package terrain.foot.com.foot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class SignUpActivity extends AppCompatActivity {
    private EditText log;
    private EditText passwd ;
    private EditText passwd2 ;
    private EditText nom;
    private EditText prenom ;
    private Button signup ;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Uri filePath=null;
    ImageView im;
    private final int PICK_iMAGE_REQUEST = 71 ;
    StorageReference storagereference;
    StorageReference ref;
    FirebaseStorage storage;
   String Img;
    EditText g,m;

    String name;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        storage = FirebaseStorage.getInstance();
        storagereference = storage.getReference();
        im =(ImageView)findViewById(R.id.imageButton3);
        g=(EditText)findViewById(R.id.gouvernorat1);
        m=(EditText)findViewById(R.id.Mobile1);

        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setColorFilter(R.color.colorPrimaryDark);
                Intent intent = new Intent(SignUpActivity.this,Login.class);

                startActivity(intent);
                finish();
            }
        });

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/+");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"Select Picture"),PICK_iMAGE_REQUEST);
                im.setImageURI(filePath);




            }
        });
        auth = FirebaseAuth.getInstance();
                  log=(EditText)findViewById(R.id.login);
                  passwd=(EditText)findViewById(R.id.passwd);
                  signup=(Button) findViewById(R.id.valider);
                  prenom=(EditText)findViewById(R.id.prenom);
                  nom=  (EditText)findViewById(R.id.nom);
                  passwd2=(EditText)findViewById(R.id.passwd2);




        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String login = log.getText().toString();
                final String password = passwd.getText().toString();
                final FirebaseDatabase database=FirebaseDatabase.getInstance();
                final DatabaseReference databaseRestaurants =database.getReference("simpleusers");

                auth = FirebaseAuth.getInstance();

                if (TextUtils.isEmpty(login)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!(passwd.getText().toString().equals((passwd2.getText().toString()))))
                {
                    Toast.makeText(getApplicationContext(), "Passwords entered do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(login, password)
                        .addOnCompleteListener(SignUpActivity.this,  new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                   // finish();
                                    uploadImag();
                                    String Nom=nom.getText().toString();
                                    String Prenom=prenom.getText().toString();
                                    String gouvernorat = g.getText().toString();
                                    String Mobile= m.getText().toString();
                                    user=auth.getCurrentUser();


                                    UserInfos userinfos=new UserInfos(Prenom,Nom,Img,gouvernorat,Mobile);
                                    databaseRestaurants.child(user.getUid()).setValue(userinfos);
                                    databaseRestaurants.child(user.getUid()).child("Mobile").setValue(Mobile);
                                    databaseRestaurants.child(user.getUid()).child("Localisation").setValue(gouvernorat)    ;
                                    Intent intent = new Intent(SignUpActivity.this, Profile.class);
                                    String id = user.getUid();
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            }
                        });



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
                im.setImageBitmap(bitmap);
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
                    Toast.makeText(SignUpActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                }
            });


        }

    }

}