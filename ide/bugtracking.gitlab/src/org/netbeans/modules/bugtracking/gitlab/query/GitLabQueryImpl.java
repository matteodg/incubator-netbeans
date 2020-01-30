/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.gitlab.query;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.netbeans.modules.bugtracking.spi.QueryProvider;
import org.netbeans.modules.bugtracking.gitlab.issue.GitLabIssueImpl;
import org.netbeans.modules.bugtracking.gitlab.repository.GitLabRepositoryImpl;
import org.netbeans.modules.bugtracking.issuetable.ColumnDescriptor;

/**
 *
 */
public class GitLabQueryImpl {

    private final GitLabRepositoryImpl repository;
    private String name;
    private final Set<GitLabIssueImpl> issues = new LinkedHashSet<>();
    private GitLabQueryController controller;
    private String summaryCriteria;

    public GitLabQueryImpl(GitLabRepositoryImpl repository) {
        this.repository = repository;
        summaryCriteria = "";
    }

    public String getDisplayName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
        repository.addQuery(this);
    }

    public String getTooltip() {
        return name != null ? "This is " + name : null;
    }

    public GitLabQueryController getController() {
        if (controller == null) {
//            controller = new GitLabQueryController(this);
        }
        return controller;
    }

    public ColumnDescriptor[] getColumnDescriptors() {
        return new ColumnDescriptor[0];
    }

    public boolean isSaved() {
        return true;
    }

    public boolean canRemove() {
        // GitLab return false if not possible - e.g. remote query
        return true;
    }

    public void remove() {
        repository.removeQuery(this);
    }

    public boolean canRename() {
        return true;
    }

    public void rename(String newName) {
        name = newName;
    }

    public Collection<GitLabIssueImpl> getIssues() {
        return issues;
    }

    private QueryProvider.IssueContainer<GitLabIssueImpl> issueContainer;

    void setIssueContainer(QueryProvider.IssueContainer<GitLabIssueImpl> c) {
        // TODO use IssueContainer to notify about the progress of a query refresh,
        // as well as to provide the results 
        issueContainer = c;
    }

    void refresh() {
        // TODO execute your query and notify/provide results
        if (issueContainer != null) {
            issueContainer.refreshingStarted();
        }
        getController().refreshingStarted();
        try {
            // name, color labels map
            Map<String, String> labels = repository.getLabels();
            getController().setLabels(labels);

            issues.clear();
            Collection<GitLabIssueImpl> repoIssues = repository.getIssues();
            if (repoIssues.isEmpty()) {

                // TODO no result - clear all issues 
                if (issueContainer != null) {
                    issueContainer.clear();
                }

            } else {

                // TODO remove issues in case it's necessary
                // issueContainer.remove(i)
                // TODO add new issues to the query
                // in this example we simple remove all and the current result,
                // but we could deal only with the delta to be more effective
                if (issueContainer != null) {
                    issueContainer.clear();
                }
                for (GitLabIssueImpl issue : repoIssues) {
                    if (issue.getSummary().toLowerCase().contains(summaryCriteria.toLowerCase())) {
                        issues.add(issue);
                        if (issueContainer != null) {
                            issueContainer.add(issue);
                        }
                    }
                }
                getController().setIssues(issues);
            }
        } finally {
            if (issueContainer != null) {
                issueContainer.refreshingFinished();
            }
            getController().refreshingFinished();
        }
    }

    String getSummaryCriteria() {
        return summaryCriteria;
    }

    void setSummaryCriteria(String c) {
        summaryCriteria = c;
    }

    GitLabRepositoryImpl getRepository() {
        return repository;
    }

}
