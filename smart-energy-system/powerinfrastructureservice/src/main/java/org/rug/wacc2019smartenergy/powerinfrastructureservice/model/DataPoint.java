package org.rug.wacc2019smartenergy.powerinfrastructureservice.model;

public class DataPoint {
    private long x;
    private double y;

    public DataPoint() {
    }

    public DataPoint(long x, double y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
