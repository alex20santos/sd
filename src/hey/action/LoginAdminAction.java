/**
 * Raul Barbosa 2014-11-07
 */
package hey.action;
import rmi.*;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

public class LoginAdminAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	public Map<String, Object> session;
	public String username = null, password = null;
	private ServerInterface rmiCon;
	private String naming =  "//127.0.0.1:7000/server";

	@Override
	public String execute() throws RemoteException, MalformedURLException, NotBoundException {
        try {
            rmiCon = (ServerInterface) Naming.lookup(naming);
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
        }

		if(this.username != null && !username.equals("")) {
			session.put("username", username);
			session.put("password", password);
			if(username.equals("admin") && password.equals("admin")) {
				session.put("loggedin", true); // this marks the user as logged in
				return SUCCESS;
			}
		}

		return LOGIN;
	}
	
	
	
	public void setUsername(String username) {
		this.username = username; // will you sanitize this input? maybe use a prepared statement?
	}

	public void setPassword(String password) {
		this.password = password; // what about this input? 
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
