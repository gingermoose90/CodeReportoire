import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author andrew
 * This is a demonstration of multithreading programming using a thread pool service.
 * The critical region of code is protected using an explicit lock in the Account class
 * deposit method.
 */

public class AccountWithSyncUsingLock {
  private static Account account = new Account();

  public static void main(String[] args) {
    ExecutorService executor = Executors.newCachedThreadPool();

    for (int i = 0; i < 100; i++) {
      executor.execute(new AddAPennyTask());
    }

    executor.shutdown();

    while (!executor.isTerminated()) {}

    System.out.println("What is balance? " + account.getBalance());
  }

  public static class AddAPennyTask implements Runnable {
    public void run() {
      account.deposit(1);
    }
  }

  public static class Account {
    private static Lock lock = new ReentrantLock();
    private int balance = 0;

    public int getBalance() {
      return balance;
    }

    public void deposit(int amount) {
      // acquire the lock
      lock.lock();

      try {
        int newBalance = balance + amount;

        // delay deliberately added to magnify the data corruption problem
        Thread.sleep(5);

        balance = newBalance;
      } catch (InterruptedException ex) {
      } finally {
        lock.unlock();
      }
    }
  }
}
