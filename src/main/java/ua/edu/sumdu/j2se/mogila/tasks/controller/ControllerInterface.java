package ua.edu.sumdu.j2se.mogila.tasks.controller;

import ua.edu.sumdu.j2se.mogila.tasks.Task;
import ua.edu.sumdu.j2se.mogila.tasks.model.ModelInterface;

import java.io.IOException;

public interface ControllerInterface {

    void addNewTask() throws IOException;

    void deleteTask();

    void editTask() throws IOException;

    void infoOfTask();

    void calendarTask();

    void act() throws IOException;

    void listOfTask();

    void startProgram();

    boolean listIsEmpty();

    void notificationThread(ModelInterface model);

    Task readTask();
}
