package org.granat.processors.algo;

import org.granat.processors.helpers.IHelper;
import org.granat.processors.helpers.deflection_points.HelperDeflectionPoints;
import org.granat.processors.helpers.height_map.HelperHeightMapClassify;
import org.granat.processors.helpers.height_map.base.HelperHeightMap;
import org.granat.scene.objects.Point;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Помощник анализа облака точек - вычисление опорных точек для дальнейшего получения профиля прогиба.
 */
public class AlgoDeflection {
    /**
     * Функция извлечения цвета класса
     */
    public static double[] getColor(int girderClass) {
        int originalColorBit = 1 + girderClass;
        int currentColorBit = 1 + girderClass;

        double[] result = new double[3];

        result[0] = ((double) (currentColorBit % 2) * 0.5 + (double) originalColorBit * 0.005);
        currentColorBit /= 2;
        result[1] = ((double) (currentColorBit % 2) * 0.5 + (double) originalColorBit * 0.005);
        currentColorBit /= 2;
        result[2] = ((double) (currentColorBit % 2) * 0.5 + (double) originalColorBit * 0.005);

        return result;
    }

    private static void calculateDeflection(
            Map<String, Double> parameters,
            Map<String, Double> deflectionPoints
    ) {
        Double girderClass = parameters.get("class");
        Double start = deflectionPoints.get("start");
        Double mid = deflectionPoints.get("mid");
        Double end = deflectionPoints.get("end");
        if (girderClass == null || start == null || end == null || mid == null) return;
        if (girderClass.isNaN() || start.isNaN() || end.isNaN() || mid.isNaN()) return;

        double result = mid - ((start + end) / 2);

        System.out.println();
        System.out.println(start + " " + mid + " " + end);
        System.out.println(Arrays.toString(getColor(girderClass.intValue())) + " = " + result);
    }

    static IHelper helperHeightMap = HelperHeightMap::run;
    static IHelper helperDeflectionPoints = HelperDeflectionPoints::run;

    public static void run(Supplier<Stream<Point>> pointsStreamSupplier, Map<String, Double> parameters) {

        Map<String, Map<String, Double>> data = new HashMap<>();
        data.put("metadata", parameters);

        //Создаётся карта высот
        Map<String, Double> heightMap = helperHeightMap.run(pointsStreamSupplier, data);
        data.put("height-map", heightMap);

        //Проводится поиск важных точек для вычисления прогиба
        Map<String, Double> deflectionPoints = helperDeflectionPoints.run(null, data);

        /*
        for (int index = 0; index < 500; index++) {
            for (int jindex = 0; jindex < 500; jindex++) {
                if (heightMap.get(index+ "-" + jindex) != null) System.out.print(" +");
                else System.out.print(" .");
            }
            System.out.println();
        }

         */

        HelperHeightMapClassify.run(pointsStreamSupplier, data);

        calculateDeflection(parameters, deflectionPoints);
    }
}
