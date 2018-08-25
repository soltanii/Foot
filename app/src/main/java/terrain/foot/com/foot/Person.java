package terrain.foot.com.foot;

public class Person {
    private String name;
    private String date;
    private String heure,etat;
    private String key;
    private String id;
    private String datederese;
    private String profilephot;
    public Person(String name, String date, String heure, String key,String etat,String id,String datederese, String profilephot) {
        this.date = date;
        this.name = name;
        this.heure = heure;
        this.key=key;
        this.etat=etat;
        this.id=id;
        this.datederese=datederese;
        this.profilephot=profilephot;
    }

    public String getDate() {
        return date;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDatederese() {
        return datederese;
    }
    public void setDatederese(String datederese) {
        this.datederese = datederese;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getEtat() {
        return etat;
    }
    public void setEtat(String etat) {
        this.etat = etat;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getProfilephot() {
        return profilephot;
    }

    public void setProfilephot(String profilephot) {
        this.profilephot = profilephot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String sex) {
        this.heure= heure;
    }
}
