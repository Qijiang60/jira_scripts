// Get group members
import com.atlassian.crowd.embedded.api.User
import com.atlassian.jira.component.ComponentAccessor

String groupName = "test@test"

def userManager = ComponentAccessor.getUserManager()
def groupManager = ComponentAccessor.getGroupManager()
def group = userManager.getGroup(groupName)
Collection<User> usersInGroup = groupManager.getUsersInGroup(group)
return usersInGroup;
