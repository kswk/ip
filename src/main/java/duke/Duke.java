package duke;

import duke.command.Command;
import duke.exception.DukeException;

import java.io.IOException;
import java.util.Scanner;

/**
 * Driver class for chat bot.
 */
public class Duke {

    private final Scanner sc;
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Class constructor.
     */
    public Duke() {
        sc = new Scanner(System.in);
        storage = new Storage();
        tasks = new TaskList();
        ui = new Ui();
    }

    private void start() {
        try {
            storage.loadTasks(tasks);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        ui.printGreeting();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = sc.nextLine();
                Command c = Parser.parse(fullCommand);
                c.execute(storage, tasks, ui);
                isExit = c.isExit();
            } catch (DukeException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Driver method for chat bot.
     * @param args Starts up the chat bot.
     */
    public static void main(String[] args) {
        new Duke().start();
    }
}
