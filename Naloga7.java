import java.io.*;
import java.util.*;


//java Naloga7 C:\Users\User\Desktop\test.txt C:\Users\User\Desktop\out.txt

class Struct {

    public HashMap <Integer, Node> Map_id;
    public HashMap <Integer, Node> Map_v;
    public int size;
    public

    class Node {

        int id;
        int v;
        int id_l;
        int id_r;
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

    public void init(int n) {

        size = n;
        Map_id = new HashMap<>();
        Map_v = new HashMap<>();
    }

    public void vnesi(String data) {

        String[] s = data.split(",");

        int a = Integer.parseInt(s[0]);
        int b = Integer.parseInt(s[1]);
        int c = Integer.parseInt(s[2]);
        int d = Integer.parseInt(s[3]);

        Node n = new Node (a, b, c ,d);

        Map_id.put (a, n);
        Map_v.put (b, n);
    }

    public int uredi() {

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

        for (int j = 1; j <= size; j++) {

            Node n  = Map_v.get(j);

            if (n.id_p == 0)
                return n.id;
        }

        return -1;
    }

    public void izpisi () {

        for (int i = 1; i <= size; i++) {

            Node n = Map_v.get(i);

            System.out.printf("ID: %d, V: %d, ID_L: %d, ID_R: %d, ID_P: %d\n\n", n.id, n.v, n.id_l, n.id_r, n.id_p);
        }
    }

    public void dodeli_koordinate (int root, PrintWriter izhod, int tip) { //0...root, 1...levi child, 2...desni child

        if (root != -1) {

            switch (tip) {

                case 0:

                    Node n = Map_id.get(root);
                    n.y = 0;
                    n.x = power(n.id_l);
                    izhod.printf("%d,%d,%d\n", n.v, n.x, n.y);

                    dodeli_koordinate(n.id_l, izhod, 1);
                    dodeli_koordinate(n.id_r, izhod, 2);
                    break;

                case 1:

                    Node m = Map_id.get(root);
                    Node parent = Map_id.get(m.id_p);
                    int r_child = m.id_r;


                    m.y = parent.y + 1;
                    m.x = parent.x - power(r_child) - 1;
                    izhod.printf("%d,%d,%d\n", m.v, m.x, m.y);

                    dodeli_koordinate(m.id_l, izhod, 1);
                    dodeli_koordinate(m.id_r, izhod, 2);
                    break;

                case 2:

                    Node b = Map_id.get(root);
                    Node paren = Map_id.get(b.id_p);
                    int l_child = b.id_l;

                    b.y = paren.y + 1;
                    b.x = paren.x + power(l_child) + 1;
                    izhod.printf("%d,%d,%d\n", b.v, b.x, b.y);

                    dodeli_koordinate(b.id_l, izhod, 1);
                    dodeli_koordinate(b.id_r, izhod, 2);
                    break;
            }
        }
    }

    public int power (int id) {

        if (id == -1)
            return 0;

        else {

            Node n = Map_id.get(id);
            int l = n.id_l;
            int r = n.id_r;

            return power(l) + 1 + power(r);
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

            struct.dodeli_koordinate(struct.uredi(), izhod, 0);

            final long et = System.currentTimeMillis();

            System.out.println("Cas: " + (et - st));

            vhod.close();
            izhod.close();
        }
    }
}
