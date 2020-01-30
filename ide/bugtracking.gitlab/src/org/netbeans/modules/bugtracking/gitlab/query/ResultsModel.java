package org.netbeans.modules.bugtracking.gitlab.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import org.netbeans.modules.bugtracking.gitlab.issue.GitLabIssueImpl;
import org.netbeans.modules.bugtracking.spi.IssueStatusProvider.Status;
import org.netbeans.spi.viewmodel.ColumnModel;
import org.netbeans.spi.viewmodel.ModelEvent;
import org.netbeans.spi.viewmodel.ModelListener;
import org.netbeans.spi.viewmodel.NodeModel;
import org.netbeans.spi.viewmodel.TableModel;
import org.netbeans.spi.viewmodel.TreeModel;
import org.netbeans.spi.viewmodel.UnknownTypeException;
import org.openide.util.NbBundle.Messages;

public class ResultsModel implements NodeModel, TreeModel, TableModel {

    private final GitLabQueryImpl query;
    private final List<ModelListener> modelListeners = new ArrayList<>();
    private Set<GitLabIssueImpl> issues;

    public ResultsModel(GitLabQueryImpl query) {
        this.query = query;
    }

    public void setIssues(Set<GitLabIssueImpl> issues) {
        this.issues = issues;
        ModelEvent event = new ModelEvent.TreeChanged(this);
        for (ModelListener l : modelListeners) {
            l.modelChanged(event);
        }
    }

    private static <T> T processNode(Object node, Function<GitLabIssueImpl, T> func) throws UnknownTypeException {
        if (node instanceof GitLabIssueImpl) {
            return func.apply((GitLabIssueImpl) node);
        } else {
            throw new UnknownTypeException(node);
        }
    }

    //<editor-fold defaultstate="collapsed" desc=" NodeModel ">
    @Override
    public String getDisplayName(Object node) throws UnknownTypeException {
        return processNode(node, GitLabIssueImpl::getID);
    }

    @Override
    public String getIconBase(Object node) throws UnknownTypeException {
        return processNode(node, issue -> null);
    }

    @Override
    public String getShortDescription(Object node) throws UnknownTypeException {
        return processNode(node, GitLabIssueImpl::getTooltip);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" TreeModel ">
    @Override
    public Object getRoot() {
        return ROOT;
    }

    @Override
    public Object[] getChildren(Object parent, int from, int to) throws UnknownTypeException {
        if (parent == ROOT) {
            List<GitLabIssueImpl> issues = new ArrayList<>(query.getIssues());
            if (to == -1) {
                to = issues.size();
            }
            List<GitLabIssueImpl> subList = issues.subList(from, to);
            return subList.toArray(new GitLabIssueImpl[subList.size()]);
        }
        return processNode(parent, issue -> new Object[0]);
    }

    @Override
    public boolean isLeaf(Object node) throws UnknownTypeException {
        return node instanceof GitLabIssueImpl;
    }

    @Override
    public int getChildrenCount(Object node) throws UnknownTypeException {
        return getChildren(node, 0, -1).length;
    }

    @Override
    public void addModelListener(ModelListener l) {
        this.modelListeners.add(l);
    }

    @Override
    public void removeModelListener(ModelListener l) {
        this.modelListeners.remove(l);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" TableModel ">
    @Override
    public Object getValueAt(Object node, String columnID) throws UnknownTypeException {
        return processNode(node, issue -> {
            if (SummaryColumnModel.ID.equals(columnID)) {
                return issue.getSummary();
            } else if (StatusColumnModel.ID.equals(columnID)) {
                return issue.getStatus();
            }
            return null;
        });
    }

    @Override
    public boolean isReadOnly(Object node, String columnID) throws UnknownTypeException {
        return processNode(node, issue -> Boolean.TRUE);
    }

    @Override
    public void setValueAt(Object node, String columnID, Object value) throws UnknownTypeException {
        // do nothing
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" SummaryColumnModel ">
    @Messages("SummaryColumnModel.displayName=Summary")
    public static class SummaryColumnModel extends ColumnModel {

        private static final String ID = "summary"; // NOI18N

        @Override
        public String getID() {
            return ID;
        }

        @Override
        public String getDisplayName() {
            return Bundle.SummaryColumnModel_displayName();
        }

        @Override
        public Class getType() {
            return String.class;
        }

        @Override
        public boolean isSortable() {
            return true;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" StatusColumnModel ">
    @Messages("StatusColumnModel.displayName=Status")
    public static class StatusColumnModel extends ColumnModel {

        private static final String ID = "status"; // NOI18N

        @Override
        public String getID() {
            return ID;
        }

        @Override
        public String getDisplayName() {
            return Bundle.StatusColumnModel_displayName();
        }

        @Override
        public Class getType() {
            return Status.class;
        }

        @Override
        public boolean isSortable() {
            return true;
        }
    }
    //</editor-fold>
}
