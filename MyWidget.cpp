#include "MyWidget.h"
#include "QHBoxLayout"
#include "QVBoxLayout"
#include "QPainter"

// вспомогательные элементы
//--------------------------------------------------------------------
#include <iomanip>
#include <iostream>
#include <QFile>
//#include <fstream>
// вспомогательные элементы для заполнения массивы near[*]
#define CHOSEN 65534	// данная вершина присоединена к Vt
#define INF 65535		// нет ребра из этой вершины в мн-во вершин Vt
#define PI 3.1415926
//--------------------------------------------------------------------
using namespace std;

MyWidget::MyWidget(QWidget *parent) :
    QWidget(parent),widgetHeight (600), widgetWidth (750), nodeRad(widgetHeight/12), ribCounter(0),
    graphRad(widgetHeight*7/16 - 0.3*nodeRad),x0(widgetWidth/2 - 2*nodeRad), y0(widgetHeight/2), N(-1)
{
// cозднание лейблов и кнопок
    greetings = new QLabel (" Ready to work!");
    info = new QLabel ("");
    info->setAlignment(Qt::AlignCenter);
    nextButton = new QPushButton (" Next step");
    prevButton = new QPushButton (" Previous step");
    startButton = new QPushButton (" Let's begin!");

    usedRibsList = new QString("");

//  создание лайоутов
    QVBoxLayout * rightLayout = new QVBoxLayout();    // Create right part of main layout
    rightLayout->addStretch(1);
    rightLayout->addWidget(greetings);
    rightLayout->addStretch(3);
    rightLayout->addWidget(info);
    rightLayout->addStretch(3);
    rightLayout->addWidget(prevButton);
    rightLayout->addWidget(nextButton);
    rightLayout->addStretch(1);
    rightLayout->addWidget(startButton);
    rightLayout->setAlignment(Qt::AlignLeft | Qt::AlignTop);


    QHBoxLayout * mainLayout = new QHBoxLayout();
    mainLayout->addStretch(10);
    mainLayout->addLayout(rightLayout);
    setLayout(mainLayout);
    setFixedHeight(widgetHeight);
    setFixedWidth(widgetWidth);
    this->setWindowTitle(" Prim's algorithm of building MST (Minimum Stroma Tree) ");

// отключение кнопок
    prevButton->setEnabled(false);
    nextButton->setEnabled(false);
    startButton->setAutoDefault(true);

// привязывание кнопок
    connect(prevButton, SIGNAL(clicked()), this, SLOT(DecCounter()));
    connect(nextButton, SIGNAL(clicked()), this, SLOT(IncCounter()));
    connect(startButton, SIGNAL(clicked()), this, SLOT(BeginAlg()));
//*********************************************

// Пробуем считать матрицу стоимостей и провернуть алгоритм Прима построения МОД
    if (PrimAlgorithm() == -1)
    {
        greetings->setText(" Error: Can't open matrix file.");
        startButton->setEnabled(false);
    };

}

