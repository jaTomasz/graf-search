import javax.xml.bind.SchemaOutputResolver;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class Node {
    int number;
    boolean visited = false;
    boolean processed = false;
    int time;
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
    String classification = "";

    public Joint(Node first, Node last, int n) {
        this.first = first;
        this.last = last;
        this.n = n;
    }

    @Override
    public String toString() {
        return first + "---<" + n + "x" + classification + ">-->" + last;
    }
}

class Graf {
    ArrayList<Joint> joints = new ArrayList<>();
    ArrayList<Node> nodes = new ArrayList<>();
}


public class Main {
    static int time = 0;
    public static String clasify(Joint j){
            if(j.last.p == j.first)
                return "TREE";
            if(j.last.visited && !j.last.processed)
                return "BACK";
            if(j.last.processed && j.last.time>j.first.time)
                return "FORWARD";
            if(j.last.processed && j.last.time<j.first.time)
                return "CROSS";
        return "unclassified";
    }
    public static void DFS(Graf g) {
        System.out.print("DFS: ");

        time = 0;
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
        u.time = time;
        time++;
        Node v;

        System.out.print(u + "-->");

        for (int i = 0; i < g.joints.size(); i++) {
            g.joints.get(i).classification = clasify(g.joints.get(i));
            v = g.joints.get(i).last;
            if (g.joints.get(i).first == u && (!v.visited)) {

                v.p = u;
                v.processed = true;
                DFS(g, v);
        time++;
            }
        }
    }


    public static void BFS(Graf g) {
        System.out.print("BFS: ");

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
        ArrayList<Joint> BFSjoints = new ArrayList<>();

        s.visited = true;
        Node v;


        Q.add(s);
        System.out.print(s);

        while(Q.size()>0) {
            s = Q.poll();
            for (int i = 0; i < g.joints.size(); i++) {
                v = g.joints.get(i).last;
                if (g.joints.get(i).first == s && (!v.visited)) {
                    BFSjoints.add(g.joints.get(i));

                    v.visited = true;
                    v.p = s;
                    Q.add(v);
                    System.out.print("-->" + v);
                }
            }
        }
        System.out.println("\n edges:" + BFSjoints);
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

        DFS(graf);
        System.out.println("\n" + graf.joints);
        System.out.println();
        BFS(graf);
    }
}
