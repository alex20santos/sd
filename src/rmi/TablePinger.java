package rmi;
import java.io.IOException;
import java.net.*;

public class TablePinger extends PingServer {
    public TablePinger(String message, String target_ip, int own_port, int target_port) {
        super(message, target_ip, own_port, target_port);
    }

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
                    System.out.println(message);
                    counter = 0;
                }catch(SocketTimeoutException ste){
                    System.out.println("PING NAO RESPONDIDO");
                    counter++;
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
