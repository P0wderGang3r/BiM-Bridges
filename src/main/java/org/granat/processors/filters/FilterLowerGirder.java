package org.granat.processors.filters;

/*
2.2.5 Метод – получение множества нижних поверхностей балок пролётных строений моста
Балки располагаются в том месте, где можно наблюдать явные поперечные перепады между поверхностью плиты, которую удерживают балки, и нижней поверхностью балки. Выполнив поиск таких мест, учитывая, что остальная часть пространства точек уже отфильтрована, можно получить множество точек, соответствующих нижним поверхностям балок, которые с помощью некоторых методов кластеризации можно разделить на множество разных поверхностей.

2.2.5.1 Дополнительные требования
·	Выполнена фильтрация – область нахождения балок пролётов моста

2.2.5.2 Входные данные
·	Множество точек

2.2.5.3 Выходные данные
·	Размеченное множество точек

2.2.5.4 Алгоритм
·	Вычисляется карта высот (см. метод – вычисление карты высот): на вход подаётся положительное направление по оси Z;
·	для полученной матрицы вычисляется среднее абсолютное отклонение (игнорируя значения, равные -1) с последующим отсечением всех «положительных» значений относительно среднего отклонения;
·	выполняем кластеризацию – выбираем случайную точку и помечаем её, соседнюю и соседа соседней точки в один общий класс, пока есть множество точек для пометки. Когда больше нет возможности помечать точки, переходим к следующей не помеченной, пока не останется ни единой точки без класса.
 */
public class FilterLowerGirder {
}