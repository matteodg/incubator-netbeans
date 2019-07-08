/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.gitlab.issue;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import org.netbeans.modules.bugtracking.spi.IssueStatusProvider;
import org.netbeans.modules.bugtracking.gitlab.repository.GitLabRepositoryImpl;

/**
 * TODO delegate to your particular issue implementation
 *
 */
public class GitLabIssueStatusProviderImpl implements IssueStatusProvider<GitLabRepositoryImpl, GitLabIssueImpl> {

    @Override
    public Status getStatus(GitLabIssueImpl i) {
        // TODO evaluate outgoign and incoming status
        // GitLabIssueImpl.getStatus() handles only local (ougoing) changes
        return i.getStatus();
    }

    @Override
    public void setSeenIncoming(GitLabIssueImpl i, boolean seen) {
        // TODO implement in case also incomnig changes are handled
    }

    @Override
    public Collection<GitLabIssueImpl> getUnsubmittedIssues(GitLabRepositoryImpl r) {
        return r.getUnsubmittedIssues();
    }

    @Override
    public void discardOutgoing(GitLabIssueImpl i) {
        i.discardOutgoing();
    }

    @Override
    public boolean submit(GitLabIssueImpl i) {
        i.getController().saveChanges();
        return i.submit();
    }

    @Override
    public void removePropertyChangeListener(GitLabIssueImpl i, PropertyChangeListener listener) {
        i.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(GitLabIssueImpl i, PropertyChangeListener listener) {
        i.addPropertyChangeListener(listener);
    }

}
