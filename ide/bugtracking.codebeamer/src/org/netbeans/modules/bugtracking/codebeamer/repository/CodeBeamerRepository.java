/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.bugtracking.codebeamer.repository;

import org.netbeans.modules.bugtracking.codebeamer.CodeBeamer;
import org.netbeans.modules.bugtracking.codebeamer.CodeBeamerConnector;
import org.netbeans.modules.bugtracking.codebeamer.issue.CodeBeamerIssue;
import org.netbeans.modules.bugtracking.codebeamer.query.CodeBeamerQuery;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.netbeans.modules.bugtracking.spi.IssueStatusProvider.Status;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;
import org.netbeans.modules.bugtracking.spi.RepositoryProvider;

/**
 * TODO - represents a implementation particular repository instance. 
 * Accessed from the bugtracking infrastructure via CodeBeamerRepositoryProvider.
 * 
 * @author tomas
 */
public class CodeBeamerRepository {
    private RepositoryInfo info;
    private final Map<String, CodeBeamerQuery> queries = Collections.synchronizedMap(new HashMap<String, CodeBeamerQuery>());

    private final Set<CodeBeamerIssue> newIssues = Collections.synchronizedSet(new HashSet<CodeBeamerIssue>());
    
    // CodeBeamer simulates repository
    private final Map<String, CodeBeamerIssue> issues = Collections.synchronizedMap(new HashMap<String, CodeBeamerIssue>());
    
    private CodeBeamerRepositoryController controller;

    public CodeBeamerRepository() {
        for (int i = 0; i < 5; i++) {
            createIssueInternal();
        }
    }

    public CodeBeamerRepository(RepositoryInfo info) {
        this();
        this.info = info;
    }

    public RepositoryInfo getInfo() {
        return info;
    }

    Image getIcon() {
        return CodeBeamer.ICON;
    }

    Collection<CodeBeamerIssue> getIssues(String[] ids) {
        List <CodeBeamerIssue> ret = new LinkedList<CodeBeamerIssue>();
        for (String id : ids) {
            CodeBeamerIssue i = issues.get(id);
            if(i != null) {
                ret.add(i);
            }
        }
        return ret;
    }

    void remove() {
        CodeBeamer.getInstance().removeRepository(this);
    }

    RepositoryController getController() {
        if(controller == null) {
            controller = new CodeBeamerRepositoryController(this);
        }
        return controller;
    }

    CodeBeamerQuery createQuery() {
        return new CodeBeamerQuery(this);
    }

    CodeBeamerIssue createIssue() {
        CodeBeamerIssue issue = new CodeBeamerIssue(this);
        newIssues.add(issue);
        return issue;
    }

    CodeBeamerIssue createIssue(String summary, String description) {
        CodeBeamerIssue issue = new CodeBeamerIssue(this, summary, description);
        newIssues.add(issue);
        return issue;
    }

    public Collection<CodeBeamerQuery> getQueries() {
        return queries.values();
    }

    Collection<CodeBeamerIssue> simpleSearch(String criteria) {
        // CodeBeamer execute remotelly 
        // for now we just fake ...
        
        List <CodeBeamerIssue> ret = new LinkedList<CodeBeamerIssue>();
        for(CodeBeamerIssue i : issues.values()) {
            if(i.getSummary().equals(criteria)) {
                ret.add(i);
            }
        }    
        CodeBeamerIssue i = issues.get(criteria);
        if(i != null) {
            ret.add(i);
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
    
    public void addQuery(CodeBeamerQuery q) {
        queries.put(q.getDisplayName(), q);
        fireQueriesChanged();
    }

    public void removeQuery(CodeBeamerQuery q) {
        queries.remove(q.getDisplayName());
        fireQueriesChanged();
    }
    
    private CodeBeamerIssue createIssueInternal() {
        String id = issues.size() + 1 + "";
        final CodeBeamerIssue issue = new CodeBeamerIssue(this, id, "Issue " + id, "This is a demo connector issue with id " + id);
        issues.put(id, issue);
        return issue;
    }

    void setData(String name, String url) {
        RepositoryInfo ri = new RepositoryInfo(
                info != null ? info.getID() : CodeBeamerConnector.ID + "-" + System.currentTimeMillis(), 
                CodeBeamerConnector.ID, 
                url, 
                name, 
                "This is " + name);
        info = ri;
    }

    public List<CodeBeamerIssue> getIssues() {
        return new ArrayList<CodeBeamerIssue>(issues.values());
    }

    public void addIssue(CodeBeamerIssue issue) {
        if(issue.getID() == null) {
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

    public Collection<CodeBeamerIssue> getUnsubmittedIssues() {
        List<CodeBeamerIssue> ret = new LinkedList<CodeBeamerIssue>();
        for(CodeBeamerIssue i : issues.values()) {
            if(i.getStatus() == Status.OUTGOING_MODIFIED) 
            {
                ret.add(i);
            }
        }
        ret.addAll(newIssues);
        return ret;
    }

}
