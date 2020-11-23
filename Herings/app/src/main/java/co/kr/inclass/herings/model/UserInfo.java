package co.kr.inclass.herings.model;

public class UserInfo {
    private String id = "";
    private String pwd = "";
    private String token = "";

    public String getId() { return id; }
    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

}
