package com.ltizzi.dev_cards.model.calendar;

import com.ltizzi.dev_cards.model.workspace.WorkspaceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * @author Leonardo Terlizzi
 */
@Entity
@Table(name="workspace_calendars")
public class WorkspaceCalendarEntity extends CalendarEntity {

    @OneToOne
    @JoinColumn(name = "ws_id", unique = true)
    private WorkspaceEntity workspace;

    public void setWorkspace(WorkspaceEntity ws){
        this.workspace = ws;
    }

    public WorkspaceEntity  getWorkspace(){
        return this.workspace;
    }
}
