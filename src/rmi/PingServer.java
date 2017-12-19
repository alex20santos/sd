package rmi;
import java.io.IOException;
import java.net.*;

public class PingServer extends Thread {
    DatagramSocket main_socket = null;
    String message;
    int own_port;
    int target_port;
    String target_ip;

    PingServer(String message,String target_ip,int own_port,int target_port) {
        this.message = message;
        this.target_ip = target_ip;
        this.own_port = own_port;
        this.target_port = target_port;
        this.start();
    }


    @Override
    public void run() {
        try {
            int counter = 0;
            this.main_socket = new DatagramSocket(this.own_port);
            System.out.println("Socket Datagram a escuta no porto "+this.own_port);
            main_socket.setSoTimeout(1000);
            while (counter < 5) {
                try {
                    Thread.sleep(1000);
                    byte[] buffer;
                    byte[] buffer1 = new byte[1000];
                    buffer = message.getBytes();
                    DatagramPacket to_backup = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(this.target_ip), this.target_port);
                    DatagramPacket from_backup = new DatagramPacket(buffer1, buffer1.length);
                    main_socket.send(to_backup);
                    main_socket.receive(from_backup);
                    System.out.println(message);
                    counter = 0;
                }catch(SocketTimeoutException ste){
                    System.out.println("PING NAO RESPONDIDO");
                    counter++;
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            main_socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
