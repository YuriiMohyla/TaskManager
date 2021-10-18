package ua.edu.sumdu.j2se.mogila.tasks;

import java.util.*;
import java.util.stream.Stream;

public class ArrayTaskList extends AbstractTaskList implements Cloneable {
    private int size;
    private Task[] array;
    private Task[] arrayForStream;
    /**
     * индекс в который добавляется новый елемент
     */
    private int index;
    private static final int DEFAULT_CAPACITY = 20;

    public ArrayTaskList() {
        array = new Task[DEFAULT_CAPACITY];
    }

    public ArrayTaskList(int capacity) {
        array = new Task[capacity];
    }

    /**
     * @param task - задача
     */
    public void add(Task task) {
        if (task == null) {
            throw new NullPointerException("Null ex");
        }
        if (index == array.length) {
            growArray();
        }
        array[index] = task;
        index++;
        size++;
    }

    @Override
    public Stream<Task> getStream() {
        arrayForStream = new Task[size];
        for (int i = 0; i<size; i++) {
            arrayForStream[i] = array[i];
        }
        return Stream.of(arrayForStream);
    }


    /**
     * увеличение масива при окончании места
     */
    private void growArray() {
        Task[] newArray = new Task[array.length + 10];
        System.arraycopy(array, 0, newArray, 0, index - 1);
        array = newArray;
    }

    /**
     * param task - задача
     * return состояние
     */
    public boolean remove(Task task) {
        for (int i = 0; i <= size; i++) {
            if (task.equals(array[i])) {
                System.arraycopy(array, i + 1, array, i, index - i);
                size--;
                index--;
                return true;
            }
        }
        return false;
    }

    /**
     * @return size - кількість задач у списку
     */
    public int size() {
        return size;
    }

    /**
     * @param index - місце у списку
     * @return array[index] - задача, яка знаходиться на вказаному місці у списку
     */
    public Task getTask(int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        checkIndex(index);
        return (array[index]);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= this.index)
            throw new IllegalArgumentException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayTaskList that = (ArrayTaskList) o;
        return size == that.size &&
                index == that.index &&
                Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size, index);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }

    @Override
    public ListTypes.types getList() {
        return ListTypes.types.ARRAY;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        ArrayTaskList copy = (ArrayTaskList) super.clone();
        copy.array = Arrays.copyOf(this.array,size);
        return copy;
}

    @Override
    public String toString() {
        return "ArrayTaskList{" +
                "size=" + size +
                ", array=" + Arrays.toString(array) +
                ", index=" + index +
                '}';
    }


    public Iterator iterator() {
        return new ArrayIterator(this);
    }

    class ArrayIterator implements Iterator {


        private int currentIndex;
        boolean canRemove;
        public ArrayIterator(AbstractTaskList array) {
        currentIndex = 0;
        }

        public boolean hasNext() {
            return currentIndex < size && array[currentIndex] != null;
        }

        public Task next() {
            canRemove = true;
            return array[currentIndex++];
        }

        public void remove() {
            if (!canRemove)
            throw new IllegalStateException();
            else {ArrayTaskList.this.remove(array[--currentIndex]);
            canRemove = false;}
        }
    }
}


