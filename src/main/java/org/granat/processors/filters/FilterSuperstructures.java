package org.granat.processors.filters;

import org.granat.processors.helpers.HelperDensityVector;
import org.granat.processors.helpers.IHelper;
import org.granat.scene.objects.Point;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Класс, отвечающий за выбор одного из срезов, соответствующих горизонтальным опорным строениям мостовых сооружений.
 */
public class FilterSuperstructures {
    IHelper helperDensityVector = HelperDensityVector::run;
    IHelper helperDensityVectorSlice = HelperDensityVector::run;

    /**
     *
     * @param pointsStreams облако точек
     * @param parameters "radius" - предельное расстояние между точками
     */
    public void run(Supplier<Stream<Point>> pointsStreams, Map<String, Double> parameters) {
        //Высота вектора
        int length = parameters.get("length").intValue();

        //Создаётся вектор плотностей по оси oZ
        Map<String, Double> verticalDensity = helperDensityVector.run(pointsStreams, parameters);

        //Вычисляется множество плоскостей, в которых наблюдаются выбросы
        Map<String, Double> verticalSlices = helperDensityVectorSlice.run(null, verticalDensity);

        //Размечаются все точки по принадлежности выбросам в векторе плотностей
        AtomicReference<Double> currentSlice = new AtomicReference<>(null);
        pointsStreams.get().forEach(point -> {
            currentSlice.set(verticalSlices.get("" + (int) ((point.getCoordinates()[2] + 1) * length)));
            if (currentSlice.get() != null)
                point.setSuperstructureParameterValue(currentSlice.get().intValue());
        });
    }
}
