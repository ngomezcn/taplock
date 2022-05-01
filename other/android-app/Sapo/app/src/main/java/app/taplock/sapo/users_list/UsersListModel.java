package app.taplock.sapo.users_list;

import com.google.gson.JsonObject;

public class UsersListModel {

    private String email;
    private String password;
    private String identifier;
    private String individual_price;

    private String name;
    private String address;
    private int active_users;
    private int down_users;
    private int used_eKeys;
    private int available_eKeys;
    private JsonObject user_list;

    public UsersListModel(String email, String password, String identifier) {
        this.email = email;
        this.password = password;
        this.identifier = identifier;
    }

    public String getIndividual_price() {
        return individual_price;
    }

    public void setIndividual_price(String individual_price) {
        this.individual_price = individual_price;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setActive_users(int active_users) {
        this.active_users = active_users;
    }

    public void setDown_users(int down_users) {
        this.down_users = down_users;
    }

    public void setUsed_eKeys(int used_eKeys) {
        this.used_eKeys = used_eKeys;
    }

    public void setAvailable_eKeys(int available_eKeys) {
        this.available_eKeys = available_eKeys;
    }

    public void setUser_list(JsonObject user_list) {
        this.user_list = user_list;
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

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getActive_users() {
        return active_users;
    }

    public int getDown_users() {
        return down_users;
    }

    public int getUsed_eKeys() {
        return used_eKeys;
    }

    public int getAvailable_eKeys() {
        return available_eKeys;
    }

    public JsonObject getUser_list() {
        return user_list;
    }
}
