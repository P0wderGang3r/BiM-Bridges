package org.granat.processors.filters;

import org.granat.processors.helpers.HelperHeightMap;
import org.granat.processors.helpers.IHelper;
import org.granat.scene.objects.Point;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/*
2.2.4 Метод  – поиск области нахождения балок пролётов моста
Балки пролётных соединений моста располагаются в том месте, где находится наибольшая концентрация точек в горизонтальном срезе, ввиду чего можно обеспечить как ускорение дальнейших вычислений, так и отсечь не релевантные точки пространства путём поиска самого верхнего среза наибольшего веса по оси Z.

2.2.4.1 Дополнительные требования
·	Выполнено преобразование – поворот облака точек

2.2.4.2 Входные данные
·	Множество точек
·	Шаг перемещения срезов в пространстве
·	Допустимый разброс точек в срезе пространства

2.2.4.3 Выходные данные
·	Размеченное множество точек

2.2.4.4 Алгоритм
·	Совершается проход по оси пространства Z:
o	совершается проход по множеству точек пространства:
§	если точка лежит в области допустимого разброса точек в срезе пространства, вес текущего среза увеличивается на единицу;
·	вычисляем среднее абсолютное отклонение для полученного множества весов срезов пространства;
·	самое верхнее отклонение будет являться искомым множеством, в котором располагаются балки мостового сооружения.
 */
public class FilterGirder {

    final IHelper helper = HelperHeightMap::generate;

    public static void preprocess(Supplier<Stream<Point>> pointsStreamSupplier, Map<String, Double> parameters) {

    }

    public static void filter(Supplier<Stream<Point>> pointsStreamSupplier, Map<String, Double> parameters) {

    }
}
