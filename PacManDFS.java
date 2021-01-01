import java.util.*;

/**
 * 
 * @author andrew
 * This is my solution to the PacManDFS problem on HackerRank
 * https://www.hackerrank.com/challenges/pacman-dfs?h_r=internal-search
 */

public class PacManDFS {
  
  private static boolean found = false;
  private static boolean traveled[][];
  
  private static boolean dfs(int[] pos, int[] food, LinkedList<int[]> explored, LinkedList<int[]> path, char[][] board) {
    if (!traveled[pos[0]][pos[1]]) { // only evaluates to true on first call, add start position to explored list
      explored.addLast(pos);
    }
    if (pos[0] == food[0] && pos[1] == food[1]) {
      return true; // end condition
    }
    
    Stack<int[]> temp = new Stack<>();
    if (!found) {
      // get adjacent nodes for current node, not yet checked for validity
      LinkedList<int[]> positions = new LinkedList<int[]>();
      int[] up = {pos[0]-1, pos[1]};
      positions.addLast(up);
      int[] left = {pos[0], pos[1]-1};
      positions.addLast(left);
      int[] right = {pos[0], pos[1]+1};
      positions.addLast(right);
      int[] down = {pos[0]+1, pos[1]};
      positions.addLast(down);
      for (int[] p : positions) {
        if (!traveled[p[0]][p[1]]) { // do not add to stack if already explored
          if (p[0] >= 0 && p[1] >= 0) { // make sure not out of bounds
            traveled[p[0]][p[1]] = true; // will not be added when visiting other adjacent nodes
            temp.push(p);
          }
        }
      }
      while (!temp.isEmpty()) {
        int[] move = temp.pop();
        char position = board[move[0]][move[1]]; // find the char value at this board position
        if ((int) position == 45 || (int) position == 46) { // is this a valid path node, or food
          explored.add(move); // add to explored list now it has been popped from stack and verified as valid node
          found = dfs(move, food, explored, path, board); // check adjacent nodes in DFS fashion
          if (found) {
            path.addFirst(move); // once food is found, start adding path nodes in reverse fasion
            break;
          }
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
    
    final LinkedList<int[]> explored = new LinkedList<int[]>();
    final LinkedList<int[]> path = new LinkedList<int[]>();
    traveled = new boolean[rows][cols];
    
    dfs(pacPos, foodPos, explored, path, board);
    path.addFirst(pacPos); // add start node to path, not done within dfs method
    
    System.out.println(explored.size());
    Iterator<int[]> iterator = explored.iterator();
    while (iterator.hasNext()) {
      int[] node = iterator.next();
      System.out.println(node[0] + " " + node[1]);
    }
    System.out.println(path.size()-1);
    iterator = path.iterator();
    while (iterator.hasNext()) {
      int[] node = iterator.next();
      System.out.println(node[0] + " " + node[1]);
    } 
  }
}