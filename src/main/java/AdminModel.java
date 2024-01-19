import org.bson.Document;

public class AdminModel {
    private String username;

    private String password;
    public AdminModel(String username,String password) {

        this.password = password;
        this.username = username;
    }

    public Document toDocument() {
        return new Document("username", username)
                .append("password", password);
    }

    public String getAdminName() {
        return username;
    }

    // Getters and setters (if needed)
}
