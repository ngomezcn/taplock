package app.taplock.sapo.emailverification;

public class ResendVerificationModel {

    String email;
    String message;

    public ResendVerificationModel(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

