package org.granat.scene.utils;

import java.util.Arrays;

public class Rotation {
    private static double[][] matrix(double alpha, double beta, double gamma) {
        double[][] rotationMatrix = new double[3][3];

        rotationMatrix[0][0] = Math.cos(alpha) * Math.cos(beta);
        rotationMatrix[1][0] = Math.cos(alpha) * Math.sin(beta) * Math.sin(gamma) - Math.sin(alpha) * Math.cos(gamma);
        rotationMatrix[2][0] = Math.cos(alpha) * Math.sin(beta) * Math.cos(gamma) + Math.sin(alpha) * Math.sin(gamma);

        rotationMatrix[0][1] = Math.sin(alpha) * Math.cos(beta);
        rotationMatrix[1][1] = Math.sin(alpha) * Math.sin(beta) * Math.sin(gamma) + Math.cos(alpha) * Math.cos(gamma);
        rotationMatrix[2][1] = Math.sin(alpha) * Math.sin(beta) * Math.cos(gamma) - Math.cos(alpha) * Math.sin(gamma);

        rotationMatrix[0][2] = -Math.sin(beta);
        rotationMatrix[1][2] = Math.cos(beta) * Math.sin(gamma);
        rotationMatrix[2][2] = Math.cos(beta) * Math.cos(gamma);

        return rotationMatrix;
    }

    public static void rotate(double[] point, double[] rotation) {
        double[] buffer = {point[0], point[1], point[2]};
        double[][] rotationMatrix = matrix(rotation[0], rotation[1], rotation[2]);

        for (int i = 0; i < 3; i++) {
            point[i] = 0;
            for (int j = 0; j < 3; j++) {
                point[i] += buffer[j] * rotationMatrix[i][j];
            }
        }
    }
}

