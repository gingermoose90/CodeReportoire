import java.util.*;

/**
 * 
 * @author andrew
 * This program searches for the nth prime number. The user enters which prime number they
 * wish to find. The program uses the following logic to search optimise the search algorithm...
 * 1) only numbers between 2 and sqrt(n) need to be examined to verify if n is prime
 * 2) the squareroot does not need to be calculated for every value of n, only the perfect squares
 * 3) not every number from 2 -> sqrt(n) needs to be examined, only every prime number from 2 -> sqrt(n)
 */

public class PrimeNumberFind {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Find the nth prime number, enter n: ");
    
    int n = scanner.nextInt();
    
    java.util.List<Integer> list = new java.util.ArrayList<>();
    
    int count = 0; // keep track of number of primes found
    int number = 2; // number to test if prime
    int squareRoot = 1;
    
    while (count < n) {
      boolean isPrime = true;
      
      if (squareRoot * squareRoot < number) {
        squareRoot++;
      }
      
      for (int k = 0; k < list.size() && list.get(k) <= squareRoot; k++) {
        if (number % list.get(k) == 0) {
          isPrime = false;
          break;
        }
      }
      
      if (isPrime) {
        count++;
        list.add(number);
      }
      number++;
    }
    System.out.println("The nth prime number is " + list.get(count-1));  
  }
}
