// Count tasks moved to {Status} in time period 8-12:

import com.atlassian.jira.jql.builder.JqlClauseBuilder;
import com.atlassian.query.Query;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.web.bean.PagerFilter;
import org.apache.log4j.Category;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.bc.JiraServiceContext;
import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.bc.filter.SearchRequestService;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.query.Query;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.jira.issue.ModifiedValue;
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

int filterId = 31215
String changedToStatus = "Testing"
int periodStart = 8
int periodStop = 12

def Category log = Category.getInstance("com.onresolve.jira.groovy.PostFunction")
ComponentManager componentManager = ComponentManager.getInstance()
JiraAuthenticationContext authenticationContext = componentManager.getJiraAuthenticationContext()
JiraServiceContext ctx = new JiraServiceContextImpl(authenticationContext.getUser());
def user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
SearchProvider searchProvider = ComponentAccessor.getComponentOfType(SearchProvider.class);
def searchRequestService = componentManager.getSearchRequestService();
def sr = searchRequestService.getFilter(ctx, filterId);
def searchResult = searchProvider.search(sr?.getQuery(), user, PagerFilter.unlimitedFilter);
def issueManager = ComponentManager.getInstance().getIssueManager();
def issues = searchResult.getIssues().collect {issueManager.getIssueObject(it.id)};
def valuesn = [];
def changeHistoryManager = componentManager.getChangeHistoryManager()

issues.eachWithIndex { issue, index ->
    def createdDate = issue.getCreated();
    def changeLog = changeHistoryManager.getChangeItemsForField(issue, "status");
    changeLog.eachWithIndex { change, indexn ->
        if (change.toString == changedToStatus) {
                moveTime = change.getCreated().getTime();
            if (null != moveTime) {
                    Timestamp stamp = new Timestamp(moveTime);
                    Date date = new Date(stamp.getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+3"));
                    String formattedDate = sdf.format(date);
                    int transitionHour = Integer.parseInt(formattedDate.substring(0, 2))
                    if ((transitionHour >= periodStart) && (transitionHour < periodStop)){
                    valuesn.add(['"' + issue.getKey() + '"', '"' + formattedDate + '"']);
            }
            }

        }
    }
}
return valuesn.size();
