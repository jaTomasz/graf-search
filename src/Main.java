import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class Node {
    int number;
    boolean visited = false;
    Node p = null;//"parent"

    public Node(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        if (visited)
            return number + "(◌)";
        else
            return number + "(●)";
    }
}

class Joint {
    Node first;
    Node last;
    int n; //how many routes from first to last

    public Joint(Node first, Node last, int n) {
        this.first = first;
        this.last = last;
        this.n = n;
    }

    @Override
    public String toString() {
        return first + "---<" + n + "x>-->" + last;
    }
}

class Graf {
    ArrayList<Joint> joints = new ArrayList<>();
    ArrayList<Node> nodes = new ArrayList<>();
}


public class Main {

    public static void DFS(Graf g) {
        for (Node u : g.nodes) {
            u.visited = false;
            u.p = null;
        }
        for (Node u : g.nodes) {
            if (!u.visited)
                DFS(g, u);
        }
    }

    public static void DFS(Graf g, Node u) {
        u.visited = true;
        Node v;

        System.out.print(u + "-->");

        for (int i = 0; i < g.joints.size(); i++) {
            v = g.joints.get(i).last;
            if (g.joints.get(i).first == u && (!v.visited)) {

                v.p = u;
                DFS(g, v);
            }
        }
    }


    public static void BFS(Graf g) {
        System.out.print("-->");

        for (Node s : g.nodes) {
            s.visited = false;
            s.p = null;
        }
        for (Node s : g.nodes) {
            if (!s.visited)
                BFS(g, s);
        }
    }

    public static void BFS(Graf g, Node s) {
        Queue<Node> Q = new LinkedList<>();

        s.visited = true;
        Node v;


        Q.add(s);
        System.out.print(s + "-->");

        while(Q.size()>0) {
            s = Q.poll();
            for (int i = 0; i < g.joints.size(); i++) {
                v = g.joints.get(i).last;
                if (g.joints.get(i).first == s && (!v.visited)) {

                    v.visited = true;
                    v.p = s;
                    Q.add(v);
                    System.out.print(v + "-->");
                }
            }
        }
    }

    public static void main(String[] args) {

        int n, i = 0, number;//number of nodes, iterator, number just read

        Path filePath = Paths.get("src/in.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        n = scanner.nextInt();

        Graf graf = new Graf();
        for (int j = 0; j < n; j++) {
            graf.nodes.add(new Node(j + 1));
        }

        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                number = scanner.nextInt();
                if (number != 0)
                    graf.joints.add(new Joint(graf.nodes.get(i / n), graf.nodes.get(i % n), number));
                i++;
            } else {
                scanner.next();
            }
        }

        System.out.println(graf.joints);

        DFS(graf);
        System.out.println('\n');
        BFS(graf);
    }
}
