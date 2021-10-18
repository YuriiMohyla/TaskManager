package ua.edu.sumdu.j2se.mogila.tasks;

public class TaskListFactory {

    public static AbstractTaskList createTaskList(ListTypes.types type) {
        if (type == ListTypes.types.ARRAY) {
            return new ArrayTaskList();
        }
        if (type == ListTypes.types.LINKED) {
            return new LinkedTaskList();
        }
        return null;
    }
}
