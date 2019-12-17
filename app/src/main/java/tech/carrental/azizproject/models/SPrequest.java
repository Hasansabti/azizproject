package tech.carrental.azizproject.models;

public class SPrequest {
    int id;
    String name;
    String carname;
    String location;
    boolean accepted;

    public SPrequest(int id, String name, String carname, String location) {
        this.id = id;
        this.name = name;
        this.carname = carname;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
