package rmi;
import java.io.Serializable;
import java.util.ArrayList;

public class Candidate implements Serializable{
    public Election election;
    public String name;
    ArrayList<Person> list = new ArrayList<>();
    int id;

    public Candidate(Election election, String name, ArrayList<Person> list,int id) {
        this.election = election;
        this.name = name;
        this.list = list;
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public ArrayList<Person> getList() {
        return list;
    }

    public void setList(ArrayList<Person> list) {
        this.list = list;
    }

}
