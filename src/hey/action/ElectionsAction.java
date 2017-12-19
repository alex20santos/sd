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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

public class ElectionsAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	public String username = null,type=null,name=null,description=null,date=null,hInit=null,hEnd=null,department=null;
    ArrayList<Election> elections ;
	private ServerInterface rmiCon;
	private String naming =  "//127.0.0.1:7000/server";

	@Override
	public String execute() throws RemoteException, MalformedURLException, NotBoundException, ParseException {
        try {
            rmiCon = (ServerInterface) Naming.lookup(naming);
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
        }
        if(type.equals("createElectionCG")) {
        		try {
	        		String parts[] = hInit.split(":");
	        		Hour hi = new Hour(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
	        		String parts2[] = hEnd.split(":");
	        		Hour hf = new Hour(Integer.parseInt(parts2[0]),Integer.parseInt(parts2[1]));
	        		String parts3[] = date.split("/");
	        		Date electionDate = new Date(Integer.parseInt(parts3[0]),Integer.parseInt(parts3[1]),Integer.parseInt(parts3[2]),hi,hf);
	        		rmiCon.createElectionConselhoGeral(name, description, electionDate);
        		}
        		catch (Exception e){
        		}
        	
        }
        
		return SUCCESS;
	}
	
	

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
