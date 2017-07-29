import com.atlassian.jira.ComponentManager
import org.apache.log4j.Category
import com.atlassian.jira.issue.link.IssueLink;
import com.opensymphony.workflow.InvalidInputException

log = Category.getInstance("com.onresolve.jira.groovy.LinkedIssues")


IssueLinkManager linkMgr = ComponentManager.getInstance().getIssueLinkManager()

if (!linkMgr.getOutwardLinks(issue.getId())) {
    log.debug("no issue links exist")
    invalidInputException = new InvalidInputException("There must be at least 1 linked issue")
    throw invalidInputException

}
