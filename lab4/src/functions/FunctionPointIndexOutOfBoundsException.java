package functions;

public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {
    public FunctionPointIndexOutOfBoundsException() {
        super();
    }

    public FunctionPointIndexOutOfBoundsException(String s) {
        super(s);
    }

    public FunctionPointIndexOutOfBoundsException(int index) {
        super("Function point index out of range: " + index);
    }
}
