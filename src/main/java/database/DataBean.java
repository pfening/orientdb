package database;

public class DataBean {
    private String fname;
    private String lname;
    private String id;  
    private String excmod;  
    private String supervisor;  
    private String country;
    private String exc;
    private String status;
           
    public DataBean(){
    
    }

    public DataBean(String fname,String lname,String id,String excmod,String supervisor,String country) {
        this.fname = fname;
        this.lname = lname;
        this.id = id;  
        this.excmod = excmod;   
        this.supervisor = supervisor;  
        this.country = country;
    }
    
    public DataBean(String id,String exc,String status) {
        this.id = id;
        this.exc = exc;
        this.status = status;  
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExcmod() {
        return excmod;
    }

    public void setExcmod(String excmod) {
        this.excmod = excmod;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }  

    public String getExc() {
        return exc;
    }

    public void setExc(String exc) {
        this.exc = exc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
