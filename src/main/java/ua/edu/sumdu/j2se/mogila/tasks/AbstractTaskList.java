package ua.edu.sumdu.j2se.mogila.tasks;

import java.io.Serializable;

import java.util.stream.*;

public abstract class AbstractTaskList implements Iterable<Task>, Serializable {

    public abstract int size();

    public abstract Task getTask(int index);

    public abstract boolean remove(Task task);

    public abstract void add(Task task);

    public abstract Stream<Task> getStream();

    public abstract ListTypes.types getList();

    /**
     * @param from - время не раньше
     * @param to   - не позже
     * @return returnTaskList - множина задач
     */
/*
       public AbstractTaskList incoming(LocalDateTime from, LocalDateTime to) {
        AbstractTaskList returnAbstract = TaskListFactory.createTaskList(getList());
            getStream().filter(task -> task.nextTimeAfter(from).isAfter(from) && task.nextTimeAfter(from).isBefore(to)).forEach(p -> returnAbstract.add(p));
        return returnAbstract;
    }
*/

}
