package duke.command;

import duke.Storage;
import duke.TaskList;
import duke.Ui;
import duke.exception.DukeInvalidCommandException;

/**
 * Represents command that is not recognised by chat bot.
 */
public class UnknownCommand extends Command {

    /**
     * Class constructor.
     * @param command String parsed by Parser object.
     */
    public UnknownCommand(String command) {
        super(command);
    }

    @Override
    public String execute(Storage storage, TaskList tasks, Ui ui)
            throws DukeInvalidCommandException {
        throw new DukeInvalidCommandException();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UnknownCommand;
    }
}
