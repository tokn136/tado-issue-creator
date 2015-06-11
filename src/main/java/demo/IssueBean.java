package demo;

import org.hibernate.validator.constraints.NotEmpty;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Thomas on 6/11/2015.
 */
public class IssueBean {
    private Integer number;
    private String title;
    private String body;
    private String url;

    public IssueBean() {
    }

    public IssueBean(JSONObject jsonObject) {
        title = getStringValueForKey(jsonObject, "title");
        body = getStringValueForKey(jsonObject, "body");
        number = getLongValueForKey(jsonObject, "number").intValue();
        url = getStringValueForKey(jsonObject, "html_url");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JSONObject getJSON(){
        JSONObject obj = new JSONObject();
        obj.put("title",title);
        obj.put("body", body);
        return obj;
    }

    private static String getStringValueForKey(JSONObject jsonObject, String key){
        if( jsonObject.containsKey(key)) {
            return (String) jsonObject.get(key);
        }
        return "";
    }

    private static Long getLongValueForKey(JSONObject jsonObject, String key){
        if( jsonObject.containsKey(key)) {
            return (Long) jsonObject.get(key);
        }
        return 0L;
    }

}
