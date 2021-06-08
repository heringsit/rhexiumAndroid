package co.kr.inclass.herings;

public class DataPage{
    int imgResource;
    boolean notToday;
    int number;

    public DataPage(int imgResource, boolean notToday, int number){
        this.imgResource = imgResource;
        this.notToday = notToday;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public boolean isNotToday() {
        return notToday;
    }

    public void setNotToday(boolean notToday) {
        this.notToday = notToday;
    }


}
