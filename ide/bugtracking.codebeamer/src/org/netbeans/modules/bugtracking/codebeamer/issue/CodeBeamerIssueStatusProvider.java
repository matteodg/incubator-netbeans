/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.bugtracking.codebeamer.issue;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import org.netbeans.modules.bugtracking.spi.IssueStatusProvider;
import org.netbeans.modules.bugtracking.codebeamer.repository.CodeBeamerRepository;

/**
 * TODO delegate to your particular issue implementation
 * 
 * @author tomas
 */
public class CodeBeamerIssueStatusProvider implements IssueStatusProvider<CodeBeamerRepository, CodeBeamerIssue> {

    @Override
    public Status getStatus(CodeBeamerIssue i) {
        // TODO evaluate outgoign and incoming status
        // CodeBeamerIssue.getStatus() handles only local (ougoing) changes
        return i.getStatus();
    }

    @Override
    public void setSeenIncoming(CodeBeamerIssue i, boolean seen) {
        // TODO implement in case also incomnig changes are handled
    }

    @Override
    public Collection<CodeBeamerIssue> getUnsubmittedIssues(CodeBeamerRepository r) {
        return r.getUnsubmittedIssues();
    }

    @Override
    public void discardOutgoing(CodeBeamerIssue i) {
        i.discardOutgoing();
    }

    @Override
    public boolean submit(CodeBeamerIssue i) {
        i.getController().saveChanges();
        return i.submit();
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
