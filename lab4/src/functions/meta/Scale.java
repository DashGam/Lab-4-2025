package functions.meta;

import functions.Function;

public class Scale implements Function {
    private Function f;
    private double xScale;
    private double yScale;

    public Scale(Function f, double xScale, double yScale) {
        if (xScale == 0) {
            throw new IllegalArgumentException("Коэффициент масштабирования X не может быть 0");
        }
        this.f = f;
        this.xScale = xScale;
        this.yScale = yScale;
    }
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        double scaledX = x / xScale;
        return yScale * f.getFunctionValue(scaledX);
    }
    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder() * xScale;
    }
    public double getRightDomainBorder() {
        return f.getRightDomainBorder() * xScale;
    }
    public Function getFunction() {
        return f;
    }
    public double getXScale() {
        return xScale;
    }
    public double getYScale() {
        return yScale;
    }
}
