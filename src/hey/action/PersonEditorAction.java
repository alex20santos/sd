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

public class PersonEditorAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
    public String id = null, newContent=null,option = null;
    public Election chosenElection;
	private ServerInterface rmiCon;
	private String naming =  "//127.0.0.1:7000/server";

	@Override
	public String execute() throws RemoteException, MalformedURLException, NotBoundException, ParseException {
        try {
            rmiCon = (ServerInterface) Naming.lookup(naming);
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
        }
        
        if(option.equals("1")) {
        		Date date = stringToDate(newContent);
        		rmiCon.editUserExpirationDate(id, date);
            return SUCCESS;
        }
        else if(option.equals("2")) {
    			rmiCon.editUserAddress(id, newContent);
    	        return SUCCESS;
        }
        else if(option.equals("3")) {
    			rmiCon.editUserContact(id, newContent);
    	        return SUCCESS;
        }
        else if(option.equals("4")) {
        		Department d = rmiCon.searchDepartmentByName(newContent);
        		if(d!=null) {
        			rmiCon.editUserDepartment(id, d);
        		}
        		return SUCCESS;
        }
        else if(option.equals("5")) {
        		if(newContent.equals("aluno")   ) {
        			rmiCon.editUserFunction(id, "student");
        	        return SUCCESS;
        		}
        		else if(newContent.equals("professor")   ) {
        			rmiCon.editUserFunction(id, "teacher");
        	        return SUCCESS;
        		}
        		else if(newContent.equals("trabalhador")   ) {
        			rmiCon.editUserFunction(id, "worker");
        	        return SUCCESS;
        		}
        		return LOGIN;
        }
        else if(option.equals("6")) {
    			rmiCon.editPassword(id,newContent);
    	        return SUCCESS;
        }
        return LOGIN;
    }
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
    // data em string: dd-mm-aaaa
    private Date stringToDate(String data1) {
        String [] parts = (data1.split("/"));
        int [] parts_int = new int[3];
        for(int i = 0; i<parts.length;i++){
            parts_int[i] = Integer.parseInt(parts[i]);
        }
        Hour h = new Hour(0,0);
        Hour h2 = new Hour(0,0);
        Date d = new Date(parts_int[0],parts_int[1],parts_int[2],h,h2);
        return d;
    }
}
