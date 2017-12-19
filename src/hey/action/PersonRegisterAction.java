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

public class PersonRegisterAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
    public String tag = null, username=null,password=null,dep_name =null,expDate=null,contact=null,address=null;
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
        Department dep=null;
        Date data=null;
        String userTag=null;
        System.out.println(dep_name);
        System.out.println(expDate);

        	dep =rmiCon.searchDepartmentByName(dep_name);
        if (dep==null ){
        		return LOGIN;
        }
        try {
        	data = stringToDate(expDate);
        }
        catch(Exception ex) {
        		return LOGIN;
        }
        if(tag.equals("professor")) {
        		userTag = "teacher";
        }
        else if(tag.equals("estudante")) {
        		userTag = "student";
        }
        else if(tag.equals("trabalhador")) {
        		userTag = "worker";
        }
        
        Person p = new Person(dep,password,contact,address,username,data,userTag);

        try {
        		rmiCon.addUser(p.getDepartment(), 0, p.getPassword(), p.getContact(), p.getAddress(), p.getId_number(), data, userTag);
        		return SUCCESS;
        }
        catch(Exception e){
        		return LOGIN;
        }
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
