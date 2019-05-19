/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.codebeamer;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.modules.bugtracking.spi.BugtrackingSupport;
import org.netbeans.modules.bugtracking.codebeamer.issue.CodeBeamerIssue;
import org.netbeans.modules.bugtracking.codebeamer.issue.CodeBeamerIssuePriorityProvider;
import org.netbeans.modules.bugtracking.codebeamer.issue.CodeBeamerIssueProvider;
import org.netbeans.modules.bugtracking.codebeamer.issue.CodeBeamerIssueScheduleProvider;
import org.netbeans.modules.bugtracking.codebeamer.issue.CodeBeamerIssueStatusProvider;
import org.netbeans.modules.bugtracking.codebeamer.query.CodeBeamerQuery;
import org.netbeans.modules.bugtracking.codebeamer.query.CodeBeamerQueryProvider;
import org.netbeans.modules.bugtracking.codebeamer.repository.CodeBeamerRepository;
import org.netbeans.modules.bugtracking.codebeamer.repository.CodeBeamerRepositoryProvider;
import org.openide.util.ImageUtilities;

/**
 *
 * @author tomas
 */
public class CodeBeamer {

    @StaticResource
    public static final String ICON_PATH = "org/netbeans/modules/bugtracking/codebeamer/resources/repository.png"; // NOI18N
    public static final Image ICON = ImageUtilities.loadImage(ICON_PATH, true);

    private CodeBeamerIssueScheduleProvider issp;
    private static CodeBeamerIssueStatusProvider isp;
    private static CodeBeamerIssuePriorityProvider ipp;

    private BugtrackingSupport<CodeBeamerRepository, CodeBeamerQuery, CodeBeamerIssue> support;

    private final Map<String, CodeBeamerRepository> repositories = new HashMap<String, CodeBeamerRepository>();
    private static CodeBeamer instance;

    public synchronized static CodeBeamer getInstance() {
        if (instance == null) {
            instance = new CodeBeamer();
        }
        return instance;
    }

    public BugtrackingSupport<CodeBeamerRepository, CodeBeamerQuery, CodeBeamerIssue> getSupport() {
        if (support == null) {
            support = new BugtrackingSupport<>(new CodeBeamerRepositoryProvider(), new CodeBeamerQueryProvider(), new CodeBeamerIssueProvider());
        }
        return support;
    }

    CodeBeamerIssueStatusProvider getIssueStatusProvider() {
        if (isp == null) {
            isp = new CodeBeamerIssueStatusProvider();
        }
        return isp;
    }

    CodeBeamerIssueScheduleProvider getIssueScheduleProvider() {
        if (issp == null) {
            issp = new CodeBeamerIssueScheduleProvider();
        }
        return issp;
    }

    CodeBeamerIssuePriorityProvider getIssuePriorityProvider() {
        if (ipp == null) {
            ipp = new CodeBeamerIssuePriorityProvider();
        }
        return ipp;
    }

    public void addRepository(CodeBeamerRepository repo) {
        repositories.put(repo.getInfo().getID(), repo);
    }

    public void removeRepository(CodeBeamerRepository repo) {
        repositories.remove(repo.getInfo().getID());
    }

    public Collection<CodeBeamerRepository> getRepositories() {
        return new ArrayList<>(repositories.values());
    }
}
