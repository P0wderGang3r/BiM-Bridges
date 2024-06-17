package org.granat.processors.helpers.height_map;

import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class HelperHeightMapClassify {

    private static Map<String, Double> buildMinMax(Map<String, Double> heightMap) {
        Map<String, Double> heightMinMax = new HashMap<>(2);

        heightMap.entrySet().stream().forEach(entry -> {
            heightMinMax.put("min", Math.min(heightMinMax.getOrDefault("min", entry.getValue()), entry.getValue()));
            heightMinMax.put("max", Math.max(heightMinMax.getOrDefault("max", entry.getValue()), entry.getValue()));
        });

        return heightMinMax;
    }

    public static void run(Supplier<Stream<Point>> pointsStreams, Map<String, Map<String, Double>> data) {
        Map<String, Double> metadata = data.get("metadata");
        Map<String, Double> heightMap = data.get("height-map");

        //Количество строк в матрице
        int rows = metadata.get("rows").intValue();
        //Количество колонок в матрице
        int cols = metadata.get("cols").intValue();
        //Номер измерения, соответствующего строке матрицы
        int axisRow = metadata.get("axis-row").intValue();
        //Номер измерения, соответствующего колонке матрицы
        int axisCol = metadata.get("axis-col").intValue();
        //Номер измерения, с которого снимаются значения для матрицы
        int axisVal = metadata.get("axis-val").intValue();
        //Нормализация
        double norm = metadata.get("norm");

        Map<String, Double> heightMinMax = buildMinMax(heightMap);
        if (heightMinMax.isEmpty()) return;

        Double delta = heightMinMax.get("max") - heightMinMax.get("min");

        pointsStreams.get().forEach(point -> {
            //Ставим в соответствие координате строку от 0 до rows
            int row = (int) ((point.getCoordinates()[axisRow] * norm + 1) / 2 * rows);
            //Ставим в соответствие координате столбец от 0 до cols
            int col = (int) ((point.getCoordinates()[axisCol] * norm + 1) / 2 * cols);
            point.setGirderHeightValue((long) (heightMap.get(row + "-" + col) == null ? 0.0 :
                    ((heightMap.get(row + "-" + col) - heightMinMax.get("min")) / delta) * 1000));
            point.setGirderHeightValue(point.getGirdersHeightParameterValue() != 0);
        });
    }

}
