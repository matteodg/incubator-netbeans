/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.gitlab.repository;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.netbeans.modules.bugtracking.spi.RepositoryController;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;
import org.openide.util.ChangeSupport;
import org.netbeans.modules.bugtracking.gitlab.GitLab;
import static org.netbeans.modules.bugtracking.gitlab.repository.GitLabRepositoryImpl.PROP_PERSONAL_ACCESS_TOKEN;
import static org.netbeans.modules.bugtracking.gitlab.repository.GitLabRepositoryImpl.PROP_PROJECT_ID;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle.Messages;

/**
 * Provides the UI for your particular repository instance and handles
 * interaction with the TopComponent in which it is visualized.
 *
 * @author Matteo Di Giovinazzo
 */
public class GitLabRepositoryController implements RepositoryController, ActionListener, DocumentListener {

    private GitLabRepositoryPanel panel;
    private final GitLabRepositoryImpl repository;
    private String errorMessage;

    private final ChangeSupport support = new ChangeSupport(this);

    public GitLabRepositoryController(GitLabRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public JComponent getComponent() {
        if (panel == null) {
            panel = new GitLabRepositoryPanel();

            // name and server URL
            panel.nameText.getDocument().addDocumentListener(this);
            panel.serverUrlText.getDocument().addDocumentListener(this);
            panel.projectIdText.getDocument().addDocumentListener(this);
            // API
            panel.rest4RadioButton.addActionListener(this);
            panel.graphqlRadioButton.addActionListener(this);
            // authentication
            panel.oauth2RadioButton.addActionListener(this);
            panel.personalAccessRadioButton.addActionListener(this);
            panel.personalAccessText.getDocument().addDocumentListener(this);
        }
        return panel;
    }

    @Override
    public org.openide.util.HelpCtx getHelpCtx() {
        return new HelpCtx("org.netbeans.modules.bugtracking.gitlab.repository.GitLabRepository"); // NOI18N
    }

    @Override
    public boolean isValid() {
        return validate();
    }

    @Override
    public void populate() {
        RepositoryInfo info = repository.getInfo();
        if (info != null) {
            panel.nameText.setText(info.getDisplayName());
            panel.serverUrlText.setText(info.getUrl());
            panel.personalAccessText.setText(info.getValue(PROP_PERSONAL_ACCESS_TOKEN));
            panel.projectIdText.setText(info.getValue(PROP_PROJECT_ID));
            // TODO: read also API and authentication
        }
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void applyChanges() {
        repository.setData(
                panel.nameText.getText().trim(),
                panel.serverUrlText.getText().trim(),
                Integer.parseInt(panel.projectIdText.getText().trim()),
                panel.personalAccessText.getText().trim()
        // TODO: store also API and authentication
        );
        GitLab.getInstance().addRepository(repository);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        support.fireChange();
    }

    private String getName() {
        return panel.nameText.getText().trim();
    }

    private String getServerUrl() {
        return panel.serverUrlText.getText().trim();
    }

    private String getProjectId() {
        return panel.projectIdText.getText().trim();
    }

    private String getPersonalAccessToken() {
        return panel.personalAccessText.getText().trim();
    }

    @Messages({
        "GitLabRepositoryController.errorMessages.emptyName=Name must not be empty.",
        "GitLabRepositoryController.errorMessages.emptyServerURL=Server URL must not be empty.",
        "GitLabRepositoryController.errorMessages.emptyProjectId=Project ID must not be empty.",
        "GitLabRepositoryController.errorMessages.emptyPersonalAccessToken=Personal Access Token must not be empty.",
        "# {0} - name",
        "GitLabRepositoryController.errorMessages.nameAlreadyUsed=Name '{0}' already used.",
        "# {0} - server URL",
        "GitLabRepositoryController.errorMessages.serverURLAlreadyUsed=Server URL '{0}' already used."
    })
    private boolean validate() {
        errorMessage = null;
        if ("".equals(getName())) {
            errorMessage = Bundle.GitLabRepositoryController_errorMessages_emptyName();
            return false;
        }

        if (errorMessage == null && "".equals(getServerUrl())) {
            errorMessage = Bundle.GitLabRepositoryController_errorMessages_emptyServerURL();
            return false;
        }

        if (errorMessage == null && "".equals(getProjectId())) {
            errorMessage = Bundle.GitLabRepositoryController_errorMessages_emptyProjectId();
            return false;
        }

        if (errorMessage == null && "".equals(getPersonalAccessToken())) {
            errorMessage = Bundle.GitLabRepositoryController_errorMessages_emptyPersonalAccessToken();
            return false;
        }

        if (errorMessage == null) {
            Collection<GitLabRepositoryImpl> repos = GitLab.getInstance().getRepositories();
            for (GitLabRepositoryImpl repo : repos) {
                String name = getName();
                if (name.equals(repo.getInfo().getDisplayName())) {
                    errorMessage = Bundle.GitLabRepositoryController_errorMessages_nameAlreadyUsed(name);
                    return false;
                }
                String serverUrl = getServerUrl();
                if (serverUrl.equals(repo.getInfo().getDisplayName())) {
                    errorMessage = Bundle.GitLabRepositoryController_errorMessages_serverURLAlreadyUsed(serverUrl);
                    return false;
                }
            }
        }
        return true;
    }
}
