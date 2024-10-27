import java.sql.Date;

public class Workout {
    private int id;
    private String type;
    private int duration;
    private int caloriesBurned;
    private Date date;

    public Workout(String type, int duration, int caloriesBurned, Date date) {
        this.type = type;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public int getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(int caloriesBurned) { this.caloriesBurned = caloriesBurned; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", type='" + type +
                ", duration=" + duration +
                ", caloriesBurned=" + caloriesBurned +
                ", date=" + date +
                '}';
    }
}