int MyWidget::PrimAlgorithm()
{
// файл, в котором находится матрица стоимостей

    QFile file("/home/gentser/QtProjects/CourseWork/CourseWork/source.txt");
    if (!file.open(QIODevice::Text | QIODevice::ReadOnly)) return -1; // пытаемся открыть файл
    QString str = file.readLine();    // первая строка файла
    QStringList lst = str.split(" "); // делим строку на элементы (разделитель - пробел)

    N = lst.at(0).toInt();  // считываем размер матрицы (его указываем в файле первым числом

// Массив ребер МОД
    Et = new int *[2];
    for (int i(0); i < 2; i++) Et[i] = new int[N]; // т.к. вершин N, то ребе N-1

// Первоначальная инициализация массива Et
    for (int i(0); i < N; i++)
    {
        Et[0][i] = Et[1][i] = INF;
    }

// Готовим матрицу стоимостей к обработке

    d = new int *[N];
    for (int i(0); i < N; i++) d[i] = new int[N];

    for (int i = 0; i < N; i++)
    {
        str = file.readLine();  // читаем строку
        lst = str.split(" ");   // разбиваем ее на элементы
        for (int j = 0; j < N; j++)
            d[i][j] = lst.at(j).toInt(); // заполняем матрицу
    }

// Вывод матрицы стоимостей на экран

    cout << " Matrix of costs: \n";	// Вывод матрицы стоимостей
    cout << " ****************************\n";
    for (int i = 0; i < N; i++)
    {
        for (int j = 0; j < N; j++)
        {
            cout << setw(3) << d[i][j];
        }; cout << endl;

    };
    cout << " ****************************\n";
//*************************************************************

// Алгоритм МОД

    used = new bool[N]();	// массив вершин, попавших в Vt
    near = new int[N]();	// массив вершин, расстояние от которых до Vt минимальное

// выбор производьной вершины

    int v0 = 0;
    used[v0] = true; // Vt = {v0}
    // Et = {};

// инициализация массива near[*]

    for (int v = 0; v < N; v++)
    {
        if (used[v] != true)	// рассматриваем все вершины, кроме выбранной первоначально, т.е. из V\Vt
        {
            if (d[v][v0] != 0) near[v] = v0;
            else near[v] = INF;
        }
        else near[v] = CHOSEN;	// выбранная первоначально вершина
    };// конец инициализации

// рассматриваем множество непросмотренных вершин, пока оно не пусто

    int v = 0;	// вспомогательная переменная для следующего цикла
    for (int i = 0; i < N-1; i++)
    {
        // Выбор v= вершина из V\Vt с min d[v][near[v]]
        v = NextNode(used, near, d, N);
        used[v] = true; // Vt = Vt +{v}

        // Et = Et + { (v, near[v]) }
        Et[0][i] = v;  Et[1][i] = near[v];
        Wt += d[v][near[v]]; // собираем вес МОД

        // коррекция массива near[*] после включения v в МОД
        for (int x = 0; x < N; x++)
        {
            if (used[x] != true)	// т.е. рассматриваем все вершины из V\Vt
            if ((d[x][near[x]] > d[x][v]) && (d[x][v] != 0)) near[x] = v;	// если ребро (x, v) существует, и его вес меньше ребра (x, near[x])
        };
    };
    cout << " Array Et:\n";
    for (int i = 0; i < N; i++)
        cout << " " << Et[0][i] + 1 << " " << Et[1][i] + 1 << endl;


    cout << " Weight of MinTree: " << Wt << endl;
//*************************************************************
    return 0;
}

int MyWidget::NextNode(bool *used, int *near, int **d, int N)
{
    int min = INF;
    int v;
    for (int x = 0; x < N; x++)
    if ((used[x] != true) && (d[x][near[x]] < min))
        {
            min = d[x][near[x]];
            v = x;
        };
    return v;
}

void MyWidget::GetNodeCoordinates(int k, int &x, int &y)
{
    x = graphRad*cos(2*PI/N*k)+x0;
    y = graphRad*sin(2*PI/N*k) + y0;
}


