package ua.edu.sumdu.j2se.mogila.tasks;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.*;

public class LinkedTaskList extends AbstractTaskList implements Cloneable, Serializable {
    private Node first;
    private Node last;
    private int size;
    private Task [] arrayForStream;

    public LinkedTaskList() {
        first = null;
        last = null;
    }

    public void add(Task task) {
       if (isEmpty()) {
           first = new Node (task);
           last = first;
           size++;
           return;
       }
        last.next = new Node(task);
        last = last.next;
        size++;
    }

    @Override
    public Stream<Task> getStream() {
        Node buf = first;
        arrayForStream = new Task[size];
        for (int i = 0; i<size; i++) {
            arrayForStream[i] = buf.currentElement;
            buf = buf.next;
        }
        return Stream.of(arrayForStream);
    }


    private boolean isEmpty() {
        return first == null;
    }
    public int size() {
        return size;
    }

    public boolean remove(Task task) {
        if (task.equals(first.currentElement)) {
            first = first.next;
            size--;
            return true;
        }
        Node buf = first;
        Node prev = null;
        while (buf != null) {
            if (buf.currentElement.equals(task)) {
                if (buf.equals(last)) {
                    last = prev;
                }
                prev.next = buf.next;
                size--;
                return true;
            }

            prev = buf;
            buf = buf.next;
        }
        return false;
    }
    public Task getTask(int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        Node buf = first;
        for (int i = 0; i < index; i++) {
          buf = buf.next;
        }
        return buf.currentElement;
    }


    private class Node {
        private Task currentElement;
        private Node next;
        Node (Task task) {
            currentElement = task;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(currentElement, node.currentElement) &&
                    Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(currentElement, next);
        }

        public Task getCurrentElement() {
            return currentElement;
        }

        public void setCurrentElement(Task currentElement) {
            this.currentElement = currentElement;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedTaskList that = (LinkedTaskList) o;
        return size == that.size &&
                Objects.equals(first, that.first) &&
                Objects.equals(last, that.last);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, last, size);
    }

    @Override
    public ListTypes.types getList() {
        return ListTypes.types.LINKED;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
         return super.clone();
    }

    public Iterator iterator() {
        return new ListIterator(this);
    }

    class ListIterator implements Iterator {
        Node current;
        boolean canRemove;
        Node prev;

        public ListIterator(AbstractTaskList list)
        {
            current = first;
        }

        public boolean hasNext()
        {
            return current != null;
        }

        public Task next() {
            canRemove = true;
            Task data = current.getCurrentElement();
            prev = current;
            current = current.getNext();
            return data;
        }

        public void remove() {

            if (!canRemove)
                throw new IllegalStateException();
            else
            LinkedTaskList.this.remove(prev.currentElement);
            canRemove = false;
        }

}


}






