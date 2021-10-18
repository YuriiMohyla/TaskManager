package ua.edu.sumdu.j2se.mogila.tasks;

import java.time.LocalDateTime;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;


public class Tasks {

    /**
     *
     * @param tasks
     * @param from
     * @param to
     * @return
     */
    public static Iterable<Task> incoming(Iterable<Task> tasks, LocalDateTime from, LocalDateTime to) {

        LinkedTaskList linkedTaskList = new LinkedTaskList();

        for (Task task : tasks) {

            if (task.nextTimeAfter(from) != null && (task.nextTimeAfter(from).isAfter(from) || task.nextTimeAfter(from).isEqual(from)) && (task.nextTimeAfter(from).isBefore(to) || task.nextTimeAfter(from).isEqual(to))) {
                linkedTaskList.add(task);

            }
        }
        return (Iterable<Task>) linkedTaskList;
    }

    /**
     *
     * @param tasks
     * @param from
     * @param to
     * @return
     */
    static public SortedMap<LocalDateTime, Set<Task>> calendar(Iterable<Task> tasks, LocalDateTime from, LocalDateTime to) {

        SortedMap<LocalDateTime, Set<Task>> treeMap = new TreeMap<>();
        LinkedTaskList linkedTaskList = new LinkedTaskList();
        for (Task task : tasks) {
            if (task.nextTimeAfter(from) != null && (task.nextTimeAfter(from).isAfter(from) || task.nextTimeAfter(from).isEqual(from)) && (task.nextTimeAfter(from).isBefore(to) || task.nextTimeAfter(from).isEqual(to))) {
                linkedTaskList.add(task);
            }
        }
        /**
         * необходимо загнать загнать таски в сеты относительно их времени выполнения
        linkedTaskList лист для всех тасок которые выполняються в заданный промежуток времени
         */

        ZonedDateTime fromToLong = from.atZone(ZoneId.systemDefault());
        ZonedDateTime toToLong = to.atZone(ZoneId.systemDefault());
        long millisFrom = fromToLong.toInstant().toEpochMilli();
        long millisTo = toToLong.toInstant().toEpochMilli();

        long rezMin = (millisTo - millisFrom)/60000;
        long tmp = 0;
        int sh = 90;
        System.out.println("Это может занять некоторое время, осталось:");

        LocalDateTime tempTime = from.plusMinutes(1);
        while ( !tempTime.isAfter(to) ) {
            if (tmp < rezMin/10) {
                tmp++;
            }
            else {
                tmp = 0;
                System.out.print(sh+"% ");
                sh-=10;
            }

            LinkedHashSet<Task> tempSet = new LinkedHashSet<>();
            for (int i = 0; i < linkedTaskList.size(); i++) {
                if (linkedTaskList.getTask(i).nextTimeAfter(tempTime.minusMinutes(1)) != null)
                if (tempTime.isEqual(linkedTaskList.getTask(i).nextTimeAfter(tempTime.minusMinutes(1))) ) {
                    tempSet.add(linkedTaskList.getTask(i));
                }
            }
            if (tempSet.size()!=0) {
                treeMap.put(tempTime, tempSet);
            }
            tempTime = tempTime.plusMinutes(1);
        }
        System.out.println();
        return treeMap;

    }

}