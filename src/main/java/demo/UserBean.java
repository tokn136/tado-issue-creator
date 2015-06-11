package demo;

/**
 * Created by Thomas on 6/11/2015.
 */
public class UserBean {
    private boolean loggedIn;
    private String username;
    private String password;
    private String repository = "tado-issue-creator";

    public UserBean() {
    }

    public UserBean(String username, String password, String repository) {
        this.username = username;
        this.password = password;
        this.repository = repository;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.loggedIn = isLoggedIn;
    }
}
