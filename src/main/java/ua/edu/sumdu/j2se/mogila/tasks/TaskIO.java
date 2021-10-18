package ua.edu.sumdu.j2se.mogila.tasks;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.mogila.tasks.model.Model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskIO implements Serializable {
    final static Logger logger = Logger.getLogger(Model.class);
    /**
     * записує задачі із списку у потік у бінарному форматі, описаному нижче
     *
     * @param tasks
     * @param out
     */
    public static void write(AbstractTaskList tasks, OutputStream out) throws IOException {

        try(DataOutputStream dataOutputStream = new DataOutputStream(out)) {
            dataOutputStream.writeInt(tasks.size());
            for (int i = 0; i < tasks.size(); i++) {
                dataOutputStream.writeInt(tasks.getTask(i).getTitle().length());
                dataOutputStream.writeUTF(tasks.getTask(i).getTitle());
                dataOutputStream.writeBoolean(tasks.getTask(i).isActive());
                dataOutputStream.writeInt(tasks.getTask(i).getRepeatInterval());
                if (tasks.getTask(i).isRepeated()) {
                    dataOutputStream.writeInt(tasks.getTask(i).getStartTime().getYear());
                    dataOutputStream.writeInt(tasks.getTask(i).getStartTime().getMonthValue());
                    dataOutputStream.writeInt(tasks.getTask(i).getStartTime().getDayOfMonth());
                    dataOutputStream.writeInt(tasks.getTask(i).getStartTime().getHour());
                    dataOutputStream.writeInt(tasks.getTask(i).getStartTime().getMinute());
                    dataOutputStream.writeInt(tasks.getTask(i).getStartTime().getSecond());
                    dataOutputStream.writeInt(tasks.getTask(i).getStartTime().getNano());

                    dataOutputStream.writeInt(tasks.getTask(i).getEndTime().getYear());
                    dataOutputStream.writeInt(tasks.getTask(i).getEndTime().getMonthValue());
                    dataOutputStream.writeInt(tasks.getTask(i).getEndTime().getDayOfMonth());
                    dataOutputStream.writeInt(tasks.getTask(i).getEndTime().getHour());
                    dataOutputStream.writeInt(tasks.getTask(i).getEndTime().getMinute());
                    dataOutputStream.writeInt(tasks.getTask(i).getEndTime().getSecond());
                    dataOutputStream.writeInt(tasks.getTask(i).getEndTime().getNano());
                } else {
                    dataOutputStream.writeInt(tasks.getTask(i).getTime().getYear());
                    dataOutputStream.writeInt(tasks.getTask(i).getTime().getMonthValue());
                    dataOutputStream.writeInt(tasks.getTask(i).getTime().getDayOfMonth());
                    dataOutputStream.writeInt(tasks.getTask(i).getTime().getHour());
                    dataOutputStream.writeInt(tasks.getTask(i).getTime().getMinute());
                    dataOutputStream.writeInt(tasks.getTask(i).getTime().getSecond());
                    dataOutputStream.writeInt(tasks.getTask(i).getTime().getNano());
                }
            }

        }
        catch (EOFException e) {
            logger.error("EOFException from save");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * зчитує задачі із потоку у даний список задач
     *
     * @param tasks
     * @param in
     */
    public static void read(AbstractTaskList tasks, InputStream in) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(in)) {
            int b = dataInputStream.readInt();
            for (int i = 0; i < b; i++) {
                Task task = new Task();
                dataInputStream.readInt();
                task.setTitle(dataInputStream.readUTF());
                task.setActive(dataInputStream.readBoolean());
                task.setInterval(dataInputStream.readInt());
                if (task.isRepeated()) {
                    task.setStart(LocalDateTime.of(dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt()));
                    task.setEnd(LocalDateTime.of(dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt()));
                } else {
                    task.setTime(LocalDateTime.of(dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readInt()));
                }
                tasks.add(task);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * записує задачі із списку у файл
     *
     * @param tasks
     * @param file
     */
    public static void writeBinary(AbstractTaskList tasks, File file) throws IOException {
        try(FileOutputStream outputStream = new FileOutputStream(file)) {
        write(tasks,outputStream);
        }
    }

    /**
     * зчитує задачі із файлу у список задач
     * @param tasks
     * @param file
     */
    public static void readBinary(AbstractTaskList tasks, File file) throws IOException {
        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            read(tasks,fileInputStream);
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * записує задачі зі списку у потік в форматі JSON
     *
     * @param tasks
     * @param out
     */
    public static void write(AbstractTaskList tasks, Writer out) throws IOException {

        try(JsonWriter jsonWriter = new JsonWriter(out)) {
            jsonWriter.beginObject();
            for (int i = 0; i < tasks.size(); i++) {

                jsonWriter.name("Title").value(tasks.getTask(i).getTitle());
                jsonWriter.name("RepeatInterval").value(tasks.getTask(i).getRepeatInterval());
                jsonWriter.name("Active").value(tasks.getTask(i).isActive());

                if(tasks.getTask(i).isRepeated()) {
                    jsonWriter.name("StartTime").value(tasks.getTask(i).getStartTime().toString());
                    jsonWriter.name("EndTime").value(tasks.getTask(i).getEndTime().toString());
                }
                else {
                    jsonWriter.name("Time").value(tasks.getTask(i).getTime().toString());
                }

            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * зчитує задачі із потоку у список
     * @param tasks
     * @param in
     */
    public static void read(AbstractTaskList tasks, Reader in) throws IOException {
        JsonReader jsonReader = new JsonReader(in);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        jsonReader.beginObject();
        String time = "";
        String title = "";
        String startTime = "";
        String endTime = "";
        String name = "";
        int repeatInterval = 0;
        boolean active = false;
        while (jsonReader.hasNext()) {

            name = jsonReader.nextName();

            if ("Title".equals(name)) {
                title = jsonReader.nextString();
                name = jsonReader.nextName();
            }
            if ("RepeatInterval".equals(name)) {
                repeatInterval = jsonReader.nextInt();
                name = jsonReader.nextName();
            }
            if ("Active".equals(name)) {
                active = jsonReader.nextBoolean();
                name = jsonReader.nextName();
            }
            if (repeatInterval != 0) {

                if ("StartTime".equals(name)) {
                    startTime = jsonReader.nextString();
                    name = jsonReader.nextName();
                }
                if ("EndTime".equals(name)) {
                    endTime = jsonReader.nextString();
                }
                Task task = new Task(title, LocalDateTime.parse(startTime, dateTimeFormatter), LocalDateTime.parse(endTime, dateTimeFormatter), repeatInterval);
                task.setActive(active);
                tasks.add(task);

            } else {
                if ("Time".equals(name)) {
                    time = jsonReader.nextString();
                }
                Task task = new Task(title, LocalDateTime.parse(time, dateTimeFormatter));
                task.setActive(active);
                tasks.add(task);
            }
        }
        jsonReader.endObject();
        jsonReader.close();
    }

    /**
     * записує задачі у файл у форматі JSON
     * @param tasks
     * @param file
     * @throws IOException
     */
    public static void writeText(AbstractTaskList tasks, File file) throws IOException {
        Gson gson = new Gson();
        try(FileWriter fileWriter = new FileWriter(file)) {
            gson.toJson(tasks.size(), fileWriter);
            for (int i = 0; i < tasks.size(); i++) {
                gson.toJson(tasks.getTask(i), fileWriter);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * зчитує задачі із файлу
     * @param tasks
     * @param file
     */
    public static void readText(AbstractTaskList tasks, File file) throws IOException {
        Gson gson = new Gson();
        try (FileReader fileReader = new FileReader(file)){
            int size = gson.fromJson(fileReader, Integer.class);
            for(int i = 0; i < size; i++) {
                tasks.add(gson.fromJson(fileReader, Task.class));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
