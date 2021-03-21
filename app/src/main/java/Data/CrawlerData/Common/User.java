package Data.CrawlerData.Common;

public class User {
    String Username;
    String Password;

    public User(String username, String password) {
        Username = username.toLowerCase();
        Password = password;
    }

    public String getUsername() { return Username;}
    public String getPassword() { return Password;}
}
