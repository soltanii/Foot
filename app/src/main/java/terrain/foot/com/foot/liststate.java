package terrain.foot.com.foot;

public class liststate {
    private int etat;
    private String temps;
    public liststate(int etat,String temps){
        this.etat=etat;
        this.temps=temps;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public String getTemps() {
        return temps;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }
}
