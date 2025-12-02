package functions.meta;

import functions.Function;

public class Power implements Function {
    private Function f;
    private double power;

    public Power(Function f, double power) {
        this.f = f;
        this.power = power;
    }
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        double base = f.getFunctionValue(x);
        return Math.pow(base, power);
    }
    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder();
    }
    public double getRightDomainBorder() {
        return f.getRightDomainBorder();
    }
    public Function getFunction() {
        return f;
    }
    public double getPower() {
        return power;
    }
}
