package tech.carrental.azizproject.models;

public class Car {
    String name,model,uuid, company,fuel,city,fair,location, userid;
    int seats,rating,days;

    public Car() {
    }



    public Car(String name, String model, String uuid, String company, String fuel, String city, String fair, String location, String userid, int seats, int rating, int days) {
        this.name = name;
        this.model = model;
        this.uuid = uuid;
        this.company = company;
        this.fuel = fuel;
        this.city = city;
        this.fair = fair;
        this.location = location;
        this.userid = userid;
        this.seats = seats;
        this.rating = rating;
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCompany() {
        return company;
    }

    public String getFuel() {
        return fuel;
    }

    public String getCity() {
        return city;
    }

    public String getFair() {
        return fair;
    }

    public String getLocation() {
        return location;
    }

    public int getDays() {
        return days;
    }

    public String getUserid() {
        return userid;
    }

    public String getModel() {
        return model;
    }
}
