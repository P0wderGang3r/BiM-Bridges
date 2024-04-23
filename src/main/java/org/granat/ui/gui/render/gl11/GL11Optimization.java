package org.granat.ui.gui.render.gl11;

import lombok.Getter;

@Getter
public class GL11Optimization {

    private double startTime = System.currentTimeMillis() / 1000.0;
    private double currentTime;

    private int frames = 0;
    private int delta = 64;

    public void nextTick() {
        frames += 1;
        currentTime = System.currentTimeMillis() / 1000.0;
        if (currentTime >= startTime + 0.1) {
            if (frames < 3) incDelta();
            if (frames > 6) decDelta();
            startTime = currentTime;
            frames = 0;
        }
    }

    private void incDelta() {
        if (delta < 65536) delta *= 2;
        else delta = 65536;
    }

    private void decDelta() {
        delta /= (2 - delta % 2);
    }
}