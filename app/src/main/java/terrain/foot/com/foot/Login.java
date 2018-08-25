package terrain.foot.com.foot;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;

public class Login extends AppCompatActivity {
    private EditText log;
    private EditText passwd ;
    private Button connexion ;
    private FirebaseAuth auth;
    private String id = null;
    String value;
    boolean existcn=false;
    ImageView wifi;
    TextView signup,forgetmdp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginn);
        wifi=findViewById(R.id.wifi1);
        if (isNetworkAvailable()) {
            wifi.setVisibility(View.GONE);
            existcn = true;

        }
        log = (EditText) findViewById(R.id.username);
        passwd = (EditText) findViewById(R.id.password);
        connexion = (Button) findViewById(R.id.signin);
forgetmdp=(TextView)findViewById(R.id.text_forgot);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        auth = FirebaseAuth.getInstance();

forgetmdp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Login.this, ResetPasswordActivity.class);
        startActivity(intent);
        finish();

    }
});
        if (existcn){
            connexion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String pseudo = log.getText().toString();
                    final String password = passwd.getText().toString();

                    if (TextUtils.isEmpty(pseudo)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {

                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                    progressDialog.setTitle("Authetification...");
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(pseudo, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        if (password.length() < 6) {
                                            passwd.setError(getString(R.string.minimum_password));
                                        } else {
                                            Toast.makeText(Login.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("users");

                                        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (!dataSnapshot.hasChild(id)) {
                                                    Toast.makeText(getApplicationContext(), "ce compte n'existe pas", Toast.LENGTH_SHORT).show();


                                                } else {


                                                    String Email = log.getText().toString();
                                                    setValue(id.length(), "id");
                                                    setValue(Email.length(), "Email");
                                                    setvalue(id, "id");
                                                    setvalue(Email, "Email");
                                                    Intent intent = new Intent(Login.this, Profile.class);
                                                    intent.putExtra("id", id);
                                                    intent.putExtra("Email", Email);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    progressDialog.dismiss();
                                                    startActivity(intent);
                                                    finish();

                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            });


                }

            });
        signup = (TextView) findViewById(R.id.textsignup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUpActivity.class));
                finish();
            }
        });
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
        // Do nothing or catch the keys you want to block
        return false;
    }
    public void setvalue(String data,String file){
        try {
            FileOutputStream fOut = openFileOutput(file,MODE_PRIVATE);
            fOut.write(data.getBytes());
            fOut.close();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setValue(int newValue,String name) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(name, newValue);
        editor.commit();
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