void MyWidget::paintEvent(QPaintEvent *)
{
     QPainter painter(this);
     QBrush greenBrush(Qt::green, Qt::SolidPattern);
     QPen bluePen(Qt::blue, 2, Qt::SolidLine, Qt::FlatCap);
     QPen redPen(Qt::red, 5, Qt::SolidLine, Qt::FlatCap);

     int x1, y1;  // координаты точек для рисования
     int x2, y2;

     QString str; // вспомогательная строка для ввода данных

// Начало отрисовки графа
    if (N != -1)  // если текстовый файл открылся и алгоритм проработал
    {
// Отрисовка ребер графа

     painter.setPen(bluePen);
     for (int i = 0; i < N; i++)
     {
         for (int j = i + 1; j < N; j++)
         {
             if (d[i][j] != 0)
             {
                 GetNodeCoordinates(i, x1, y1);
                 GetNodeCoordinates(j, x2, y2);
                 painter.drawLine(x1, y1, x2, y2);
             }
         }
     }

// Отрисовка ребер МОД
     painter.setPen(redPen);
     for (int i = 0; i < N && i < ribCounter; i++)
     {
         if (Et[0][i] != INF)
         {
             GetNodeCoordinates(Et[0][i], x1, y1);
             GetNodeCoordinates(Et[1][i], x2, y2);
             painter.drawLine(x1, y1, x2, y2);
         }
     }
     //painter.draw

// Отрисовка весов ребер графа

     painter.setPen(bluePen);
     for (int i = 0; i < N; i++)
     {
         for (int j = i + 1; j < N; j++)
         {
             if (d[i][j] != 0)
             {  // получаем координаты места, где будет размещен вес ребра
                 GetNodeCoordinates(i, x1, y1);
                 GetNodeCoordinates(j, x2, y2);
                // меняем размер шрифта и его цвет
                 QFont font=painter.font() ;
                 font.setPointSize(16);
                 painter.setFont(font);
                 painter.setPen(QColor(0, 0, 0));
                // рисуем вес ребра
                 str = QString::number(d[i][j]);
                 painter.drawText((x1+x2)/2, (y1+y2)/2, nodeRad, nodeRad, Qt::AlignAbsolute, str);

             }
         }
     }

// Отрисовка вершин графа
     painter.setBrush(greenBrush);
     for (int i = 0; i < N; i++)
     {
         GetNodeCoordinates(i, x1, y1);
         painter.drawEllipse(x1 - nodeRad/2, y1 - nodeRad/2, nodeRad, nodeRad);
         str = QString('a' + i);
         painter.drawText(x1 - nodeRad/2, y1 - nodeRad/2, nodeRad, nodeRad, Qt::AlignCenter | Qt::AlignVCenter, str);
     }


    } // конец отрисовки графа
}

void MyWidget::IncCounter()
{
    if (ribCounter < N-1)   // всего может быть максимум N-1 ребер в МОД
    {
        ribCounter++;   // увеличиваем число отрисовываемых ребер МОД

        if (ribCounter == N-1)  // достигли максимального количества ребер
        nextButton->setEnabled(false);
        else if (!prevButton->isEnabled())   // если кнопка выключена, то ее нужно включить
             prevButton->setEnabled(true);

        usedRibsList->clear();  // заполняем информацию о включенных на следующем этапе в МОД ребрах
        for (int i = 0; i < N-1 && i < ribCounter + 1; i++)
        {
            usedRibsList->append("  Add rib (" + QString('a'+(Et[0][i])) + ", "+ QString('a'+(Et[1][i]))+") = " + QString::number( d[ Et[0][i] ][ Et[1][i] ] ) +"\n");
        };
    }

    if (ribCounter == N-1)  // если отрисованы все ребра
        usedRibsList->append(" Weight of MST = " + QString::number(Wt));
    info->setText(usedRibsList->toLatin1());

    repaint();
}

void MyWidget::DecCounter()
{
    if (ribCounter > 0)   // всего может быть максимум N-1 ребер в МОД
    {
        ribCounter--;       // уменьшаем число отрисовываемых ребер МОД

        if (ribCounter == 0)  // достигли максимального количества ребер
        prevButton->setEnabled(false);
        else if (!nextButton->isEnabled())   // если кнопка выключена, то ее нужно включить
             nextButton->setEnabled(true);

        usedRibsList->clear();  // заполняем информацию о включенных на следующем этапе в МОД ребрах
        for (int i = 0; i < N && i < ribCounter + 1; i++)
        {
            usedRibsList->append("  Add rib (" + QString('a'+(Et[0][i])) + ", "+ QString('a'+(Et[1][i]))+") = " + QString::number( d[ Et[0][i] ][ Et[1][i] ] ) +"\n");

        };
        info->setText(usedRibsList->toLatin1()); // выводим эту информацию
    }
    repaint();
}

void MyWidget::BeginAlg()
{
    nextButton->setEnabled(true);
    startButton->setEnabled(false);

    usedRibsList->clear();  // заполняем информацию о включенных на следующем этапе в МОД ребрах
    for (int i = 0; i < N-1 && i < ribCounter + 1; i++)
    {
        usedRibsList->append("  Add rib (" + QString('a'+(Et[0][i])) + ", "+ QString('a'+(Et[1][i]))+") = " + QString::number( d[ Et[0][i] ][ Et[1][i] ] ) +"\n");
    };
    info->setText(usedRibsList->toLatin1());

    greetings->setText(" Work of algorithm...\n (press buttons 'next' & 'prev')");
}
