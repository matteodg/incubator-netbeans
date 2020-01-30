/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.gitlab.query;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.netbeans.modules.bugtracking.spi.QueryController;
import org.openide.util.HelpCtx;
import org.openide.util.RequestProcessor;
import org.netbeans.modules.bugtracking.gitlab.issue.GitLabIssueImpl;
import org.netbeans.modules.bugtracking.gitlab.repository.GitLabRepositoryImpl;
import org.netbeans.spi.viewmodel.Models;

/**
 *
 */
class GitLabQueryController implements QueryController, DocumentListener, ActionListener {

    private GitLabQueryPanel panel;
    private final GitLabRepositoryImpl repository;
    private final GitLabQueryImpl query;
    private String originSummaryCriteria;
    private ResultsModel resultsModel;
    private DefaultListModel<Map.Entry<String, String>> labelsModel;

    private static final String SEARCHING_LABEL = "Searching...";

    public GitLabQueryController(GitLabRepositoryImpl repository, GitLabQueryImpl query) {
        this.repository = repository;
        this.query = query;
    }

    @Override
    public boolean providesMode(QueryMode mode) {
        return mode == QueryMode.EDIT;
    }

    @Override
    public JComponent getComponent(QueryMode mode) {
        if (panel == null) {
            panel = new GitLabQueryPanel();

            // set values
            originSummaryCriteria = query.getSummaryCriteria();
            panel.titleTextField.setText(originSummaryCriteria);

            resultsModel = new ResultsModel(query);
            Models.setModelsToView(panel.resultsComponent, Models.createCompoundModel(Arrays.asList(
                    resultsModel,
                    new ResultsModel.SummaryColumnModel(),
                    new ResultsModel.StatusColumnModel()
            )));

            panel.titleTextField.getDocument().addDocumentListener(this);
            panel.reloadAttributesButton.addActionListener(this);

//            panel.issueList.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent evt) {
//                    JList list = (JList) evt.getSource();
//                    if (evt.getClickCount() == 2) {
//                        int index = list.locationToIndex(evt.getPoint());
//                        if (index > -1) {
//                            Object item = panel.issueList.getModel().getElementAt(index);
//                            if (item instanceof GitLabIssueImpl) {
//                                ((GitLabIssueImpl) item).open();
//                            }
//                        }
//                    }
//                }
//            });
        }
        return panel;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    public void opened() {

    }

    @Override
    public void closed() {

    }

    @Override
    public boolean saveChanges(String name) {
        if (query.getDisplayName() == null) {
            if (name == null) {
                return false;
            }
            query.setName(name);
        }

        // savevalues
        originSummaryCriteria = panel.titleTextField.getText();
        query.setSummaryCriteria(panel.titleTextField.getText());
        fireChanged();

        if (query.getIssues().isEmpty()) {
            refresh();
        }
        return true;
    }

    @Override
    public boolean discardUnsavedChanges() {
        panel.titleTextField.setText(query.getSummaryCriteria());
        return true;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }

    private void fireChanged() {
        support.firePropertyChange(PROP_CHANGED, null, null);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        fireChanged();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        fireChanged();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        fireChanged();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == panel.reloadAttributesButton) {
            refresh();
        }
    }

    @Override
    public boolean isChanged() {
        return !originSummaryCriteria.equals(panel.titleTextField.getText());
    }

    void refresh() {
//        resultsModel.refresh();

        labelsModel = new DefaultListModel<>();
        panel.labelsList.setModel(labelsModel);

        query.setSummaryCriteria(panel.titleTextField.getText().trim());
        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                query.refresh();
            }
        });
    }

    public void refreshingStarted() {
    }

    public void refreshingFinished() {
    }

    public void setLabels(Map<String, String> labelsMap) {
        for (Map.Entry<String, String> entry : labelsMap.entrySet()) {
            labelsModel.addElement(entry);
        }
    }

    void setIssues(Set<GitLabIssueImpl> issues) {
        resultsModel.setIssues(issues);
    }
}
