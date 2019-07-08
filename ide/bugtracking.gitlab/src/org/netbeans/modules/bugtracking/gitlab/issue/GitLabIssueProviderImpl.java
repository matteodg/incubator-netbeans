/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.gitlab.issue;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import org.netbeans.modules.bugtracking.spi.IssueController;
import org.netbeans.modules.bugtracking.spi.IssueProvider;

/**
 * TODO delegates to the particular issue instance
 *
 */
public class GitLabIssueProviderImpl implements IssueProvider<GitLabIssueImpl> {

    @Override
    public String getDisplayName(GitLabIssueImpl i) {
        return i.getDisplayName();
    }

    @Override
    public String getTooltip(GitLabIssueImpl i) {
        return i.getTooltip();
    }

    @Override
    public String getID(GitLabIssueImpl i) {
        return i.getID();
    }

    @Override
    public Collection<String> getSubtasks(GitLabIssueImpl i) {
        return i.getSubtasks();
    }

    @Override
    public String getSummary(GitLabIssueImpl i) {
        return i.getSummary();
    }

    @Override
    public boolean isNew(GitLabIssueImpl i) {
        return i.isNew();
    }

    @Override
    public boolean isFinished(GitLabIssueImpl i) {
        return i.isFinished();
    }

    @Override
    public boolean refresh(GitLabIssueImpl i) {
        return i.refresh();
    }

    @Override
    public void addComment(GitLabIssueImpl i, String comment, boolean close) {
        i.getController().saveChanges();
        i.addComment(comment, close);
    }

    @Override
    public void attachFile(GitLabIssueImpl i, File file, String description, boolean isPatch) {
        i.attachFile(file, description, isPatch);
    }

    @Override
    public IssueController getController(GitLabIssueImpl i) {
        return i.getController();
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
