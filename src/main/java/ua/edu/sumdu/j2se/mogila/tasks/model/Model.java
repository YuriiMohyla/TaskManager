package ua.edu.sumdu.j2se.mogila.tasks.model;

import ua.edu.sumdu.j2se.mogila.tasks.*;
import org.apache.log4j.Logger;
import java.io.*;
import java.time.LocalDateTime;


public class Model implements ModelInterface {
    final static Logger logger = Logger.getLogger(Model.class);
    private static AbstractTaskList taskList = new LinkedTaskList();

    @Override
    public AbstractTaskList getTaskList() { return taskList; }


    @Override
    public void add(Task task) {
        taskList.add(task);
        save();
    }

    @Override
    public boolean delete(int index) {
        if (searchIndex(index) == null) return false;
        else { taskList.remove(searchIndex(index)); return true; }
    }
    /**
     * сохраниние taskList в файл
     */
    @Override
    public void save() {
        File directory = new File("resDir");
        directory.mkdir();
        File file = new File(directory, "res.bin");

        try {
            TaskIO.writeBinary(taskList, file);
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
    public Task searchIndex(int index) {
        if (index<0 || index>=taskList.size()) return null;
        return taskList.getTask(index);
    }

    @Override
    public Task search(String title) {
        for (int i = 0; i <= taskList.size();i++) {
            if (taskList.getTask(i).getTitle().equals(title)) return taskList.getTask(i);
        }
        return null;
    }

    @Override
    public void edit(int index, Task task) {
        if (index > taskList.size() || index < 0) throw new IndexOutOfBoundsException();
        else {
            taskList.getTask(index).setStart(task.getStartTime());
            taskList.getTask(index).setEnd(task.getEndTime());
            taskList.getTask(index).setInterval(task.getRepeatInterval());
            taskList.getTask(index).setActive(task.isActive());
            taskList.getTask(index).setTitle(task.getTitle());
            taskList.getTask(index).setTime(task.getTime());
        }
    }

    @Override
    public void openFile() {
        File file = new File("resDir", "res.bin");
        try {
            TaskIO.readBinary(taskList,file);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Iterable<Task> nextWeekIncoming() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextWeek = LocalDateTime.now().plusDays(7);
        return Tasks.incoming(taskList, now, nextWeek);
    }
}
