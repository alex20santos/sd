package rmi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.SplittableRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class VotingTableServer implements Runnable {
    ServerSocket serverSocket;
    Socket socket;
    boolean isConnected;
    String backup_naming = "//127.0.0.1:7000/server";
    String main_naming = "//127.0.0.1:7000/server";
    String main_ip = "127.0.0.1";
    String backup_ip = "127.0.0.1";
    Lock lock = new ReentrantLock();
    String id;
    int [] ports;

    VotingTableServer(ServerSocket serverSocket, Socket socket, int [] ports, boolean isConnected, String id,Lock lock){
        this.serverSocket = serverSocket;
        this.socket = socket;
        this.ports = ports;
        this.isConnected = isConnected;
        this.id = id;
        this.lock = lock;
        new Thread(this,"").start();
    }

    @Override
    public void run(){
        System.out.println("Client Connected");

        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        PrintWriter out = null;

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String answer = null;


        while (true){

            lock.lock();
            out.println(voterID(answer, out, input));
            lock.unlock();


            while (true) {

                try {
                    answer = input.readLine();
                } catch (IOException e) {
                    System.out.println("Cliente desconectou-se!");
                    break;
                }
                if (answer != null) {
                    if (answer.equals("type|error;status|noelection")) {
                        System.out.println("Cliente desconectou-se!");
                        break;
                    }
                    else if (answer.equals("type|logoff")) {
                        break;
                    } else {
                        answer = Interpreta(answer, out);
                    }
                }
                else {
                    System.out.println("Cliente desconectou-se!");
                    answer = "Cliente desconectou-se!";
                    break;
                }
                if (answer != null)
                    out.println(answer);
            }
            if (answer.equals("Cliente desconectou-se!")){
                break;
            }

        }

    }


    public String voterID(String answer,PrintWriter out,BufferedReader in){
        /*if(!isConnected){
            System.out.println("BACK");
            String aux = backup_naming;
            backup_naming = main_naming;
            main_naming = aux;
            RMIClient client = new RMIClient(main_naming);
            this.ports = client.startTable(id);


        }*/
        String voterID = null;
        RMIClient rmiClient = new RMIClient(main_naming);
        boolean voter = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("||[" + this.socket.getPort() + "] - " + "Voter ID||");
        voterID = scanner.nextLine();
        try {
            voter = rmiClient.sheachUser(voterID);
        }catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!voter){
            System.out.println("Voter does not exist");
            return voterID(answer,out,in);
        }

        answer = "type|login;username|" + voterID;
        System.out.println("Sent user to terminal - " + "[" + this.socket.getPort() + "]");
        return answer;
    }

    public String Interpreta(String answer,PrintWriter out){
        String [] args;
        String username = null;
        String password = null;
        args = answer.split(";");

        if (args[0].split(Pattern.quote("|"))[1].equals("login")){
            username = args[1].split(Pattern.quote("|"))[1];
            password = args[2].split(Pattern.quote("|"))[1];
            try {
                if (checksPassword(username,password)){
                    System.out.println("User - " + username + " and password match");
                    answer = AvailableElections(answer);
                }
                else {
                    System.out.println("[" + username + "]" + " - User and password dont match");
                    answer = "UsernotFound";
                }
            }catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NotBoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else if (args[0].split(Pattern.quote("|"))[1].equals("candidates")){
            //System.out.println(answer); type|vote;username|9090;election|DEI
            try {
                answer = AvailableCandidates(answer.split(";")[2].split(Pattern.quote("|"))[1]);
            }catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NotBoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else if (args[0].split(Pattern.quote("|"))[1].equals("vote")){
            try {
                answer = voteList(answer);
            }catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NotBoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else if (args[0].split(Pattern.quote("|"))[1].equals("resetLogin")){
            answer = "type|login;" + args[1];
        }

        return answer;
    }

    public String voteList(String resposta) throws MalformedURLException,RemoteException,NotBoundException{
        RMIClient rmiClient = new RMIClient(main_naming);
        System.out.println(resposta.split(";")[1].split(Pattern.quote("|"))[1] + resposta.split(";")[2].split(Pattern.quote("|"))[1] + resposta.split(";")[3].split(Pattern.quote("|"))[1]);
        return rmiClient.vote(resposta.split(";")[1].split(Pattern.quote("|"))[1],resposta.split(";")[2].split(Pattern.quote("|"))[1],resposta.split(";")[3].split(Pattern.quote("|"))[1]);
    }


    public boolean checksPassword(String username,String password) throws MalformedURLException,RemoteException,NotBoundException{
        RMIClient rmiClient = new RMIClient(main_naming);
        return rmiClient.passwordChecks(username,password);
    }

    public String AvailableCandidates(String answer) throws MalformedURLException, RemoteException, NotBoundException{
        System.out.println("Getting Candidates");
        RMIClient rmiClient = new RMIClient(main_naming);

        return rmiClient.getCandidates(answer);
    }



    //TODO alterar isto para ler da bd
    public String AvailableElections(String answer) throws MalformedURLException, RemoteException, NotBoundException, ParseException {
        System.out.println("Getting available Elections");
        boolean userVote;
        //type|login;username|%%%%%%%;password|%%%%%%%%"
        String [] args = answer.split(";");
        String [] username = args[1].split(Pattern.quote("|"));
        //[username,%%%%%%%%]
        RMIClient rmiClient = new RMIClient(main_naming);
        Person p = rmiClient.searchUser(username[1]);
        ArrayList<Election> elections = rmiClient.getElectionsByUser(p);
        answer = "type|item_list;item_count|" + elections.size();
        System.out.println(elections.size());
        for (int i = 0;i < elections.size();i++){
            answer += ";" + "item_name|" + elections.get(i).getName();
            System.out.println(answer);
        }
        return answer;
    }



}
