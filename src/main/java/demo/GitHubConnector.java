package demo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Created by Thomas on 6/11/2015.
 */
public class GitHubConnector {

    private final static String GITHUB_URL = "https://api.github.com";
    private String gitHubUser;
    private String gitHubIssuesUrl;
    private String gitHubRepository;

    public GitHubConnector() {
    }

    public void init(UserDetails userDetails) {
        this.gitHubUser = userDetails.getUsername();
        this.gitHubRepository = userDetails.getRepository();
        this.gitHubIssuesUrl = GITHUB_URL + "/repos/" + gitHubUser + "/" + gitHubRepository + "/issues";
    }

    public static boolean getUserCredentialsCorrect(String username, String password){
        String gitHubBasicAuthentication = HttpConnector.getBasicAuthenticationString(username, password);
        boolean userCredentialsCorrect = false;
        try {
            int responseCode = HttpConnector.sendGetResponseCodeOnly(GITHUB_URL, gitHubBasicAuthentication);
            if(responseCode == 200){
                userCredentialsCorrect = true;
            }
        } catch (Exception e){
        }
        return userCredentialsCorrect;
    }

    public JSONObject fetchSingleIssue(int number) {
        JSONObject singleIssue = new JSONObject();
        String singleIssueUrl = gitHubIssuesUrl + "/" + number;
        try {
            String issueResponse = HttpConnector.sendGet(singleIssueUrl);
            singleIssue = (JSONObject) JSONValue.parse(issueResponse);
        } catch (Exception e) {
            System.out.println("Couldn't fetch issue " + number +" for user: " + gitHubUser + " and repository: " + gitHubRepository);
        }
        return singleIssue;
    }

    public JSONArray fetchAllIssues(){
        JSONArray issuesList = new JSONArray();
        try {
            String issuesResponse = HttpConnector.sendGet(gitHubIssuesUrl);
            issuesList = (JSONArray) JSONValue.parse(issuesResponse);
        } catch (Exception e) {
            System.out.println("Couldn't fetch issues for user: " + gitHubUser + " and repository: " + gitHubRepository);
        }
        return issuesList;
    }

    public void createNewIssue(JSONObject issueBean, String userName, String password) throws Exception{
        String gitHubBasicAuthentication = HttpConnector.getBasicAuthenticationString(userName, password);
        String issuePayload = issueBean.toJSONString();
        HttpConnector.sendPost(gitHubIssuesUrl, issuePayload, gitHubBasicAuthentication);
    }

    public void updateIssue(JSONObject issueBean, Integer number, String userName, String password) throws Exception{
        String gitHubBasicAuthentication = HttpConnector.getBasicAuthenticationString(userName, password);
        String issuePayload = issueBean.toJSONString();
        String updateIssueUrl = gitHubIssuesUrl + "/" + number;
        HttpConnector.sendPatch(updateIssueUrl, issuePayload, gitHubBasicAuthentication);
    }

}
