/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.gitlab.issue;

import org.netbeans.modules.bugtracking.spi.IssuePriorityInfo;
import org.netbeans.modules.bugtracking.spi.IssuePriorityProvider;

/**
 *
 */
public class GitLabIssuePriorityProviderImpl implements IssuePriorityProvider<GitLabIssueImpl> {

    @Override
    public String getPriorityID(GitLabIssueImpl i) {
        return i.getPriorityID();
    }

    @Override
    public IssuePriorityInfo[] getPriorityInfos() {
        // TODO in case you want use your own infos, 
        // then provide them via IssuePriorityInfo(id, displayname, icon)
        return new IssuePriorityInfo[]{
            new IssuePriorityInfo("1", "P1"),
            new IssuePriorityInfo("2", "P2"),
            new IssuePriorityInfo("3", "P3"),
            new IssuePriorityInfo("4", "P4"),
            new IssuePriorityInfo("5", "P5"),};
    }

}
