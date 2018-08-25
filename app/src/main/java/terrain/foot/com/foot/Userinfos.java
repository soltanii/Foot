package terrain.foot.com.foot;

public class Userinfos {
    private String firstname,lastname,time,number,image;

    public Userinfos(String firstname,String lastname,String time,String number,String image){

        this.firstname=firstname;
        this.lastname=lastname;
        this.time=time;
        this.number=number;
        this.image=image;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getImage() {
        return image;
    }

    public String getLastname() {
        return lastname;
    }

    public String getNumber() {
        return number;
    }

    public String getTime() {
        return time;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
