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

public class CandidatesManageAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	public String username = null,type=null,chosenElectionString=null,listName=null,listType=null,members=null;
    ArrayList<Election> elections ;
    Election chosenElection;
	private ServerInterface rmiCon;
	private String naming =  "//127.0.0.1:7000/server";

	@Override
	public String execute() throws RemoteException, NotBoundException {
        try {
            rmiCon = (ServerInterface) Naming.lookup(naming);
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
        }
        if(type.equals("getElections")) {
        		elections= rmiCon.getElections();
        		session.put("elections", elections);
        		return LOGIN;
        }
        else if(type.equals("setElection")) {
        		chosenElection = rmiCon.searchElection(chosenElectionString);
    			if(chosenElection!=null) {
    		        session.put("chosenElection",this.chosenElection);
    		        return LOGIN;
    			}        			
        }
        else if(type.equals("createCandidateList")) {
        		try {
	        		ArrayList<Person> candidatesPeople = new ArrayList<>();
	        		String parts[] = members.split(",");
	        		for(int i=0;i<parts.length;i++) {
	        			Person p = rmiCon.searchUser(parts[i]);
	        			if(p!=null) {
	        				candidatesPeople.add(p);
	        			}
	        		}
	        		Election e = (Election)session.get("chosenElection");
	        		Candidate candidate = new Candidate(e,listName,candidatesPeople);
	        		rmiCon.addCandidateToElection(e, candidate);
	        		return SUCCESS;
        		}
        		catch(Exception ex) {
        			return LOGIN;
        		}
        }
        else if(type.equals("deleteCandidateList")) {
    			Election e = (Election)session.get("chosenElection");
        		rmiCon.removeListFromElection(e, listName);
        		return SUCCESS;
        }
    		return LOGIN;        
	}


	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
