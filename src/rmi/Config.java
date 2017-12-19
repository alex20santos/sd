package rmi;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Config {
    public String mainNaming;
    public String backupNaming;
    public String mainIp;
    public String backupIp;
    public int portMainServer,portBackupServer,portToAdminConsole,portFromAdminConsole;
    public int tableMonitorPort;

    public Config() throws IOException {
        ArrayList<String> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("config.txt"));
        try {
            String line;
            for (;(line = br.readLine()) != null;) {
                data.add(line);
            }
        }catch (Exception e){
            System.out.println("Erro na leitura do config file");
        } finally {
            br.close();
        }

        this.mainNaming = data.get(0);
        this.backupNaming = data.get(1);
        this.mainIp = data.get(2);
        this.backupIp = data.get(3);
        this.portMainServer = Integer.parseInt(data.get(4));
        this.portBackupServer = Integer.parseInt(data.get(5));
        this.portToAdminConsole = Integer.parseInt(data.get(6));
        this.portFromAdminConsole = Integer.parseInt(data.get(7));
        this.tableMonitorPort = Integer.parseInt(data.get(8));
    }

    public String getMainNaming() {
        return mainNaming;
    }

    public String getBackupNaming() {
        return backupNaming;
    }

    public String getMainIp() {
        return mainIp;
    }

    public String getBackupIp() {
        return backupIp;
    }

    public int getPortMainServer() {
        return portMainServer;
    }

    public int getPortBackupServer() {
        return portBackupServer;
    }

    public int getPortToAdminConsole() {
        return portToAdminConsole;
    }

    public int getPortFromAdminConsole() {
        return portFromAdminConsole;
    }

    public int getTableMonitorPort() {
        return tableMonitorPort;
    }
}
