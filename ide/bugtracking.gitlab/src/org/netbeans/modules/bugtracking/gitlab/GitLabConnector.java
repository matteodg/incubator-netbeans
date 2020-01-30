/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.gitlab;

import org.netbeans.modules.bugtracking.api.Repository;
import org.netbeans.modules.bugtracking.spi.BugtrackingConnector;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;
import org.netbeans.modules.bugtracking.gitlab.repository.GitLabRepositoryImpl;
import org.openide.util.NbBundle.Messages;

/**
 *
 */
@BugtrackingConnector.Registration(
        id = GitLabConnector.ID,
        displayName = "#GitLabConnector.displayName",
        tooltip = "#GitLabConnector.tooltip",
        iconPath = GitLab.ICON_PATH
)
@Messages({
    "GitLabConnector.displayName=GitLab",
    "GitLabConnector.tooltip=GitLab"
})
public class GitLabConnector implements BugtrackingConnector {

    public static final String ID = "org.netbeans.modules.bugtracking.gitlab.gitlabconnector"; // NOI18N

    private GitLabConnector() {
    }

    @Override
    public Repository createRepository() {
        GitLabRepositoryImpl repo = new GitLabRepositoryImpl();
        return createRepository(repo);
    }

    @Override
    public Repository createRepository(RepositoryInfo info) {
        GitLabRepositoryImpl repo = new GitLabRepositoryImpl(info);
        return createRepository(repo);
    }

    private Repository createRepository(GitLabRepositoryImpl repo) {
        return GitLab.getInstance().getBugtrackingSupport().createRepository(
                repo,
                GitLab.getInstance().getIssueStatusProvider(),
                GitLab.getInstance().getIssueScheduleProvider(),
                GitLab.getInstance().getIssuePriorityProvider(),
                GitLab.getInstance().getIssueFinder());
    }
}
