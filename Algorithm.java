package sample;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by Максим on 26.06.2016.
 */
public class Algorithm {

    Vector<Integer> revNum = new Vector<>();
     //Vector<Integer> NumVert = new Vector<>();
     Vector<Integer> Low;// = new Vector<>(G.V);
     Vector<Vector<Integer>> P ;//= new Vector<>(G.V);
     Vector<Vector<Integer>> DFS ;//= new Vector<>(G.V);
     Vector<Vector<Integer>> Back ;//= new Vector<>(G.V); // обратные ребра
    private static int Number = 0;

    public Vector<Vector<Integer>> Order = new Vector<>(/*2*G.E*/); // массив, в котором указывается порядок включения ребер в Обратные/Прямые ребра
    public int Numeration;      // для нумерации ребер в предыдущем массиве

    private void incNumeration() { Numeration++;}

    /**
     *
     * @param G
     */
    public void DFS_bridge(graph G)
    {
        Vector<Integer> NumVert = new Vector<>(G.V);
        Vector<Integer> Low = new Vector<>(G.V);
        Vector<Vector<Integer>> P = new Vector<>(G.V);
        Vector<Vector<Integer>> DFS = new Vector<>(G.V);
        Vector<Vector<Integer>> Back = new Vector<>(G.V); // обратные ребра

       // Vector<Vector<Integer>> Order = new Vector<>(2*G.E); // порядок включения ребер !!!!!!!!!!!!!!!!!

        Vector<Integer> searched = new Vector<>(G.V); // просмотренные вершины
        Vector<Integer> unsearched = new Vector<>(); // непросмотренные вершины

        for (int k = 0; k < G.V; k++)
        {
            unsearched.add(k); //посмотреть
            searched.add(k,-1);
            Back.add(new Vector<>());
            DFS.add(new Vector<>());
            P.add(new Vector<>());
            NumVert.add(-1);
            Low.add(-1);
        }
        System.out.println("***Размерность основного массива Order = " + 2*G.E + "***");
        for (int k = 0; k < 2*G.E; k++)
        {
            /*
             первый и второй элемент - вершины ребра, \
             3-ий элемент - идентификатор:
             '0' - мост;
             '1' - прямое ребро;
             '2' - обратное ребро;
             */
            Order.add(new Vector<>());
        }
        Numeration = 0;
        //построение DFS-остова
        int iV;//текущая вершина

       // int Number = 0; // номер для перенумерования вершин в дереве

        int first_vertex = (int)(Math.random()* G.V);
        System.out.println("Корень = " + first_vertex);



        //NumVert[first_vertex] = Number; // перенумеровали в первую вершину
        NumVert.setElementAt(Number,first_vertex);

        P.get(first_vertex).add(first_vertex);//[first_vertex].push_back(first_vertex); // вычисляем P(v) для первой вершины

        iV = first_vertex;

        unsearched.setElementAt(-1,iV);
        searched.setElementAt(iV,iV);

        DFS_pre(G, unsearched, searched, iV, Number,NumVert,DFS,Back,P, Order);


        Vector<Integer> watch = new Vector<>(G.V);
        for (int i = 0; i < G.V; i++) {
            watch.add(i,-1);
        }
        Vector<Integer> unwatch = new Vector<>();
        for (int k = 0; k < DFS.size(); k++)
        {
            unwatch.add(k);//push_back(k);
        }

        iV = first_vertex;

        System.out.println(" ");

        //Vector<Integer> revNum = new Vector<>(G.V);

        for (int i = 0; i < G.V; i++)
        {
            revNum.add(0); // например ноль
        }
        for (int in = 0; in < G.V; in++)
        {
            revNum.setElementAt(in,NumVert.get(in));
        }

        int safety = 0;
        int FOR_RECORD = 0;
        int count = 0;
        for (int pk = 0; pk < G.V; pk++)
        {
            safety = pk;
            pk = revNum.get(pk);

            if (pk != first_vertex) // чтобы первую не задействовать
            {
                find_bridges(pk, pk, watch, count,DFS,Back,P);
            }
            watch.setElementAt(pk,pk);

            pk = safety;

        }


        for (int u = 0; u < DFS.size(); u++)
        {
            for (int y = 0; y < P.get(u).size(); y++)
            {
                P.get(u).setElementAt(NumVert.get(P.get(u).get(y)),y);
            }

        }



        for (int r = 0; r < DFS.size(); r++)
        {
            Low.setElementAt(Collections.min(P.get(r)),r);
        }

        System.out.println("МОСТЫ:");

        for (int fin = 0; fin < G.V; fin++)
        {
            for (int infin = 0; infin < DFS.get(fin).size(); infin++)
            {
                if(Low.get(DFS.get(fin).get(infin)) >= NumVert.get(DFS.get(fin).get(infin))) //проверить, например на equals
                {

                    System.out.println(fin + " - " + DFS.get(fin).get(infin));
                    FOR_RECORD++;

                 // Запоминаем порядок включения мостов
                    Order.get(Numeration).add(0, fin);
                    Order.get(Numeration).add(1, DFS.get(fin).get(infin));
                    Order.get(Numeration).add(2, 3);    // тип ребра: '2' == обратное
                    incNumeration();
                }
            }
        }
        show_all(G,NumVert,DFS,Back,P);
        System.out.println("Количество  мостов - " + FOR_RECORD);

        System.out.println("\n  Размер вектора Order = " + Order.size());
        Order.setSize(G.E + FOR_RECORD);
        System.out.println("    А теперь = " + Order.size());
        Order.setSize(Numeration);
        System.out.println("    А вот прям теперь = " + Order.size());
        // Проверка получившегося массива
        System.out.println("\n Порядок добавления ребер:");
        for (int i = 0; i < Order.size(); i++)  // WARNING Иногда цикл выжодит за область существующих элементов --> ERROR
        {
            System.out.println("\n(" + Order.get(i).get(0) + ", " +
                     Order.get(i).get(1) + ", " + Order.get(i).get(2) + ") ");
        }

    }

