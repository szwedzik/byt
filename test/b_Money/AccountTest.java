package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;

	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Adam", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	/**
	 * Tests the addition and removal of a scheduled, cyclic fund transfer on an account.
	 * Initially, a timed payment is added to `testAccount` using `addTimedPayment()`, and
	 * its existence is verified. It is then removed using `removeTimedPayment()`, and
	 * its non-existence is confirmed. This ensures proper functionality of both adding
	 * and removing timed payments in the account system.
	 */
	@Test
	public void testAddRemoveTimedPayment() {
		testAccount.addTimedPayment("payment1", 2, 1, new Money(1000, SEK), SweBank, "Alice");
		assertTrue(testAccount.timedPaymentExists("payment1"));
		testAccount.removeTimedPayment("payment1");
		assertFalse(testAccount.timedPaymentExists("payment1"));
	}
	/**
	 * Tests the correctness of scheduling and executing a cyclic fund transfer between accounts in different banks.
	 * Verifies that `addTimedPayment()` correctly schedules the transfer and `tick()` successfully executes it.
	 * After two `tick()` calls, it checks if the balance of the target account (Alice at SweBank) has increased as expected.
	 */
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		testAccount.addTimedPayment("payment1", 2, 1, new Money(1000, SEK), SweBank, "Alice");
		testAccount.tick();
		testAccount.tick();
		assertEquals(new Money(1001000, SEK).getAmount(), SweBank.getBalance("Alice"));
	}
	/**
	 * Tests the accuracy of depositing and withdrawing funds from an account.
	 * Verifies that after a deposit, the account balance increases correctly, 
	 * and after a withdrawal, it decreases accordingly.
	 */
	@Test
	public void testAddWithdraw() {
		testAccount.deposit(new Money(5000, SEK));
		assertEquals(new Money(10005000, SEK).toString(), testAccount.getBalance().toString());
		testAccount.withdraw(new Money(2000, SEK));
		assertEquals(new Money(10003000, SEK).toString(), testAccount.getBalance().toString());
	}
	/**
	 * Tests the correctness of retrieving the account balance.
	 * Verifies that the `getBalance()` method returns the expected balance amount.
	 */
	@Test
	public void testGetBalance() {
		assertEquals(new Money(10000000, SEK).toString(), testAccount.getBalance().toString());
	}
}