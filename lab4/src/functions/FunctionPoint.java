package functions;

import java.io.*;

public class FunctionPoint implements Externalizable{
    private double x;
    private double y;
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }
    public FunctionPoint() {
        this(0.0, 0.0);
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(x);
        out.writeDouble(y);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        x = in.readDouble();
        y = in.readDouble();
    }
}
