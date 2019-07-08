/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.gitlab.repository;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gitlab.api.AuthMethod;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.TokenType;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabLabel;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;
import org.netbeans.modules.bugtracking.spi.RepositoryProvider;
import org.netbeans.modules.bugtracking.spi.IssueStatusProvider.Status;
import org.netbeans.modules.bugtracking.gitlab.GitLab;
import org.netbeans.modules.bugtracking.gitlab.GitLabConnector;
import org.netbeans.modules.bugtracking.gitlab.issue.GitLabIssueImpl;
import org.netbeans.modules.bugtracking.gitlab.query.GitLabQueryImpl;
import org.openide.util.Exceptions;

/**
 * TODO - represents a implementation particular repository instance. Accessed
 * from the bugtracking infrastructure via GitLabRepositoryProvider.
 *
 */
public class GitLabRepositoryImpl {

    public static final String PROP_PERSONAL_ACCESS_TOKEN = "personalAccessToken"; // NOI18N
    public static final String PROP_PROJECT_ID = "projectId"; // NOI18N

    private RepositoryInfo info;
    private final Map<String, GitLabQueryImpl> queries = Collections.synchronizedMap(new HashMap<String, GitLabQueryImpl>());
    private final Set<GitLabIssueImpl> newIssues = Collections.synchronizedSet(new HashSet<GitLabIssueImpl>());

    // GitLab simulates repository
    private final Map<String, GitLabIssueImpl> issues = Collections.synchronizedMap(new HashMap<String, GitLabIssueImpl>());

    private GitLabRepositoryController controller;

    public GitLabRepositoryImpl() {
    }

    public GitLabRepositoryImpl(RepositoryInfo info) {
        this.info = info;
    }

    public RepositoryInfo getInfo() {
        return info;
    }

    Image getIcon() {
        return GitLab.ICON;
    }

    Collection<GitLabIssueImpl> getIssues(String[] ids) {
        List<GitLabIssueImpl> ret = new LinkedList<>();

        final String serverUrl = info.getUrl();
        final String personalAccessToken = info.getValue(PROP_PERSONAL_ACCESS_TOKEN);
        final String projectId = info.getValue(PROP_PROJECT_ID);

        GitlabAPI api = GitlabAPI.connect(serverUrl, personalAccessToken, TokenType.PRIVATE_TOKEN, AuthMethod.HEADER);
        for (String id : ids) {
            try {
                GitlabIssue issue = api.getIssue(projectId, Integer.parseInt(id));
                if (issue != null) {
                    String title = issue.getTitle();
                    String description = issue.getDescription();
                    String[] labels = issue.getLabels();
                    ret.add(new GitLabIssueImpl(this, id, title, description, labels));
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return ret;
    }

    void remove() {
        GitLab.getInstance().removeRepository(this);
    }

    RepositoryController getController() {
        if (controller == null) {
            controller = new GitLabRepositoryController(this);
        }
        return controller;
    }

    GitLabQueryImpl createQuery() {
        return new GitLabQueryImpl(this);
    }

    GitLabIssueImpl createIssue() {
        GitLabIssueImpl issue = new GitLabIssueImpl(this);
        newIssues.add(issue);
        return issue;
    }

    GitLabIssueImpl createIssue(String summary, String description) {
        GitLabIssueImpl issue = new GitLabIssueImpl(this, summary, description);
        newIssues.add(issue);
        return issue;
    }

    public Collection<GitLabQueryImpl> getQueries() {
        return queries.values();
    }

    Collection<GitLabIssueImpl> simpleSearch(String criteria) {
        List<GitLabIssueImpl> ret = new LinkedList<>();

        String serverUrl = info.getUrl();
        String personalAccessToken = info.getValue(PROP_PERSONAL_ACCESS_TOKEN);
        String projectId = info.getValue(PROP_PROJECT_ID);

        GitlabAPI api = GitlabAPI.connect(serverUrl, personalAccessToken, TokenType.PRIVATE_TOKEN, AuthMethod.HEADER);
        for (GitlabIssue issue : api.getIssues(projectId)) {
            int id = issue.getIid();
            String title = issue.getTitle();
            String description = issue.getDescription();
            String[] labels = issue.getLabels();
            ret.add(new GitLabIssueImpl(this, Integer.toString(id), title, description, labels));
        }

        return ret;
    }

    boolean canAttachFiles() {
        return true;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void addQuery(GitLabQueryImpl q) {
        queries.put(q.getDisplayName(), q);
        fireQueriesChanged();
    }

    public void removeQuery(GitLabQueryImpl q) {
        queries.remove(q.getDisplayName());
        fireQueriesChanged();
    }

    void setData(String name, String url, int projectId, String personalAccessToken) {
        String repositoryId = this.info != null ? this.info.getID() : GitLabConnector.ID + "-" + System.currentTimeMillis();

        RepositoryInfo ri = new RepositoryInfo(repositoryId, GitLabConnector.ID, url, name, url);
        ri.putValue(PROP_PERSONAL_ACCESS_TOKEN, personalAccessToken);
        ri.putValue(PROP_PROJECT_ID, Integer.toString(projectId));
        // TODO: add other properties about API and authentication

        this.info = ri;
    }

    public Collection<GitLabIssueImpl> getIssues() {
        return simpleSearch(null);
    }

    public void addIssue(GitLabIssueImpl issue) {
        if (issue.getID() == null) {
            issue.setID(issues.size() + 1 + "");
        }
        issues.put(issue.getID(), issue);
        newIssues.remove(issue);
    }

    private void fireQueriesChanged() {
        support.firePropertyChange(RepositoryProvider.EVENT_QUERY_LIST_CHANGED, null, null);
    }

    public void fireUnsubmittedChanged() {
        support.firePropertyChange(RepositoryProvider.EVENT_UNSUBMITTED_ISSUES_CHANGED, null, null);
    }

    public Collection<GitLabIssueImpl> getUnsubmittedIssues() {
        List<GitLabIssueImpl> ret = new LinkedList<>();
        for (GitLabIssueImpl i : issues.values()) {
            if (i.getStatus() == Status.OUTGOING_MODIFIED) {
                ret.add(i);
            }
        }
        ret.addAll(newIssues);
        return ret;
    }

    public Map<String, String> getLabels() {
        Map<String, String> ret = new LinkedHashMap<>();

        String serverUrl = info.getUrl();
        String personalAccessToken = info.getValue(PROP_PERSONAL_ACCESS_TOKEN);
        String projectId = info.getValue(PROP_PROJECT_ID);

        GitlabAPI api = GitlabAPI.connect(serverUrl, personalAccessToken, TokenType.PRIVATE_TOKEN, AuthMethod.HEADER);
        try {
            for (GitlabLabel label : api.getLabels(projectId)) {
                ret.put(label.getName(), label.getColor());
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        return ret;
    }

}
