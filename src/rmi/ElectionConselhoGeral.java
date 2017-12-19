package rmi;
import java.io.Serializable;
import java.util.ArrayList;

public class ElectionConselhoGeral extends Election implements Serializable{

    //candidates from super class are considered student_candidates


    public ElectionConselhoGeral(String name,String description,Date date,int blank_votes, int null_votes, int id) {
        super(name, description, date,blank_votes,null_votes,id);

    }




    @Override
    public String toString() {
        return "Eleicao Conselho Geral" +
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
