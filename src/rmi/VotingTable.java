package rmi;
import java.io.Serializable;
import java.util.ArrayList;

public class VotingTable implements Serializable{
    // VOTING TABLE/tcp server and rmi client
    Department department;
    boolean up;
    int id;
    ArrayList<Person> members = new ArrayList<>();
    Person delegate;

    public ArrayList<Person> getMembers() {
        return members;
    }

    public Person getDelegate() {
        return delegate;
    }

    public VotingTable(Department department, int id) {
        this.department = department;
    }

    public VotingTable() {}


    public void setId(int id) {
        this.id = id;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }




    public boolean isUp() {
        return up;
    }


    public void setUp(boolean up) {
        this.up = up;
    }

    @Override
    public String toString() {
        return "Voting Table" +
                "\n\tDepartmento:" + department +"\n";
    }
}
