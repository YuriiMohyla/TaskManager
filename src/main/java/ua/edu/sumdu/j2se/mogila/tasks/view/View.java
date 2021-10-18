package ua.edu.sumdu.j2se.mogila.tasks.view;


import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.mogila.tasks.Task;
import ua.edu.sumdu.j2se.mogila.tasks.Tasks;
import ua.edu.sumdu.j2se.mogila.tasks.model.Formatter;
import ua.edu.sumdu.j2se.mogila.tasks.model.ModelInterface;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.time.LocalDateTime;

import java.time.format.DateTimeParseException;
import java.util.*;


public class View implements ViewInterface {

    private final static Logger logger = Logger.getLogger(View.class.getName());
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public int mainMenu() {
        int number;
        System.out.println("Приложение Task manager (действия):\n1:добавить задачу\n2:информация о нужной задаче\n3:удалить задачу"
                + "\n4:список задач\n5:изменить задачу\n6:календарь задач на промежуток времени\n0:выход");
        try {
            number = Integer.parseInt(reader.readLine());
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
            return mainMenu();
        }
        catch (NumberFormatException e) {
            System.out.println("Неверный формат числа попробуйте снова");
            logger.error(e.getMessage(), e);
           return mainMenu();
        }
        return number;
    }

    @Override
    public void printList(ModelInterface model) {
        System.out.println("//------------СПИСОК ЗАДАЧ-------------//");
        for (int i = 1; i <= model.getTaskList().size(); i++)
        System.out.println ("#" + i + " " + model.getTaskList().getTask(i-1).getTitle());
        System.out.println("//-----------------------------//");
    }

    @Override
    public void printInfo (ModelInterface model, int index) {
        System.out.println(model.getTaskList().getTask(index));
    }

    @Override
    public void printTaskNextWeek(ModelInterface model) {
        System.out.println("В ближайшие 7 дней не забудьте выполнить такие задачи:");
        for (Task task:model.nextWeekIncoming()) {
            System.out.println("Задача: " + task.getTitle() + " - " + task.nextTimeAfter(LocalDateTime.now()).
                    format(Formatter.getFormat()));
        }
        System.out.println("///////////////////////////");
    }
    public void notification(Task task) {
        System.out.println("Пора выполнить - " + task.getTitle());
    }

    @Override
    public void empty() {
        System.out.println("Список пуст!");
    }

    @Override
    public SortedMap<LocalDateTime, Set<Task>> calendar(ModelInterface model) {
        LocalDateTime from;
        LocalDateTime to;
        try {
            System.out.println("Введите время начала календаря в формате dd-MM-yyyy HH:mm");
            String start = reader.readLine();
            from = LocalDateTime.parse(start,Formatter.getFormat());
            System.out.println("Введите время окончания календаря в формате dd-MM-yyyy HH:mm");
            String end = reader.readLine();
            to = LocalDateTime.parse(end,Formatter.getFormat());
            if (from.isAfter(to)) {
                System.out.println("Время начала должно быть до времени окончания");
                return calendar(model);
            }
        }
        catch (DateTimeParseException | IOException e) {
            System.out.println ("Не верно введена дата попробуйте снова");
            logger.error(e.getMessage(), e);
            return calendar(model);
        }
        return Tasks.calendar(model.getTaskList(), from, to);
    }

    @Override
    public int deleteTaskView() {
        System.out.println("Введите номер задачи которую необходимо удалить");
        int index;
        try {
             index = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException | IOException e) {
            logger.error(e.getMessage(), e);
            return deleteTaskView();
        }
        return index-1;
    }

    @Override
    public int editTaskView(ModelInterface model) {
        int index;
        System.out.println("Введите номер задачи которую хотите изменить: ");
        try {
             index = Integer.parseInt(reader.readLine());
             if (index-1 >= model.getTaskList().size() || index-1 < 0) {
                 System.out.println("Нет задачи с таким номером, проверте номер задачи");
                 return editTaskView(model);
             }
        }
        catch (NumberFormatException e) {
            System.out.println("Неверный формат числа попробуйте снова");
            logger.error(e.getMessage(), e);
            return editTaskView(model);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return editTaskView(model);
        }
        return index-1;
    }

    @Override
    public void noCommandPrint() {
        System.out.println("Нет такой команды");
    }

    @Override
    public void success() {
        System.out.println("Успех!");
    }

    @Override
    public void deleteFail() {
        System.out.println("Задача не найдена");
    }

