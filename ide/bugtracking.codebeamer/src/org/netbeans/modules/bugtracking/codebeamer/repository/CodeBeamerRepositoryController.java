/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.bugtracking.codebeamer.repository;

import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;
import org.openide.util.ChangeSupport;
import org.netbeans.modules.bugtracking.codebeamer.CodeBeamer;

/**
 * TODO - provides the UI for your particular repository instance and 
 * handles interaction with the TopComponent in which it is visualized.
 * 
 * @author tomas
 */
public class CodeBeamerRepositoryController implements RepositoryController, DocumentListener {
    private CodeBeamerRepositoryPanel panel;
    private final CodeBeamerRepository repository;
    private String errorMessage;

    // TODO fire change events in case anything changed in the UI
    private final ChangeSupport support = new ChangeSupport(this);
    
    public CodeBeamerRepositoryController(CodeBeamerRepository repository) {
        this.repository = repository;
    }

    @Override
    public JComponent getComponent() {
        if(panel == null) {
            panel = new CodeBeamerRepositoryPanel();
            panel.nameTextField.getDocument().addDocumentListener(this);
            panel.urlTextField.getDocument().addDocumentListener(this);
        }
        return panel;
    }

    @Override
    public org.openide.util.HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    public boolean isValid() {
        validate();
        return panel != null && 
               errorMessage == null; 
    }

    @Override
    public void populate() {
        RepositoryInfo info = repository.getInfo();
        if(info != null) {
            panel.nameTextField.setText(info.getDisplayName());
            panel.nameTextField.setText(info.getUrl());
        }
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void applyChanges() {
        repository.setData(panel.nameTextField.getText().trim(), panel.urlTextField.getText().trim());
        CodeBeamer.getInstance().addRepository(repository);
    }

    @Override
    public void cancelChanges() {
        
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        support.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        support.removeChangeListener(l);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        support.fireChange();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        support.fireChange();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        support.fireChange();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // impl private
    //////////////////////////////////////////////////////////////////////////////////////
    
    private void validate() {
        errorMessage = null;
        if("".equals(panel.nameTextField.getText().trim())) {
            errorMessage = "Name must not be empty.";
        }
        if(errorMessage == null && "".equals(panel.urlTextField.getText().trim())) {
            errorMessage = "Url must not be empty.";
        }
        if(errorMessage == null) {
            Collection<CodeBeamerRepository> repos = CodeBeamer.getInstance().getRepositories();
            for (CodeBeamerRepository repo : repos) {
                String name = panel.nameTextField.getText().trim();
                if(name.equals(repo.getInfo().getDisplayName())) {
                    errorMessage = "Name '" + name + "' already used.";
                    break;
                }
                String url = panel.urlTextField.getText().trim();
                if(url.equals(repo.getInfo().getDisplayName())) {
                    errorMessage = "Url '" + url + "' already used.";
                    break;
                }            
            }
        }
    }    
}
