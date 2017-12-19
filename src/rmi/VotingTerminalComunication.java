package rmi;

import javax.xml.bind.SchemaOutputResolver;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;


public class VotingTerminalComunication implements Runnable {
    Socket socket;
    PrintWriter out;
    BufferedReader input;
    Scanner scanner;
    String ip;
    int port;

    VotingTerminalComunication(Socket socket,BufferedReader input,PrintWriter out,String ip,int port){
        this.socket = socket;
        this.input = input;
        this.out = out;
        this.ip = ip;
        this.port = port;
        new Thread(this,"").start();
    }





    @Override
    public void run(){


        String resposta = null;
        System.out.println("Connected to Server");
        System.out.println("Voting terminal [" + this.socket.getLocalPort() + "]");

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }




        while (resposta != ("type|status;logged|off")){

            try {
                resposta = input.readLine();
            }catch (IOException e){
                e.printStackTrace();
            }
            // Is recives a "type|login;username|%%%%%%%"

            if (resposta.split(";")[0].split(Pattern.quote("|"))[1].equals("login")){
                resposta = initialMenu(resposta,out);

            }
            else if (resposta.split(";")[0].split(Pattern.quote("|"))[1].equals("voted")){
                System.out.println("you voted");
                resposta = "type|resetLogin;" + resposta.split(";")[1];
            }


            out.println(resposta);

        }

    }


    public String initialMenu(String resposta,PrintWriter out){
        int x = 120; // wait 2 min at most
        String aux = null;
        String op = "fds";

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                System.out.print("Password: ");
                long startTime = System.currentTimeMillis();
                while ((System.currentTimeMillis() - startTime) < x * 1000 && !in.ready()){
                }

                if (!in.ready()) {
                    System.out.println("\nUser disconected, took over 2 min");
                    return "type|error;status|noelection";
                }

                aux = (resposta);
                op = resposta + ";password|" + in.readLine();
                out.println(op);
                // It sends a "type|login;username|%%%%%%%;password|%%%%%%%%"
                try {
                    resposta = input.readLine();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if (resposta.equals("UsernotFound")){
                    resposta = aux;
                }
                else break;
            }
            /*if (!in.ready()){
                return "type|error;status|noelection";
            }*/
        }catch (IOException e){
            e.printStackTrace();
        }
        //Se nao exite elementos vamos ter: type|item_list;item_count|0
        resposta = votingMenu(resposta,aux,out);

        return resposta;
    }


    public String votingMenu(String resposta,String aux,PrintWriter out){
        Scanner scanner = new Scanner(System.in);
        int op;
        System.out.println("\n\n|Menu|\n\n");
        System.out.println("1 - Vote in my available elctions");
        System.out.println("2 - Exit");
        System.out.print("\nOption:");
        op = scanner.nextInt();

        if (op == 1){
            return vote(resposta,aux,out);
        }
        else {
            System.out.println("|User logged off|");
            return "type|logoff";
        }
    }


    public String vote(String resposta,String aux,PrintWriter out){
        int nitems;
        int electionop;
        Scanner sc = new Scanner(System.in);
        nitems = Integer.parseInt(resposta.split(";")[1].split(Pattern.quote("|"))[1]);
        if (nitems == 0){
            System.out.println("||No Elections found||, please direct to the Voting Table");
            resposta = "type|error;status|noelection";
        }
        else {
            //resposta = type|item_list;item_count|%%%;item_name|%%%%%%%
            System.out.println("Elections:");
            for (int i = 0; i < Integer.parseInt(resposta.split(";")[1].split(Pattern.quote("|"))[1]); i++){
                System.out.println(i+1 + " - " + resposta.split(";")[i+2].split(Pattern.quote("|"))[1]);
            }
            System.out.print("\n\nChose the election you want to vote in: ");
            electionop = sc.nextInt();
            aux = "type|vote;" + aux.split(";")[1] + ";election|" + resposta.split(";")[(electionop-1)+2].split(Pattern.quote("|"))[1];
            resposta = "type|candidates;" + aux.split(";")[1] + ";election|" + resposta.split(";")[(electionop-1)+2].split(Pattern.quote("|"))[1];
            out.println(resposta);
            try {
                resposta = input.readLine();
            }catch (IOException e){
                e.printStackTrace();
            }
            for (int i = 0; i < Integer.parseInt(resposta.split(";")[1].split(Pattern.quote("|"))[1]); i++){
                System.out.println(i+1 + " - " + resposta.split(";")[i+2].split(Pattern.quote("|"))[1]);
            }
            System.out.print("\n\nChose the list you want to vote in: ");
            electionop = sc.nextInt();
            resposta = aux + ";candidate|" + resposta.split(";")[(electionop-1)+2].split(Pattern.quote("|"))[1];
        }

        return resposta;
    }






}