    @Override
    public void calendarTaskView(ModelInterface model) {
        for (Map.Entry<LocalDateTime, Set<Task>> entry :calendar(model).entrySet()) {
            System.out.println("Date: " + entry.getKey().format(Formatter.getFormat()) + " Task: " + entry.getValue());
        }
    }

    @Override
    public void infoOfTaskView(ModelInterface model) {
        try {
            System.out.println("Введите номер задачи о которой хотите узнать");
            int index = Integer.parseInt(reader.readLine());
            if (index-1 >= model.getTaskList().size() || index-1 < 0) System.out.println("Нет такой задачи");
            else printInfo(model, index-1);
        }
        catch (NumberFormatException e) {
            System.out.println("Неверный формат числа попробуйте снова");
            logger.error(e.getMessage(), e);
            infoOfTaskView(model);
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
            infoOfTaskView(model);
        }
    }

    @Override
    public String readTitleFromLine() {
        String title;
        System.out.println("Введите название задачи");
        try {
             title = reader.readLine();
            if (title.equals("") || title.startsWith(" ")) {
                System.out.println("Введите текст");
                return readTitleFromLine();
            }
        } catch (Exception e) {
            System.out.println("Ошибка при вводе названия, попробуйте еще раз");
            logger.error(e.getMessage(), e);
            return readTitleFromLine();
        }

        return title;
    }

    @Override
    public boolean readTaskForm() {
        boolean viewTask;
        System.out.println("Ваша задача находиться за заданом промежутке времени? (true/любой символ если нет)");
        try {
             viewTask = Boolean.parseBoolean(reader.readLine());
        } catch (Exception e) {
            System.out.println("Ошибка при вводе вида задачи, попробуйте еще раз");
            logger.error(e.getMessage(), e);
            return readTaskForm();
        }
        return viewTask;
    }

    @Override
    public LocalDateTime readStartTimeFromLine() {
            String start;
            LocalDateTime startTime;
            System.out.println("Введите время начала в формате dd-MM-yyyy HH:mm");
        try {
            start = reader.readLine();
                if (start.equals("") || start.startsWith(" ")) {
                    System.out.println("Введите текст");
                    return readStartTimeFromLine();
                }
            startTime = LocalDateTime.parse(start, Formatter.getFormat());
        } catch (DateTimeParseException e) {
            System.out.println("Не верно введена дата попробуйте снова");
            logger.error(e.getMessage(), e);
           return readStartTimeFromLine();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return readStartTimeFromLine();
        }
        return startTime;
    }

    @Override
    public LocalDateTime readEndTimeFromLine() {
        String end;
        LocalDateTime endTime;
        System.out.println("Введите время окончания в формате dd-MM-yyyy HH:mm");
        try {
            end = reader.readLine();
                if (end.equals("") || end.startsWith(" ")) {
                    System.out.println("Введите текст");
                   return readEndTimeFromLine();
                }
            endTime = LocalDateTime.parse(end, Formatter.getFormat());
        } catch (DateTimeParseException|IOException e) {
            System.out.println("Не верно введена дата попробуйте снова");
            logger.error(e.getMessage(), e);
            return readEndTimeFromLine();
        }
        return endTime;
    }

    @Override
    public LocalDateTime readTimeFromLine() {
        String time;
        LocalDateTime time1;
        System.out.println("Введите время выполнения в формате dd-MM-yyyy HH:mm");
        try {
            time = reader.readLine();
                if (time.equals("") || time.startsWith(" ")) {
                    System.out.println("Введите текст");
                    readTimeFromLine();
                }
            time1 = LocalDateTime.parse(time, Formatter.getFormat());
        } catch (DateTimeParseException|IOException e) {
            System.out.println("Не верно введена дата попробуйте снова");
            logger.error(e.getMessage(), e);
            return readTimeFromLine();
        }
      return time1;
    }

    @Override
    public int readInterval() {
        int interval;
        System.out.println("Введите интервал в секундах ");
        try {
            interval = Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            System.out.println("Не верно введен интервал попробуйте снова");
            logger.error(e.getMessage(), e);
            return readInterval();
        }
        return interval;
    }

    @Override
    public boolean readTaskActive() {
        boolean activeTask;
        System.out.println("Задача активна? (true/любой символ если нет)");
        try {
            activeTask = Boolean.parseBoolean(reader.readLine());
        } catch (Exception e) {
            System.out.println("Ошибка при вводе, попробуйте еще раз");
            logger.error(e.getMessage(), e);
            return readTaskActive();
        }
        return activeTask;
    }


}
