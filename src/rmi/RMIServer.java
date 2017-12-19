package rmi;

import java.io.IOException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.File;


public class RMIServer extends UnicastRemoteObject implements ServerInterface {
    static int my_port = 7000;
    int thread_port = 10000;



    protected RMIServer() throws RemoteException {
    }

    protected RMIServer(int port) throws RemoteException {
        super(port);
    }

    protected RMIServer(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }



    public ArrayList<Election> getPreviousElections() throws RemoteException{
        Statement stmt = cria_ligacao_bd();
        String query = "select * from elections ;";
        ArrayList<Election> elections = new ArrayList<>();
        ResultSet res = null;
        String columnValue="";

        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                ArrayList<String> info = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    info.add(columnValue);
                }
                int id = Integer.parseInt(info.get(0));
                String name = info.get(1);
                String desc = info.get(2);
                String data_ini_str = info.get(3);
                String data_end_str = info.get(4);
                Date d = stringToDate(data_ini_str,data_end_str);
                String type = info.get(5);
                String placeID = info.get(6);
                int nulos = Integer.parseInt(info.get(7));
                int brancos = Integer.parseInt(info.get(8));
                Election election = null;
                if(type.equals("cg")){
                    election = new ElectionConselhoGeral(name,desc,d,brancos,nulos,id);
                }
                else if(type.equals("nucleo")){
                    Department department = new Department(placeID);
                    department.name = getDepartmentName(department.id);
                    election = new ElectionNucleo(name,desc,d,department,brancos,nulos,id);
                }
                else if(type.equals("department")){
                    Department department = new Department(placeID);
                    department.name = getDepartmentName(department.id);
                    election = new ElectionDepartment(name,desc,d,department,brancos,nulos,id);
                }
                try {
                    if(compareDateToActual(election.getDate())>0){ // já passou a dia/hora de fim
                        elections.add(election);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return elections;
    }

    public  void teste (){
        Statement stmt = cria_ligacao_bd();

        String query = "select * from people;";
        String columnValue="";
        System.out.println("querying: "+ query);
        ResultSet res = null;
        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    System.out.println(columnValue);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMemberToVotingTable(String id, VotingTable v) throws  RemoteException{
        Statement stmt = cria_ligacao_bd();

        int dep_id = getDepartmentID(v.getDepartment().getName());
        String query = "insert into delegates value ("+dep_id+","+Integer.parseInt(id)+",0);";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query); //utiliza-se para inserir
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public VotingTable searchVotingTable(VotingTable v) throws RemoteException {
        Statement stmt = cria_ligacao_bd();

        String query = "select * from departments where name like'"+v.getDepartment().getName()+"';";
        ResultSet res = null;
        String columnValue="";
        try {
            String name;
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    if(i==1) {
                        VotingTable vt = new VotingTable();
                        name = getDepartmentName(Integer.parseInt(columnValue));
                        Department d = searchDepartmentByName(name);
                        vt.setDepartment(d);
                        vt.setId(Integer.parseInt(columnValue));
                        return vt;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void changeVotingTableDelegate(String id, VotingTable v) throws RemoteException{
        Statement stmt = cria_ligacao_bd();

        Person p = searchUser(id);
        ResultSet res = null;
        String columnValue="";
        VotingTable votingTable = searchVotingTable(v);
        String query = "delete from delegates where placeID ="+votingTable.id+" and isMainDelegate=1;";
        try {
            stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        query = "insert into delegates value ("+votingTable.id+","+Integer.parseInt(id)+",1);";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void removeMemberFromVotingTable(String id, VotingTable v) throws RemoteException{
        Statement stmt = cria_ligacao_bd();

        Person p = searchUser(id);
        ResultSet res = null;
        String columnValue="";
        VotingTable votingTable = searchVotingTable(v);
        String query = "delete from delegates where placeID ="+votingTable.id+";";
        try {
            stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyDepartment(String name) throws RemoteException{
        Statement stmt = cria_ligacao_bd();

        ResultSet res = null;
        String query = "select * from departments where name like'"+name+"';";
        String columnValue="";
        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    if(columnValue.equals(name)){
                        return false;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean verifyUserID(String id_number) throws RemoteException{
        Statement stmt = cria_ligacao_bd();

        boolean resBool = true;
        ResultSet res = null;
        String query = "select * from people where id_cc ="+Integer.parseInt(id_number)+";";
        String columnValue="";
        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    System.out.println(columnValue);
                    if(columnValue.equals(id_number)){
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resBool;
    }


    //todo voting function;
    public ArrayList<Election> getElections() throws RemoteException { // retorna eleicoes que existem e que ainda nao acabaram
        Statement stmt = cria_ligacao_bd();

        String query = "select * from elections;";
        ArrayList<Election> elections = new ArrayList<>();
        ResultSet res = null;
        String columnValue="";

        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                ArrayList<String> info = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    info.add(columnValue);
                }
                int id = Integer.parseInt(info.get(0));
                String name = info.get(1);
                String desc = info.get(2);
                String data_ini_str = info.get(3);
                String data_end_str = info.get(4);
                Date d = stringToDate(data_ini_str,data_end_str);
                String type = info.get(5);
                String placeID = info.get(6);
                int nulos = Integer.parseInt(info.get(7));
                int brancos = Integer.parseInt(info.get(8));
                Election election = null;
                if(type.equals("cg")){
                    election = new ElectionConselhoGeral(name,desc,d,brancos,nulos,id);
                }
                else if(type.equals("nucleo")){
                    Department department = new Department(placeID);
                    department.name = getDepartmentName(department.id);
                    election = new ElectionNucleo(name,desc,d,department,brancos,nulos,id);
                }
                else if(type.equals("department")){
                    Department department = new Department(placeID);
                    department.name = getDepartmentName(department.id);
                    election = new ElectionDepartment(name,desc,d,department,brancos,nulos,id);
                }
                try {
                    if(compareDateToActual(election.getDate())<0){ // já passou a dia/hora de fim
                        elections.add(election);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return elections;
    }

    // data em string: dd-mm-aaaa-hh-mm
    private Date stringToDate(String data1,String data2) {
        String [] parts = (data1.split("-"));
        int [] parts_int = new int[5];
        for(int i = 0; i<parts.length;i++){
            parts_int[i] = Integer.parseInt(parts[i]);
        }
        Hour h = new Hour(parts_int[3],parts_int[4]);
        parts = (data2.split("-"));
        Hour h2 = new Hour(Integer.parseInt(parts[3]),Integer.parseInt(parts[4]));
        Date d = new Date(parts_int[0],parts_int[1],parts_int[2],h,h2);
        return d;
    }

    private Date stringToDateExpDate(String data) {
        String [] parts = (data.split("-"));
        int [] parts_int = new int[5];
        for(int i = 0; i<parts.length;i++){
            parts_int[i] = Integer.parseInt(parts[i]);
        }
        Hour h = new Hour(parts_int[3],parts_int[4]);
        Hour h2 = new Hour(0,0);
        Date d = new Date(parts_int[0],parts_int[1],parts_int[2],h,h2);
        return d;
    }
    //ALL ELECTIONS A USER CAN VOTE, REGARDLESS OF HAPPENING NOW
    public ArrayList<Election> getElectionsByUser(String tag,Department user_dep) throws RemoteException, ParseException {
        Statement stmt = cria_ligacao_bd();

        String query = "select * from elections;";
        ArrayList<Election> elections = new ArrayList<>();
        ResultSet res = null;
        String columnValue="";

        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                ArrayList<String> info = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    info.add(columnValue);
                }
                int id = Integer.parseInt(info.get(0));
                String name = info.get(1);
                String desc = info.get(2);
                String data_ini_str = info.get(3);
                String data_end_str = info.get(4);
                Date d = stringToDate(data_ini_str,data_end_str);
                String type = info.get(5);
                String placeID = info.get(6);
                int nulos = Integer.parseInt(info.get(7));
                int brancos = Integer.parseInt(info.get(8));
                Election election = null;
                if(type.equals("cg")){
                    election = new ElectionConselhoGeral(name,desc,d,brancos,nulos,id);
                    if(compareDateToActual(election.getDate())<0 ){//todo check tags já passou a dia/hora de fim
                        elections.add(election);
                    }
                }
                else if(type.equals("nucleo") &&  Integer.parseInt(placeID)== user_dep.id){
                    Department department = new Department(placeID);
                    department.name = getDepartmentName(department.id);
                    election = new ElectionNucleo(name,desc,d,department,brancos,nulos,id);

                    if(compareDateToActual(election.getDate())<0 && tag.equals("student")){//todo check tags já passou a dia/hora de fim
                        elections.add(election);
                    }
                }
                else if(type.equals("department")  && Integer.parseInt(placeID)== user_dep.id){
                    Department department = new Department(placeID);
                    department.name = getDepartmentName(department.id);
                    election = new ElectionDepartment(name,desc,d,department,brancos,nulos,id);
                    if(compareDateToActual(election.getDate())<0 && tag.equals("teacher")){//todo check tags já passou a dia/hora de fim
                        elections.add(election);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return elections;
    }

    public ArrayList<Election> getElectionsByUserNow(String tag,Department user_dep) throws  RemoteException{
        Statement stmt = cria_ligacao_bd();

        String query = "select * from elections"+";";
        ArrayList<Election> elections = new ArrayList<>();
        ResultSet res = null;
        String columnValue="";

        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                ArrayList<String> info = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    info.add(columnValue);
                }
                int id = Integer.parseInt(info.get(0));
                String name = info.get(1);
                String desc = info.get(2);
                String data_ini_str = info.get(3);
                String data_end_str = info.get(4);
                Date d = stringToDate(data_ini_str,data_end_str);
                String type = info.get(5);
                String placeID = info.get(6);
                int nulos = Integer.parseInt(info.get(7));
                int brancos = Integer.parseInt(info.get(8));
                Election election = null;
                if(type.equals("cg")) {
                    election = new ElectionConselhoGeral(name, desc, d, brancos, nulos, id);
                    if (isHappening2(election.getDate())) {
                        elections.add(election);
                    }
                }
                else if(type.equals("nucleo") &&  Integer.parseInt(placeID)== user_dep.id){
                    Department department = new Department(placeID);
                    department.name = getDepartmentName(department.id);
                    election = new ElectionNucleo(name,desc,d,department,brancos,nulos,id);

                    if(isHappening2(election.getDate()) && tag.equals("student")){//todo check tags já passou a dia/hora de fim
                        elections.add(election);
                    }
                }
                else if(type.equals("department") &&  Integer.parseInt(placeID)== user_dep.id ){
                    Department department = new Department(placeID);
                    department.name = getDepartmentName(department.id);
                    election = new ElectionDepartment(name,desc,d,department,brancos,nulos,id);
                    if(isHappening2(election.getDate()) && tag.equals("student")){//todo check tags já passou a dia/hora de fim
                        elections.add(election);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return elections;
    }

    @Override
    public int getNumberOfVotes(Election e) throws RemoteException {
        Statement stmt = cria_ligacao_bd();
        Election election = searchElection(e.getName());
        String query = "select num_nulos, num_brancos from Elections where idElection ="+election.id+";";
        System.out.println("querying: "+ query);
        ResultSet res = null;
        int total_votes=0;
        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = res.getString(i);
                    total_votes = total_votes + Integer.parseInt(columnValue);
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        stmt = cria_ligacao_bd();
        query = "select num_votes from Candidates where idElection =" + election.id + ";";
        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = res.getString(i);
                    total_votes = total_votes + Integer.parseInt(columnValue);
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return total_votes;
    }

    @Override
    public int getNumberOfVotesOfCandidate(Candidate c) throws RemoteException {
        System.out.println(c.getName());
        Statement stmt = cria_ligacao_bd();
        int total_votes=0;
        if(!c.getName().equals("nulo") && !c.getName().equals("branco") ){
            String query = "select num_votes from candidates where candidateID ="+c.id+";";
            System.out.println("querying: "+ query);
            ResultSet res = null;
            try {
                res = stmt.executeQuery(query);
                ResultSetMetaData rsmd = res.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (res.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        String columnValue = res.getString(i);
                        total_votes = total_votes + Integer.parseInt(columnValue);
                    }

                }
                return total_votes;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        else if(c.getName().equals("nulo")){
            Election el = searchElection(c.getElection().getName());
            String query = "select num_nulos from Elections where idElection ="+c.getElection().id+";";
            ResultSet res = null;
            try {
                res = stmt.executeQuery(query);
                ResultSetMetaData rsmd = res.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (res.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        String columnValue = res.getString(i);
                        total_votes = total_votes + Integer.parseInt(columnValue);
                    }

                }
                return total_votes;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        else if(c.getName().equals("branco")){
            Election el = searchElection(c.getElection().getName());
            String query = "select num_brancos from Elections where idElection ="+c.getElection().id+";";
            ResultSet res = null;
            try {
                res = stmt.executeQuery(query);
                ResultSetMetaData rsmd = res.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (res.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        String columnValue = res.getString(i);
                        total_votes = total_votes + Integer.parseInt(columnValue);
                    }

                }
                return total_votes;
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return total_votes;
    }

    public int getDepartmentID(String dep_name){
        Statement stmt = cria_ligacao_bd();
        String query = "select * from departments where name like'"+dep_name+"';";
        int id=0;
        System.out.println("querying: "+ query);
        ResultSet res = null;
        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = res.getString(i);
                    id = Integer.parseInt(columnValue);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public String getDepartmentName (int id){
        Statement stmt = cria_ligacao_bd();
        String query = "select name from departments where placeID ="+id+";";
        String columnValue="";
        System.out.println("querying: "+ query);
        ResultSet res = null;
        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnValue;
    }

    public ArrayList<Election> getElectionsNow() throws ParseException , RemoteException {
        Statement stmt = cria_ligacao_bd();
        String query = "select * from elections"+";";
        ArrayList<Election> elections = new ArrayList<>();
        ResultSet res = null;
        String columnValue="";

        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                ArrayList<String> info = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    info.add(columnValue);
                }
                int id = Integer.parseInt(info.get(0));
                String name = info.get(1);
                String desc = info.get(2);
                String data_ini_str = info.get(3);
                String data_end_str = info.get(4);
                Date d = stringToDate(data_ini_str,data_end_str);
                String type = info.get(5);
                String placeID = info.get(6);
                int nulos = Integer.parseInt(info.get(7));
                int brancos = Integer.parseInt(info.get(8));
                Election election = null;
                if(type.equals("cg")){
                    election = new ElectionConselhoGeral(name,desc,d,brancos,nulos,id);
                }
                else if(type.equals("nucleo")){
                    Department department = new Department(placeID);
                    department.name = getDepartmentName(department.id);
                    election = new ElectionNucleo(name,desc,d,department,brancos,nulos,id);
                }
                else if(type.equals("department")){
                    Department department = new Department(placeID);
                    department.name = getDepartmentName(department.id);
                    election = new ElectionDepartment(name,desc,d,department,brancos,nulos,id);
                }
                if(isHappening2(election.getDate())){
                    elections.add(election);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return elections;
    }


    public void addUser(Department department, int user_id, String password, String contact, String address, String id_number, Date id_expiration_date,String tag) throws RemoteException {
        Statement stmt = cria_ligacao_bd();
        Person p = new Person(department,user_id, password, contact, address, id_number, id_expiration_date,tag);
        System.out.println(p);
        System.out.println("["+p.getTag() +"] user with id number " + p.getId_number()+ " added to system");
        int cc = Integer.parseInt(id_number);
        String query = "insert into People value ("+cc+",'"+contact+"','"+address+"','"+id_expiration_date.getDay()+"-"+id_expiration_date.getMonth()+"-"+id_expiration_date.getYear()+"',"+getDepartmentID(department.name)+",'"+tag+"','"+password+"');";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query); //utiliza-se para inserir
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createElectionConselhoGeral(String name,String description,Date date) throws RemoteException {
        Statement stmt = cria_ligacao_bd();
        String data_init = date.getDay()+"-"+date.getMonth()+"-"+date.getYear()+"-"+date.getStarting_hour().getHour()+"-"+date.getStarting_hour().getMinute();
        String data_end = date.getDay()+"-"+date.getMonth()+"-"+date.getYear()+"-"+date.getEnding_hour().getHour()+"-"+date.getEnding_hour().getMinute();
        String query = "insert into Elections value (null,'"+name+"','"+description+"','"+data_init+"','"+data_end+"',"+"'cg',null,0,0);";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query); //utiliza-se para inserir
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("CG election created");
    }

    public void createElectionNucleo(String name,String description,Date date,Department department) throws RemoteException {
        Statement stmt = cria_ligacao_bd();
        String data_init = date.getDay()+"-"+date.getMonth()+"-"+date.getYear()+"-"+date.getStarting_hour().getHour()+"-"+date.getStarting_hour().getMinute();
        String data_end = date.getDay()+"-"+date.getMonth()+"-"+date.getYear()+"-"+date.getEnding_hour().getHour()+"-"+date.getEnding_hour().getMinute();
        String query = "insert into Elections value (null,'"+name+"','"+description+"','"+data_init+"','"+data_end+"',"+"'nucleo',"+department.id+",0,0);";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query); //utiliza-se para inserir
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Nucleo election created");
    }


    public void createElectionDepartment(String name, String description, Date date, Department department) throws RemoteException {
        Statement stmt = cria_ligacao_bd();
        String data_init = date.getDay()+"-"+date.getMonth()+"-"+date.getYear()+"-"+date.getStarting_hour().getHour()+"-"+date.getStarting_hour().getMinute();
        String data_end = date.getDay()+"-"+date.getMonth()+"-"+date.getYear()+"-"+date.getEnding_hour().getHour()+"-"+date.getEnding_hour().getMinute();
        Department dep = searchDepartmentByName(department.getName());
        System.out.println("id dei:"+ dep.id);
        String query = "insert into Elections value (null,'"+name+"','"+description+"','"+data_init+"','"+data_end+"',"+"'department',"+dep.id+",0,0);";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query); //utiliza-se para inserir
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Department election created");
    }

    public void removeListFromElection(Election e, String name) throws RemoteException{
        Statement stmt = cria_ligacao_bd();
        Election election = searchElection(e.name);
        int election_id = election.id;
        String query = "delete from Candidates where candidateID=(select candidateID from candidates where idElection="+election_id+" and name like '"+name+"');";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query); //utiliza-se para inserir
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void editElectionDate(Election e, Date d) throws RemoteException {
        Statement stmt = cria_ligacao_bd();
        Election election = searchElection(e.getName());
        String date_init= d.getDay()+"-"+d.getMonth()+"-"+d.getYear()+"-"+d.getStarting_hour().getHour()+d.getStarting_hour().getMinute();
        String date_end= d.getDay()+"-"+d.getMonth()+"-"+d.getYear()+"-"+d.getEnding_hour().getHour()+d.getEnding_hour().getMinute();
        String query = "update Elections " +
                "set date_init ='"+date_init+"'"+
                "where name like '"+e.name+"';";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        query = "update Elections " +
                "set date_end ='"+date_end+"'"+
                "where name like '"+e.name+"';";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        System.out.println("Eleicao "+election.getName()+" mudou de data para "+d.getDay()+"/"+d.getMonth()+"/"+d.getYear());
    }

    public void editElectionDescription(Election e, String n_string) throws RemoteException {
        Statement stmt = cria_ligacao_bd();
        Election election = searchElection(e.getName());
        String query = "update Elections " +
                "set description ='"+n_string+"'"+
                "where name like '"+e.name+"';";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createDepartment(String name) throws RemoteException{
        Statement stmt = cria_ligacao_bd();
        Department d = new Department(name);
        String query = "insert into Departments value (null,'"+name+"');";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query); //utiliza-se para inserir
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editDepartmentName(Department department, String newName) throws RemoteException {
        Statement stmt = cria_ligacao_bd();
        String query = "update Departments " +
                       "set name ='"+newName+"'"+
                       "where name like '"+department.name+"';";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query); //utiliza-se para inserir
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteDepartment(Department depart) throws RemoteException {
        Statement stmt = cria_ligacao_bd();
        int id_dep = getDepartmentID(depart.getName());
        String query = "delete from departments where placeID ="+id_dep+";";
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("department "+id_dep+" removed");
    }

    /*public void addVotingTableToElection(Department department, Person delegate, String election) throws RemoteException{
        Election e = searchElection(election);
        Department depAux = new Department(department.getName());
        Person delegateAux = new Person(delegate.getDepartment(),delegate.getUser_id(), delegate.getPassword(), delegate.getContact(),
                delegate.getAddress(),delegate.getId_number(),delegate.getId_expiration_date(),delegate.getTag());
        VotingTable votingTable = new VotingTable(depAux,delegateAux,e);
        votingTables.add(votingTable);
    }*/


    public void registerVote(Election election,Vote vote,Person person) throws RemoteException {
        Statement stmt = cria_ligacao_bd();
        System.out.println(election.getName());
        Election election_aux = searchElection(election.getName());
        String query = "select * from votes where voterID ="+person.getId_number()+" and  idElection = "+election_aux.id+";";
        ResultSet res = null;
        try {
            res = stmt.executeQuery(query);
            while (res.next()) {
                return;
            }
            System.out.println("election id "+election_aux.id);
            query = "insert into votes (voterID,idElection,placeID) " +"values ("+person.getId_number()+","+election_aux.id+",1);";
            stmt.executeUpdate(query);
            System.out.println("choice:"+vote.getChoice().getName());
            if(vote.getChoice().getName().equals("nulo")){
                System.out.println(" -------- vota no nulo -------");
                query = "update elections set num_nulos=num_nulos+1 where idElection="+election_aux.id+";";
                stmt.executeUpdate(query);
            }
            else if(vote.getChoice().getName().equals("branco")){
                query = "update elections set num_brancos=num_brancos+1 where idElection="+election_aux.id+";";
                stmt.executeUpdate(query);
            }
            else{
                Candidate c = getCandidateByName(vote.choice.name ,election);
                query = "update candidates set num_votes=num_votes+1 where idElection = "+election_aux.id+" and candidateId = "+c.id+";";
                stmt.executeUpdate(query);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean loginVoter(Election election,Person person,String password){//TODO CAN ONLY VOTE IN THE RIGHT PLACES
        Election e = searchElection(election.getName());
        Person p = searchUser(person.getId_number());
        return e.loginVoter(p,p.getPassword());
    }

    public boolean loginDelegate(VotingTable table,Person person,String password){//at the voting table the delegate must login
        Statement stmt = cria_ligacao_bd();
        Person p = searchUser(person.getId_number());
        String query = "select * from delegates where personID ="+p.getId_number()+" and isMainDelegate="+1+";";
        ResultSet res = null;
        try {
            res = stmt.executeQuery(query);
            while (res.next()) {
                if(p.getPassword().equals(password)){
                    return true;
                }
                else{
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public  Department searchDepartmentByName(String dep_name) {
        Statement stmt = cria_ligacao_bd();
        System.out.println("Lookin in db for"+dep_name);
        String query = "select * from departments where name like'"+dep_name+"';";
        ResultSet res = null;
        ArrayList<String> info;
        String columnValue="";
        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                info= new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    info.add(columnValue);
                }
                System.out.println("info:"+info);
                return new Department(info.get(1),Integer.parseInt(info.get(0)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  Department searchDepartmentById(String d_Id) {
        Statement stmt = cria_ligacao_bd();
        String query = "select placeID from departments where placeID ="+d_Id+";";
        ResultSet res = null;
        String columnValue="";
        try {
            String name;
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    name = getDepartmentName(Integer.parseInt(columnValue));
                    return new Department(name,Integer.parseInt(d_Id));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public Person searchUser(String id){
        Statement stmt = cria_ligacao_bd();
        String query = "select * from people where id_cc ="+id+";";
        ResultSet res = null;
        String columnValue="";

        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                ArrayList<String> info = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    info.add(columnValue);
                }
                System.out.println(info);
                String id_user = info.get(0);
                String contact = info.get(1);
                String adddress = info.get(2);
                String date_exp = info.get(3);
                Date d = stringToDateExpDate(date_exp);
                int depID = Integer.parseInt(info.get(4));
                String tag = info.get(5);
                String password = info.get(6);
                String dep_name = getDepartmentName(depID);
                Department dep = searchDepartmentByName(dep_name);
                System.out.println("dep id->>"+dep.id);
                Person person = new Person(dep,0,password,contact,adddress,id_user,d,tag);
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Election searchElection(String election)  {
        Statement stmt = cria_ligacao_bd();
        String query = "select * from elections where name like '"+election+"';";
        Election election_res = null;
        ResultSet res = null;
        String columnValue="";

        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                ArrayList<String> info = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    info.add(columnValue);
                }
                int id = Integer.parseInt(info.get(0));
                String name = info.get(1);
                String desc = info.get(2);
                String data_ini_str = info.get(3);
                String data_end_str = info.get(4);
                Date d = stringToDate(data_ini_str,data_end_str);
                String type = info.get(5);
                String placeID = info.get(6);
                int nulos = Integer.parseInt(info.get(7));
                int brancos = Integer.parseInt(info.get(8));
                if(type.equals("cg")){
                    election_res = new ElectionConselhoGeral(name,desc,d,brancos,nulos,id);
                    return  election_res;
                }
                else if(type.equals("nucleo")){
                    Department department = new Department(placeID);
                    department.name = getDepartmentName(department.id);
                    election_res = new ElectionNucleo(name,desc,d,department,brancos,nulos,id);
                    return  election_res;

                }
                else if(type.equals("department")){
                    Department department = new Department(placeID);
                    department.name = getDepartmentName(department.id);
                    election_res = new ElectionDepartment(name,desc,d,department,brancos,nulos,id);
                    return  election_res;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Election searchElectionID(String elId)  {
        Statement stmt = cria_ligacao_bd();
        String query = "select * from elections where  idElection= "+elId+";";
        Election election_res = null;
        ResultSet res = null;
        String columnValue="";

        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                ArrayList<String> info = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    info.add(columnValue);
                }
                int id = Integer.parseInt(info.get(0));
                String name = info.get(1);
                String desc = info.get(2);
                String data_ini_str = info.get(3);
                String data_end_str = info.get(4);
                Date d = stringToDate(data_ini_str,data_end_str);
                String type = info.get(5);
                String placeID = info.get(6);
                int nulos = Integer.parseInt(info.get(7));
                int brancos = Integer.parseInt(info.get(8));
                if(type.equals("cg")){
                    election_res = new ElectionConselhoGeral(name,desc,d,brancos,nulos,id);
                    return  election_res;
                }
                else if(type.equals("nucleo")){
                    Department department = new Department(placeID);
                    department.name = getDepartmentName(department.id);
                    election_res = new ElectionNucleo(name,desc,d,department,brancos,nulos,id);
                    return  election_res;

                }
                else if(type.equals("department")){
                    Department department = new Department(placeID);
                    department.name = getDepartmentName(department.id);
                    election_res = new ElectionDepartment(name,desc,d,department,brancos,nulos,id);
                    return  election_res;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean checkPassword(String username,String password){
        System.out.println("id"+username);
        Person p = searchUser(username);
        System.out.println("user"+p);
        if(p.getPassword().equals(password)){
            return true;
        }
        else {
            return false;
        }
    }

    /*public VotingTable searchVotingTable(Department department){
        for(VotingTable votingTable:this.votingTables){
            if(votingTable.getDepartment().equals(department))
                if (!votingTable.up){
                    votingTable.up = true;
                }
        }
        return null;
    }**/


    public void editElectionName(Election e,String n_string) throws RemoteException{
        Statement stmt = cria_ligacao_bd();
        String query = "update Elections " +
                "set name ='"+n_string+"'"+
                "where name like '"+e.name+"';";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void editUserExpirationDate(String id_number, Date date) throws RemoteException{
        Statement stmt = cria_ligacao_bd();
        String data_aux=date.getDay()+"-"+ date.getMonth()+"-"+date.getYear();
        String query = "update People " +
                "set expiration_date ='"+data_aux+"' "+
                "where id_cc = "+Integer.parseInt(id_number)+";";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query); //utiliza-se para inserir
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("changed date");
    }

    public void editUserAddress(String id_number, String n_m) throws RemoteException{
        Statement stmt = cria_ligacao_bd();
        String query = "update People " +
                "set address ='"+n_m+"'"+
                "where id_cc = "+Integer.parseInt(id_number)+";";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query); //utiliza-se para inserir
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("changed address");
    }

    public void editUserContact(String id_number, String n_num) throws RemoteException{
        Statement stmt = cria_ligacao_bd();
        String query = "update People " +
                "set contact ='"+n_num+"'"+
                "where id_cc = "+Integer.parseInt(id_number)+";";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query); //utiliza-se para inserir
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("changed contact");
    }

    public void editUserDepartment(String id_number, Department n_dep) throws RemoteException{
        Statement stmt = cria_ligacao_bd();
        System.out.println("mudar de dep");
        String query = "update People " +
                "set department ='"+getDepartmentID(n_dep.getName())+"'"+
                "where id_cc = "+Integer.parseInt(id_number)+";";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("changed dep");
    }

    public void editUserFunction(String id_number, String n_func) throws RemoteException{
        Statement stmt = cria_ligacao_bd();
        String query = "update People " +
                "set tag ='"+n_func+"'"+
                "where id_cc = "+Integer.parseInt(id_number)+";";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("changed func");
    }


    public void addCandidateToElection(Election e, Candidate n_cand) throws RemoteException {
        Statement stmt = cria_ligacao_bd();
        Election election = searchElection(e.getName());
        System.out.println(election.id);
        String query =
                "insert into candidates " +
                "value (null," +
                "'"+n_cand.getName()+"'," +
                election.id+"," +
                " 0);";
        try {
            stmt.executeUpdate(query);
            System.out.println("candidate inserted");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        stmt = cria_ligacao_bd();
        query = "SELECT candidateID from candidates where name like '"+n_cand.getName()+"' and idElection ="+election.id+";";
        ResultSet res = null;
        String columnValue="";
        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    break;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        int id_candidate = Integer.parseInt(columnValue);
        int i =1;
        for(Person p : n_cand.getList()){
            query = "insert into PersonCandidates value ("+id_candidate+","+p.getId_number()+","+i+++");";
            try {
                stmt.executeUpdate(query);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        }
    }

    public Candidate getCandidateByName(String candidate,Election election) throws  RemoteException{
        Statement stmt = cria_ligacao_bd();
        String query = "SELECT * from candidates where idElection ="+election.id+" and name like '"+candidate+"';";
        ResultSet res = null;
        String columnValue="";
        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                ArrayList<String> info = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    info.add(columnValue);
                }
                Candidate c = new Candidate(election,info.get(1),null,Integer.parseInt(info.get(0)));
                return c;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if(candidate.equals("nulo")) {
        		Candidate c =  new Candidate(election,"nulo",null,0);
        		return c;
        }
        if(candidate.equals("branco")) {
    			Candidate c =  new Candidate(election,"branco",null,0);
    			return c;
        }
        return null;
    }

    public ArrayList<Candidate> getCandidates(Election election) throws  RemoteException{
        Statement stmt = cria_ligacao_bd();
        ArrayList<Candidate> candidates = new ArrayList<>();
        Election election1 = searchElection(election.getName());
        String query = "SELECT * from candidates where idElection ="+election1.id+";";
        ResultSet res = null;
        String columnValue="";
        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                ArrayList<String> info = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    info.add(columnValue);
                }
                Candidate c = new Candidate(election1,info.get(1),null,Integer.parseInt(info.get(0)));
                candidates.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        Candidate nulo = new Candidate(election1,"nulo",null,0);
        Candidate branco = new Candidate(election1,"branco",null,0);
        candidates.add(nulo);
        candidates.add(branco);
        return  candidates;
    }


    public void editPassword(String id_number, String n_password) throws RemoteException{
        Statement stmt = cria_ligacao_bd();
        String query = "update People " +
                "set password ='"+n_password+"'"+
                "where id_cc = "+Integer.parseInt(id_number)+";";
        System.out.println("querying: "+ query);
        try {
            stmt.executeUpdate(query); //utiliza-se para inserir
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //OI MIKE, ESTA FUNÇAO E DO PASSADO, SERVE PARA REGISTAR UMA VOTING TABLE E DEVOLVE OS PORTS A QUE ESTA SE DEVE LIGAR
    //BOAS, PRIMEIRO DEVOLVE O PORT QUE ESTA DEVE USAR E DEPOIS O PORT QUE SE DEVE CONECTAR
    //abraços miguel, do teu amigo francisco
    public int[] registerTable(String ip,String department){
        VotingTable v = new VotingTable();
        v.setDepartment(searchDepartmentByName(department));
        if(v==null)
            return null;
        int port1 = thread_port++;
        int port2 = thread_port++;
        PingThread t = new PingThread("Beni","127.0.0.1",port1,port2,v);
        v.up = true;
        int x[] = new int[2];
        x[0]= port2;
        x[1]=port1;
        return x;
    }

    public ArrayList<VotingTable> getVotingTables() throws  RemoteException{
        Statement stmt = cria_ligacao_bd();
        ArrayList<VotingTable> votingTables = new ArrayList<>();
        String query = "SELECT * from departments;";
        ResultSet res = null;
        String columnValue="";
        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                ArrayList<String> info = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    info.add(columnValue);
                }
                int depID = Integer.parseInt(info.get(0));
                String name = info.get(1);
                Department dep = searchDepartmentByName(name);
                VotingTable v = new VotingTable(dep,depID);
                votingTables.add(v);

                query = "SELECT * from delegates; where placeID = "+v.department.id+";";
                res = stmt.executeQuery(query);
                ResultSetMetaData rsmd2 = res.getMetaData();
                int columnsNumber2 = rsmd2.getColumnCount();
                while (res.next()) {
                    ArrayList<String> info_del = new ArrayList<>();
                    for (int i = 1; i <= columnsNumber; i++) {
                        columnValue = res.getString(i);
                        info_del.add(columnValue);
                    }
                    if(Integer.parseInt(info_del.get(2))==1){
                        v.delegate = new Person(info_del.get(1));
                    }else{
                        v.members.add(new Person(info_del.get(1)));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return votingTables;
    }

    public ArrayList<Vote> getPreviousVotes(String id) throws RemoteException{
        Statement stmt = cria_ligacao_bd();
        ArrayList<Vote> votes = new ArrayList<>();
        String query = "SELECT * from votes where voterID = "+id+";";
        ResultSet res = null;
        String columnValue="";
        try {
            res = stmt.executeQuery(query);
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (res.next()) {
                ArrayList<String> info_v = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    columnValue = res.getString(i);
                    info_v.add(columnValue);
                    System.out.println(columnValue);
                }
                System.out.println(info_v);
                Election e = searchElectionID(info_v.get(1));
                Department d = searchDepartmentById(info_v.get(2));
                Vote v = new Vote(d,null,e);
                votes.add(v);
            }
            return votes;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /*
     return
            0 -> datas iguais
            >0 -> d1 é depois de d2
            <0 -> d1 é antes de d2
     */
    public static int compareDates(Date d1,Date d2) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:m");
        String d1String = ""+d1.getYear()+"-"+d1.getMonth()+"-"+d1.getDay()+" "+d1.getStarting_hour().getHour()+":"+d1.getStarting_hour().getMinute()+"";
        String d2String = ""+d2.getYear()+"-"+d2.getMonth()+"-"+d2.getDay()+" "+d2.getEnding_hour().getHour()+":"+d2.getEnding_hour().getMinute()+"";
        java.util.Date dateObject = sdf.parse(d1String);
        java.util.Date dateObject2 = sdf.parse(d2String);
        return dateObject.compareTo(dateObject2);
    }


    /*
     return
            0 -> datas igual à atual
            1 -> d1 é depois de atual
            -1 -> d1 é antes de atual
     */
    public static int compareDateToActual(Date d1) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:m");
        String d1String = ""+d1.getYear()+"-"+d1.getMonth()+"-"+d1.getDay()+" "+d1.getEnding_hour().getHour()+":"+d1.getEnding_hour().getMinute()+"";
        java.util.Date dateObject = sdf.parse(d1String);
        java.util.Date date = new java.util.Date();
        return date.compareTo(dateObject);
    }

    public static boolean isHappening2(Date my_date){
        java.util.Date my_date_s = null;
        java.util.Date my_date_e = null;
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:m");
        String end_date = ""+my_date.getYear()+"-"+my_date.getMonth()+"-"+my_date.getDay()+" "+my_date.getEnding_hour().getHour()+":"+my_date.getEnding_hour().getMinute()+"";
        String start_date = ""+my_date.getYear()+"-"+my_date.getMonth()+"-"+my_date.getDay()+" "+my_date.getStarting_hour().getHour()+":"+my_date.getStarting_hour().getMinute()+"";
        try {
            my_date_s = sdf.parse(start_date);
            my_date_e = sdf.parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.util.Date actual_date = new java.util.Date();
        if(my_date_s.compareTo(actual_date)<0 && my_date_e.compareTo(actual_date)>0){
            return true;
        }
        return false;
    }


    public static boolean isHappening(Date d1){
        int x, y;
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-h-m");
        java.util.Date current = new java.util.Date();
        String current_string = sdf.format(current);
        String [] parts = current_string.split("-");
        if(Integer.parseInt(parts[0]) == d1.getYear() && Integer.parseInt(parts[1]) == d1.getMonth() && Integer.parseInt(parts[2]) == d1.getDay()){
            if(Integer.parseInt(parts[3])<d1.getEnding_hour().getHour() && Integer.parseInt(parts[3])>d1.getStarting_hour().getHour()) return true;
            if(Integer.parseInt(parts[3])==d1.getStarting_hour().getHour() && Integer.parseInt(parts[4])>= d1.getStarting_hour().getMinute()) return true;
            if(Integer.parseInt(parts[3])==d1.getEnding_hour().getHour() && Integer.parseInt(parts[4])< d1.getEnding_hour().getMinute()) return true;
        }
        return false;
    }

    public Statement cria_ligacao_bd(){
        Statement stmt=null;
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found, check if the mysql-connector jar is reachable !");
            e.printStackTrace();
            return null;
        }
        System.out.println("JDBC Driver works .. attempting connection");

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/projecto_bd?autoReconnect=true&useSSL=false",
                    "root",
                    "password");
        } catch (SQLException e) {
            System.out.println("Ligacao falhou.. erro:");
            e.printStackTrace();
            return null;

        }

        if (connection != null) {
            System.out.println("Connected with success");
        } else {
            System.out.println("Connection not established");
        }
        try {

            if(connection.createStatement() == null){
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/projecto_bd?autoReconnect=true&useSSL=false",
                        "root",
                        "password");
            }

            if((stmt = connection.createStatement()) == null) {
                System.out.println("Erro nao foi possível criar uma statement ou retornou null");
                System.exit(-1);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stmt;

    }

    public static void main(String [] args) throws RemoteException {


        Config config = null;
        try {
            config =new Config();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PingServer pingServer = new PingServer("PING","127.0.0.1",config.getPortMainServer(),config.getPortBackupServer());
        try {
            pingServer.join();
        } catch (InterruptedException e) {
            System.out.println("Exception while joining thread");
        }


        ServerInterface si = new RMIServer();
        System.setProperty("java.rmi.server.hostname",config.getMainIp());
        try {
            LocateRegistry.createRegistry(my_port).rebind("server", si);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Servidor RMI ligado!");

        PingClient pingClient = new PingClient("PONG","127.0.0.1",config.getPortMainServer(),config.getPortBackupServer());

    }


}
