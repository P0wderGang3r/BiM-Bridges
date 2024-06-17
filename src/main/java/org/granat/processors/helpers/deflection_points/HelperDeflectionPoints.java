package org.granat.processors.helpers.deflection_points;

import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class HelperDeflectionPoints {

    /*
    top-col
    ------v
    . . . = . . . . . . . . <- top-row
    . . = = + . . . . . . .
    . = = = + + . . . . . .
    = = = = + + + . . . . . <- left-row
    . + + + + + + + . . . .
    . . + + + + + + + . . .
    . . . + + + + + + + . .
    . . . . + + + + + + + .
    . . . . . + + + = = = = <- right-row
    . . . . . . + + = = = .
    . . . . . . . + = = . .
    . . . . . . . . = . . . <- bottom-row
    ----------------^
    bottom-col
     */

    //-----------------------------------------------------------------------------------------------------ЛЕВАЯ СТОРОНА

    private static Map<String, Double> computeLeftSided(
            Map<String, Double> heightMap,
            Map<String, Double> heightMapCorners,
            int amountBorder
    ) {
        Map<String, Double> deflectionPoints = new HashMap<>(3);

        double start = 0.0;
        double end = 0.0;
        double mid = 0.0;
        int startCounter = 0;
        int endCounter = 0;
        int midCounter = 0;

        //Начало
        for (int indexRow = heightMapCorners.get("top-row").intValue();
             indexRow <= heightMapCorners.get("left-row").intValue(); indexRow++) {
            for (int indexCol = heightMapCorners.get("top-col").intValue();
                 indexCol <= heightMapCorners.get("left-col"); indexCol++) {
                if (heightMap.get(indexRow + "-" + indexCol) != null) {
                    start += heightMap.get(indexRow + "-" + indexCol);
                    startCounter++;
                }
            }
        }

        //Конец
        for (int indexRow = heightMapCorners.get("right-row").intValue();
             indexRow <= heightMapCorners.get("bottom-row").intValue(); indexRow++) {
            for (int indexCol = heightMapCorners.get("bottom-col").intValue();
                 indexCol <= heightMapCorners.get("right-col"); indexCol++) {
                if (heightMap.get(indexRow + "-" + indexCol) != null) {
                    end += heightMap.get(indexRow + "-" + indexCol);
                    endCounter++;
                }
            }
        }

        //Координаты середины
        int midCenterRow = heightMapCorners.get("bottom-row").intValue() - heightMapCorners.get("top-row").intValue();
        int midCenterCol = heightMapCorners.get("right-col").intValue() - heightMapCorners.get("left-col").intValue();;
        int amountCenter = (int) Math.sqrt((double) amountBorder * 2.0);

        for (int indexRow = midCenterRow - amountCenter / 2; indexRow <= midCenterRow + amountCenter / 2; indexRow++) {
            for (int indexCol = midCenterCol - amountCenter / 2; indexCol <= midCenterCol + amountCenter / 2; indexCol++) {
                if (heightMap.get(indexRow + "-" + indexCol) != null) {
                    mid += heightMap.get(indexRow + "-" + indexCol);
                    midCounter++;
                }
            }
        }

        deflectionPoints.put("start", start / (double) startCounter);
        deflectionPoints.put("end", end / (double) endCounter);
        deflectionPoints.put("mid", mid / (double) midCounter);

        return deflectionPoints;
    }

    //------------------------------------------------------------------------------------------------ВЫШЕ ЛЕВАЯ СТОРОНА

    /*
    top-col
    ------------------v
    . . . . . . . . . = . . . <- top-row
    . . . . . . . . + = = . .
    . . . . . . . + + = = = .
    . . . . . . + + + = = = = <- right-row
    . . . . . + + + + + + + .
    . . . . + + + + + + + . .
    . . . + + + + + + + . . .
    . . + + + + + + + . . . .
    . + + + + + + + . . . . .
    = = = = + + + . . . . . . <- left-row
    . = = = + + . . . . . . .
    . . = = + . . . . . . . .
    . . . = . . . . . . . . . <- bottom-row
    ------^
    bottom-col
     */

    //----------------------------------------------------------------------------------------------------ПРАВАЯ СТОРОНА

    private static Map<String, Double> computeRightSided(
            Map<String, Double> heightMap,
            Map<String, Double> heightMapCorners,
            int amountBorder
    ) {
        Map<String, Double> deflectionPoints = new HashMap<>(3);

        double start = 0.0;
        double startMid = 0.0;
        double end = 0.0;
        double midEnd = 0.0;
        double mid = 0.0;
        int startCounter = 0;
        int startMidCounter = 0;
        int endCounter = 0;
        int midEndCounter = 0;
        int midCounter = 0;

        int amountCenter = (int) Math.sqrt((double) amountBorder * 2.0);

        //Начало
        for (int indexRow = heightMapCorners.get("top-row").intValue();
             indexRow <= heightMapCorners.get("right-row").intValue(); indexRow++) {
            for (int indexCol = heightMapCorners.get("top-col").intValue();
                 indexCol <= heightMapCorners.get("right-col").intValue(); indexCol++) {
                if (heightMap.get(indexRow + "-" + indexCol) != null) {
                    start += heightMap.get(indexRow + "-" + indexCol);
                    startCounter++;
                }
            }
        }

        //Конец
        for (int indexRow = heightMapCorners.get("left-row").intValue();
             indexRow <= heightMapCorners.get("bottom-row").intValue(); indexRow++) {
            for (int indexCol = heightMapCorners.get("left-col").intValue();
                 indexCol <= heightMapCorners.get("bottom-col"); indexCol++) {
                if (heightMap.get(indexRow + "-" + indexCol) != null) {
                    end += heightMap.get(indexRow + "-" + indexCol);
                    endCounter++;
                }
            }
        }

        //Координаты середины
        int midCenterRow = (heightMapCorners.get("bottom-row").intValue() + heightMapCorners.get("top-row").intValue()) / 2;
        int midCenterCol = (heightMapCorners.get("right-col").intValue() + heightMapCorners.get("left-col").intValue()) / 2;

        for (int indexRow = midCenterRow - amountCenter; indexRow <= midCenterRow + amountCenter; indexRow++) {
            for (int indexCol = midCenterCol - amountCenter; indexCol <= midCenterCol + amountCenter; indexCol++) {
                if (heightMap.get(indexRow + "-" + indexCol) != null) {
                    mid += heightMap.get(indexRow + "-" + indexCol);
                    midCounter++;
                }
            }
        }

        //Координаты начала - середины
        int startMidCenterRow = (heightMapCorners.get("top-row").intValue() + midCenterRow) / 2;
        int startMidCenterCol = (midCenterCol + heightMapCorners.get("left-col").intValue()) / 2;

        for (int indexRow = startMidCenterRow - amountCenter; indexRow <= startMidCenterRow + amountCenter; indexRow++) {
            for (int indexCol = startMidCenterCol - amountCenter; indexCol <= startMidCenterCol + amountCenter; indexCol++) {
                if (heightMap.get(indexRow + "-" + indexCol) != null) {
                    startMid += heightMap.get(indexRow + "-" + indexCol);
                    startMidCounter++;
                }
            }
        }

        //Координаты середины - конца
        int midEndCenterRow = (midCenterRow + heightMapCorners.get("bottom-row").intValue()) / 2;
        int midEndCenterCol = (heightMapCorners.get("right-col").intValue() + midCenterCol) / 2;

        for (int indexRow = midEndCenterRow - amountCenter; indexRow <= midEndCenterRow + amountCenter; indexRow++) {
            for (int indexCol = midEndCenterCol - amountCenter; indexCol <= midEndCenterCol + amountCenter; indexCol++) {
                if (heightMap.get(indexRow + "-" + indexCol) != null) {
                    midEnd += heightMap.get(indexRow + "-" + indexCol);
                    midEndCounter++;
                }
            }
        }

        /*
        System.out.println("COORDINATES");
        System.out.println(" " + heightMapCorners.get("top-row").intValue() + " " + startMidCenterRow + " " + midCenterRow + " " + midEndCenterRow + " " + heightMapCorners.get("bottom-row"));
        System.out.println(" " + heightMapCorners.get("left-col").intValue() + " " + startMidCenterCol + " " + midCenterCol + " " + midEndCenterCol + " " + heightMapCorners.get("right-col"));
        */

        deflectionPoints.put("start", start / (double) startCounter);
        deflectionPoints.put("start-mid", startMid / (double) startMidCounter);
        deflectionPoints.put("mid", mid / (double) midCounter);
        deflectionPoints.put("mid-end", midEnd / (double) midEndCounter);
        deflectionPoints.put("end", end / (double) endCounter);

        return deflectionPoints;
    }

    //-----------------------------------------------------------------------------------------------ВЫШЕ ПРАВАЯ СТОРОНА

    /**
     * Вычисляется, какая сторона, на основе количества элементов, соответствующих левому и правому треугольникам сверху
    */
    private static Map<String, Double> buildDeflectionPoints(Map<String, Double> heightMap, Map<String, Double> heightMapCorners) {
        int leftTriangle = 0;
        int rightTriangle = 0;

        //Проверка расположения слева направо
        for (int indexRow = heightMapCorners.get("top-row").intValue();
             indexRow <= heightMapCorners.get("left-row").intValue(); indexRow++) {
            for (int indexCol = heightMapCorners.get("top-col").intValue();
                 indexCol <= heightMapCorners.get("left-col"); indexCol++) {
                if (heightMap.get(indexRow + "-" + indexCol) != null) leftTriangle++;
            }
        }

        //Проверка расположения справа налево
        for (int indexRow = heightMapCorners.get("top-row").intValue();
             indexRow <= heightMapCorners.get("right-row").intValue(); indexRow++) {
            for (int indexCol = heightMapCorners.get("top-col").intValue();
                 indexCol <= heightMapCorners.get("right-col").intValue(); indexCol++) {
                if (heightMap.get(indexRow + "-" + indexCol) != null) rightTriangle++;
            }
        }

        //Если слева направо
        if (leftTriangle > rightTriangle) {
            return computeLeftSided(heightMap, heightMapCorners, leftTriangle);
        } //иначе если справа налево
        else {
            return computeRightSided(heightMap, heightMapCorners, rightTriangle);
        }
    }

    private static Map<String, Double> buildCorners(Map<String, Double> heightMap) {
        Map<String, Double> mapCorners = new HashMap<>();

        heightMap.forEach((key, value) -> {
            double row = Double.parseDouble(key.split("-")[0]);
            double col = Double.parseDouble(key.split("-")[1]);

            if (Math.min(mapCorners.getOrDefault("left-row", row), row) == row) {
                mapCorners.put("left-row", row);
                mapCorners.put("left-col", col);
            }

            if (Math.max(mapCorners.getOrDefault("right-row", row), row) == row) {
                mapCorners.put("right-row", row);
                mapCorners.put("right-col", col);
            }

            if (Math.min(mapCorners.getOrDefault("top-col", col), col) == col) {
                mapCorners.put("top-row", row);
                mapCorners.put("top-col", col);
            }

            if (Math.max(mapCorners.getOrDefault("bottom-col", col), col) == col) {
                mapCorners.put("bottom-row", row);
                mapCorners.put("bottom-col", col);
            }
        });

        return mapCorners;
    }

    /**
     * @param data карта высот; rows, cols - размерность карты высот
     * @return карта высот; rows, cols - размерность карты высот
     */
    public static Map<String, Double> run(Supplier<Stream<Point>> ignored, Map<String, Map<String, Double>> data) {
        Map<String, Double> heightMap = data.get("height-map");

        //Вычисление крайних значений карты высот
        Map<String, Double> heightMapCorners = buildCorners(heightMap);

        //Вычисление значений, соответствующих крайним и промежуточному значениям карты высот
        Map<String, Double> deflectionPoints = buildDeflectionPoints(heightMap, heightMapCorners);

        return deflectionPoints;
    }
}
