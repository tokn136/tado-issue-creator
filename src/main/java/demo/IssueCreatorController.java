package demo;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Thomas on 6/11/2015.
 */

@PropertySources(value = {@PropertySource("classpath:application.properties")})

@Controller
@EnableAutoConfiguration
public class IssueCreatorController {

    private UserBean loggedInUser;
    private GitHubConnector gitHubConnector;

    @ModelAttribute("loggedInUser")
    public UserBean populateUser() {
        return loggedInUser;
    }

    @RequestMapping(value = {"/", "/login"}, method=RequestMethod.GET)
    public ModelAndView homeController() {
        if(loggedInUser == null) {
            loggedInUser = new UserBean();
        }
        if (loggedInUser.isLoggedIn()){
            return new ModelAndView("redirect:/issues/list");
        }
        return new ModelAndView("issues/login", "user", loggedInUser);
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ModelAndView postLogInController(@Valid UserBean filledUserBean, BindingResult result,
                                            RedirectAttributes redirect) {
        if(result.hasErrors()){
            return new ModelAndView("issues/login", "formErrors", result.getAllErrors());
        }

        gitHubConnector = new GitHubConnector(filledUserBean);
        loggedInUser = filledUserBean;
        loggedInUser.setIsLoggedIn(gitHubConnector.getUserCredentialsCorrect());
        if(loggedInUser.isLoggedIn()) {
            redirect.addFlashAttribute("globalMessage", "Successfully logged in @ GitHub!");
            return new ModelAndView("redirect:/issues/list");
        } else {
            redirect.addFlashAttribute("globalMessage", "Could not log in @ GitHub!");
            return new ModelAndView("redirect:/login");
        }
    }

    @RequestMapping("/issues/list")
    public ModelAndView showAllIssuesController() {
        Iterable<IssueBean> issues = gitHubConnector.fetchAllIssues();
        return new ModelAndView("issues/list", "issues", issues);
    }

    @RequestMapping("/issues/{number}")
    public ModelAndView showAllIssuesController(@PathVariable("number") Long number) {
        IssueBean issue = new IssueBean(gitHubConnector.fetchSingleIssue(number.intValue()));
        return new ModelAndView("issues/create", "issue", issue);
    }

    @RequestMapping("/issues/{gitHubUser}/{gitHubRepository}")
    public ModelAndView showIssuesController(
            @PathVariable("gitHubUser") String gitHubUser,
            @PathVariable("gitHubRepository") String gitHubRepository) {
        if(gitHubConnector == null){
            gitHubConnector = new GitHubConnector(gitHubUser, gitHubRepository);
        }
        Iterable<IssueBean> issues = gitHubConnector.fetchAllIssues();
        return new ModelAndView("issues/list", "issues", issues);
    }

    @RequestMapping(value="/issues/create", method=RequestMethod.GET)
    public ModelAndView createForm() {
        return new ModelAndView("issues/create", "issue", new IssueBean());
    }

    @RequestMapping(value="/issues/create", method=RequestMethod.POST)
    public ModelAndView postLogInController(@Valid IssueBean filledIssueBean, BindingResult result,
                                            RedirectAttributes redirect) {

        if(result.hasErrors()){
            return new ModelAndView("issues/form", "formErrors", result.getAllErrors());
        }

        if (filledIssueBean != null && filledIssueBean.getNumber() != null && filledIssueBean.getNumber() > 0){
            try {
                gitHubConnector.updateIssue(filledIssueBean.getJSON(), filledIssueBean.getNumber());
                redirect.addFlashAttribute("globalMessage", "Successfully update issue!");
            } catch (Exception e){
                redirect.addFlashAttribute("globalMessage", "Could not update issue!");
            }
        } else{
            try {
                gitHubConnector.createNewIssue(filledIssueBean.getJSON());
                redirect.addFlashAttribute("globalMessage", "Successfully created a new issue!");
            } catch (Exception e){
                redirect.addFlashAttribute("globalMessage", "Could not create issue!");
            }
        }
        return new ModelAndView("redirect:/issues/list");

    }

}
