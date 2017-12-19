package rmi;
import java.util.ArrayList;

public class ElectionNucleo  extends Election {

    public Department department;

    public ElectionNucleo(String name,String description,Date date,Department department,int blank,int null_votes,int id) {
        super(name,description,date,blank,null_votes,id);
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }





    @Override
    public String toString() {
        return "Eleicao Nucleo" +
                "\n\t\tTitulo: " + name +
                "\n\t\tDescricao: " + description +
                "\n\t\tData: " + date ;
        //", voting_tables=" + voting_tables +
        //"\nNumero de votos:" + numberOfVotes +
    }
}
