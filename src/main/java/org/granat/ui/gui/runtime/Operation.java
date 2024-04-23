package org.granat.ui.gui.runtime;

import lombok.Getter;

import java.util.List;

public record Operation(
        @Getter State state,
        @Getter List<IJob> job
) {
}
