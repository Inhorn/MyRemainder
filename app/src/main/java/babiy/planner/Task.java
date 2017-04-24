package babiy.planner;


public class Task {

    private String myTask;
    private String date;
    private long id;
    private String time;
    private String status;

    public Task() {

    }

    public Task(String myTask, String date, String time, String status) {
        this.myTask = myTask;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getTask() {
        return this.myTask;
    }

    public void setTask(String myTask) {
        this.myTask = myTask;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime (String time) {
        this.time = time;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Task task) {
        id = task.getId();
    }
}
