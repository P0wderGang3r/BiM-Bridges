package org.granat.ui.gui.render.gl11.functions;

public class Rotation {
    double[][] rotationMatrix = new double[3][3];

    private void rotMatrix(double alpha, double beta, double gamma) {
        rotationMatrix[0][0] = Math.cos(alpha) * Math.cos(beta);
        rotationMatrix[1][0] = Math.cos(alpha) * Math.sin(beta) * Math.sin(gamma) - Math.sin(alpha) * Math.cos(gamma);
        rotationMatrix[2][0] = Math.cos(alpha) * Math.sin(beta) * Math.cos(gamma) + Math.sin(alpha) * Math.sin(gamma);


        rotationMatrix[0][1] = Math.sin(alpha) * Math.cos(beta);
        rotationMatrix[1][1] = Math.sin(alpha) * Math.sin(beta) * Math.sin(gamma) + Math.cos(alpha) * Math.cos(gamma);
        rotationMatrix[2][1] = Math.sin(alpha) * Math.sin(beta) * Math.cos(gamma) - Math.cos(alpha) * Math.sin(gamma);

        rotationMatrix[0][2] = -Math.sin(beta);
        rotationMatrix[1][2] = Math.cos(beta) * Math.sin(gamma);
        rotationMatrix[2][2] = Math.cos(beta) * Math.cos(gamma);
    }

    public double[] rotateDot(double[] xyzPrev, double[] rotation) {
        double[] xyzrBuf = {xyzPrev[0], xyzPrev[1], xyzPrev[2]};

        rotMatrix(rotation[0], rotation[1], rotation[2]);

        double[] xyz = {0, 0, 0};

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                xyz[i] += xyzrBuf[j] * rotationMatrix[i][j];
            }

        return xyz;
    }
}

