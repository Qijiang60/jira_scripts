import java.util.Map
import java.util.HashMap
import java.util.Set
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder;
import com.atlassian.jira.issue.ModifiedValue;
import com.atlassian.jira.issue.index.IssueIndexManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.event.type.EventDispatchOption;
import com.atlassian.jira.issue.watchers.WatcherManager;

ComponentAccessor componentAccessor = new ComponentAccessor()
CustomFieldManager customFieldManager = componentAccessor.getCustomFieldManager()
IssueIndexManager indexManaager = componentAccessor.getIssueIndexManager()
IssueManager issueManager = componentAccessor.getIssueManager()
def userManager = ComponentAccessor.getUserManager()
def watcherManager = componentAccessor.getWatcherManager()
def groupManager = ComponentAccessor.getGroupManager()

String TEAM_ONE = "iOS"
String TEAM_TWO = "Android"

Map<String,String> teams = [
    "RD_iOS": TEAM_ONE,
    "RD_Android": TEAM_TWO,
]

MutableIssue issue = issue
CustomField teamField = customFieldManager.getCustomFieldObjectByName("Development Team")

def teamCode = issue.getCustomFieldValue(teamField)

if (teamCode.equals(null)) {
    return
}

String groupName = teams.find{ it.value == teamCode.toString() }?.key
def group = groupManager.getGroupObject(groupName)
Collection<String> usersInGroup = groupManager.getUserNamesInGroup(group)

for (String user : usersInGroup){
     if (null != user) {
         watcherManager.startWatching(userManager.getUserByKey(user), issue)
     }
}
