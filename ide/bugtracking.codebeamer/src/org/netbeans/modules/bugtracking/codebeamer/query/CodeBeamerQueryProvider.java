/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.bugtracking.codebeamer.query;

import org.netbeans.modules.bugtracking.spi.QueryController;
import org.netbeans.modules.bugtracking.spi.QueryProvider;
import org.netbeans.modules.bugtracking.codebeamer.issue.CodeBeamerIssue;

/**
 *
 * @author tomas
 */
public class CodeBeamerQueryProvider implements QueryProvider<CodeBeamerQuery, CodeBeamerIssue>{

    @Override
    public String getDisplayName(CodeBeamerQuery q) {
        return q.getDisplayName();
    }

    @Override
    public String getTooltip(CodeBeamerQuery q) {
        return q.getTooltip();
    }

    @Override
    public QueryController getController(CodeBeamerQuery q) {
        return q.getController();
    }

    @Override
    public boolean canRemove(CodeBeamerQuery q) {
        return q.canRemove();
    }

    @Override
    public void remove(CodeBeamerQuery q) {
        q.remove();
    }

    @Override
    public boolean canRename(CodeBeamerQuery q) {
        return q.canRename();
    }

    @Override
    public void rename(CodeBeamerQuery q, String newName) {
        q.rename(newName);
    }

    @Override
    public void refresh(CodeBeamerQuery q) {
        q.getController().refresh();
    }

    @Override
    public void setIssueContainer(CodeBeamerQuery q, IssueContainer<CodeBeamerIssue> c) {
        q.setIssueContainer(c);
    }
    
}
