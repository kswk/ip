package duke;

import duke.command.*;
import duke.exception.*;

public class Parser {

    private static final String BYE = "bye";
    private static final String DEADLINE = "deadline";
    private static final String DELETE = "delete";
    private static final String DONE = "done";
    private static final String EVENT = "event";
    private static final String LIST = "list";
    private static final String TODO = "todo";

    public static Command parse(String input) throws DukeEmptyArgumentException {
        String command = input.strip();
        String[] commandWordArray = command.split(" ");
        if (command.equals(BYE)) {
            return new ByeCommand(command, true);
        }
        if (command.equals(LIST)) {
            return new ListCommand(command);
        }
        if (commandWordArray[0].equals(DONE)) {
            if (command.substring(commandWordArray[0].length()).isBlank()) {
                throw new DukeEmptyArgumentException(DONE);
            }
            return new DoneCommand(commandWordArray[0], commandWordArray[1]);
        }
        if (commandWordArray[0].equals(DELETE)) {
            if (command.substring(commandWordArray[0].length()).isBlank()) {
                throw new DukeEmptyArgumentException(DELETE);
            }
            return new DeleteCommand(commandWordArray[0], commandWordArray[1]);
        }
        if (commandWordArray[0].equals(TODO) || commandWordArray[0].equals(DEADLINE)
                || commandWordArray[0].equals(EVENT)) {
            return new AddCommand(commandWordArray[0], command.substring(commandWordArray[0].length() + 1));
        }
        return new UnknownCommand(command);
    }
}
