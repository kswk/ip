import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Duke {

    private static final int DATE_INDEX = 4;
    private static final int DEADLINE_INDEX = 9;
    private static final int DELETE_INDEX = 7;
    private static final int DONE_INDEX = 5;
    private static final int EVENT_INDEX = 6;
    private static final int TODO_INDEX = 5;

    private static final String BYE = "bye";
    private static final String DEADLINE = "deadline";
    private static final String DELETE = "delete";
    private static final String DONE = "done";
    private static final String EVENT = "event";
    private static final String LIST = "list";
    private static final String TODO = "todo";

    private static int storageCount = 0;
    private static final List<Task> taskStorage = new ArrayList<>(100);

    private static void loadTasks() {
        String currDir = System.getProperty("user.dir");
        Path filePath = Paths.get(currDir, "data", "tasks.csv");
        File file = filePath.toFile();
        if (!file.exists()) {
            return;
        }
        try {
            BufferedReader br = Files.newBufferedReader(filePath);
            br.readLine();
            String line = br.readLine();
            while (line != null) {
                String[] taskEntry = line.split(",");
                String type = taskEntry[0];
                String done = taskEntry[1];
                String description = taskEntry[2];
                if (type.equals(TODO)) {
                    Task task = new Todo(description, done.equals("1"));
                    taskStorage.add(storageCount, task);
                }
                if (type.equals(DEADLINE)) {
                    String date = taskEntry[3];
                    Task task = new Deadline(description, done.equals("1"), date);
                    taskStorage.add(storageCount, task);
                }
                if (type.equals(EVENT)) {
                    String date = taskEntry[3];
                    Task task = new Event(description, done.equals("1"), date);
                    taskStorage.add(storageCount, task);
                }
                storageCount++;
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void saveTasks() {
        String currDir = System.getProperty("user.dir");
        Path folderPath = Paths.get(currDir, "data");
        try {
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            Path filePath = Paths.get(currDir, "data", "tasks.csv");
            File file = filePath.toFile();
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Task,Done?,Description,Date");
            for (Task task: taskStorage) {
                bw.write(task.write());
            }
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void botStart() {
        loadTasks();
        Scanner sc = new Scanner(System.in);
        System.out.println("=========================================="
                + "\nHi, my name is Duke."
                + "\nWhat can I do for you today?"
                + "\n==========================================");
        while (true) {
            try {
                String command = sc.nextLine().trim();
                String[] commandWordArray = command.split(" ");
                if (command.equals(BYE)) {
                    saveTasks();
                    System.out.println("List saved to hard disk at data/tasks.csv");
                    System.out.println("Thanks for chatting with me, see you soon!"
                            + "\n==========================================");
                    break;
                } else if (command.equals(LIST)) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < storageCount; i++) {
                        System.out.println(i + 1 + "." + taskStorage.get(i));
                    }
                } else if (commandWordArray[0].equals(DONE)) {
                    if (command.substring(DONE_INDEX - 1).isBlank()) {
                        throw new DukeEmptyArgumentException(DONE);
                    }
                    try {
                        int taskNumber = Integer.parseInt(command.substring(DONE_INDEX));
                        taskStorage.get(taskNumber - 1).markAsDone();
                        System.out.println("I have marked this task as done: "
                                + "\n" + taskStorage.get(taskNumber - 1));
                    } catch (IndexOutOfBoundsException e) {
                        throw new DukeInvalidTaskException();
                    } catch (NumberFormatException e) {
                        throw new DukeInvalidArgumentException(DONE);
                    }
                } else if (commandWordArray[0].equals(DELETE)) {
                    if (command.substring(DELETE_INDEX - 1).isBlank()) {
                        throw new DukeEmptyArgumentException(DELETE);
                    }
                    try {
                        int taskNumber = Integer.parseInt(command.substring(DELETE_INDEX));
                        System.out.println("I have deleted this task: "
                                + "\n" + taskStorage.get(taskNumber - 1));
                        taskStorage.remove(taskNumber - 1);
                        storageCount--;
                    } catch (IndexOutOfBoundsException e) {
                        throw new DukeInvalidTaskException();
                    } catch (NumberFormatException e) {
                        throw new DukeInvalidArgumentException(DELETE);
                    }
                } else {
                    Task task;
                    if (commandWordArray[0].equals(TODO)) {
                        if (command.substring(TODO_INDEX - 1).isBlank()) {
                            throw new DukeEmptyDescriptionException(TODO);
                        }
                        task = new Todo(command.substring(TODO_INDEX));
                    } else {
                        if (commandWordArray[0].equals(DEADLINE)) {
                            if (command.substring(DEADLINE_INDEX - 1).isBlank()) {
                                throw new DukeEmptyDescriptionException(DEADLINE);
                            } else {
                                try {
                                    String date = command.substring(command.indexOf("/") + DATE_INDEX);
                                    task = new Deadline(
                                            command.substring(
                                                    DEADLINE_INDEX, command.indexOf("/") - 1), date);
                                } catch (StringIndexOutOfBoundsException e) {
                                    throw new DukeInvalidDateException(DEADLINE);
                                }
                            }
                        } else if (commandWordArray[0].equals(EVENT)) {
                            if (command.substring(EVENT_INDEX - 1).isBlank()) {
                                throw new DukeEmptyDescriptionException(EVENT);
                            } else {
                                try {
                                    String date = command.substring(command.indexOf("/") + DATE_INDEX);
                                    task = new Event(
                                            command.substring(
                                                    EVENT_INDEX, command.indexOf("/") - 1), date);
                                } catch (StringIndexOutOfBoundsException e) {
                                    throw new DukeInvalidDateException(EVENT);
                                }
                            }
                        } else {
                            throw new DukeInvalidCommandException();
                        }
                    }
                    taskStorage.add(storageCount, task);
                    System.out.println(">" + "added: " + task + "<");
                    storageCount++;
                    System.out.println("You now have " + storageCount + " task(s) in your list.");
                }
            } catch (DukeException e) {
                System.out.println(e.getMessage());
            }
        }
        sc.close();
    }

    public static void main(String[] args) {
        Duke.botStart();
    }
}
