package duke.command;

import duke.*;

/**
 * Represents command to show list of tasks.
 */
public class ListCommand extends Command {

    /**
     * Class constructor.
     * @param command String parsed by Parser object.
     */
    public ListCommand(String command) {
        super(command);
    }

    @Override
    public void execute(Storage storage, TaskList tasks, Ui ui) {
        ui.printList(tasks);
        for (int i = 0; i < tasks.getSize(); i++) {
            System.out.println(i + 1 + "." + tasks.getTask(i));
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ListCommand;
    }
}
