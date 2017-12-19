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

public class DepartmentManagementAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
    public String type = null,depName=null,newName=null;
	private ServerInterface rmiCon;
	private String naming =  "//127.0.0.1:7000/server";

	@Override
	public String execute() throws RemoteException, MalformedURLException, NotBoundException, ParseException {
        try {
            rmiCon = (ServerInterface) Naming.lookup(naming);
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
        }
        if(type.equals("createDep")) {
            rmiCon.createDepartment(depName);
            return SUCCESS;
        }
        else if(type.equals("deleteDep")) {
        		if(!rmiCon.verifyDepartment(depName)) {
        			Department depart = rmiCon.searchDepartmentByName(depName);
        			if(depart!=null)
        				rmiCon.deleteDepartment(depart);
        		}
        		return SUCCESS;
        }
        else if(type.equals("editDep")) {
	        	if(!rmiCon.verifyDepartment(depName)) {
	    			Department depart = rmiCon.searchDepartmentByName(depName);
	    			if(depart!=null)
	    				rmiCon.editDepartmentName(depart, newName);
	    		}
	        	return SUCCESS;
        }
        
        return SUCCESS;
	}
	

	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
