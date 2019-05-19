/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.bugtracking.codebeamer.repository;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;
import org.netbeans.modules.bugtracking.spi.RepositoryProvider;
import org.netbeans.modules.bugtracking.codebeamer.issue.CodeBeamerIssue;
import org.netbeans.modules.bugtracking.codebeamer.query.CodeBeamerQuery;

/**
* TODO delegates to the particular repository instance
 * @author tomas
 */
public class CodeBeamerRepositoryProvider implements RepositoryProvider<CodeBeamerRepository, CodeBeamerQuery, CodeBeamerIssue>{

    @Override
    public RepositoryInfo getInfo(CodeBeamerRepository r) {
        return r.getInfo();
    }

    @Override
    public Image getIcon(CodeBeamerRepository r) {
        return r.getIcon();
    }

    @Override
    public Collection<CodeBeamerIssue> getIssues(CodeBeamerRepository r, String... ids) {
        return r.getIssues(ids);
    }

    @Override
    public void removed(CodeBeamerRepository r) {
        r.remove();
    }

    @Override
    public RepositoryController getController(CodeBeamerRepository r) {
        return r.getController();
    }

    @Override
    public CodeBeamerQuery createQuery(CodeBeamerRepository r) {
        return r.createQuery();
    }

    @Override
    public CodeBeamerIssue createIssue(CodeBeamerRepository r) {
        return r.createIssue();
    }

    @Override
    public CodeBeamerIssue createIssue(CodeBeamerRepository r, String summary, String description) {
        return r.createIssue(summary, description);
    }

    @Override
    public Collection<CodeBeamerQuery> getQueries(CodeBeamerRepository r) {
        return r.getQueries();
    }

    @Override
    public Collection<CodeBeamerIssue> simpleSearch(CodeBeamerRepository r, String criteria) {
        return r.simpleSearch(criteria);
    }

    @Override
    public boolean canAttachFiles(CodeBeamerRepository r) {
        return r.canAttachFiles();
    }

    @Override
    public void removePropertyChangeListener(CodeBeamerRepository r, PropertyChangeListener listener) {
        r.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(CodeBeamerRepository r, PropertyChangeListener listener) {
        r.addPropertyChangeListener(listener);
    }
    
}
