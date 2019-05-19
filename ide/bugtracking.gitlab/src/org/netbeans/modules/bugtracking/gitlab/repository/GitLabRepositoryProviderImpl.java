/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.gitlab.repository;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;
import org.netbeans.modules.bugtracking.spi.RepositoryProvider;
import org.netbeans.modules.bugtracking.gitlab.issue.GitLabIssueImpl;
import org.netbeans.modules.bugtracking.gitlab.query.GitLabQueryImpl;

/**
 * Delegates to the particular repository instance
 *
 * @author tomas
 */
public class GitLabRepositoryProviderImpl implements RepositoryProvider<GitLabRepositoryImpl, GitLabQueryImpl, GitLabIssueImpl> {

    @Override
    public RepositoryInfo getInfo(GitLabRepositoryImpl r) {
        return r.getInfo();
    }

    @Override
    public Image getIcon(GitLabRepositoryImpl r) {
        return r.getIcon();
    }

    @Override
    public Collection<GitLabIssueImpl> getIssues(GitLabRepositoryImpl r, String... ids) {
        return r.getIssues(ids);
    }

    @Override
    public void removed(GitLabRepositoryImpl r) {
        r.remove();
    }

    @Override
    public RepositoryController getController(GitLabRepositoryImpl r) {
        return r.getController();
    }

    @Override
    public GitLabQueryImpl createQuery(GitLabRepositoryImpl r) {
        return r.createQuery();
    }

    @Override
    public GitLabIssueImpl createIssue(GitLabRepositoryImpl r) {
        return r.createIssue();
    }

    @Override
    public GitLabIssueImpl createIssue(GitLabRepositoryImpl r, String summary, String description) {
        return r.createIssue(summary, description);
    }

    @Override
    public Collection<GitLabQueryImpl> getQueries(GitLabRepositoryImpl r) {
        return r.getQueries();
    }

    @Override
    public Collection<GitLabIssueImpl> simpleSearch(GitLabRepositoryImpl r, String criteria) {
        return r.simpleSearch(criteria);
    }

    @Override
    public boolean canAttachFiles(GitLabRepositoryImpl r) {
        return r.canAttachFiles();
    }

    @Override
    public void removePropertyChangeListener(GitLabRepositoryImpl r, PropertyChangeListener listener) {
        r.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(GitLabRepositoryImpl r, PropertyChangeListener listener) {
        r.addPropertyChangeListener(listener);
    }
}
