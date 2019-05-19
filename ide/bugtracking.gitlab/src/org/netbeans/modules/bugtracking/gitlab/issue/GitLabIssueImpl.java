/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.gitlab.issue;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import org.netbeans.modules.bugtracking.spi.IssueProvider;
import org.netbeans.modules.bugtracking.spi.IssueScheduleInfo;
import org.netbeans.modules.bugtracking.spi.IssueStatusProvider;
import org.netbeans.modules.bugtracking.spi.IssueStatusProvider.Status;
import org.openide.util.Exceptions;
import org.netbeans.modules.bugtracking.gitlab.GitLab;
import org.netbeans.modules.bugtracking.gitlab.repository.GitLabRepositoryImpl;

/**
 * TODO - represents a implementation particular issue instance. Accessed from
 * the bugtracking infrastructure via GitLabIssueProvider.
 *
 * @author tomas
 */
public class GitLabIssueImpl {

    private GitLabIssueController controller;
    private final GitLabRepositoryImpl repository;
    private String id;
    //
    private String remoteSummary;
    private String remoteDescription;
    private String[] remoteLabels;
    //
    private String localSummary;
    private String localDescription;
    private String[] localLabels;

    private IssueScheduleInfo scheduleInfo;
    private int estimate;
    private Date dueDate;

    public GitLabIssueImpl(GitLabRepositoryImpl repository, String id, String summary, String description, String[] labels) {
        this(repository, summary, description, labels);
        this.id = id;
    }

    public GitLabIssueImpl(GitLabRepositoryImpl repository, String summary, String description) {
        this(repository, summary, description, null);
    }

    public GitLabIssueImpl(GitLabRepositoryImpl repository, String summary, String description, String[] labels) {
        this(repository);
        this.remoteSummary = summary;
        this.remoteDescription = description;
        this.remoteLabels = labels;
    }

    public GitLabIssueImpl(GitLabRepositoryImpl repository) {
        this.repository = repository;
    }

    public String getDisplayName() {
        return id != null ? id + " - " + getSummary() : "New Task [" + (localSummary != null ? localSummary : "empty summary") + "]";
    }

    public String getTooltip() {
        return "This is " + getDisplayName();
    }

    public String getID() {
        return id;
    }

    public Collection<String> getSubtasks() {
        return Collections.emptyList();
    }

    public String getSummary() {
        return localSummary != null ? localSummary : remoteSummary;
    }

    public String getRemoteSummary() {
        return remoteSummary;
    }

    void setSummary(String summary) {
        if ("".equals(summary.trim())) {
            localSummary = null;
        } else {
            this.localSummary = summary;
        }
    }

    String getDescription() {
        return localDescription != null ? localDescription : remoteDescription;
    }

    String getRemoteDescription() {
        return remoteDescription;
    }

    void setDescription(String description) {
        if ("".equals(description)) {
            localDescription = null;
        }
        this.localDescription = description;
    }

    String[] getLabels() {
        return localLabels != null ? localLabels : remoteLabels;
    }

    String[] getRemoteLabels() {
        return remoteLabels;
    }

    void setLabels(String[] labels) {
        this.localLabels = labels;
    }

    public boolean isNew() {
        return id == null;
    }

    public boolean isFinished() {
        return false;
    }

    public boolean refresh() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
        fireDataChanged();
        return true;
    }

    public void open() {
        GitLab.getInstance().getSupport().openIssue(repository, this);
    }

    public IssueStatusProvider.Status getStatus() {
        if (id == null) {
            return Status.OUTGOING_NEW;
        }
        if (localDescription != null
                || localSummary != null
                || localLabels != null) {
            return Status.OUTGOING_MODIFIED;
        }
        return Status.SEEN;
    }

    void discardOutgoing() {
        getController().discardUnsavedChanges();
    }

    private void fireDataChanged() {
        support.firePropertyChange(IssueProvider.EVENT_ISSUE_DATA_CHANGED, null, null);
    }

    public void addComment(String comment, boolean close) {
        localDescription = localDescription + "\n\n" + comment;
        submit();
    }

    public void attachFile(File file, String description, boolean isPatch) {

    }

    public GitLabIssueController getController() {
        if (controller == null) {
            controller = new GitLabIssueController(this);
        }
        return controller;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public boolean submit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

        if (localSummary != null) {
            remoteSummary = localSummary;
        }
        if (localDescription != null) {
            remoteDescription = localDescription;
        }
        if (localLabels != null) {
            remoteLabels = localLabels;
        }
        localSummary = null;
        localDescription = null;
        localLabels = null;

        repository.addIssue(this);
        fireDataChanged();
        repository.fireUnsubmittedChanged();

        getController().reload();
        return true;
    }

    public void setID(String id) {
        this.id = id;
    }

    GitLabRepositoryImpl getRepository() {
        return repository;
    }

    void setDueDate(Date date) {
        this.dueDate = date;
        fireDataChanged();
    }

    void setSchedule(IssueScheduleInfo scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
        fireDataChanged();
    }

    void setEstimate(int hours) {
        this.estimate = hours;
        fireDataChanged();
    }

    Date getDueDate() {
        return dueDate;
    }

    IssueScheduleInfo getSchedule() {
        return scheduleInfo;
    }

    int getEstimate() {
        return estimate;
    }

    String getPriorityID() {
        return "" + ((Integer.parseInt(id) - 1) % 5 + 1);
    }

}
