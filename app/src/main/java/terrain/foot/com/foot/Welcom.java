package terrain.foot.com.foot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class Welcom extends AppCompatActivity {
    String id,Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vide);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
if(getValue("Email")==-1){

    Intent intent = new Intent(Welcom.this,Login.class);

    startActivity(intent);
    finish();
}
else {
    id=getvalue("id",getValue("id"));
    Email=getvalue("Email",getValue("Email"));
    Intent intent = new Intent(Welcom.this,Profile.class);
    intent.putExtra("id", id);
    intent.putExtra("Email", Email);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    finish();

}


    }
    public int getValue(String name) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt(name, -1);
    }


public String getvalue(String file,int len){
    try{
        FileInputStream fIn = openFileInput(file);
        InputStreamReader isr = new InputStreamReader(fIn);
        char[] inputBuffer = new char[len];
        //len is the length of that saved string in the file

        isr.read(inputBuffer);

        String readString = new String(inputBuffer);
        return readString;
    }catch(IOException e){

    }
return null;
}
    }
