import functions.*;
import functions.basic.*;
import functions.meta.*;
import java.io.*;
import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Тестирование TabuletedFunctions\n");
        testTabulatedFunctions();
        System.out.println("\n\nТестирование аналитических функций и методов TabuletedFunctions\n");
        testAnalyticFunctions();
        System.out.println("\nПроверка исключений через интерфейс\n");
        testExceptionsWithInterface();
        System.out.println("\nТестирование сериализации\n");
        System.out.println(">>> Serializable (ArrayTabulatedFunction)");
        ArrayTabulatedFunction tabSer = testTaskSerializable();

        System.out.println(">>> Externalizable (LinkedListTabulatedFunction)");
        LinkedListTabulatedFunction tabExt = testTaskExternalizable();
        System.out.println("\nСравнение способов хранения");

        int points = Math.min(tabSer.getPointsCount(), tabExt.getPointsCount());

        System.out.println("   x\t\t\tSerializable\t\t\tExternalizable");
        for (int i = 0; i < points; i++) {

            double vSer = tabSer.getPointY(i);
            double vExt =tabExt.getPointY(i);

            System.out.printf("   %.1f\t\t%.6f\t\t\t%.6f %n", tabSer.getPointX(i), vSer, vExt);
        }

        System.out.println("\n   Размер файлов:");
        File fSer = new File("ser.bin");
        File fExt = new File("ext.bin");
        System.out.println("      Serializable (ser.bin): " + fSer.length() + " байт");
        System.out.println("      Externalizable (ext.bin): " + fExt.length() + " байт");
    }
    private static void testTabulatedFunctions() {
        double left = -2;
        double right = 3;
        int count = 6;

        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(left, right, count);
        System.out.println("ArrayTabulatedFunction");
        System.out.println("Функция f(x) = 2x*x + 3x - 1");
        System.out.println("Левая граница функции: " + arrayFunc.getLeftDomainBorder());
        System.out.println("Правая граница функции: " + arrayFunc.getRightDomainBorder());

        for (int i = 0; i < arrayFunc.getPointsCount(); i++) {
            double x = arrayFunc.getPointX(i);
            arrayFunc.setPointY(i, 2*x*x + 3*x - 1);
        }
        System.out.println("\nТочки ArrayTabulatedFunction:");
        for (int i = 0; i < arrayFunc.getPointsCount(); i++) {
            System.out.println("(" + arrayFunc.getPointX(i) + "; " + arrayFunc.getPointY(i) + ")");
        }
        System.out.println("\nПроверка значений ArrayTabulatedFunction:");
        for (double x = -3; x <= 4; x += 0.5) {
            System.out.println("f(" + x + ") = " + arrayFunc.getFunctionValue(x));
        }

        TabulatedFunction linkedFunc = new LinkedListTabulatedFunction(left, right, count);
        System.out.println("\nLinkedListTabulatedFunction");
        System.out.println("Функция f(x) = 2x*x + 3x - 1");
        System.out.println("Левая граница функции: " + linkedFunc.getLeftDomainBorder());
        System.out.println("Правая граница функции: " + linkedFunc.getRightDomainBorder());

        for (int i = 0; i < linkedFunc.getPointsCount(); i++) {
            double x = linkedFunc.getPointX(i);
            linkedFunc.setPointY(i, 2*x*x + 3*x - 1);
        }
        System.out.println("\nТочки LinkedListTabulatedFunction:");
        for (int i = 0; i < linkedFunc.getPointsCount(); i++) {
            System.out.println("(" + linkedFunc.getPointX(i) + "; " + linkedFunc.getPointY(i) + ")");
        }
        System.out.println("\nПроверка значений LinkedListTabulatedFunction:");
        for (double x = -3; x <= 4; x += 0.5) {
            System.out.println("f(" + x + ") = " + linkedFunc.getFunctionValue(x));
        }

        double[] values = {1, -2, -1, 4, 13, 26};
        TabulatedFunction func1 = new ArrayTabulatedFunction(-2, 3, values);
        System.out.println("\nArrayTabulatedFunction с массивом значений");
        System.out.println("Функция создана с массивом значений:");
        for (int i = 0; i < func1.getPointsCount(); i++) {
            System.out.println("x=" + func1.getPointX(i) + ", y=" + func1.getPointY(i));
        }

        TabulatedFunction func2 = new LinkedListTabulatedFunction(-2, 3, values);
        System.out.println("\nLinkedListTabulatedFunction с массивом значений");
        System.out.println("Функция создана с массивом значений:");
        for (int i = 0; i < func2.getPointsCount(); i++) {
            System.out.println("x=" + func2.getPointX(i) + ", y=" + func2.getPointY(i));
        }

        System.out.println("\nОперации с ArrayTabulatedFunction");
        System.out.println("Изменяем значение y третьей точки:");
        try {
            arrayFunc.setPoint(2, new FunctionPoint(0, -1));
            System.out.println("Новая точка 2: x = " + arrayFunc.getPointX(2) + ", y = " + arrayFunc.getPointY(2));
        }
        catch (InappropriateFunctionPointException e) {
            System.err.println("Ошибка при изменении точки: " + e.getMessage());
        }

        System.out.println("\nДобавляем новую точку (x=1.5, y=8):");
        try {
            arrayFunc.addPoint(new FunctionPoint(1.5, 8));
            System.out.println("Точки ArrayTabulatedFunction после добавления:");
            for (int i = 0; i < arrayFunc.getPointsCount(); i++) {
                System.out.println("(" + arrayFunc.getPointX(i) + "; " + arrayFunc.getPointY(i) + ")");
            }
        }
        catch (InappropriateFunctionPointException e) {
            System.err.println("Ошибка при добавлении точки: " + e.getMessage());
        }

        System.out.println("\nОперации с LinkedListTabulatedFunction");
        System.out.println("Изменяем значение y третьей точки:");
        try {
            linkedFunc.setPoint(2, new FunctionPoint(0, -1));
            System.out.println("Новая точка 2: x = " + linkedFunc.getPointX(2) + ", y = " + linkedFunc.getPointY(2));
        }
        catch (InappropriateFunctionPointException e) {
            System.err.println("Ошибка при изменении точки: " + e.getMessage());
        }

        System.out.println("\nДобавляем новую точку (x=1.5, y=8):");
        try {
            linkedFunc.addPoint(new FunctionPoint(1.5, 8));
            System.out.println("Точки LinkedListTabulatedFunction после добавления:");
            for (int i = 0; i < linkedFunc.getPointsCount(); i++) {
                System.out.println("(" + linkedFunc.getPointX(i) + "; " + linkedFunc.getPointY(i) + ")");
            }
        }
        catch (InappropriateFunctionPointException e) {
            System.err.println("Ошибка при добавлении точки: " + e.getMessage());
        }

        System.out.println("\nНаходим значение y для 2.5 с помощью линейной интерполяции:");
        double valueAt25Array = arrayFunc.getFunctionValue(2.5);
        double valueAt25Linked = linkedFunc.getFunctionValue(2.5);
        System.out.println("ArrayTabulatedFunction f(2.5) = " + valueAt25Array);
        System.out.println("LinkedListTabulatedFunction f(2.5) = " + valueAt25Linked);

        System.out.println("\nУдаляем четвертую точку:");
        try {
            arrayFunc.deletePoint(3);
            System.out.println("Точки ArrayTabulatedFunction после удаления:");
            for (int i = 0; i < arrayFunc.getPointsCount(); i++) {
                System.out.println("(" + arrayFunc.getPointX(i) + "; " + arrayFunc.getPointY(i) + ")");
            }
        }
        catch (Exception e) {
            System.err.println("Ошибка при удалении точки: " + e.getMessage());
        }
        try {
            linkedFunc.deletePoint(3);
            System.out.println("Точки LinkedListTabulatedFunction после удаления:");
            for (int i = 0; i < linkedFunc.getPointsCount(); i++) {
                System.out.println("(" + linkedFunc.getPointX(i) + "; " + linkedFunc.getPointY(i) + ")");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при удалении точки: " + e.getMessage());
        }
    }
    private static void testAnalyticFunctions() {
        System.out.println("1. Аналитические функции:");
        Function sin = new Sin();
        Function cos = new Cos();

        System.out.println("\nЗначения sin(x) от 0 до π с шагом 0.1:");
        printFunctionValues(sin, 0, Math.PI, 0.1);
        System.out.println("\nЗначения cos(x) от 0 до π с шагом 0.1:");
        printFunctionValues(cos, 0, Math.PI, 0.1);

        System.out.println("\n\n2. Табулированные аналоги функции :");

        TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sin, 0, Math.PI, 10);
        TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cos, 0, Math.PI, 10);

        System.out.println("\nТабулированный sin(x):");
        printTabulatedFunction(tabulatedSin);
        System.out.println("\nТабулированный cos(x):");
        printTabulatedFunction(tabulatedCos);

        System.out.println("\nСравнение аналитического и табулированного sin(x):");
        compareFunctions(sin, tabulatedSin, 0, Math.PI, 0.1);

        System.out.println("\n\n3. Сумма квадратов:");

        Function sinSquared = Functions.power(tabulatedSin, 2);
        Function cosSquared = Functions.power(tabulatedCos, 2);
        Function sumOfSquares = Functions.sum(sinSquared, cosSquared);

        System.out.println("Значения суммы квадратов с 10 точками табуляции:");
        printFunctionValues(sumOfSquares, 0, Math.PI, 0.1);

        System.out.println("\n\nВлияние количества точек:");
        for (int points : new int[]{5, 10, 20, 50}) {
            System.out.println("\nКоличество точек: " + points);
            TabulatedFunction sinTab = TabulatedFunctions.tabulate(sin, 0, Math.PI, points);
            TabulatedFunction cosTab = TabulatedFunctions.tabulate(cos, 0, Math.PI, points);

            Function sin2 = Functions.power(sinTab, 2);
            Function cos2 = Functions.power(cosTab, 2);
            Function sum = Functions.sum(sin2, cos2);
            double summ = 0;
            int count = 0;
            for (double x=0; x < Math.PI; x += 0.05) {
                summ += sum.getFunctionValue(x);
                count += 1;
            }
            System.out.printf("Средняя погрешность суммы квадратов: %.5f", 1-summ/count );
        }

        System.out.println("\n\n4. Экспонента и файлы (символы):");
        try {

            Function exp = new Exp();
            TabulatedFunction tabulatedExp = TabulatedFunctions.tabulate(exp, 0, 10, 11);

            System.out.println("Исходная табулированная exp(x):");
            printTabulatedFunction(tabulatedExp);

            String textFileName = "exp_function.txt";
            try (FileWriter writer = new FileWriter(textFileName)) {
                TabulatedFunctions.writeTabulatedFunction(tabulatedExp, writer);
            }
            System.out.println("Функция записана в файл: " + textFileName);

            TabulatedFunction readExp;
            try (FileReader reader = new FileReader(textFileName)) {
                readExp = TabulatedFunctions.readTabulatedFunction(reader);
            }

            System.out.println("\nСчитанная из файла exp(x):");
            printTabulatedFunction(readExp);

            System.out.println("\nСравнение исходной и считанной функций:");
            compareTabulatedFunctions(tabulatedExp, readExp, 0, 10, 1);

        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        System.out.println("\n\n5. Логарифм и файлы (байты):");
        try {

            Function ln = new Log(Math.E);
            TabulatedFunction tabulatedLn = TabulatedFunctions.tabulate(ln, 1, 10, 11);

            System.out.println("Исходная табулированная ln(x) от 1 до 10:");
            printTabulatedFunction(tabulatedLn);

            String binaryFileName = "ln_function.dat";
            try (FileOutputStream fos = new FileOutputStream(binaryFileName)) {
                TabulatedFunctions.outputTabulatedFunction(tabulatedLn, fos);
            }
            System.out.println("Функция записана в файл: " + binaryFileName);

            TabulatedFunction readLn;
            try (FileInputStream fis = new FileInputStream(binaryFileName)) {
                readLn = TabulatedFunctions.inputTabulatedFunction(fis);
            }

            System.out.println("\nСчитанная из файла ln(x):");
            printTabulatedFunction(readLn);

            System.out.println("\nСравнение исходной и считанной функций:");
            compareTabulatedFunctions(tabulatedLn, readLn, 1, 10, 1);
        }
        catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
    private static void testExceptionsWithInterface() {
        System.out.println("Проверка исключений");
        System.out.println("ArrayTabulatedFunction:");
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 5, 3);
        testSimpleExceptions(arrayFunc);

        System.out.println("LinkedListTabulatedFunction:");
        TabulatedFunction linkedFunc = new LinkedListTabulatedFunction(0, 5, 3);
        testSimpleExceptions(linkedFunc);

        System.out.println("Проверка исключений в конструкторах:");
        testConstructorExceptions();
    }
    private static void testSimpleExceptions(TabulatedFunction func) {
        try {
            func.getPoint(10);
            System.out.println("Ошибка: не выброшено FunctionPointIndexOutOfBoundsException");
        }
        catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("FunctionPointIndexOutOfBoundsException: " + e.getMessage());
        }

        try {
            func.addPoint(new FunctionPoint(2.0, 5.0));
            func.addPoint(new FunctionPoint(2.0, 10.0));
            System.out.println("Ошибка: не выброшено InappropriateFunctionPointException");
        }
        catch (InappropriateFunctionPointException e) {
            System.out.println("InappropriateFunctionPointException: " + e.getMessage());
        }

        try {
            TabulatedFunction tempFunc;
            if (func instanceof ArrayTabulatedFunction) {
                tempFunc = new ArrayTabulatedFunction(0, 2, 3);
            }
            else {
                tempFunc = new LinkedListTabulatedFunction(0, 2, 3);
            }
            tempFunc.deletePoint(0);
            tempFunc.deletePoint(0);
            System.out.println("Ошибка: не выброшено IllegalStateException");
        }
        catch (IllegalStateException e) {
            System.out.println("IllegalStateException: " + e.getMessage());
        }

        System.out.println();
    }
    private static void testConstructorExceptions() {
        try {
            new ArrayTabulatedFunction(5, 3, 4);
            System.out.println("Ошибка: не выброшено IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException (левая > правой): " + e.getMessage());
        }

        try {
            new LinkedListTabulatedFunction(0, 5, 1);
            System.out.println("Ошибка: не выброшено IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException (точек < 2): " + e.getMessage());
        }
    }
    private static void printFunctionValues(Function function, double from, double to, double step) {
        DecimalFormat df = new DecimalFormat("0.0000");
        int count = 0;

        for (double x = from; x <= to + 1e-10; x += step) {
            double value = function.getFunctionValue(x);
            if (Double.isNaN(value)) {
                System.out.printf("f(%.1f) = не определено  ", x);
            }
            else {
                System.out.printf("f(%.1f) = %-10s", x, df.format(value));
            }
            count++;
            if (count % 4 == 0) System.out.println();
        }
        if (count % 4 != 0) System.out.println();
    }
    private static void printTabulatedFunction(TabulatedFunction function) {
        System.out.println("Количество точек: " + function.getPointsCount());
        System.out.println("Область определения: [" + String.format("%.2f", function.getLeftDomainBorder()) + ", " +
                String.format("%.2f", function.getRightDomainBorder()) + "]");

        System.out.println("Точки функции:");
        for (int i = 0; i < function.getPointsCount(); i++) {
            System.out.printf("  %2d: x = %7.4f, y = %10.6f%n", i, function.getPointX(i), function.getPointY(i));
        }
    }
    private static void compareFunctions(Function analytic, TabulatedFunction tabulated, double from, double to, double step) {
        System.out.println("x\t\tАналитическая\tТабулированная\tПогрешность");

        for (double x = from; x <= to + 1e-10; x += step) {
            double analyticValue = analytic.getFunctionValue(x);
            double tabulatedValue = tabulated.getFunctionValue(x);
            double error = Math.abs(analyticValue - tabulatedValue);
            if (Double.isNaN(analyticValue) || Double.isNaN(tabulatedValue)) {
                System.out.printf("%.1f\t\t%s\t\t%s\t\t%s%n", x, "не опр.", "не опр.", "не опр.");
            }
            else {
                System.out.printf("%.1f\t\t%.6f\t\t%.6f\t\t%.6f%n", x, analyticValue, tabulatedValue, error);
            }
        }
    }
    private static void compareTabulatedFunctions(TabulatedFunction f1, TabulatedFunction f2, double from, double to, double step) {
        System.out.println("x\t\tИсходная\tСчитанная\tСовпадают");

        boolean allMatch = true;
        for (double x = from; x <= to + 1e-10; x += step) {
            double v1 = f1.getFunctionValue(x);
            double v2 = f2.getFunctionValue(x);
            boolean match = Math.abs(v1 - v2) < 1e-10 || (Double.isNaN(v1) && Double.isNaN(v2));
            if (!match) allMatch = false;
            String matchSymbol = match ? "да" : "нет";
            System.out.printf("%.1f\t\t%.6f\t%.6f\t%s%n", x, v1, v2, matchSymbol);
        }
        if (allMatch) {
            System.out.println("Все значения совпадают");
        }
        else {
            System.out.println("Обнаружены расхождения");
        }
    }
    public static ArrayTabulatedFunction testTaskSerializable() throws Exception {
        System.out.println("\nСериализация через Serializable:");

        ArrayTabulatedFunction tab = (ArrayTabulatedFunction)
                TabulatedFunctions.tabulate(new Composition(new Log(Math.E), new Exp()), 0, 10, 11);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("ser.bin"))) {
            out.writeObject(tab);
        }

        ArrayTabulatedFunction tab2;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("ser.bin"))) {
            tab2 = (ArrayTabulatedFunction) in.readObject();
        }

        System.out.println("   Serializable успешно: первая точка: (" + tab2.getPointX(0)+ ", " + tab2.getPointY(0) + ")");

        return tab2; // возвращаем для сравнения
    }


    public static LinkedListTabulatedFunction testTaskExternalizable() throws Exception {
        System.out.println("\nСериализация через Externalizable:");

        LinkedListTabulatedFunction tab = new LinkedListTabulatedFunction ( 0, 10, 11);

        for (int i = 0; i < tab.getPointsCount(); i++){
            tab.setPointY(i,new Composition(new Log(Math.E), new Exp()).getFunctionValue(tab.getPointX(i)));
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("ext.bin"))) {
            tab.writeExternal(out);
        }

        LinkedListTabulatedFunction tab2 = new LinkedListTabulatedFunction();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("ext.bin"))) {
            tab2.readExternal(in);
        }

        System.out.println("   Externalizable успешно: первая точка: " + tab2.getPointX(0)+ ", " + tab2.getPointY(0) + ")");

        return tab2; // возвращаем для сравнения
    }
}
