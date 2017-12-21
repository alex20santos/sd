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

public class ElectionsNowAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	public String username = null,type=null,name=null,description=null,date=null,hInit=null,hEnd=null,department=null,option=null,newContent=null,chosenElectionString=null;
    ArrayList<Election> elections ;
    ArrayList<Candidate> candidatesOfElection;
    Election chosenElection;
	private ServerInterface rmiCon;
	private String naming =  "//127.0.0.1:7000/server";

	@Override
	public String execute() throws RemoteException, MalformedURLException, NotBoundException, ParseException {
        try {
            rmiCon = (ServerInterface) Naming.lookup(naming);
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
        }
        if(type.equals("electionsNow")) {
        		elections = rmiCon.getElectionsNow();
        		session.put("electionsNow", elections);
             return SUCCESS;
        }
        else if(type.equals("getInfo")) {
        		chosenElection = rmiCon.searchElection(chosenElectionString);
			if(chosenElection!=null) {
		        session.put("chosenElection",this.chosenElection);
		        candidatesOfElection = rmiCon.getCandidates(chosenElection);
		        session.put("candidatesOfElection", candidatesOfElection);
		        ArrayList<Integer> num_votes = new ArrayList<>();
		        for(Candidate c : candidatesOfElection) {
		        		num_votes.add(rmiCon.getNumberOfVotesOfCandidate(c));
		        }
		        session.put("candidatesVotes", num_votes);
		        int totalVotes = rmiCon.getNumberOfVotes(chosenElection);
		        session.put("totalVotes", totalVotes);
	        		return LOGIN;
	        }
        }
        return SUCCESS;
	}


	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
