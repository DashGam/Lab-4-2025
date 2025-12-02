package functions.basic;

import functions.Function;

public class Log implements Function {
    private double base;

    public Log(double base) {
        if (base <= 0 || base == 1) {
            throw new IllegalArgumentException("Основание логарифма должно быть положительным и не равным 1: " + base);
        }
        this.base = base;
    }
    public double getFunctionValue(double x) {
        if (x <= 0) {
            return Double.NaN; // Логарифм определен только для x > 0
        }
        return Math.log(x) / Math.log(base);
    }
    public double getLeftDomainBorder() {
        return 0; // Фактически (0, +∞), но возвращаем 0 как формальную границу
    }
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    public double getBase() {
        return base;
    }
}
