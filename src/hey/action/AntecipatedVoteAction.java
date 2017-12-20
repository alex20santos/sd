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

public class AntecipatedVoteAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
    ArrayList<Election> elections ;
    public String chosenElectionString,username=null,password=null;
    public String type = null, chosenCandidate=null;
    public ArrayList<Candidate> candidatesOfElection;
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
        if(type.equals("getElections")) {
	        	if(this.username != null && !username.equals("")) {
	    			session.put("username", username);
	    			session.put("password", password);
	    				if(rmiCon.checkPassword(username, password)==true) {
	    					if(rmiCon.verifyUserID(username)==false) {
	    						session.put("loggedin", true); // this marks the user as logged in
	    						Person p = rmiCon.searchUser(username);
	    						session.put("user_object", p);
	    						elections = rmiCon.getElectionsByUser(p.getTag(),p.getDepartment());
	    						session.put("elections",elections);
	    			    			return LOGIN;
	    					}
	    				}
	    		}
	        	return NONE;
        }
        else if(type.equals("getCandidates")) {
	        chosenElection = rmiCon.searchElection(chosenElectionString);
	        session.put("chosenElection",this.chosenElection);
	        candidatesOfElection = rmiCon.getCandidates(chosenElection);
	        session.put("candidatesOfElection",candidatesOfElection);
	        return SUCCESS;
        }

        else if(type.equals("register_vote")){
    			Person p = (Person) session.get("user_object");
    			System.out.println(p);
    			System.out.println(chosenCandidate);
    			Election e = (Election) session.get("chosenElection");
    			System.out.println(e);
    			Candidate c = rmiCon.getCandidateByName(chosenCandidate, e);
    			if(c!=null) {
            		Vote vote = new Vote(p.getDepartment(), c, e);
    				System.out.println(vote);
            		rmiCon.registerVote(e, vote, p);
    			}
    			return NONE;
        }
	    else if(type.equals("setElection")) {
    			chosenElection = rmiCon.searchElection(chosenElectionString);
			if(chosenElection!=null) {
		        session.put("chosenElection",this.chosenElection);
		        candidatesOfElection = rmiCon.getCandidates(chosenElection);
		        session.put("candidatesOfElection",candidatesOfElection);
		        return SUCCESS;
			}        			
	    }
        
        return SUCCESS;
	}
	

	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
