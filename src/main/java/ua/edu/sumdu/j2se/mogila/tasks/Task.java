package ua.edu.sumdu.j2se.mogila.tasks;

import java.io.Serializable;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
/**
 * Клас Task
 * @author Юра Могила
 */
public class Task implements Cloneable, Serializable {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    /** Поле название */
    private String title;
    /** Поле время */
    private LocalDateTime time;
    private LocalDateTime start;
    private LocalDateTime end;

    private int interval;
    private boolean active;

    /**int nextTimeAfter(int current), що повертає час наступного виконання задачі після вказаного часу current,
     * якщо після вказаного часу задача не виконується, то метод має повертати null.*/
    public LocalDateTime nextTimeAfter(LocalDateTime current) {
        if (!active) { return null; }

        LocalDateTime last = lastExecution(start, end, interval);

        if (current.isEqual(start) && !(start.isEqual(end)) ) return start.plusSeconds(interval);

        if (current.isBefore(start)) return start;

        if (current.plusSeconds(interval).isAfter(end) & !(last.isEqual(end) & current.isBefore(end)) & (last.isBefore(current) || current.isEqual(end))) return null;
        if (current.isBefore(end)) {

            LocalDateTime copy = start;

            while (copy.isBefore(end)) {

                if (copy.isAfter(current)) { return copy; }

                copy = copy.plusSeconds(interval);

                if (copy.isEqual(current)) return copy.plusSeconds(interval);
                if (copy.isAfter(current)) return copy;
            }

            if (last.isAfter(current) && (current.isBefore(end) || current.isEqual(end)) && !current.isAfter(end)) return last;
        }

        if (last.plusSeconds(interval).isAfter(end) && current.isBefore(last)) return last;
        if (last.isEqual(end) & current.isBefore(end)) return end;
        if (current.isBefore(time)) { return time; }
       return null;
    }
    /**int getRepeatInterval(), у разі, якщо задача не повторюється метод має повертати 0*/
    public int getRepeatInterval() {
        if (interval == 0) {
            return 0;
        }
        return interval;
    }

    public boolean isRepeated() {
        if (interval == 0) {
            return false;
        }
        else {
            return true;
        }
    }
    /**void setTime(int start, int end, int interval),у разі, якщо
     задача не повторювалася метод має стати такою, що повторюється.*/
    public void setTime(LocalDateTime start, LocalDateTime end, int interval) {
            time = start;    //зделал чтобы сбросить ранее заданое время другом конструктором
            this.start = start;
            this.end = end;
            this.interval = interval;
    }

    public boolean isActive() {
        return active;
    }
/**int getStartTime(), у разі, якщо задача не повторюється метод має повертати час виконання задачі*/
    public LocalDateTime getStartTime() {
        if(interval == 0)
            return time;
            else return start;
    }
/**int getEndTime(), у разі, якщо задача не повторюється метод має повертати час виконання задачі*/
    public LocalDateTime getEndTime() {
        if(interval == 0) {
            return time;
        }
        return end;
    }

    public int getInterval() {
        return interval;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**int getTime(), у разі, якщо задача повторюється метод має повертати час початку повторення*/
    public LocalDateTime getTime() {
        if(interval > 0) {
            return start;
        }
        else
            return time;
    }
/**void setTime(int time), у разі,
 * якщо задача повторювалась, вона має стати такою, що не повторюється.*/
    public void setTime(LocalDateTime time) {
        if(interval != 0) {
            interval = 0;
        }
        this.time = time;
        this.start = time;
        this.end = time;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Task(String title, LocalDateTime start, LocalDateTime end, int interval) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Время окончания меньше времени начала");
        }
        if (interval <= 0) {
            throw new IllegalArgumentException("Интервал должен быть больше 0");
        }
        this.title = title;
        this.start = start;
        this.end = end;
        this.interval = interval;
    }

    public Task(String title, LocalDateTime time) {
        if (time == null) {
            throw new IllegalArgumentException("Время меньше 0");

        }
        this.title = title;
        this.time = time;
        this.start = time;
        this.end = time;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return interval == task.interval &&
                active == task.active &&
                Objects.equals(title, task.title) &&
                Objects.equals(time, task.time) &&
                Objects.equals(start, task.start) &&
                Objects.equals(end, task.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, time, start, end, interval, active);
    }

    @Override
    public String toString() {

        return "Задача {" + " Название: '" + title + '\''
                + ", Время выполнения: " + time
                + ", Время начала выполнения: " + start
                + ", Время окончания выполнения: " + end
                + ", Интервал : " + interval + " секунд"
                + ", Состояние активности: " + active + '}';
    }

    public Task() {
        super();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public LocalDateTime lastExecution (LocalDateTime start, LocalDateTime end, int interval) {
        LocalDateTime temp = start;
        if (temp.isEqual(end)) return temp;
        while (temp.isBefore(end)) {
            temp = temp.plusSeconds(interval);
            if (temp.isEqual(end)) return temp;
        }
        return temp.minusSeconds(interval);
    }

}




