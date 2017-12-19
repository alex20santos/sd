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

public class VoteAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
    ArrayList<Election> elections ;
    public String chosenElectionString;
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
        if(type.equals("getCandidates")) {
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
    			session.clear();
    			return LOGIN;
        }
        return SUCCESS;
	}
	

	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
