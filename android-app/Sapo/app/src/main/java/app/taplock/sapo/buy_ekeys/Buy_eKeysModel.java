package app.taplock.sapo.buy_ekeys;

public class Buy_eKeysModel {

    private String email;
    private String password;
    private String identifier;
    private String amount;
    private String message;
    private String url;

    public Buy_eKeysModel(String email, String password, String identifier, String amount) {
        this.email = email;
        this.password = password;
        this.identifier = identifier;
        this.amount = amount;

    }

    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
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

    public String getAmount() {
        return amount;
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

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
