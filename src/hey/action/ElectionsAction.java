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
	public String username = null,type=null,name=null,description=null,date=null,hInit=null,hEnd=null,department=null,option=null,newContent=null,chosenElectionString=null;
    ArrayList<Election> elections ;
    ArrayList<Election> electionsNow ;
    ArrayList<Election> previousElections ;
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
        if(type.equals("createElectionCG")) {
        		try {
	        		String parts[] = hInit.split(":");
	        		Hour hi = new Hour(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
	        		String parts2[] = hEnd.split(":");
	        		Hour hf = new Hour(Integer.parseInt(parts2[0]),Integer.parseInt(parts2[1]));
	        		String parts3[] = date.split("/");
	        		Date electionDate = new Date(Integer.parseInt(parts3[0]),Integer.parseInt(parts3[1]),Integer.parseInt(parts3[2]),hi,hf);
	        		rmiCon.createElectionConselhoGeral(name, description, electionDate);
	        		return SUCCESS;
        		}
        		catch (Exception e){
        		}
        	
        }
        else if(type.equals("createElectionNucleo")) {
	    		try {
	    			Department dep = rmiCon.searchDepartmentByName(department);
	    			String parts[] = hInit.split(":");
	        		Hour hi = new Hour(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
	        		String parts2[] = hEnd.split(":");
	        		Hour hf = new Hour(Integer.parseInt(parts2[0]),Integer.parseInt(parts2[1]));
	        		String parts3[] = date.split("/");
	        		Date electionDate = new Date(Integer.parseInt(parts3[0]),Integer.parseInt(parts3[1]),Integer.parseInt(parts3[2]),hi,hf);
	        		rmiCon.createElectionNucleo(name, description, electionDate, dep);
	        		return SUCCESS;
	    		}
	    		catch (Exception e){
	    		}
        }
    		else if(type.equals("createElectionDep")) {
		try {
			Department dep = rmiCon.searchDepartmentByName(department);
			String parts[] = hInit.split(":");
			Hour hi = new Hour(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
			String parts2[] = hEnd.split(":");
			Hour hf = new Hour(Integer.parseInt(parts2[0]),Integer.parseInt(parts2[1]));
			String parts3[] = date.split("/");
	    		Date electionDate = new Date(Integer.parseInt(parts3[0]),Integer.parseInt(parts3[1]),Integer.parseInt(parts3[2]),hi,hf);
	    		rmiCon.createElectionDepartment(name, description, electionDate, dep);
	    		return SUCCESS;
			}
			catch (Exception e){
			}
    		}
    		else if(type.equals("getElections")) {
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
    		else if(type.equals("pastElections")) {
    			previousElections = rmiCon.getPreviousElections();
    			session.put("previousElections", previousElections);
    			return NONE;
    		}
    		else if(type.equals("getPastInfo")) {
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
    		        return INPUT;
    			} 
    		}
        
    		return SUCCESS;        
	}


	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
