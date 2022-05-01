package app.taplock.sapo.sign_in;

public class SignInModel {

    private String email;
    private String password;
    private String message;
    private String SYSTEM_CODE;

    public SignInModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getSYSTEM_CODE() {
        return SYSTEM_CODE;
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
