package rmi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class VotingTerminal {
    Socket socket;
    BufferedReader input;
    PrintWriter out;

    VotingTerminal(String ip,int port){
        try {
            socket = new Socket(ip,port);
        }catch (UnknownHostException e){
            System.out.println("Unknown Error");
        }catch (IOException e){
            System.out.println("Server Unvilable");
        }

        try {
            out = new PrintWriter(socket.getOutputStream(),true);
        }catch (IOException e){
            e.printStackTrace();
        }

        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e1){
            e1.printStackTrace();
        }

        new VotingTerminalComunication(socket,input,out,ip,port);
    }


    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);

        System.out.print("IP: ");
        String ip = scanner.nextLine();
        System.out.print("Port: ");
        int port = scanner.nextInt();

        new  VotingTerminal(ip,port);
    }

}
