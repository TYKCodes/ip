package duke;

import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Manages the file where all the tasks are read or written into
 */
public class Storage {

    protected Path outputFile;

    /**
     * A constructor for the Storage class
     *
     * @param f The path of the output file to contain the list of tasks
     */
    public Storage(Path f) {
        this.outputFile = f;
    }

    /**
     * Updates or refreshes the list of tasks and synchronises it to the output file
     *
     * @param taskList The list of tasks recorded so far
     * @throws IOException If the file cannot be written into
     */
    public void refreshList(List<Task> taskList) throws IOException{
        BufferedWriter writer = Files.newBufferedWriter(this.outputFile);
        writer.write("");
        writer.flush();
        for (Task curTask : taskList) {
            if (curTask instanceof Event) {
                writer.write("duke.Event | " + curTask.getStatusNumber() + " | "
                        + curTask.getDescription() + " | "
                        + ((Event) curTask).getDatetime() + "\n");
            } else if (curTask instanceof Todo) {
                writer.write("duke.Todo | " + curTask.getStatusNumber() + " | "
                        + curTask.getDescription() + "\n");
            } else if (curTask instanceof Deadline) {
                writer.write("Deadline | " + curTask.getStatusNumber() + " | "
                        + curTask.getDescription()
                        + " | " + ((Deadline) curTask).getDatetime() + "\n");
            }
        }
        writer.close();
    }

    /**
     * Reads in all the tasks recorded from the output file
     *
     * @return The list of tasks
     * @throws IOException If the output file cannot be read
     */
    public List<Task> loadTasks() throws IOException {
        List<Task> inputTasks = new ArrayList<>();
        BufferedReader reader = Files.newBufferedReader(this.outputFile);
        String taskLine = reader.readLine();
        while (taskLine != null) {
            String[] taskDetails = taskLine.split("\\s\\|\\s");
            switch (taskDetails[0]) {
            case "duke.Event":
                Task loadEvent = new Event(taskDetails[2], taskDetails[3]);
                if (taskDetails[1].equals("0")) {
                    loadEvent.markAsUndone();
                } else {
                    loadEvent.markAsDone();
                }
                inputTasks.add(loadEvent);
                taskLine = reader.readLine();
                break;
            case "Deadline":
                Task loadDeadline = new Deadline(taskDetails[2], taskDetails[3]);
                if (taskDetails[1].equals("0")) {
                    loadDeadline.markAsUndone();
                } else {
                    loadDeadline.markAsDone();
                }
                inputTasks.add(loadDeadline);
                taskLine = reader.readLine();
                break;
            case "duke.Todo":
                Task loadTodo = new Todo(taskDetails[2]);
                if (taskDetails[1].equals("0")) {
                    loadTodo.markAsUndone();
                } else {
                    loadTodo.markAsDone();
                }
                inputTasks.add(loadTodo);
                taskLine = reader.readLine();
                break;
            }
        }
        return inputTasks;
    }
}
