package functions;

import functions.meta.*;

public final class Functions {

    private Functions() {
        throw new AssertionError("Нельзя создать экземпляр класса Functions");
    }
    public static Function shift(Function f, double shiftX, double shiftY) {
        if (f == null) {
            throw new IllegalArgumentException("Исходная функция не может быть null");
        }
        return new Shift(f, shiftX, shiftY);
    }
    public static Function scale(Function f, double scaleX, double scaleY) {
        if (f == null) {
            throw new IllegalArgumentException("Исходная функция не может быть null");
        }
        if (scaleX == 0) {
            throw new IllegalArgumentException("Коэффициент масштабирования по X не может быть 0");
        }
        return new Scale(f, scaleX, scaleY);
    }
    public static Function power(Function f, double power) {
        if (f == null) {
            throw new IllegalArgumentException("Исходная функция не может быть null");
        }
        return new Power(f, power);
    }
    public static Function sum(Function f1, Function f2) {
        if (f1 == null || f2 == null) {
            throw new IllegalArgumentException("Функции не могут быть null");
        }
        return new Sum(f1, f2);
    }
    public static Function mult(Function f1, Function f2) {
        if (f1 == null || f2 == null) {
            throw new IllegalArgumentException("Функции не могут быть null");
        }
        return new Mult(f1, f2);
    }
    public static Function composition(Function f1, Function f2) {
        if (f1 == null || f2 == null) {
            throw new IllegalArgumentException("Функции не могут быть null");
        }
        return new Composition(f1, f2);
    }
    public static Function difference(Function f1, Function f2) {
        if (f1 == null || f2 == null) {
            throw new IllegalArgumentException("Функции не могут быть null");
        }
        return new Sum(f1, new Scale(f2, 1, -1));
    }
    public static Function division(Function f1, Function f2) {
        if (f1 == null || f2 == null) {
            throw new IllegalArgumentException("Функции не могут быть null");
        }
        return new Mult(f1, new Power(f2, -1));
    }
}
