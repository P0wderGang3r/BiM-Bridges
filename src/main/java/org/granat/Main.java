package org.granat;

public class Main {

    public static void main(String[] args) {
        GUIApplication GUIApplication = new GUIApplication();
        GUIApplication.run();
    }
}

//TODO: подсистема ввода/вывода - переключение режима отображения (ControllerOutput.doKeyboardInput)

//TODO: фильтр: окрестности моста
//TODO: фильтр: окрестности моста: вывод точек разного цвета - градация от красного до зелёного (ControllerScene.getColor)
//TODO: фильтр: окрестности моста: подсистема графического вывода только прошедших фильтрацию точек (ControllerScene.getPoints)

//TODO: поиск множества плоскостей в пространстве
//TODO: фильтр: около-горизонтальные поверхности
//TODO: фильтр: объекты мостового сооружения

//TODO: путь до файла в параметрах запуска ПС
//TODO: ели ввод не осуществляется, дорисовывать не нарисованные точки