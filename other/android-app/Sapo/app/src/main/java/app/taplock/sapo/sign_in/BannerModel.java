package app.taplock.sapo.sign_in;

public class BannerModel {

    private String message;
    private String url;
    private int type;

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }

    public int getType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(int type) {
        this.type = type;
    }
}
