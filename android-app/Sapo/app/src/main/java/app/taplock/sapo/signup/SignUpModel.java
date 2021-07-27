package app.taplock.sapo.signup;

public class SignUpModel {

    // string variables for our name and job
    private String email;
    private String password;
    private String name;
    private String phone;
    private Boolean terms_of_service;
    private String SYSTEM_CODE;
    private String message;

    public SignUpModel(String email, String password, String name, String phone, Boolean terms_of_service) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.terms_of_service = terms_of_service;

    }


    public String getSYSTEM_CODE() {
        return SYSTEM_CODE;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getTerms_of_service() {
        return terms_of_service;
    }

    public void setTerms_of_service(Boolean terms_of_service) {
        this.terms_of_service = terms_of_service;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}