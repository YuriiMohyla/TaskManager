package ua.edu.sumdu.j2se.mogila.tasks.controller;

import ua.edu.sumdu.j2se.mogila.tasks.Task;
import ua.edu.sumdu.j2se.mogila.tasks.model.ModelInterface;
import ua.edu.sumdu.j2se.mogila.tasks.view.ViewInterface;

import java.time.LocalDateTime;


public class Controller implements ControllerInterface {

    private ModelInterface model;
    private ViewInterface view;

    public Controller(ModelInterface model, ViewInterface view) {
        this.model = model;
        this.view = view;
        startProgram();
    }

    @Override
    public void addNewTask() {
        model.add(readTask());
        listOfTask();
        view.success();
    }

    @Override
    public void deleteTask() {
        if (!listIsEmpty()) {
        if (model.delete(view.deleteTaskView())) {
            view.success();
            model.save();
            listOfTask();
        }
        else view.deleteFail();
        }
    }

    @Override
    public void editTask() {
        Task tempTask = readTask();
            model.edit(view.editTaskView(model), tempTask);
            view.success();
            listOfTask();

        }

    @Override
    public void infoOfTask() {
        if (!listIsEmpty())
        view.infoOfTaskView(model);
    }

    @Override
    public void calendarTask() {
        if (!listIsEmpty())
        view.calendarTaskView(model);
    }

    @Override
    public void act() {
        switch (view.mainMenu()) {
            case 0: model.save(); System.exit(0);
            case 1: addNewTask(); break;
            case 2: infoOfTask(); break;
            case 3: deleteTask(); break;
            case 4: listOfTask(); break;
            case 5: editTask(); break;
            case 6: calendarTask(); break;
            default: view.noCommandPrint();
        }
    }

    @Override
    public void listOfTask() {
        if (!listIsEmpty())
        view.printList(model);
    }

    @Override
    public void startProgram() {
        model.openFile();
        view.printTaskNextWeek(model);
        notificationThread(model);
    }

    @Override
    public boolean listIsEmpty() {
        if (model.getTaskList().size() == 0) {
            view.empty();
            return true;
        }
        else return false;
    }

    @Override
    public void notificationThread(ModelInterface model) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                    int timeDomain = 3;
                    boolean flag;
                    while (true) {
                        flag = false;
                        synchronized (model.getTaskList()){
                        for (Task task : model.getTaskList()) {
                            if (task.nextTimeAfter(LocalDateTime.now()) != null
                                    && task.nextTimeAfter(LocalDateTime.now())
                                    .isAfter(LocalDateTime.now().minusSeconds(timeDomain))
                                    && task.nextTimeAfter(LocalDateTime.now())
                                    .isBefore(LocalDateTime.now().plusSeconds(timeDomain))) {
                                view.notification(task);
                                flag = true;
                            }
                        }
                        try {
                            if (flag) {
                                Thread.sleep(8000);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    }
                }
        });
        thread.start();

    }

    @Override
    public Task readTask() {
        Task task = new Task();
        task.setTitle(view.readTitleFromLine());
        if (view.readTaskForm()) {
            task.setStart(view.readStartTimeFromLine());
            task.setEnd(view.readEndTimeFromLine());
            task.setInterval(view.readInterval());
        } else {
            task.setTime(view.readTimeFromLine());
        }
        task.setActive(view.readTaskActive());
        return task;
    }

}
