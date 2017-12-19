package rmi;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class connectClient implements Runnable {
    String backup_naming = "//127.0.0.1:7000/server";
    String main_naming = "//127.0.0.1:7000/server";
    String main_ip = "127.0.0.1";
    String backup_ip = "127.0.0.1";
    String id;
    Lock lock = new ReentrantLock();
    boolean isConnected;

    connectClient() {
        new Thread(this, "").start();
    }

    @Override
    public void run() {
        String department;
        System.out.println("Insira o ID da mesa de voto que deseja ativar");
        Scanner scanIn = new Scanner(System.in);
        id = scanIn.nextLine();
        RMIClient c = new RMIClient(main_naming);
        int[] ports = new int[2];
        try {
            ports = c.startTable(id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.isConnected = true;

        System.out.println("Server ON");


        ServerSocket serverTCP = null;
        try {
            serverTCP = new ServerSocket(9091);
        } catch (IOException e) {
			/*
			 *
			 * Fail-Over
			 *
			 * */
        }


        VotingTablePing t = new VotingTablePing("Mike",main_ip,ports[0],ports[1],this);

        while (true) {


            Socket socket = null;


            try {
                socket = serverTCP.accept();

            } catch (IOException e) {
                e.printStackTrace();
            }

/*
            try
            {
                Thread.sleep(5000);//2 seconds
            }
            catch(InterruptedException ie){
                ie.printStackTrace();
            }
*/

            new VotingTableServer(serverTCP,socket,ports,this.isConnected,id,lock);

        }

    }


    public String menuMesadeVoto() throws MalformedURLException,RemoteException,NotBoundException{
        String vottable_id;
        RMIClient rmiClient = new RMIClient(main_naming);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Voting Table Department::");
        vottable_id = scanner.nextLine();
        return "oi";
    }

}

