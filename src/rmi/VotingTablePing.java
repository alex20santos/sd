package rmi;
import java.io.IOException;
import java.net.*;
import java.rmi.NotBoundException;

public class VotingTablePing extends PingServer {
    connectClient v;

    public VotingTablePing(String message, String target_ip, int own_port, int target_port,connectClient v) {
        super(message, target_ip, own_port, target_port);
        this.v = v;
    }

    @Override
    public void run() {
        try {
            int counter = 0;
            this.main_socket = new DatagramSocket(this.own_port);
            System.out.println("Socket Datagram a escuta no porto "+this.own_port);
            main_socket.setSoTimeout(3000);
            while (true) {
                try {
                    Thread.sleep(1000);
                    byte[] buffer;
                    byte[] buffer1 = new byte[1000];
                    buffer = message.getBytes();
                    DatagramPacket to_backup = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(this.target_ip), this.target_port);
                    DatagramPacket from_backup = new DatagramPacket(buffer1, buffer1.length);
                    main_socket.send(to_backup);
                    main_socket.receive(from_backup);
                    counter = 0;
                } catch (SocketTimeoutException ste) {
                    System.out.println("PING NAO RESPONDIDO");
                    counter++;
                    if (counter >= 5) {
                        String aux = this.v.backup_naming;
                        this.v.backup_naming = this.v.main_naming;
                        this.v.main_naming = aux;
                        System.out.println("NOT CONNECTED");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        RMIClient client = new RMIClient(this.v.main_naming);
                        int [] x = new int[2];
                        try {
                            x = client.startTable(this.v.id);
                        } catch (NotBoundException e) {
                            e.printStackTrace();
                        }
                        this.own_port = x[0];
                        this.target_port = x[1];
                        aux = this.v.backup_ip;
                        this.v.backup_ip = this.v.main_ip;
                        this.v.main_ip = aux;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
