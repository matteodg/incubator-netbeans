/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.bugtracking.codebeamer.query;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Comparator;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.netbeans.modules.bugtracking.spi.QueryController;
import org.netbeans.modules.bugtracking.spi.QueryProvider;
import org.openide.util.HelpCtx;
import org.openide.util.RequestProcessor;
import org.netbeans.modules.bugtracking.codebeamer.issue.CodeBeamerIssue;

/**
 *
 * @author tomas
 */
class CodeBeamerQueryController implements QueryController, DocumentListener, ActionListener {
    private CodeBeamerQueryPanel panel;
    private final CodeBeamerQuery query;
    private String originSummaryCriteria;

    private static final String SEARCHING_LABEL = "Searching...";
    
    public CodeBeamerQueryController(CodeBeamerQuery query) {
        this.query = query;
    }

    @Override
    public boolean providesMode(QueryMode mode) {
        return mode == QueryMode.EDIT;
    }

    @Override
    public JComponent getComponent(QueryMode mode) {
        if(panel == null) {
            panel = new CodeBeamerQueryPanel();
            
            originSummaryCriteria = query.getSummaryCriteria();
            panel.summaryTextField.setText(originSummaryCriteria);
            
            panel.summaryTextField.getDocument().addDocumentListener(this);
            panel.refresButton.addActionListener(this);
            panel.issueList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    JList list = (JList)evt.getSource();
                    if (evt.getClickCount() == 2) {
                        int index = list.locationToIndex(evt.getPoint());
                        if(index > -1) {
                            Object item = panel.issueList.getModel().getElementAt(index);
                            if(item instanceof CodeBeamerIssue) {
                                ((CodeBeamerIssue)item).open();
                            }
                        }
                    } 
                }
            });
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
        if(query.getDisplayName() == null) {
            if(name == null) {
                return false;
            }
            query.setName(name);
        }
        originSummaryCriteria = panel.summaryTextField.getText();
        query.setSummaryCriteria(panel.summaryTextField.getText());
        fireChanged();
        if(query.getIssues().isEmpty()) {
            refresh();
        }
        return true;
    }

    @Override
    public boolean discardUnsavedChanges() {
        panel.summaryTextField.setText(query.getSummaryCriteria());
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
        if(e.getSource() == panel.refresButton) {
            refresh();
        }
    }

    @Override
    public boolean isChanged() {
        return !originSummaryCriteria.equals(panel.summaryTextField.getText());
    }
    
    private DefaultListModel model;   
    void refresh() {
        model = new DefaultListModel();
        panel.issueList.setModel(model);
        query.setSummaryCriteria(panel.summaryTextField.getText().trim());
        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                query.refresh();
            }
        });
    }

    public void refreshingStarted() {
        model.addElement(SEARCHING_LABEL);
    }

    public void refreshingFinished() {
        model.removeElement(SEARCHING_LABEL);
    }

    public void add(CodeBeamerIssue... issues) {
        for (CodeBeamerIssue issue : issues) {
            model.addElement(issue);
        }
    }
    
}