    /**
     *
     * @param G
     * @param unsearched
     * @param searched
     * @param iV
     * @param Number
     * @param NumVert
     * @param DFS
     * @param Back
     * @param P
     * @param Order
     * @return
     */
   public int DFS_pre(graph G, Vector<Integer> unsearched, Vector<Integer> searched, int iV, int Number,
                       Vector<Integer> NumVert,Vector<Vector<Integer>> DFS,Vector<Vector<Integer>> Back,
                       Vector<Vector<Integer>> P, Vector<Vector<Integer>> Order)
    {
        for (int j = 0; j < G.gr_.get(iV).size(); j++)
        {
            if(searched.get(G.gr_.get(iV).get(j)) == G.gr_.get(iV).get(j)) //проверить на equals
            {
                boolean kek = false;
                int step_ = 0;
                Vector<Integer> watched = new Vector<>(G.gr_.size());
                for (int i = 0; i < G.V; i++) {
                    watched.add(i,-1);
                }


                if (!wooden(DFS, iV, G.gr_.get(iV).get(j)) && !(rev(Back, iV, G.gr_.get(iV).get(j)))) // нужна проверка, есть ли это ребро уже в обратных или древесых
                {
                    if (back_check(DFS, G.gr_.get(iV).get(j), iV, kek, step_, watched)) // поменял местами вершины в функции
                    {
                        Back.get(iV).add(G.gr_.get(iV).get(j));

                        // Запоминаем порядок включения обратных ребер
                        Order.get(Numeration).add(iV);
                        Order.get(Numeration).add(G.gr_.get(iV).get(j));
                        Order.get(Numeration).add(2);    // тип ребра: '2' == обратное
                        incNumeration();
                    }
                }
            }
            else
            {
                DFS.get(iV).add(G.gr_.get(iV).get(j));

                // Запоминаем порядок включения прямых ребер
                //System.out.println("***Размерность = (" + Order.capacity() + ", " + Order.get(0).capacity() + ")***");
                Order.get(Numeration).add(0, iV);
                Order.get(Numeration).add(1, G.gr_.get(iV).get(j));
                Order.get(Numeration).add(2, 1);    // тип ребра: '1' == прямое
                incNumeration();

                //System.out.print("\n Последний элемент:" + );

               Number++;
                //int T = Number;

                NumVert.setElementAt( Number,G.gr_.get(iV).get(j));
                //  NumVert.setElementAt(Number+1,G.gr_.get(iV).get(j));
                P.get(G.gr_.get(iV).get(j)).add(G.gr_.get(iV).get(j));
                unsearched.setElementAt(-1,G.gr_.get(iV).get(j));
                searched.setElementAt(G.gr_.get(iV).get(j),G.gr_.get(iV).get(j));
                Number = DFS_pre(G, unsearched, searched, G.gr_.get(iV).get(j), Number,NumVert,DFS,Back,P, Order); // рекурсия

            }


        }
        return Number;


    }

