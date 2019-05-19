/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.bugtracking.codebeamer.query;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.netbeans.modules.bugtracking.spi.QueryProvider;
import org.openide.util.Exceptions;
import org.netbeans.modules.bugtracking.codebeamer.issue.CodeBeamerIssue;
import org.netbeans.modules.bugtracking.codebeamer.repository.CodeBeamerRepository;

/**
 *
 * @author tomas
 */
public class CodeBeamerQuery {
    private final CodeBeamerRepository repository;
    private String name;
    private Set<CodeBeamerIssue> issues = new HashSet<CodeBeamerIssue>();
    private CodeBeamerQueryController controller;
    private String summaryCriteria;

    public CodeBeamerQuery(CodeBeamerRepository repository) {
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

    public CodeBeamerQueryController getController() {
        if(controller == null) {
            controller = new CodeBeamerQueryController(this);
        }
        return controller;
    }

    public boolean canRemove() {
        // XXX return false if not possible - e.g. remote query
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

    public Collection<CodeBeamerIssue> getIssues() {
        return issues;
    }

    private QueryProvider.IssueContainer<CodeBeamerIssue> issueContainer;
    void setIssueContainer(QueryProvider.IssueContainer<CodeBeamerIssue> c) {
        // TODO use IssueContainer to notify about the progress of a query refresh,
        // as well as to provide the results 
        issueContainer = c;
    }
    
    void refresh() {
        // TODO execute your query and notify/provide results
        if(issueContainer != null) issueContainer.refreshingStarted();
        getController().refreshingStarted();
        try {
            sleep(1000);
            issues.clear();
            List<CodeBeamerIssue> repoIssues = repository.getIssues();
            if(repoIssues.isEmpty()) {
                
                // TODO no result - clear all issues 
                if(issueContainer != null) issueContainer.clear();
                
            } else {
                
                // TODO remove issues in case it's necessary
                // issueContainer.remove(i)
                
                // TODO add new issues to the query
                // in this example we simple remove all and the current result,
                // but we could deal only with the delta to be more effective
                if(issueContainer != null) issueContainer.clear();
                for (CodeBeamerIssue i : repoIssues) {
                    if(i.getSummary().toLowerCase().contains(summaryCriteria.toLowerCase())) {
                       issues.add(i);
                       if(issueContainer != null) issueContainer.add(i);
                       getController().add(i);
                       sleep(1000);
                    }
                }
            }
        } finally {
            if(issueContainer != null) issueContainer.refreshingFinished();
            getController().refreshingFinished();
        }
    }
    
    private void sleep(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    String getSummaryCriteria() {
        return summaryCriteria;
    }

    void setSummaryCriteria(String c) {
        summaryCriteria = c;
    }

    CodeBeamerRepository getRepository() {
        return repository;
    }

}
