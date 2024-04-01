package org.granat.ui.gui.input.mouse;

public enum InputMouseBinding {
    ROTATION {
        @Override
        public double[] interpretMouseInput(double[] deltaPos) {
            return new double[] {0, deltaPos[0], -deltaPos[1]};
        }
    },
    POSITION {
        @Override
        public double[] interpretMouseInput(double[] deltaPos) {
            return new double[] {deltaPos[0], deltaPos[1], 0};
        }
    },
    ZOOM {
        @Override
        public double[] interpretMouseInput(double[] deltaPos) {
            return new double[] {deltaPos[0], -deltaPos[1]};
        }
    };

    public abstract double[] interpretMouseInput(double[] deltaPos);
}
