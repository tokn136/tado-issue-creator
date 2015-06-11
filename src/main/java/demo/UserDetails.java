package demo;

import org.json.simple.JSONObject;

/**
 * Created by Thomas on 6/11/2015.
 */
public class UserDetails extends GitHubConnector{
    private Boolean loggedIn;
    private String username;
    private String password;
    private String repository = "tado-issue-creator";


    public UserDetails() {
    }

    public void initGitHubConnection(){
        init(this);
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
        if(loggedIn == null) {
            loggedIn = getUserCredentialsCorrect(username, password);
        }
        return loggedIn;
    }

    public void updateIssue(JSONObject issueBean, Integer number) throws Exception {
        updateIssue(issueBean, number, username, password);
    }

    public void createNewIssue(JSONObject issueBean) throws Exception {
        createNewIssue(issueBean, username, password);
    }

}
