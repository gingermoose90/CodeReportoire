import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

/**
 * 
 * @author andrew
 * This is my submission for Dijkstra's shortest reach algorithm on HackerRank
 * https://www.hackerrank.com/challenges/dijkstrashortreach/problem
 */

public class DijkstraShortestReach {

    private static class Node {
        public final int id;
        public int distance;
        public HashMap<Node, Edge> neighbours;

        public Node(int id) {
            this.id = id;
            distance = -1;
            neighbours = new HashMap<>();
        }

        public void addNeighbour(Node neighbour, Edge edge) {
            neighbours.put(neighbour, edge);
            neighbour.neighbours.put(this, edge);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            } else if (other == null || !(other instanceof Node)) {
                return false;
            }
            Node otherNode = (Node) other;
            return this.id == otherNode.id;
        }

        public int hashCode() {
            return id;
        }
    }

    private static class Edge {
        public int w;
        public Node[] nodes = new Node[2];

        public Edge(int weight, Node n1, Node n2) {
            this.w = weight;
            nodes[0] = n1;
            nodes[1] = n2;
        }
        
        public int getWeight() {
            return w;
        }
    }

    static void shortestReach(Node start) {
        if (start == null) {
            return;
        }
        ArrayDeque<Node> deque = new ArrayDeque<>();
        start.distance = 0;
        deque.add(start);
        while (!deque.isEmpty()) {
            Node curr = deque.remove();
            for (Node neighbour : curr.neighbours.keySet()) {
              Edge e = curr.neighbours.get(neighbour);
              if (neighbour.distance == -1) {
                neighbour.distance = curr.distance + e.getWeight();
                deque.add(neighbour);
              } else if (curr.distance + e.getWeight() < neighbour.distance) {
                neighbour.distance = curr.distance + e.getWeight();
                deque.add(neighbour);
              }
            }
        }
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int t = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int tItr = 0; tItr < t; tItr++) {
            String[] nm = scanner.nextLine().split(" ");

            int n = Integer.parseInt(nm[0]);

            int m = Integer.parseInt(nm[1]);

            Node[] nodes = new Node[n+1];
            nodes[0] = null;
            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node(i);
            }

            for (int i = 0; i < m; i++) {
                String[] edgesRowItems = scanner.nextLine().split(" ");
                scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
                int n1 = Integer.valueOf(edgesRowItems[0]);
                int n2 = Integer.valueOf(edgesRowItems[1]);
                Edge e = new Edge(Integer.valueOf(edgesRowItems[2]), nodes[n1], nodes[n2]);
                if (nodes[n1].neighbours.containsKey(nodes[n2])) {
                  if (e.getWeight() < nodes[n1].neighbours.get(nodes[n2]).getWeight()) {
                    nodes[n1].addNeighbour(nodes[n2], e);
                  }
                } else {
                  nodes[n1].addNeighbour(nodes[n2], e);
                }
            }

            int s = scanner.nextInt();
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            shortestReach(nodes[s]);

            for (int i = 1; i <= n; i++) {
                if (i != s) {
                    bufferedWriter.write(nodes[i].distance + " ");
                }
            }

            bufferedWriter.newLine();
        }

        bufferedWriter.close();

        scanner.close();
    }
}
