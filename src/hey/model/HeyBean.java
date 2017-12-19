/**
 * Raul Barbosa 2014-11-07
 */
package hey.model;
import rmi.ServerInterface;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class HeyBean {
	private ServerInterface server;
	private String username; // username and password supplied by the user
	private String password;

	public HeyBean() {
		try {
			server = (ServerInterface) Naming.lookup("//127.0.0.1:7000/server");
		}
		catch(NotBoundException|MalformedURLException|RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
	}

	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	
}
