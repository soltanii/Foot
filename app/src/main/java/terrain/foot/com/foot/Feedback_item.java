package terrain.foot.com.foot;


public class Feedback_item {
    private String text,img,titre,key,photoitem;
    public Feedback_item(String Text,String Img,String titre,String key,String photoitem){
        this.text=Text;
        this.img=Img;
        this.titre=titre;
        this.key=key;
        this.photoitem=photoitem;


    }
    public String getImg(){return this.img;}
    public String getText(){return this.text;}
    public String getTitre(){return this.titre;}
    public void setTitre(String Titre){this.titre=Titre;}
    public String getKey(){return this.key;}
    public void setKey(String key){this.key=key;}
    public void setText(String Text){this.text=Text;}
    public void setImg(String Img){this.img=Img;}

    public String getPhotoitem() {
        return photoitem;
    }

    public void setPhotoitem(String photoitem) {
        this.photoitem = photoitem;
    }
}