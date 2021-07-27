package app.taplock.sapo;

public class DataModal {

    // string variables for our name and job
    private String name;
    private String job;
    private String xd;

    private String password;
    private String email;
    private String message;

    public DataModal(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getXd() {
        return xd;
    }
}