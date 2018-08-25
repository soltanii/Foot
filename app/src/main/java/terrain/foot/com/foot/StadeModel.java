package terrain.foot.com.foot;

public class StadeModel {
    private String firstName = "";
    private String localisation = "";
    private String id = "0";
    private String profilePic = "-1";
    private  int position ;
    private int mobile;

    public StadeModel(int position, String firstName, String localisation, String id, String pic,int mobile) {
        this.firstName = firstName;
        this.localisation = localisation;
        this.id = id;
        this.profilePic = pic;
        this.position = position;
        this.mobile=mobile;

    }
    public  int getMobile () {return  mobile;}

    public void setMobile(int mobile) { this.mobile = mobile; }

    public  int getPosition () {return  position;}

    public void setPosition(int position) { this.position = position; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public String toString() {
        return this.firstName ;
    }
}