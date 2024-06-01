package org.granat.processors.filters;

import org.granat.scene.objects.Point;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

/*
    Кластеризация с помощью SVM.
    Уравнение выглядит так:
    Расстояние_Гиперплоскость_n -> max
    Расстояние_Гиперплоскость_n - Расстояние_Гиперплоскость_m = ±погрешность
 */
public class FilterGirders {
    /**
     *
     * @param pointsStreams облако точек
     * @param parameters "radius" - предельное расстояние между точками
     */
    public void run(Supplier<Stream<Point>> pointsStreams, Map<String, Double> parameters) {
        //Функция, которая проводит максимальное количество разделяющих линий в пространстве
        //Множество таких линий должно быть приблизительно равной удалённости между границами классов
        //На входе - множество точек после применения фильтра поиска пролётных строений моста
        //Создаётся карта высот
        //Создаётся карта изменения высот
        //Создаётся карта классов высот
        //Выбирается нижний класс карты классов высот
    }
}
