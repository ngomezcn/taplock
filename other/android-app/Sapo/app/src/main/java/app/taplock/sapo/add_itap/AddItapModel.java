package app.taplock.sapo.add_itap;

public class AddItapModel {
    private String email;
    private String password;

    private String message;
    private String activation_key;

    private String name;
    private String address;

    public AddItapModel(String email, String password, String name, String address, String activation_key) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.activation_key = activation_key;

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

    public String getActivation_key() {
        return activation_key;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
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

    public void setActivation_key(String activation_key) {
        this.activation_key = activation_key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
