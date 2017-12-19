package rmi;
import java.io.IOException;
import java.net.*;

class PingClient extends Thread{
    private DatagramSocket backup_socket = null;
    private String message;
    int own_port;
    int target_port;
    String target_ip;

    PingClient(String message,String ip,int own_port,int target_port) {
        this.target_ip = ip;
        this.message = message;
        this.own_port = own_port;
        this.target_port = target_port;
        this.start();
    }

    @Override
    public void run() {
        try {
            backup_socket = new DatagramSocket(this.own_port);
            backup_socket.setSoTimeout(3000);
            while (true) {
                try {
                    Thread.sleep(1000);
                    byte[] buffer = message.getBytes();
                    byte[] buffer1 = new byte[1000];
                    DatagramPacket to_main = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(this.target_ip), target_port);
                    DatagramPacket from_main = new DatagramPacket(buffer1, buffer1.length);
                    backup_socket.send(to_main);
                    backup_socket.receive(from_main);
                    System.out.println("Backup a enviar para Main: "+message);
                }catch (SocketTimeoutException ste){
                    System.out.println(ste.getMessage());
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
