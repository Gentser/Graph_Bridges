#ifndef MYWIDGET_H
#define MYWIDGET_H

#include <QWidget>
#include <QPushButton>
#include <QPen>
#include <QPainter>
#include <QLabel>

class MyWidget : public QWidget
{
    Q_OBJECT
private:

    QLabel      * greetings;
    QLabel      * info;
    QPushButton * nextButton;
    QPushButton * prevButton;
    QPushButton * startButton;

    const int widgetHeight; // ширина и высота виджета
    const int widgetWidth;
    const int nodeRad;      // радиус вершин при отображении
    const int graphRad;     // радиус окружности, на которой лежат вершины графа
    const int x0, y0;       // координаты центра окружности, на которой лежат вершины графа
    int ribCounter;         // счетчик отрисованных ребер графа
    QString *usedRibsList;  // письменный список используемых ребер

    int N;                  // кол-во вершин графа
    int **d;                // матрица стоимостей
    bool *used;             // массив вершин, попавших в Vt
    int *near;              // массив вершин, расстояние от которых до Vt минимальное
    int **Et;               // массив ребер МОД
    int Wt = 0;             // вес МОД


    int NextNode(bool *used, int *near, int **d, int N);    // функция, возвращающая ближайшее ребро
    void GetNodeCoordinates(int k, int &x, int &y);     // функция, высчитывающая координаты к-ой вершины графа

public:
    explicit MyWidget(QWidget *parent = 0);
    int PrimAlgorithm();

    virtual void paintEvent(QPaintEvent *);


signals:

private slots:
    void IncCounter();  // увеличение счетчика отрисованных ребер МОД
    void DecCounter();
    void BeginAlg();    // дает возможность пользователю начать алгоритм Прима
};

#endif // MYWIDGET_H
