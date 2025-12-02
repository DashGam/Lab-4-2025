package functions.meta;

import functions.Function;

public class Composition implements Function {
    private Function outer;
    private Function inner;

    public Composition(Function outer, Function inner) {
        this.outer = outer;
        this.inner = inner;
    }
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        double innerValue = inner.getFunctionValue(x);
        return outer.getFunctionValue(innerValue);
    }
    public double getLeftDomainBorder() {
        return inner.getLeftDomainBorder();
    }
    public double getRightDomainBorder() {
        return inner.getRightDomainBorder();
    }
    public Function getOuterFunction() {
        return outer;
    }
    public Function getInnerFunction() {
        return inner;
    }
}
