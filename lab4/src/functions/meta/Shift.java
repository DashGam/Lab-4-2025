package functions.meta;

import functions.Function;

public class Shift implements Function {
    private Function f;
    private double xShift;
    private double yShift;

    public Shift(Function f, double xShift, double yShift) {
        this.f = f;
        this.xShift = xShift;
        this.yShift = yShift;
    }
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return f.getFunctionValue(x - xShift) + yShift;
    }
    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder() + xShift;
    }
    public double getRightDomainBorder() {
        return f.getRightDomainBorder() + xShift;
    }
    public Function getFunction() {
        return f;
    }
    public double getXShift() {
        return xShift;
    }
    public double getYShift() {
        return yShift;
    }
}
