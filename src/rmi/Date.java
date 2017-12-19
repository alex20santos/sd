package rmi;
import java.io.Serializable;

public class Date implements Serializable{
    private int day,month,year;
    private Hour starting_hour,ending_hour;

    public Date(int day, int month, int year, Hour starting_hour, Hour ending_hour) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.starting_hour = starting_hour;
        this.ending_hour = ending_hour;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public Hour getStarting_hour() {
        return starting_hour;
    }

    public Hour getEnding_hour() {
        return ending_hour;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setStarting_hour(Hour starting_hour) {
        this.starting_hour = starting_hour;
    }

    public void setEnding_hour(Hour ending_hour) {
        this.ending_hour = ending_hour;
    }




    @Override
    public String toString() {
        return
                day+"/"+month + "/" + year+" " + "Hora inicio:" + starting_hour + "Hora fim:" + ending_hour+" ";
    }
}
