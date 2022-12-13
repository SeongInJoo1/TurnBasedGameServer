package datapackage;

import java.io.Serializable;

public class Data implements Serializable {

    private Info info;
    private int iChoice;
    byte[] img;

    public Info GetInfo(){return info;}
    public void SetInfo(Info info){this.info = info;}
    public void SetChoice(int iChoice) {
        this.iChoice = iChoice;
    }
    public int GetChoice() { return iChoice; }
    public byte[] getImg() {
        return img;
    }
    public void setImg(byte[] img) {
        this.img = img;
    }


}