/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.bugtracking.codebeamer.issue;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import org.netbeans.modules.bugtracking.spi.IssueController;
import org.netbeans.modules.bugtracking.spi.IssueProvider;

/**
 * TODO delegates to the particular issue instance
 * @author tomas
 */
public class CodeBeamerIssueProvider implements IssueProvider<CodeBeamerIssue>{
    
    @Override
    public String getDisplayName(CodeBeamerIssue i) {
        return i.getDisplayName();
    }

    @Override
    public String getTooltip(CodeBeamerIssue i) {
        return i.getTooltip();
    }

    @Override
    public String getID(CodeBeamerIssue i) {
        return i.getID();
    }

    @Override
    public Collection<String> getSubtasks(CodeBeamerIssue i) {
        return i.getSubtasks();
    }

    @Override
    public String getSummary(CodeBeamerIssue i) {
        return i.getSummary();
    }

    @Override
    public boolean isNew(CodeBeamerIssue i) {
        return i.isNew();
    }

    @Override
    public boolean isFinished(CodeBeamerIssue i) {
        return i.isFinished();
    }

    @Override
    public boolean refresh(CodeBeamerIssue i) {
        return i.refresh();
    }

    @Override
    public void addComment(CodeBeamerIssue i, String comment, boolean close) {
        i.getController().saveChanges();
        i.addComment(comment, close);
    }

    @Override
    public void attachFile(CodeBeamerIssue i, File file, String description, boolean isPatch) {
        i.attachFile(file, description, isPatch);
    }

    @Override
    public IssueController getController(CodeBeamerIssue i) {
        return i.getController();
    }

    @Override
    public void removePropertyChangeListener(CodeBeamerIssue i, PropertyChangeListener listener) {
        i.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(CodeBeamerIssue i, PropertyChangeListener listener) {
        i.addPropertyChangeListener(listener);
    }
    
}
