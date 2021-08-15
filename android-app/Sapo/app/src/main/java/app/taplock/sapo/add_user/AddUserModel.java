package app.taplock.sapo.add_user;

public class AddUserModel {

    String email, password, identifier,message, token, name, phone;

    public AddUserModel(String email, String password, String identifier, String user_name, String user_phone) {
        this.email = email;
        this.password = password;
        this.identifier = identifier;
        this.name = user_name;
        this.phone = user_phone;

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getIdentifier() {
        return identifier;
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

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
