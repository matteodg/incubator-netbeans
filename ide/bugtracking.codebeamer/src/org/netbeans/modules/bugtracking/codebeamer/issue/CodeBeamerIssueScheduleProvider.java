/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.bugtracking.codebeamer.issue;

import java.util.Date;
import org.netbeans.modules.bugtracking.spi.IssueScheduleInfo;
import org.netbeans.modules.bugtracking.spi.IssueScheduleProvider;

/**
 *
 * @author tomas
 */
public class CodeBeamerIssueScheduleProvider implements IssueScheduleProvider<CodeBeamerIssue> {

    @Override
    public void setSchedule(CodeBeamerIssue i, IssueScheduleInfo scheduleInfo) {
        i.setSchedule(scheduleInfo);
    }

    @Override
    public Date getDueDate(CodeBeamerIssue i) {
        return i.getDueDate();
    }

    @Override
    public IssueScheduleInfo getSchedule(CodeBeamerIssue i) {
        return i.getSchedule();
    }
    
}
