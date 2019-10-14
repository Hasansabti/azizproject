package tech.sabtih.azizproject.models;

public class Renter_Car {
    int id;
    String name;
    String owner;
    double price;
    String image;
    String details;

    public Renter_Car(int id, String name, String owner, double price,String details) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.price = price;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
