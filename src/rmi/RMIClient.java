package rmi;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.StringJoiner;

public class RMIClient {
    String naming;
    boolean isConnected;

    public RMIClient(String naming){
            this.naming = naming;
    }



    public String getNaming() {
        return naming;
    }

    public void setNaming(String naming) {
        this.naming = naming;
    }


    public String vote(String username, String election, String candidate) throws MalformedURLException,RemoteException,NotBoundException{
        ServerInterface ci = (ServerInterface) Naming.lookup(naming);
        Election electiono = ci.searchElection(election);
        Person person = ci.searchUser(username);
        ArrayList<Candidate> candidates = ci.getCandidates(electiono);
        if(candidate.equals("nulo")) {
            for (Candidate candi : candidates) {
                if (candi.getName().equals("nulo")) {
                    Vote vote = new Vote(person.getDepartment(), candi, electiono);
                    ci.registerVote(electiono, vote, person);
                    return "type|voted;username|" + username;
                }
            }
        }
        else if(candidate.equals("branco")) {
            for (Candidate candi : candidates) {
                if (candi.getName().equals("branco")) {
                    Vote vote = new Vote(person.getDepartment(), candi, electiono);
                    ci.registerVote(electiono, vote, person);
                    return "type|voted;username|" + username;
                }
            }
        }
        else{
            Vote vote = new Vote(person.getDepartment(),ci.getCandidateByName(candidate,electiono), electiono);
            ci.registerVote(ci.searchElection(election),vote,person);
            return "type|voted;username|" + username;
        }
        return null;
    }

    public String getCandidates(String election) throws MalformedURLException,RemoteException,NotBoundException{
        ServerInterface ci = (ServerInterface) Naming.lookup(naming);
        Election electiono = ci.searchElection(election);
        ArrayList<Candidate> candidates = ci.getCandidates(electiono);
        String cadidates = "type|item_list;item_count|" + candidates.size();

        for (Candidate candidate : candidates){
            cadidates += ";" + "item_name|" + candidate.getName();
        }

        return cadidates;
    }

    public ArrayList<Election> getElections() throws MalformedURLException, RemoteException, NotBoundException, ParseException {
        ServerInterface ci = (ServerInterface) Naming.lookup(naming);
        ArrayList<Election> elections = ci.getElectionsNow();
        return elections;
    }

    public ArrayList<Election> getElectionsByUser(Person user) throws MalformedURLException, RemoteException, NotBoundException, ParseException {
        ServerInterface ci = (ServerInterface) Naming.lookup(naming);
        System.out.println("tag:"+user.tag);
        System.out.println("dep:"+user.department);
        ArrayList<Election> elections = ci.getElectionsByUserNow(user.tag,user.department);
        return elections;
    }


    public boolean passwordChecks(String username,String password) throws MalformedURLException,RemoteException,NotBoundException{
        ServerInterface ci = (ServerInterface) Naming.lookup(naming);
        boolean userchecks = ci.checkPassword(username,password);

        return userchecks;
    }

    public String numberElections(String username) throws  MalformedURLException,RemoteException, NotBoundException{
        int counter = 0;
        ServerInterface ci = (ServerInterface) Naming.lookup(naming);
        Person user = ci.searchUser(username);
        ArrayList<Election> elections = null;
        try {
            elections = ci.getElectionsByUser(user.tag,user.department);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(elections.size());
    }

    public boolean checkUser(Election election,String id_user,String password) throws MalformedURLException,RemoteException,NotBoundException{
        ServerInterface ci = (ServerInterface) Naming.lookup(naming);
        Person user = ci.searchUser(id_user);
        if (user == null){
            return false;
        }

        return ci.loginVoter(election,user,password);
    }

    public boolean sheachUser(String userId)throws MalformedURLException,RemoteException,NotBoundException{
        ServerInterface ci = (ServerInterface) Naming.lookup(naming);
        Person user = ci.searchUser(userId);
        if (user==null){
            return false;
        }
        return true;
    }

    public Person searchUser(String id){
        ServerInterface ci = null;
        try {
            ci = (ServerInterface) Naming.lookup(naming);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            return ci.searchUser(id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int[] startTable(String id) throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException {
        ServerInterface ci = (ServerInterface) Naming.lookup(naming);
        String my_ip = InetAddress.getLocalHost().toString();
        int[] x = ci.registerTable(my_ip,id);
        return x;
    }

}
