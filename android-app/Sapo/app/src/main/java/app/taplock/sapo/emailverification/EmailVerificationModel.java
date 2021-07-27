package app.taplock.sapo.emailverification;

public class EmailVerificationModel {

    private String email;
    private String password;
    private Boolean is_active;
    private String token;
    private String SYSTEM_CODE;

    private String message;

    public EmailVerificationModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getSYSTEM_CODE() {
        return SYSTEM_CODE;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
