/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.gitlab.query;

import org.netbeans.modules.bugtracking.spi.QueryController;
import org.netbeans.modules.bugtracking.spi.QueryProvider;
import org.netbeans.modules.bugtracking.gitlab.issue.GitLabIssueImpl;

/**
 *
 */
public class GitLabQueryProviderImpl implements QueryProvider<GitLabQueryImpl, GitLabIssueImpl> {

    @Override
    public String getDisplayName(GitLabQueryImpl q) {
        return q.getDisplayName();
    }

    @Override
    public String getTooltip(GitLabQueryImpl q) {
        return q.getTooltip();
    }

    @Override
    public QueryController getController(GitLabQueryImpl q) {
        return q.getController();
    }

    @Override
    public boolean canRemove(GitLabQueryImpl q) {
        return q.canRemove();
    }

    @Override
    public void remove(GitLabQueryImpl q) {
        q.remove();
    }

    @Override
    public boolean canRename(GitLabQueryImpl q) {
        return q.canRename();
    }

    @Override
    public void rename(GitLabQueryImpl q, String newName) {
        q.rename(newName);
    }

    @Override
    public void refresh(GitLabQueryImpl q) {
        q.getController().refresh();
    }

    @Override
    public void setIssueContainer(GitLabQueryImpl q, IssueContainer<GitLabIssueImpl> c) {
        q.setIssueContainer(c);
    }

}
