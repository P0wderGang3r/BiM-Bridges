package org.granat.processors.filters;

import org.granat.processors.helpers.density_vector.HelperDensity;
import org.granat.processors.helpers.density_vector.HelperDensitySlice;
import org.granat.processors.helpers.IHelper;
import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Класс, отвечающий за выбор одного из срезов, соответствующих горизонтальным опорным строениям мостовых сооружений.
 */
public class FilterSuperstructures {
    static IHelper helperDensityVector = HelperDensity::run;
    static IHelper helperDensityVectorSlice = HelperDensitySlice::run;

    /**
     *
     * @param pointsStreams облако точек
     * @param parameters "radius" - предельное расстояние между точками
     */
    public static void run(Supplier<Stream<Point>> pointsStreams, Map<String, Double> parameters) {
        //Высота вектора
        int length = parameters.get("length").intValue();

        Map<String, Map<String, Double>> data = new HashMap<>();
        data.put("metadata", parameters);

        //Создаётся вектор плотностей по оси oZ
        Map<String, Double> densityVector = helperDensityVector.run(pointsStreams, data);
        data.put("density-vector", densityVector);

        //Вычисляется множество плоскостей, в которых наблюдаются выбросы
        Map<String, Double> verticalSlices = helperDensityVectorSlice.run(null, data);
        data.remove("density-vector"); densityVector = null;

        //Размечаются все точки по принадлежности выбросам в векторе плотностей
        AtomicReference<Double> currentSlice = new AtomicReference<>(null);
        pointsStreams.get().forEach(point -> {
            currentSlice.set(verticalSlices.get("" + (int) ((point.getCoordinates()[2] + 1) * length)));
            if (currentSlice.get() != null)
                point.setSuperstructureParameterValue(currentSlice.get().intValue());
        });
    }
}
