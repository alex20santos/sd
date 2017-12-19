package rmi;
import java.io.Serializable;
import java.util.ArrayList;

public class ElectionDepartment extends Election implements Serializable{
    Department department;

    public ElectionDepartment(String name, String description, Date date, Department department) {
        super(name, description, date);
        this.department = department;
    }
    public ElectionDepartment(String name,String description,Date date,Department department,int blank,int null_votes,int id) {
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
        return "Eleicao Departamento" +
                //"Alunos candidatos: " + candidates +
                //"Professores candidatos: " + teacherCandidates +
                //"\nTrabalhadores candidatos: " + workerCandidates +
                "\n\t\tTitulo: " + name +
                "\n\t\tDescricao: " + description +
                "\n\t\tData: " + date ;
        //", voting_tables=" + voting_tables +
        //"\nNumero de votos:" + numberOfVotes +
    }

}
