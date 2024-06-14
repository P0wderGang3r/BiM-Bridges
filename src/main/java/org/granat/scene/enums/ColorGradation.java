package org.granat.scene.enums;

import org.granat.scene.objects.Point;

public enum ColorGradation {
    /**
     * Яркость точки в зависимости от интенсивности отражённого света.
     */
    INTENSITY {
        /**
         * Яркость точки в зависимости от интенсивности отражённого света.
         */
        @Override
        public void setColor(Point point, double[] ignored) {
            point.getColor()[0] = point.getIntensity();
            point.getColor()[1] = point.getIntensity();
            point.getColor()[2] = point.getIntensity();
        }
    },
    /**
     * Цветовая окраска точки в зависимости от количества точек в срезах по трём измерениям.
     */
    DENSITY {
        /**
         * Цветовая окраска точки в зависимости от количества точек в срезах по трём измерениям.
         * @param parameters [0] - максимальное количество точек в срезе.
         */
        @Override
        public void setColor(Point point, double[] parameters) {
            if (parameters.length == 0 || parameters[0] == 0) return;
            point.getColor()[0] = (parameters[0] - point.getDensityParameterValue()) / parameters[0];
            point.getColor()[1] = point.getDensityParameterValue() / parameters[0];
            point.getColor()[2] = 0.0;
        }
    },
    SUPERSTRUCTURES {
        /**
         * Цветовая окраска точки в зависимости от количества точек в срезах по трём измерениям.
         * @param parameters [0] - максимальное количество точек в срезе.
         */
        @Override
        public void setColor(Point point, double[] parameters) {
            if (parameters.length == 0 || parameters[0] == 0) return;
            int currentColorBit = 7 - (int) point.getSuperstructuresParameterValue();
            point.getColor()[0] = currentColorBit % 2;
            currentColorBit /= 2;
            point.getColor()[1] = currentColorBit % 2;
            currentColorBit /= 2;
            point.getColor()[2] = currentColorBit % 2;
        }
    },
    GIRDERS {
        /**
         * Цветовая окраска точки в зависимости от количества точек в срезах по трём измерениям.
         * @param parameters [0] - максимальное количество точек в срезе.
         */
        @Override
        public void setColor(Point point, double[] parameters) {
            if (parameters.length == 0 || parameters[0] == 0) return;
            int originalColorBit = 1 + (int) point.getGirdersParameterValue();
            int currentColorBit = 1 + (int) point.getGirdersParameterValue();
            point.getColor()[0] = (double) (currentColorBit % 2) * 0.5 + (double) originalColorBit * 0.005;
            currentColorBit /= 2;
            point.getColor()[1] = (double) (currentColorBit % 2) * 0.5 + (double) originalColorBit * 0.005;
            currentColorBit /= 2;
            point.getColor()[2] = (double) (currentColorBit % 2) * 0.5 + (double) originalColorBit * 0.005;
        }
    };

    public abstract void setColor(Point point, double[] parameters);
}