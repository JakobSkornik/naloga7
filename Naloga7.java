import java.io.*;
import java.util.*;

//java Naloga7 C:\Users\User\Desktop\test.txt C:\Users\User\Desktop\out.txt

class Struct {

    //struktura dveh hashmap, in vrste za izpis po nivojih

    private HashMap <Integer, Node> Map_id;
    private HashMap <Integer, Node> Map_v;
    private int size;
    private Q last;

    class Node {

        final int id;
        final int v;
        final int id_l;
        final int id_r;
        int id_p;
        int x;
        int y;

        Node(int id, int v, int id_l, int id_r) {

            this.id = id;
            this.v = v;
            this.id_l = id_l;
            this.id_r = id_r;
        }
    }

    class Q {

        final Node s;
        Q next;

        Q(Node s, Q next) {

            this.s = s;
            this.next = next;
        }
    }

    public void init(int n) {

        size = n;
        Map_id = new HashMap<>();
        Map_v = new HashMap<>();
    }

    public void vnesi(String data) {

        //format podatkov: id, vrednost, id levega otroka, id desnega otroka

        String[] s = data.split(",");

        int a = Integer.parseInt(s[0]);
        int b = Integer.parseInt(s[1]);
        int c = Integer.parseInt(s[2]);
        int d = Integer.parseInt(s[3]);

        Node n = new Node (a, b, c ,d);

        //id in vrednost sta enolicna identifikatorja, v je od 1 do N, uporaben za for zanke

        Map_id.put (a, n);
        Map_v.put (b, n);
    }

    public int uredi() {

        //for zanka cez vrednosti pregleda podatke o otrokih in vse, ki otroke imajo, doloci kot njihove starse

        for (int i = 1; i <= size; i++) {

            Node n = Map_v.get(i);

            if (n.id_l != -1) {

                Node m = Map_id.get(n.id_l);
                m.id_p = n.id;
            }

            if (n.id_r != -1) {

                Node b = Map_id.get(n.id_r);
                b.id_p = n.id;
            }
        }

        //tukaj funkcija vrne root drevesa

        for (int j = 1; j <= size; j++) {

            Node n  = Map_v.get(j);

            if (n.id_p == 0)
                return n.id;
        }

        return -1;
    }

    public void dodeli_koordinate (int root, int tip) {

        //ta funkcija zacne pri rootu in sledi formuli za izracun x lokacije
        //y lokacijo dobim iz y lokacije starsa (+1, root ima 0)
        //root ima x lokacijo enako stevilu levih otrok
        //levi otrok ima x lokacijo enako (x_oceta - velikost_desnega_poddrevesa - 1)
        //desni otrok ima x lokacijo enako (x_oceta + velikost_levega_poddrevesa + 1)
        //switch za dolocitev formule
        //0...root, 1...levi child, 2...desni child

        if (root != -1) {

            switch (tip) {

                case 0:

                    Node n = Map_id.get(root);
                    n.y = 0;
                    n.x = power(n.id_l);

                    dodeli_koordinate(n.id_l, 1);
                    dodeli_koordinate(n.id_r, 2);
                    break;

                case 1:

                    Node m = Map_id.get(root);
                    Node parent = Map_id.get(m.id_p);
                    int r_child = m.id_r;


                    m.y = parent.y + 1;
                    m.x = parent.x - power(r_child) - 1;

                    dodeli_koordinate(m.id_l, 1);
                    dodeli_koordinate(m.id_r, 2);
                    break;

                case 2:

                    Node b = Map_id.get(root);
                    Node paren = Map_id.get(b.id_p);
                    int l_child = b.id_l;

                    b.y = paren.y + 1;
                    b.x = paren.x + power(l_child) + 1;

                    dodeli_koordinate(b.id_l, 1);
                    dodeli_koordinate(b.id_r, 2);
                    break;
            }
        }
    }

    private int power (int id) {

        //rekurzivna formula za izracun velikosti poddrevesa

        if (id == -1)
            return 0;

        else {

            Node n = Map_id.get(id);
            int l = n.id_l;
            int r = n.id_r;

            return power(l) + 1 + power(r);
        }
    }

    public void writer (PrintWriter izhod, int root) {

        //izpis po nivojih s pomocjo vrste

        Q queue = new Q (Map_id.get(root), null);
        last = queue;

        while (queue != null) {

            izhod.printf("%d,%d,%d", queue.s.v, queue.s.x, queue.s.y);
            izhod.println();
            add_2_q(queue.s.id_l);
            add_2_q(queue.s.id_r);
            queue = queue.next;
        }
    }

    private void add_2_q (int id){

        if (id != -1) {

            Node n = Map_id.get(id);

            Q queue = new Q(n, null);

            last.next = queue;
            last = queue;
        }
    }
}

public class Naloga7 {

    public static void main(String[] args) throws IOException{

        if (args.length == 2) {

            final long st = System.currentTimeMillis();

            BufferedReader vhod = new BufferedReader(
                    new FileReader(args[0])
            );

            PrintWriter izhod = new PrintWriter(
                    new FileWriter(args[1])
            );

            int n = Integer.parseInt(vhod.readLine());

            Struct struct = new Struct();
            struct.init(n);

            for (int i = 0; i < n; i++)
                struct.vnesi(vhod.readLine());

            int root = struct.uredi();
            struct.dodeli_koordinate(root, 0);
            struct.writer(izhod, root);

            final long et = System.currentTimeMillis();

            System.out.println("Cas: " + (et - st));

            vhod.close();
            izhod.close();
        }
    }
}
