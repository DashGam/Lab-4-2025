package functions;

import java.io.*;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {
    private static class FunctionNode implements Serializable {
        private static final long serialVersionUID = 1L;
        FunctionPoint point;
        FunctionNode prev;
        FunctionNode next;
        public FunctionNode() {
            this.point = null;
            this.prev = this;
            this.next = this;
        }
        public FunctionNode(FunctionPoint point) {
            this.point = point;
            this.prev = this;
            this.next = this;
        }
        public FunctionNode(FunctionPoint point, FunctionNode prev, FunctionNode next) {
            this.point = point;
            this.prev = prev;
            this.next = next;
        }

    }
    private FunctionNode head;
    private int size;
    private FunctionNode lastAccessedNode;
    private int lastAccessedIndex;
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения больше или равна правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек меньше двух");
        }

        initializeList();
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            FunctionNode newNode = addNodeToTail();
            newNode.point = new FunctionPoint(x, 0.0);
        }
    }
    public LinkedListTabulatedFunction() {
        initializeList();
    }
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения больше или равна правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек меньше двух");
        }

        initializeList();
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * step;
            FunctionNode newNode = addNodeToTail();
            newNode.point = new FunctionPoint(x, values[i]);
        }
    }
    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Массив точек не может быть null");
        }
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2: " + points.length);
        }

        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Точка с индексом " + i + " равна null");
            }
            if (points[i + 1] == null) {
                throw new IllegalArgumentException("Точка с индексом " + (i + 1) + " равна null");
            }
            if (points[i].getX() >= points[i + 1].getX()) {
                throw new IllegalArgumentException("Точки не упорядочены по X: " +
                        points[i].getX() + " >= " + points[i + 1].getX());
            }
        }
        initializeList();

        for (FunctionPoint point : points) {
            FunctionNode newNode = addNodeToTail();
            newNode.point = new FunctionPoint(point); // Защитная копия
        }
    }
    private void initializeList() {
        this.head = new FunctionNode();
        this.size = 0;
        this.lastAccessedNode = head;
        this.lastAccessedIndex = -1;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(size);

        FunctionNode current = head.next;
        for (int i = 0; i < size; i++) {
            out.writeDouble(current.point.getX());
            out.writeDouble(current.point.getY());
            current = current.next;
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int pointsCount = in.readInt();
        if (pointsCount < 0) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }

        initializeList();

        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            FunctionNode node = addNodeToTail();
            node.point = new FunctionPoint(x, y);
        }
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами списка: " + index);
        }
        FunctionNode currentNode;
        int currentIndex;
        if (lastAccessedIndex != -1) {
            int distanceFromLast = Math.abs(index - lastAccessedIndex);
            int distanceFromStart = index;
            int distanceFromEnd = size - 1 - index;
            if (distanceFromLast <= distanceFromStart && distanceFromLast <= distanceFromEnd) {
                currentNode = lastAccessedNode;
                currentIndex = lastAccessedIndex;
            } else if (distanceFromStart <= distanceFromEnd) {
                currentNode = head.next;
                currentIndex = 0;
            } else {
                currentNode = head.prev;
                currentIndex = size - 1;
            }
        } else {
            currentNode = head.next;
            currentIndex = 0;
        }
        while (currentIndex != index) {
            if (currentIndex < index) {
                currentNode = currentNode.next;
                currentIndex++;
            } else {
                currentNode = currentNode.prev;
                currentIndex--;
            }
        }
        lastAccessedNode = currentNode;
        lastAccessedIndex = index;
        return currentNode;
    }


    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode();
        FunctionNode tail = head.prev;
        newNode.prev = tail;
        newNode.next = head;
        tail.next = newNode;
        head.prev = newNode;
        size++;
        lastAccessedIndex = -1;
        return newNode;
    }

    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > size) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами списка: " + index);
        }

        if (index == size) {
            return addNodeToTail();
        }

        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode prevNode = nextNode.prev;
        FunctionNode newNode = new FunctionNode();
        newNode.prev = prevNode;
        newNode.next = nextNode;
        prevNode.next = newNode;
        nextNode.prev = newNode;
        size++;
        lastAccessedIndex = -1;
        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами списка: " + index);
        }
        if (size <= 2) {
            throw new IllegalStateException("Невозможно удалить точку: должно остаться минимум 2 точки");
        }

        FunctionNode nodeToDelete = getNodeByIndex(index);
        FunctionNode prevNode = nodeToDelete.prev;
        FunctionNode nextNode = nodeToDelete.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        nodeToDelete.prev = null;
        nodeToDelete.next = null;

        size--;
        lastAccessedIndex = -1;

        return nodeToDelete;
    }
    public double getLeftDomainBorder() {
        if (size == 0) return Double.NaN;
        return head.next.point.getX();
    }
    public double getRightDomainBorder() {
        if (size == 0) return Double.NaN;
        return head.prev.point.getX();
    }
    public double getFunctionValue(double x) {
        if (size == 0 || x < getLeftDomainBorder() - 1e-10 || x > getRightDomainBorder() + 1e-10) {
            return Double.NaN;
        }
        for (int i = 0; i < size - 1; i++) {
            FunctionNode node = getNodeByIndex(i);
            FunctionNode nextNode = node.next;
            double x0 = node.point.getX();
            double x1 = nextNode.point.getX();
            double y0 = node.point.getY();
            double y1 = nextNode.point.getY();
            if (Math.abs(x - x0) < 1e-10) return y0;
            if (Math.abs(x - x1) < 1e-10) return y1;
            if (x > x0 - 1e-10 && x < x1 + 1e-10) {
                return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
            }
        }
        return Double.NaN;
    }
    public int getPointsCount() {
        return size;
    }
    public FunctionPoint getPoint(int index) {
        return new FunctionPoint(getNodeByIndex(index).point);
    }
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс точки выходит за границы: " + index);
        }

        FunctionNode node = getNodeByIndex(index);

        if (index > 0) {
            FunctionNode prevNode = node.prev;
            if (point.getX() <= prevNode.point.getX()) {
                throw new InappropriateFunctionPointException("Координата X точки нарушает упорядоченность точек функции");
            }
        }
        if (index < size - 1) {
            FunctionNode nextNode = node.next;
            if (point.getX() >= nextNode.point.getX()) {
                throw new InappropriateFunctionPointException("Координата X точки нарушает упорядоченность точек функции");
            }
        }

        node.point = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        return getNodeByIndex(index).point.getX();
    }


    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс точки выходит за границы: " + index);
        }

        FunctionNode node = getNodeByIndex(index);
        FunctionPoint currentPoint = node.point;

        if (index > 0) {
            FunctionNode prevNode = node.prev;
            if (x <= prevNode.point.getX()) {
                throw new InappropriateFunctionPointException("Координата X точки нарушает упорядоченность точек функции");
            }
        }
        if (index < size - 1) {
            FunctionNode nextNode = node.next;
            if (x >= nextNode.point.getX()) {
                throw new InappropriateFunctionPointException("Координата X точки нарушает упорядоченность точек функции");
            }
        }

        node.point = new FunctionPoint(x, currentPoint.getY());
    }

    public double getPointY(int index) {
        return getNodeByIndex(index).point.getY();
    }

    public void setPointY(int index, double y) {
        FunctionNode node = getNodeByIndex(index);
        FunctionPoint currentPoint = node.point;
        node.point = new FunctionPoint(currentPoint.getX(), y);
    }

    public void deletePoint(int index) {
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        for (int i = 0; i < size; i++) {
            if (Math.abs(getPointX(i) - point.getX()) < 1e-10) {
                throw new InappropriateFunctionPointException("Точка с координатой X=" + point.getX() + " уже существует");
            }
        }

        int insertIndex = 0;
        double x = point.getX();
        while (insertIndex < size && getPointX(insertIndex) < x) {
            insertIndex++;
        }

        FunctionNode newNode = addNodeByIndex(insertIndex);
        newNode.point = new FunctionPoint(point);
    }

    public void printList() {
        System.out.println("Список точек (" + size + " точек):");
        FunctionNode current = head.next;
        int index = 0;
        while (current != head) {
            System.out.println(index + ": (" + current.point.getX() + ", " + current.point.getY() + ")");
            current = current.next;
            index++;
        }
    }
}


