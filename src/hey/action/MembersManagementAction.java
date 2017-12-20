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

public class MembersManagementAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
    public String depName = null,type = null,userID=null,op=null;
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
        if(type.equals("setDep")) {
        		Department department = rmiCon.searchDepartmentByName(depName);
        		if(department!=null) {
        			session.put("chosenDepartment", department);
        			return SUCCESS;
        		}
        		else
        			return LOGIN;
        }
        else if(op.equals("1")) {
        		Person p = rmiCon.searchUser(userID);
        		if(p!=null) {
        			Department dep = (Department)session.get("chosenDepartment");
        	        VotingTable v = new VotingTable(dep,dep.getId());
        			rmiCon.addMemberToVotingTable(userID, v);
        			return LOGIN;
        		}
        }
        else if(op.equals("2")) {
        		System.out.println(userID);
	    		Person p = rmiCon.searchUser(userID);
	    		if(p!=null) {
	    			Department dep = (Department)session.get("chosenDepartment");
	    	        VotingTable v = new VotingTable(dep,dep.getId());
	    			rmiCon.removeMemberFromVotingTable(userID, v);
	    			return LOGIN;
	    		}
	    }
        else if(op.equals("3")) {
    		Person p = rmiCon.searchUser(userID);
    		if(p!=null) {
    			Department dep = (Department)session.get("chosenDepartment");
    	        VotingTable v = new VotingTable(dep,dep.getId());
    			rmiCon.changeVotingTableDelegate(userID, v);
    			return LOGIN;
    			}
        }
	    return LOGIN;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
