package rmi;
import java.io.Serializable;

public class Vote implements Serializable{
    Department place_of_vote;
    Candidate choice;
    Election election;


    public Vote(Department place_of_vote, Candidate choice, Election election) {
        this.place_of_vote = place_of_vote;
        this.choice = choice;
        this.election = election;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public Department getPlace_of_vote() {
        return place_of_vote;
    }

    public void setPlace_of_vote(Department place_of_vote) {
        this.place_of_vote = place_of_vote;
    }

    public Candidate getChoice() {
        return choice;
    }

    public void setChoice(Candidate choice) {
        this.choice = choice;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "place_of_vote=" + place_of_vote +
                ", election=" + election +
                '}';
    }
}
