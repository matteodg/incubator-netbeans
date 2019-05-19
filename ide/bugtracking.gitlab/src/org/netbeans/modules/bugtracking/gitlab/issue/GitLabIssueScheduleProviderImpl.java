/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.bugtracking.gitlab.issue;

import java.util.Date;
import org.netbeans.modules.bugtracking.spi.IssueScheduleInfo;
import org.netbeans.modules.bugtracking.spi.IssueScheduleProvider;

/**
 *
 * @author tomas
 */
public class GitLabIssueScheduleProviderImpl implements IssueScheduleProvider<GitLabIssueImpl> {

    @Override
    public void setSchedule(GitLabIssueImpl i, IssueScheduleInfo scheduleInfo) {
        i.setSchedule(scheduleInfo);
    }

    @Override
    public Date getDueDate(GitLabIssueImpl i) {
        return i.getDueDate();
    }

    @Override
    public IssueScheduleInfo getSchedule(GitLabIssueImpl i) {
        return i.getSchedule();
    }

}
