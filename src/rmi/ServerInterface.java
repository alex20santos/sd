package rmi;
import java.rmi.*;
import java.text.ParseException;
import java.util.ArrayList;

public interface ServerInterface extends Remote{
    public ArrayList<Election> getElections() throws  RemoteException;

    void addUser(Department department, int user_id, String password, String contact,
                 String address, String id_number, Date id_expiration_date, String tag) throws RemoteException;

    Person searchUser(String id) throws RemoteException;

    Election searchElection(String election) throws RemoteException;

    boolean checkPassword(String username,String password) throws RemoteException;

    void createElectionConselhoGeral(String name,String description,Date date) throws RemoteException;

    void createElectionNucleo(String name,String description,Date date,Department department) throws RemoteException;

    public void createDepartment(String name) throws RemoteException;

    public void editDepartmentName(Department department, String newName) throws RemoteException;

    void registerVote(Election election,Vote vote,Person person) throws RemoteException;

    public boolean loginVoter(Election election,Person person,String password) throws RemoteException;

    public boolean loginDelegate(VotingTable table,Person person,String password) throws RemoteException;

    Department searchDepartmentByName(String name) throws RemoteException;

    void deleteDepartment(Department depart) throws RemoteException;

    void editElectionName(Election e,String n_string) throws RemoteException;

    void editElectionDate(Election e, Date d) throws RemoteException;

    void editElectionDescription(Election e, String n_string) throws RemoteException;

    void editUserExpirationDate(String id_number, Date date) throws RemoteException;

    void editUserAddress(String id_number, String n_m) throws RemoteException;

    void editUserContact(String id_number, String n_num) throws RemoteException;

    void editUserDepartment(String id_number, Department n_dep) throws RemoteException;

    void editUserFunction(String id_number, String n_func) throws RemoteException;

    void createElectionDepartment(String name, String description, Date d, Department department) throws RemoteException;

    void removeListFromElection(Election e, String name) throws RemoteException;

    void addCandidateToElection(Election e, Candidate n_cand) throws RemoteException;

    int[] registerTable(String ip,String department) throws RemoteException;

    public ArrayList<Election> getElectionsNow() throws ParseException,RemoteException;

    ArrayList<Election> getElectionsByUser(String tag,Department user_dep) throws RemoteException, ParseException;

    void addMemberToVotingTable(String id, VotingTable v) throws  RemoteException;

    void removeMemberFromVotingTable(String id, VotingTable v) throws RemoteException;

    void changeVotingTableDelegate(String id, VotingTable v) throws RemoteException;

    ArrayList<Election> getPreviousElections() throws RemoteException;

    boolean verifyUserID(String id_number) throws RemoteException;

    boolean verifyDepartment(String name) throws RemoteException;

    void editPassword(String id_number, String n_password) throws RemoteException;

    ArrayList<VotingTable> getVotingTables() throws  RemoteException;

    ArrayList<Vote> getPreviousVotes(String id) throws RemoteException;

    ArrayList<Candidate> getCandidates(Election election) throws  RemoteException;

    Candidate getCandidateByName(String candidate,Election election) throws  RemoteException;

    ArrayList<Election> getElectionsByUserNow(String tag,Department user_dep) throws  RemoteException;

    int getNumberOfVotes(Election e) throws RemoteException;

    int getNumberOfVotesOfCandidate(Candidate c) throws RemoteException;
    
    String electionInfoRealTime(String id) throws RemoteException;
}
