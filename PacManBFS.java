import java.io.*;
import java.util.*;

/**
 * 
 * @author andrew
 * This is my solution to the PacManBFS problem on HackerRank 
 * https://www.hackerrank.com/challenges/pacman-bfs?h_r=internal-search
 * 
 */

public class PacManBFS {

  private static boolean found = false;
  private static boolean traveled[][];

  private static class Node {
    private int row;
    private int col;
    private Node prev;

    Node(int r, int c) {
      this.row = r;
      this.col = c;
    }

    public int getRow() {
      return row;
    }

    public int getCol() {
      return col;
    }

    public void setPrev(Node previous) {
      this.prev = previous;
    }

    public Node getPrev() {
      return this.prev;
    }

    @Override
    public String toString() {
      return Integer.toString(this.getRow()) + " " + Integer.toString(this.getCol());
    }
  }

  private static LinkedList<Node> calculatePath(int[] pacPos, int[] foodPos,
      LinkedList<Node> explored) {
    LinkedList<Node> path = new LinkedList<>();
    int[] start = foodPos;
    int[] finish = pacPos;
    Node curr = null;
    Node end = null;
    for (Node node : explored) {
      if (node.getRow() == start[0] && node.getCol() == start[1]) {
        curr = node;
      }
      if (node.getRow() == finish[0] && node.getCol() == finish[1]) {
        end = node;
      }
    }
    path.addFirst(curr);
    while (curr != end) {
      curr = curr.getPrev();
      path.addFirst(curr);
    }
    return path;
  }

  private static LinkedList<Node> findNextMoves(Node pos) {
    LinkedList<Node> nextMove = new LinkedList<>();

    Node up = new Node(pos.getRow() - 1, pos.getCol());
    if (up.getRow() >= 0 && up.getCol() >= 0) {
      if (!traveled[up.getRow()][up.getCol()]) {
        nextMove.add(up);
        up.setPrev(pos);
        traveled[up.getRow()][up.getCol()] = true;
      }
    }

    Node left = new Node(pos.getRow(), pos.getCol() - 1);
    if (left.getRow() >= 0 && left.getCol() >= 0) {
      if (!traveled[left.getRow()][left.getCol()]) {
        nextMove.add(left);
        left.setPrev(pos);
        traveled[left.getRow()][left.getCol()] = true;
      }
    }

    Node right = new Node(pos.getRow(), pos.getCol() + 1);
    if (right.getRow() >= 0 && right.getCol() >= 0) {
      if (!traveled[right.getRow()][right.getCol()]) {
        nextMove.add(right);
        right.setPrev(pos);
        traveled[right.getRow()][right.getCol()] = true;
      }
    }

    Node down = new Node(pos.getRow() + 1, pos.getCol());
    if (down.getRow() >= 0 && down.getCol() >= 0) {
      if (!traveled[down.getRow()][down.getCol()]) {
        nextMove.add(down);
        down.setPrev(pos);
        traveled[down.getRow()][down.getCol()] = true;
      }
    }
    return nextMove;
  }

  private static boolean bfs(Node pos, Node food, LinkedList<Node> level, LinkedList<Node> explored,
      char[][] board) {
    if (!traveled[pos.getRow()][pos.getCol()]) {
      explored.add(pos); // executed on first call to bfs for start vertex
    }
    if (pos.getRow() == food.getRow() && pos.getCol() == food.getCol()) { // check if this position
                                                                          // is food before
                                                                          // continuing
      return true;
    }
    if (level.isEmpty()) { // executed on the first call to bfs to get adjacent nodes from start
                           // vertex
      level = findNextMoves(pos);
    }
    LinkedList<Node> nextLevel = new LinkedList<>();
    while (!level.isEmpty()) {
      Node curr = level.poll();
      char position = board[curr.getRow()][curr.getCol()];
      if ((int) position == 45 || (int) position == 46) { // is valid move
        explored.add(curr);
        nextLevel = findNextMoves(curr);
        for (Node n : nextLevel) {
          level.add(n);
        }
        found = bfs(curr, food, level, explored, board); // check if this move is food
        if (found) {
          break;
        }
      }
    }
    return found;
  }

  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {

    String[] pacman = scanner.nextLine().split(" ");
    int[] pacPos = {Integer.valueOf(pacman[0]), Integer.valueOf(pacman[1])};
    String[] food = scanner.nextLine().split(" ");
    int[] foodPos = {Integer.valueOf(food[0]), Integer.valueOf(food[1])};
    String[] rc = scanner.nextLine().split(" ");
    int rows = Integer.valueOf(rc[0]);
    int cols = Integer.valueOf(rc[1]);

    char[][] board = new char[rows][cols];

    for (int i = 0; i < rows; i++) {
      String input = scanner.nextLine();
      for (int j = 0; j < cols; j++) {
        board[i][j] = input.charAt(j);
      }
    }

    Node pacmanNode = new Node(pacPos[0], pacPos[1]);
    Node foodNode = new Node(foodPos[0], foodPos[1]);
    final LinkedList<Node> explored = new LinkedList<>();
    final LinkedList<Node> level = new LinkedList<>();
    traveled = new boolean[rows][cols];

    bfs(pacmanNode, foodNode, level, explored, board);

    System.out.println(explored.size());
    Iterator<Node> iterator = explored.iterator();
    while (iterator.hasNext()) {
      Node node = iterator.next();
      System.out.println(node);
    }

    LinkedList<Node> path = calculatePath(pacPos, foodPos, explored);

    System.out.println(path.size() - 1);
    Iterator<Node> pathIterator = path.iterator();
    while (pathIterator.hasNext()) {
      Node node = pathIterator.next();
      System.out.println(node);
    }
  }
}
