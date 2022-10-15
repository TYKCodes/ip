package duke.commands;

import duke.DukeException;
import duke.task.Task;
import duke.TaskList;
import duke.Storage;

/**
 * A <code>Command</code> class that handles the deleting of a <code>Task</code>
 */
public class DeleteCommand extends Command {
    private final String[] arguments;

    /**
     * A constructor for the <code>DeleteCommand</code> class
     *
     * @param args Arguments from user input
     */
    public DeleteCommand(String[] args) {
        this.arguments = args;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String execute(Storage storage, TaskList tl) throws DukeException, AssertionError {
        if (arguments.length < 2) {
            throw new DukeException("You did not state a task number!");
        } else if (arguments.length > 2) {
            throw new DukeException("You can only state one task number!");
        }

        String response;
        try {
            int taskNum = Integer.parseInt(arguments[1]) - 1;
            assert taskNum > -1 : "The task number should be a valid number (starting from 1)";
            Task deletedTask = tl.deleteTask(taskNum);
            response = "Okay, I have removed this task from the list:\n  "
                    + deletedTask.toString()
                    + "\nNow you have " + tl.getSize() + " tasks in the list.";
            storage.refreshList(tl.getTasks());
        } catch (IndexOutOfBoundsException indexErr) {
            response = "This task number does not exist in the list!";
        } catch (NumberFormatException parseErr) {
            response = "Is that even a number?! Try entering an actual number next time";
        }
        return response;
    }
}
