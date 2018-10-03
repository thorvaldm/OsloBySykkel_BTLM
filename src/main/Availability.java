package main;

public class Availability {
    private int bikes;
    private int locks;

    public Availability(int bikes, int locks) {
        this.bikes = bikes;
        this.locks = locks;
    }

    public int getBikes() {
        return bikes;
    }

    public void setBikes(int bikes) {
        this.bikes = bikes;
    }

    public int getLocks() {
        return locks;
    }

    public void setLocks(int locks) {
        this.locks = locks;
    }
}
