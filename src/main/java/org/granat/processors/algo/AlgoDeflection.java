package org.granat.processors.algo;

import org.granat.processors.helpers.IHelper;
import org.granat.processors.helpers.deflection_points.HelperDeflectionPoints;
import org.granat.processors.helpers.height_map.base.HelperHeightMap;
import org.granat.scene.objects.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/*
2.2.6 Метод – вычисление профиля прогиба
Нижние поверхности балок – это такие поверхности, которые имеют некоторую длину, ширину и прогиб. Для вычисления профиля прогиба балок требуется определить их края, а также некоторые иные параметры, зависящие от типа нагружения, для вычисления конечного значения.

2.2.6.1 Дополнительные требования
·	Выполнена фильтрация – вычисление множества поверхностей балок

2.2.6.2 Входные данные
·	Множество точек
·	Тип нагружения

2.2.6.3 Выходные данные
·	Профиль прогиба

2.2.6.4 Алгоритм
·	Вычисляется карта высот (см. метод – вычисление карты высот): на вход подаётся положительное направление по оси Z;
·	из полученной карты высот на основе типа нагружения извлекаются соответствующие данные для вычисления конечного значения профиля прогиба.
 */
public class AlgoDeflection {

    private static void calculateDeflection(Supplier<Stream<Point>> pointsStreamSupplier, Map<String, Double> parameters) {

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

        calculateDeflection(pointsStreamSupplier, parameters);
    }
}
