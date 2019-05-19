/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.gitlab.issue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.netbeans.modules.bugtracking.spi.IssueController;
import org.openide.util.HelpCtx;
import org.openide.util.RequestProcessor;

/**
 * TODO - provides the UI for your particular issue instance and handles
 * interaction with the TopComponent in which it is visualized.
 *
 * @author tomas
 */
class GitLabIssueController implements IssueController, DocumentListener, ActionListener {

    private final GitLabIssueImpl issue;

    private GitLabIssuePanel panel;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public GitLabIssueController(GitLabIssueImpl issue) {
        this.issue = issue;
    }

    @Override
    public GitLabIssuePanel getComponent() {
        if (panel == null) {
            panel = new GitLabIssuePanel();

            reload();

            panel.summaryTextField.getDocument().addDocumentListener(this);
            panel.descTextArea.getDocument().addDocumentListener(this);
            panel.submitButton.addActionListener(this);
            panel.cancelButton.addActionListener(this);
        }
        return panel;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    public void opened() {
        // TODO - in case your impl needs to know the issue was opened in a TopComponent 
    }

    @Override
    public void closed() {

    }

    @Override
    public boolean saveChanges() {
        issue.setSummary(getComponent().summaryTextField.getText().trim());
        issue.setDescription(getComponent().descTextArea.getText().trim());
        fireSaved();
        return true;
    }

    @Override
    public boolean discardUnsavedChanges() {
        getComponent().summaryTextField.setText(issue.getRemoteSummary());
        getComponent().descTextArea.setText(issue.getRemoteDescription());
        issue.getRepository().fireUnsubmittedChanged();
        return true;
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public boolean isChanged() {
        return !getComponent().summaryTextField.getText().equals(issue.getSummary())
                || !getComponent().descTextArea.getText().equals(issue.getDescription());
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        support.firePropertyChange(PROP_CHANGED, null, null);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        support.firePropertyChange(PROP_CHANGED, null, null);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        support.firePropertyChange(PROP_CHANGED, null, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getComponent().submitButton) {
            saveChanges();
            RequestProcessor.getDefault().post(new Runnable() {
                @Override
                public void run() {
                    issue.submit();
                    setIssueID();
                }
            });
        } else if (e.getSource() == getComponent().cancelButton) {
            discardUnsavedChanges();
            fireSaved();
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // impl private
    //////////////////////////////////////////////////////////////////////////////////////
    private void fireSaved() {
        support.firePropertyChange(PROP_CHANGED, null, null);
        issue.getRepository().fireUnsubmittedChanged();
    }

    void reload() {
        panel.summaryTextField.setText(issue.getSummary());
        panel.descTextArea.setText(issue.getDescription());

        setIssueID();
    }

    private void setIssueID() {
        if (issue.getID() != null) {
            getComponent().issueLabel.setVisible(true);
            getComponent().cancelButton.setVisible(true);
            getComponent().issueLabel.setText("Issue #" + issue.getID() + " - " + issue.getSummary());
        } else {
            getComponent().issueLabel.setVisible(false);
            getComponent().cancelButton.setVisible(false);
        }
    }

}
