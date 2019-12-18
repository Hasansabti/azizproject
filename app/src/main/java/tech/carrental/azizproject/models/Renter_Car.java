package tech.carrental.azizproject.models;

public class Renter_Car {
    String id;
    String carid;
    String requesterid;
    String ownerid;
    long start;
    long end;
    double price;
    String location;
    private String status;
    boolean deliver;

    float sprate;
    float renterrate;

    public Renter_Car(){

    }
    public Renter_Car(String id, String carid, String requesterid, String ownerid, long start, long end, double price, String location, boolean deliver, String status, float sprate,float renterrate) {
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
        this.sprate = sprate;
        this.renterrate = renterrate;
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

    public long getStart() {
        return start;
    }

    public long getEnd() {
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

    public float getSprate() {
        return sprate;
    }

    public float getRenterrate() {
        return renterrate;
    }
}
