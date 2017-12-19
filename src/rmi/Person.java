package rmi;
import java.io.Serializable;
import java.util.ArrayList;

public class Person implements Serializable {
    Department department;
    int user_id;// not used
    String name;
    String password;
    String contact, address, id_number;
    Date id_expiration_date;
    String tag; // student, teacher or worker

    public Person(Department department, int user_id, String password, String contact, String address, String id_number, Date id_expiration_date,String tag) {
        this.department = department;
        this.user_id = user_id;
        this.password = password;
        this.contact = contact;
        this.address = address;
        this.id_number = id_number;
        this.id_expiration_date = id_expiration_date;
        this.tag = tag;
    }
    
    public Person(Department department, String password, String contact, String address, String id_number, Date id_expiration_date,String tag) {
        this.department = department;
        this.password = password;
        this.contact = contact;
        this.address = address;
        this.id_number = id_number;
        this.id_expiration_date = id_expiration_date;
        this.tag = tag;
    }
    

    public Person(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public Date getId_expiration_date() {
        return id_expiration_date;
    }

    public void setId_expiration_date(Date id_expiration_date) {
        this.id_expiration_date = id_expiration_date;
    }



    @Override
    public String toString() {
        return "Person{" +
                "department=" + department +
                ", user_id=" + user_id +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                ", id_number='" + id_number + '\'' +
                ", id_expiration_date=" + id_expiration_date +
                ", tag='" + tag + '\'' +
                '}';
    }
}