    /**
     *
     * @param DFS
     * @param vertex1
     * @param vertex2
     * @param check
     * @param step_
     * @param watched
     * @return
     */
    boolean back_check(Vector<Vector<Integer>> DFS, int vertex1, int vertex2, boolean check, int step_, Vector<Integer> watched)
    {

        for (int j = 0; j < DFS.get(vertex1).size(); j++)
        {
            if ((check) || (DFS.get(vertex1).get(j) == vertex2)) //  если уже найдено или нашлось сейчас//опять equals
            {
                if (check) // если обратное
                {
                    return check;
                }
                else
                {
                    if (step_ == 0) // если прямой предок
                    {
                        check = false;
                        return check;
                    }
                    else // если непрямой предок
                    {
                        check = true;
                        return check; //  проверить
                    }
                }
            }
            else
            {
                if (watched.get(j).equals(j))
                {
                    watched.setElementAt(j,j);
                    back_check(DFS, j, vertex2, check, ++step_, watched); // проверяем дальше
                }
                //иначе продолжаем цикл
            }
        }
        return true;


    }

    /**
     *
     * @param G
     * @param NumVert
     * @param DFS
     * @param Back
     * @param P
     */
    public void show_all(graph G, Vector<Integer> NumVert, Vector<Vector<Integer>> DFS,Vector<Vector<Integer>> Back,Vector<Vector<Integer>> P)
    {
        System.out.println("Остовное дерево:");
        G.show(DFS);
        System.out.println("Обратные ребра:");
        G.show(Back);
        System.out.println("P(V) для вершин:");
        G.show(P);
        System.out.println("Перенумерация в DFS-обходе:");
        for (int j = 0; j < NumVert.size(); j++)
        {
            System.out.print(j + "-я вершина: ");
            System.out.println(NumVert.get(j) + " ");//опять махинации с пробелом!!
        }
        System.out.println(" ");
    }


    /**
     *
     * @param DFS
     * @param iV
     * @param KREK
     * @return
     */
    boolean wooden(Vector<Vector<Integer>> DFS, int iV, int KREK) // пока проверка на древесность в одну сторону
    {
        //cout << "ДРЕВЕСНОСТЬ" << endl;
        for (int zer = 0; zer < DFS.get(KREK).size(); zer++)
        {

            if (DFS.get(KREK).get(zer) == iV)
            {
                //cout << "Да, древесная" << endl;
                return true;
            }

        }
        //cout << "Нет, не древесная" << endl;
        return false;
    }

    /**
     *
     * @param Back
     * @param iV
     * @param KREK
     * @return
     */
    boolean rev(Vector<Vector<Integer>> Back, int iV, int KREK)
    {
        //cout << "ОБРАТНОСТЬ" << endl;
        for (int zer = 0; zer < Back.get(KREK).size(); zer++)
        {
            if (Back.get(KREK).get(zer) == iV) // equals проверить
            {
                //cout << "Да, обратная" << endl;
                return true;
            }

        }
        //cout << "Нет, не обратная" << endl;
        return false;
    }

    /**
     *
     * @param iV
     * @param FOR_RECORD
     * @param watch
     * @param count
     * @param DFS
     * @param Back
     * @param P
     */
    void find_bridges( int iV, int FOR_RECORD, Vector<Integer> watch, int count, Vector<Vector<Integer>> DFS,Vector<Vector<Integer>> Back,Vector<Vector<Integer>> P)
    {

        for (int j = 0; j < DFS.get(iV).size(); j++)
        {
            //cout << "Тек вершина " << iV << ", тек номер j " << j << endl;
            if(DFS.get(DFS.get(iV).get(j)).size() != 0)
            {

                find_bridges( DFS.get(iV).get(j), FOR_RECORD, watch, count,DFS,Back,P); // проверить

            }

            for (int z = 0; z < Back.get(DFS.get(iV).get(j)).size(); z++)
            {
                 // если ведущее обратное ребро попало а просмотренную вершину, вершина в p(v)
                if(watch.get(Back.get(DFS.get(iV).get(j)).get(z)) ==  Back.get(DFS.get(iV).get(j)).get(z)) //equals все дела
                {
                    if (!(P.get(FOR_RECORD).contains(Back.get(DFS.get(iV).get(j)).get(z))))
                    {
 //запихать вершину в которую можно попасть таким способом
                        P.get(FOR_RECORD).add(Back.get(DFS.get(iV).get(j)).get(z));
                        Collections.sort(P.get(FOR_RECORD));
                    }

                }
            }



        }
        if (count == 0)
        {
            for (int z = 0; z < Back.get(FOR_RECORD).size(); z++)
            {
                if (watch.get(Back.get(FOR_RECORD).get(z)) == Back.get(FOR_RECORD).get(z)) //equals
                {
                    if(!(P.get(FOR_RECORD).contains(Back.get(FOR_RECORD).get(z))))
                    {
                        P.get(FOR_RECORD).add(Back.get(FOR_RECORD).get(z));
                    }

                }
            }
        }


    }
}
