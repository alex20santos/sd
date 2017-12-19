package rmi;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Election implements Serializable{
    public String name;
    public String description;
    public Date date;
    public int blank_votes, null_votes,id;


    public Election(String name, String description, Date date) {
        this.name = name;
        this.description = description;
        this.date = date;

    }

    public Election(String name, String description, Date date, int blank_votes, int null_votes, int id) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.blank_votes = blank_votes;
        this.null_votes = null_votes;
        this.id = id;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public boolean loginVoter(Person person,String password){
        return person.getPassword().equals(password);
    }




    @Override
    public String toString() {
        return "Election{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date;
    }

}
