package sample;

import java.util.Scanner;
import java.util.Vector;

/**
 * Created by Максим on 24.06.2016.
 */
public class graph {
    Vector<Vector<Integer>> gr_; // список смежности графа

    public enum density { rare, normal };

    graph(int number) {
    V = number;
        gr_ = new Vector<Vector<Integer>>();


    for(int i = 0; i < V; i++) //добавлено
    {
        //Vector<Integer> In = new Vector<Integer>();
        gr_.add(new Vector<Integer>());
    }

    }
    public static int V;
    public int E;

    /**
     *
     * @param b
     * @param V - количество вершин
     */
    public void do_carcass(Vector<Vector<Integer>> b, int V){
        Vector<Integer> searched = new Vector<Integer>(); // просмотренные вершины
        Vector<Integer> unsearched= new Vector<Integer>(); // непросмотренные вершины
        for (int k = 0; k < V; k++)
        {
            unsearched.add(k);
        }

        int RAND_FOR_CARCASS = 0;
        int first = 0;
        while (unsearched.size() != 0)
        {
            if (searched.size() == 0)
            {
                first = 0;
            }
            else
            {
                first = (int)(Math.random()* searched.size());
                first = searched.get(first);
            }

            boolean flag1 = true;
            boolean flag2 = true;
            for (int l = 0; l < searched.size(); l++)
            {
                if (searched.get(l) == first)
                {
                    flag1 = false;
                }
            }
            if (flag1)
            {
                searched.add(first);
            }

            boolean flag33 = false;
            boolean flag4 = false;
            int INDEX1 = 0;
            int INDEX2 = 0;
            for (int u = 0; u < unsearched.size(); u++)
            {
                if (unsearched.get(u) == first)
                {
                    flag33 = true;
                    INDEX1 = unsearched.get(u);
                }
            }
            if (flag33)
            {
                unsearched.removeElement(INDEX1);
            }


            RAND_FOR_CARCASS = (int)(Math.random()*unsearched.size());
            RAND_FOR_CARCASS = unsearched.get(RAND_FOR_CARCASS); //  посмотреть

            gr_.get(first).add(RAND_FOR_CARCASS);// = rand() % (V - 1 + 1) + 1;

            gr_.get(RAND_FOR_CARCASS).add(first);

//вставка
            for (int l = 0; l < searched.size(); l++)
            {
                if (searched.get(l) == RAND_FOR_CARCASS)
                {
                    flag2 = false;
                }
            }
            if (flag2)
            {
                searched.add(RAND_FOR_CARCASS);
            }

//удаление
            for (int u = 0; u < unsearched.size(); u++)
            {
                if (unsearched.get(u) == RAND_FOR_CARCASS)
                {
                    flag4 = true;
                    INDEX2 = unsearched.get(u);
                }
            }
            if (flag4)
            {

                unsearched.removeElement(INDEX2);
            }

            b.get(first).removeElement(RAND_FOR_CARCASS);
            b.get(RAND_FOR_CARCASS).removeElement(first);
        }


    }

    /**
     *
     * @param V
     * @param d
     * @return
     */
    int initialisation(int V, density d)
    {
        int E = 0;
        if (V == 2)
        {
            E = 1;
        }
        else
        {
            if (d == density.rare) // разреженный граф
            {
                E = (int)(Math.random()*(V*(V - 1) / 2 - V + 1) + V - 1); // что-то типа от V-1 до V*(V - 1) / 2
                while (E > (V*(V - 1) / 2 + (V - 1)) / 2)
                {
                    E = (int)(Math.random()*(V*(V - 1) / 2 - V + 1 ) + V - 1) ;
                }
            }
            else
            {
                E = (int)(Math.random()*(V*(V - 1) / 2 - V + 1) + V - 1); // что-то типа от V-1 до V*(V - 1) / 2
                while (E <= (V*(V - 1) / 2 + (V - 1)) / 2)
                {
                    E = (int)(Math.random()*(V*(V - 1) / 2 - V + 1 + 1) + V - 1); // добавил единичку
                }
            }
        }
        int rand_vertex = 0;

        Vector<Vector<Integer>> b = new Vector<Vector<Integer>> (V);
        for (int ink = 0; ink < V; ink++){
            b.add(new Vector<Integer>());
        }

        for (int ind = 0; ind < V; ind++)
        {
            for (int dni = 0; dni < V ; dni++)
            {
                if (ind != dni)
                {
                    b.get(ind).add(dni);
                }
            }
        }

        do_carcass(b, V);

        int balance_v = E - V + 1;

        int pnt = 0;
        while (balance_v != 0)
        {
            pnt = (int)(Math.random()* V); // выбираем случайную вершину

            if (b.get(pnt).size() != 0)
            {
                rand_vertex = (int)(Math.random()* (b.get(pnt).size()));
                rand_vertex = b.get(pnt).get(rand_vertex);

                gr_.get(pnt).add(rand_vertex);
                b.get(pnt).removeElement(rand_vertex);

                gr_.get(rand_vertex).add(pnt);
                b.get(rand_vertex).removeElement(pnt);

                balance_v--;
            }

        }
        return E;
    }

    /**
     * Тут должна выводится диаграмма графа в окошке
     */


    public int enter(){
        int V;
        System.out.println("Введите количество вершин в графе:");
        Scanner sc = new Scanner(System.in);
        V = sc.nextInt();

        while (V <= 1) // исправил на 1
        {
            System.out.println("Повторите ввод:");
            V = sc.nextInt();
        }
        return V;
    }

    public void show(Vector<Vector<Integer>> K){
        System.out.println("Список смежности графа:");
        for (int i = 0; i < K.size(); i++)
        {
            System.out.print(i + "-я вершина: ");//i << "-я вершина: ";
            for (int j = 0; j < K.get(i).size(); j++)
            {
                System.out.print(K.get(i).get(j));
               // cout << graph[i][j] << ' ';
            }
            System.out.println('\n');
        }
    }
}
