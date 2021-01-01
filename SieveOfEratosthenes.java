import java.util.Scanner;
/**
 * 
 * @author andrew
 * This is an implementation for finding prime numbers using the Sieve of Eratosthenes.
 */

public class SieveOfEratosthenes {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("Find all prime numbers <= n, enter n: ");
		int n = input.nextInt();
		
		boolean[] primes = new boolean[n+1];
		
		// initialise primes[i] to be true
		for (int i = 0; i < primes.length; i++) {
			primes[i] = true;
		}
		
		// calculate which candidates are NOT prime
		for (int k = 2; k <= n; k++) {
			if (primes[k]) {
				for (int i = k; i <= n / k; i++) { 
					// the limit (i <= n / k) is to avoid out of boundary error
					primes[k*i] = false;
				}
			}
		}
		
		final int NUMBER_PER_LINE = 10;
		int count = 0;
		
		// find prime numbers
		for (int i = 2; i < primes.length; i++) {
			if (primes[i]) {
				count++;
				if (count % NUMBER_PER_LINE == 0) {
					System.out.printf("%7d\n", i);
				} else {
					System.out.printf("%7d", i);
				}
			}
		}
		
		System.out.println("\n " + count + " prime(s) less than or equal to " + n);
	}
}
