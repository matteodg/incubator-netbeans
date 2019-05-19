/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.codebeamer;

import org.netbeans.modules.bugtracking.codebeamer.repository.CodeBeamerRepository;
import org.netbeans.modules.bugtracking.api.Repository;
import org.netbeans.modules.bugtracking.spi.BugtrackingConnector;
import org.netbeans.modules.bugtracking.spi.RepositoryInfo;

/**
 *
 * @author tomas
 */
// TODO - connector registration
@BugtrackingConnector.Registration(
        id = CodeBeamerConnector.ID,
        displayName = CodeBeamerConnector.NAME,
        tooltip = CodeBeamerConnector.NAME,
        iconPath = CodeBeamer.ICON_PATH
)
public class CodeBeamerConnector implements BugtrackingConnector {

    public static final String NAME = "CodeBeamer"; // NOI18N
    public static final String ID = "org.netbeans.modules.bugtracking.codebeamer"; // NOI18N

    private CodeBeamerConnector() {
    }

    @Override
    public Repository createRepository() {
        // TODO create a new repository
        CodeBeamerRepository repo = new CodeBeamerRepository();
        return createRepository(repo);
    }

    @Override
    public Repository createRepository(RepositoryInfo info) {
        // TODO create an already registered repository
        CodeBeamerRepository repo = new CodeBeamerRepository(info);
        return createRepository(repo);
    }

    private Repository createRepository(CodeBeamerRepository repo) {
        return CodeBeamer.getInstance().getSupport().createRepository(
                repo,
                CodeBeamer.getInstance().getIssueStatusProvider(),
                CodeBeamer.getInstance().getIssueScheduleProvider(),
                CodeBeamer.getInstance().getIssuePriorityProvider(),
                null);
    }

}
