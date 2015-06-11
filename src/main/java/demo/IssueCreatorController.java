package demo;

import org.apache.catalina.User;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas on 6/11/2015.
 */

@PropertySources(value = {@PropertySource("classpath:application.properties")})
@Controller
@EnableAutoConfiguration
public class IssueCreatorController {

    private Map<HttpSession, UserDetails> userDetailsList = new HashMap<>();

    @ModelAttribute("loggedInUser")
    public UserDetails populateUser(HttpSession session) {
        return userDetailsList.get(session);
    }

    @RequestMapping(value = {"/", "/login"}, method=RequestMethod.GET)
    public ModelAndView homeController(HttpSession session) {
        UserDetails currentUser = userDetailsList.get(session);

        if(currentUser == null) {
            currentUser = new UserDetails();
        }

        if (currentUser.isLoggedIn()){
            return new ModelAndView("redirect:/issues/list");
        }

        return new ModelAndView("issues/login", "user", currentUser);
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ModelAndView postLogInController(HttpSession session,
                                            @Valid UserDetails userDetails,
                                            BindingResult result,
                                            RedirectAttributes redirect) {
        if(result.hasErrors()){
            return new ModelAndView("issues/login", "formErrors", result.getAllErrors());
        }

        userDetailsList.put(session, userDetails);
        userDetails.initGitHubConnection();

        if(userDetails.isLoggedIn()) {
            redirect.addFlashAttribute("globalMessage", "Successfully logged in @ GitHub!");
            return new ModelAndView("redirect:/issues/list");
        } else {
            redirect.addFlashAttribute("globalMessage", "Could not log in @ GitHub!");
            return new ModelAndView("redirect:/login");
        }
    }

    @RequestMapping("/issues/list")
    public ModelAndView showAllIssuesController(HttpSession session) {
        UserDetails currentUser = userDetailsList.get(session);
        Iterable<IssueBean> issues = currentUser.fetchAllIssues();
        return new ModelAndView("issues/list", "issues", issues);
    }

    @RequestMapping("/issues/{number}")
    public ModelAndView showAllIssuesController(
            HttpSession session,
            @PathVariable("number") Long number) {

        UserDetails currentUser = userDetailsList.get(session);
        IssueBean issue = new IssueBean(currentUser.fetchSingleIssue(number.intValue()));
        return new ModelAndView("issues/create", "issue", issue);
    }

    @RequestMapping(value="/issues/create", method=RequestMethod.GET)
    public ModelAndView createForm(HttpSession session) {
        return new ModelAndView("issues/create", "issue", new IssueBean());
    }

    @RequestMapping(value="/issues/create", method=RequestMethod.POST)
    public ModelAndView createIssueController(
                                            HttpSession session,
                                            @Valid IssueBean filledIssueBean, BindingResult result,
                                            RedirectAttributes redirect) {

        if(result.hasErrors()){
            return new ModelAndView("issues/create", "formErrors", result.getAllErrors());
        }

        boolean isNumberExistingInUserBean = (filledIssueBean != null && filledIssueBean.getNumber() != null);

        if (isNumberExistingInUserBean && filledIssueBean.getNumber() > 0){
            try {
                UserDetails currentUser = userDetailsList.get(session);
                currentUser.updateIssue(filledIssueBean.getJSON(), filledIssueBean.getNumber());
                redirect.addFlashAttribute("globalMessage", "Successfully update issue!");
            } catch (Exception e){
                redirect.addFlashAttribute("globalMessage", "Could not update issue!");
            }
        } else{
            try {
                UserDetails currentUser = userDetailsList.get(session);
                currentUser.createNewIssue(filledIssueBean.getJSON());
                redirect.addFlashAttribute("globalMessage", "Successfully created a new issue!");
            } catch (Exception e){
                redirect.addFlashAttribute("globalMessage", "Could not create issue!");
            }
        }
        return new ModelAndView("redirect:/issues/list");

    }

}
