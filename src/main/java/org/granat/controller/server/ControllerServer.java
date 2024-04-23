package org.granat.controller.server;

import lombok.Getter;
import org.granat.ui.gui.runtime.IJob;
import org.granat.ui.gui.runtime.Server;
import org.granat.ui.gui.runtime.State;

import java.util.List;

public record ControllerServer(
        @Getter Server server
) {
    //?----------------------------------------------------------------------------------------------------------PRIVATE


    //?-----------------------------------------------------------------------------------------------------------PUBLIC

    public void setPendingOperation(State state, List<IJob> jobs) {
        this.server.setPendingOperation(state, jobs);
    }
}