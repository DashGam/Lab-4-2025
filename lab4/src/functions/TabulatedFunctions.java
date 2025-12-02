package functions;

import java.io.*;
import java.util.StringTokenizer;

public final class TabulatedFunctions {

    private TabulatedFunctions() {
        throw new AssertionError("Нельзя создать экземпляр класса TabulatedFunctions");
    }
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (function == null) {
            throw new IllegalArgumentException("Функция не может быть null");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2: " + pointsCount);
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой: " + leftX + " >= " + rightX);
        }
        if (leftX < function.getLeftDomainBorder()) {
            throw new IllegalArgumentException("Левая граница табулирования выходит за область определения функции: " +
                            leftX + " < " + function.getLeftDomainBorder());
        }
        if (rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Правая граница табулирования выходит за область определения функции: " +
                            rightX + " > " + function.getRightDomainBorder());
        }
        TabulatedFunction tabulatedFunc = new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            double x = tabulatedFunc.getPointX(i);
            double y = function.getFunctionValue(x);
            tabulatedFunc.setPointY(i, y);
        }
        return tabulatedFunc;
    }
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX) {
        int pointsCount = (int) Math.max(2, Math.ceil((rightX - leftX) * 10) + 1);
        return tabulate(function, leftX, rightX, pointsCount);
    }
    public static TabulatedFunction tabulate(Function function, int pointsCount) {
        if (function == null) {
            throw new IllegalArgumentException("Функция не может быть null");
        }
        double leftX = function.getLeftDomainBorder();
        double rightX = function.getRightDomainBorder();
        if (Double.isInfinite(leftX) || Double.isInfinite(rightX)) {
            throw new IllegalArgumentException("Нельзя табулировать функцию с бесконечной областью определения: [" +
                            leftX + ", " + rightX + "]");
        }
        return tabulate(function, leftX, rightX, pointsCount);
    }
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        if (function == null) {
            throw new IllegalArgumentException("Функция не может быть null");
        }
        if (out == null) {
            throw new IllegalArgumentException("Выходной поток не может быть null");
        }
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeInt(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        dataOut.flush();
    }
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("Входной поток не может быть null");
        }
        DataInputStream dataIn = new DataInputStream(in);
        int pointsCount = dataIn.readInt();
        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
        return new ArrayTabulatedFunction(points);
    }
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        if (function == null) {
            throw new IllegalArgumentException("Функция не может быть null");
        }
        if (out == null) {
            throw new IllegalArgumentException("Выходной поток не может быть null");
        }
        PrintWriter writer = new PrintWriter(out);
        writer.print(function.getPointsCount());
        writer.print(' ');
        for (int i = 0; i < function.getPointsCount(); i++) {
            writer.print(function.getPointX(i));
            writer.print(' ');
            writer.print(function.getPointY(i));
            if (i < function.getPointsCount() - 1) {
                writer.print(' ');
            }
        }
        writer.flush();
    }
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("Входной поток не может быть null");
        }
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new IOException("Ожидалось количество точек");
        }
        int pointsCount = (int) tokenizer.nval;
        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }
        FunctionPoint[] points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата x для точки " + i);
            }
            double x = tokenizer.nval;
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new IOException("Ожидалась координата y для точки " + i);
            }
            double y = tokenizer.nval;
            points[i] = new FunctionPoint(x, y);
        }
        return new ArrayTabulatedFunction(points);
    }
}