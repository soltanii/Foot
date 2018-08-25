package terrain.foot.com.foot;

import android.net.Uri;

/**
 * Created by user on 26/11/2017.
 */

public class UserInfos {
    private String name,lastname,Mobile,gouvernorat;
    private String filephoto;
    public UserInfos(String name, String lastname, String filepfoto,String Mobile,String gouvernorat){

        this.name=name;
        this.lastname=lastname;
        this.filephoto=filepfoto;
        this.Mobile=Mobile;
        this.gouvernorat=gouvernorat;

    }

    public String get_name ()
    {
        return(this.name);
    }
    public String get_lastname ()
    {
        return(this.lastname);
    }


    public String get_filephoto(){return (this.filephoto);}

    public void set_name(String name){
        this.name=name;
    }
    public void set_lastname(String lastname){
        this.lastname=lastname;
    }


    public  void set_filephoto(String filephoto){this.filephoto=filephoto;}


}
