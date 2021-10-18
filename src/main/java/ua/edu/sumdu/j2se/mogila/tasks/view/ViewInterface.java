package ua.edu.sumdu.j2se.mogila.tasks.view;

import ua.edu.sumdu.j2se.mogila.tasks.Task;
import ua.edu.sumdu.j2se.mogila.tasks.model.Model;
import ua.edu.sumdu.j2se.mogila.tasks.model.ModelInterface;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.SortedMap;

public interface ViewInterface {


    int mainMenu();

    void printList(ModelInterface  model);

    void printInfo(ModelInterface  model, int index);

    void printTaskNextWeek(ModelInterface  model);

    SortedMap<LocalDateTime, Set<Task>> calendar(ModelInterface  model);

    int deleteTaskView();

    int editTaskView(ModelInterface  model);

    void noCommandPrint();

    void success();

    void deleteFail();

    void calendarTaskView(ModelInterface model);

    void infoOfTaskView(ModelInterface  model);

    String readTitleFromLine();

    boolean readTaskForm();

    LocalDateTime readStartTimeFromLine();

    LocalDateTime readEndTimeFromLine();

    LocalDateTime readTimeFromLine();

    int readInterval();

    boolean readTaskActive();

    void notification(Task task);

    void empty();

}
