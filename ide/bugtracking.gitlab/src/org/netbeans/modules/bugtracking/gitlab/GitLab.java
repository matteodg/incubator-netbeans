/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.gitlab;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.modules.bugtracking.spi.BugtrackingSupport;
import org.netbeans.modules.bugtracking.gitlab.issue.GitLabIssueFinderImpl;
import org.netbeans.modules.bugtracking.gitlab.issue.GitLabIssueImpl;
import org.netbeans.modules.bugtracking.gitlab.issue.GitLabIssuePriorityProviderImpl;
import org.netbeans.modules.bugtracking.gitlab.issue.GitLabIssueProviderImpl;
import org.netbeans.modules.bugtracking.gitlab.issue.GitLabIssueScheduleProviderImpl;
import org.netbeans.modules.bugtracking.gitlab.issue.GitLabIssueStatusProviderImpl;
import org.netbeans.modules.bugtracking.gitlab.query.GitLabQueryImpl;
import org.netbeans.modules.bugtracking.gitlab.query.GitLabQueryProviderImpl;
import org.netbeans.modules.bugtracking.gitlab.repository.GitLabRepositoryImpl;
import org.netbeans.modules.bugtracking.gitlab.repository.GitLabRepositoryProviderImpl;
import org.openide.util.ImageUtilities;

/**
 *
 * @author tomas
 */
public class GitLab {

    @StaticResource
    public static final String ICON_PATH = "org/netbeans/modules/bugtracking/gitlab/resources/repository.png"; // NOI18N
    public static final Image ICON = ImageUtilities.loadImage(ICON_PATH, true);

    private GitLabIssueScheduleProviderImpl issp;
    private static GitLabIssueStatusProviderImpl isp;
    private static GitLabIssuePriorityProviderImpl ipp;
    private static GitLabIssueFinderImpl iff;

    private BugtrackingSupport<GitLabRepositoryImpl, GitLabQueryImpl, GitLabIssueImpl> support;

    private final Map<String, GitLabRepositoryImpl> repositories = new HashMap<>();
    private static GitLab instance;

    public synchronized static GitLab getInstance() {
        if (instance == null) {
            instance = new GitLab();
        }
        return instance;
    }

    public BugtrackingSupport<GitLabRepositoryImpl, GitLabQueryImpl, GitLabIssueImpl> getSupport() {
        if (support == null) {
            support = new BugtrackingSupport<>(
                    new GitLabRepositoryProviderImpl(),
                    new GitLabQueryProviderImpl(),
                    new GitLabIssueProviderImpl());
        }
        return support;
    }

    GitLabIssueStatusProviderImpl getIssueStatusProvider() {
        if (isp == null) {
            isp = new GitLabIssueStatusProviderImpl();
        }
        return isp;
    }

    GitLabIssueScheduleProviderImpl getIssueScheduleProvider() {
        if (issp == null) {
            issp = new GitLabIssueScheduleProviderImpl();
        }
        return issp;
    }

    GitLabIssuePriorityProviderImpl getIssuePriorityProvider() {
        if (ipp == null) {
            ipp = new GitLabIssuePriorityProviderImpl();
        }
        return ipp;
    }

    GitLabIssueFinderImpl getIssueFinder() {
        if (iff == null) {
            iff = new GitLabIssueFinderImpl();
        }
        return iff;
    }

    public void addRepository(GitLabRepositoryImpl repo) {
        repositories.put(repo.getInfo().getID(), repo);
    }

    public void removeRepository(GitLabRepositoryImpl repo) {
        repositories.remove(repo.getInfo().getID());
    }

    public Collection<GitLabRepositoryImpl> getRepositories() {
        return new ArrayList<>(repositories.values());
    }
}
