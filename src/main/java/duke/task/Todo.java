package duke.task;

public class Todo extends Task {

    public Todo(String description) {
        super(description);
    }

    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String write() {
        return "\ntodo," + super.write();
    }

//    public static void main(String[] args) {
//        Task todo = new Todo("read book");
//        System.out.println(todo);
//    }
}
