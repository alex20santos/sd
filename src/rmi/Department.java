package rmi;
import java.io.Serializable;

public class Department implements Serializable{
    public String name;
    public int id;

    public Department(String name, int i) {
        this.name = name;
        this.id = i;

    }



    public String getName() {
        return name;
    }
    
    public int getId() {
        return id;
    }


    public void setName(String name) {

        this.name = name;
    }

    public Department(String name) {
        this.name = name;

    }

    public Department(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                '}';
    }
}
