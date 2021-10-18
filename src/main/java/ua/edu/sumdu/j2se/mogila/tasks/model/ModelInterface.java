package ua.edu.sumdu.j2se.mogila.tasks.model;

import ua.edu.sumdu.j2se.mogila.tasks.AbstractTaskList;
import ua.edu.sumdu.j2se.mogila.tasks.Task;

public interface ModelInterface {

    void add(Task task);

    boolean delete(int index);

    void save();

    Task search(String title);

    void edit(int index, Task task);

    void openFile();

    Iterable<Task> nextWeekIncoming();

    AbstractTaskList getTaskList();
}
