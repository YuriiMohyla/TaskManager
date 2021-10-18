package ua.edu.sumdu.j2se.mogila.tasks.model;

import java.time.format.DateTimeFormatter;

public class Formatter {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static DateTimeFormatter getFormat() {
        return formatter;
    }
}
