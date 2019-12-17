package tech.carrental.azizproject.models;

public class User {
    private String name;
    private String email;
    private String natid;
    private boolean sp;
    private String city;
    private String mobile;

    public User(String name, String email, String natid, boolean sp, String city, String mobile) {
        this.name = name;
        this.email = email;
        this.natid = natid;
        this.sp = sp;
        this.city = city;
        this.mobile = mobile;
    }
    public User(){

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSp() {
        return sp;
    }

    public String getCity() {
        return city;
    }

    public String getMobile() {
        return mobile;
    }

    public String getNatid() {
        return natid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNatid(String natid) {
        this.natid = natid;
    }

    public void setSp(boolean sp) {
        this.sp = sp;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
