package tech.carrental.azizproject.models;

public class Renter_Car {
    String id;
    String carid;
    String requesterid;
    String ownerid;
    String start;
    String end;
    double price;
    String location;
    private String status;
    boolean deliver;

    public Renter_Car(){

    }
    public Renter_Car(String id, String carid, String requesterid, String ownerid, String start, String end, double price, String location, boolean deliver, String status) {
        this.id = id;
        this.carid = carid;
        this.requesterid = requesterid;
        this.ownerid = ownerid;
        this.start = start;
        this.end = end;
        this.price = price;
        this.location = location;
        this.status = status;
        this.deliver = deliver;
    }

    public String getId() {
        return id;
    }

    public String getCarid() {
        return carid;
    }

    public String getRequesterid() {
        return requesterid;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public double getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public boolean isDeliver() {
        return deliver;
    }
}
