package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Elsa");
		SweBank.openAccount("Lars");
		Nordea.openAccount("Lars");
		DanskeBank.openAccount("Ingrid");
	}
	/**
	 * Tests the correctness of retrieving the name of the bank.
	 */
	@Test
	public void testGetName() {
		//fail("Write test case here");
		assertEquals("SweBank", SweBank.getName());
		assertEquals("Nordea", Nordea.getName());
		assertEquals("DanskeBank", DanskeBank.getName());
	}
	/**
	 * Tests the correctness of retrieving the currency of the bank.
	 */
	@Test
	public void testGetCurrency() {
		//fail("Write test case here");
		assertEquals(SEK, SweBank.getCurrency());
		assertEquals(DKK, DanskeBank.getCurrency());
		assertEquals(SEK, Nordea.getCurrency());
	}
	/**
	 * Tests the correctness of opening an account in the bank.
	 * It expects the method to create a new account for a unique identifier and throw an AccountExistsException if an account with the given identifier already exists.
	 */
	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
	//	fail("Write test case here");
		assertTrue(SweBank.accountExists("Ulrika"));
	}
	/**
	 * Tests the correctness of depositing funds into a specific account in the bank.
	 */
	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		//fail("Write test case here");
		SweBank.deposit("Elsa", new Money(10000, SEK));
    	assertEquals(Integer.valueOf(10000), SweBank.getBalance("Elsa"));
	}
	/**
	 * Tests the correctness of withdrawing funds from an account in the bank.
	 * It expects the withdraw() method to correctly withdraw the specified amount from the account.
	 */
	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		SweBank.deposit("Elsa", new Money(10000, SEK));
		SweBank.withdraw("Elsa", new Money(5000, SEK));
		assertEquals(Integer.valueOf(5000), SweBank.getBalance("Elsa"));
	}
	/**
	 * Tests the correctness of retrieving the account balance in the bank.
	 */
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		assertEquals(Integer.valueOf(0), SweBank.getBalance("Elsa"));
	}
	/**
	 * Tests the correctness of transferring funds between accounts in different banks.
	 * It expects the method to correctly transfer funds from one account to another.
	 */
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		SweBank.deposit("Elsa", new Money(10000, SEK));
		SweBank.transfer("Elsa", Nordea, "Lars", new Money(5000, SEK));
		assertEquals(Integer.valueOf(5000), SweBank.getBalance("Elsa"));
		assertEquals(Integer.valueOf(5000), Nordea.getBalance("Lars"));
	}
	/**
	 * Tests the correctness of a scheduled, cyclic transfer of funds between accounts in the same bank.
	 * It expects addTimedPayment() to correctly create a cyclic transfer, and tick() to execute it.
	 */
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		SweBank.deposit("Elsa", new Money(100000, SEK));
		SweBank.addTimedPayment("Elsa", "payment1", 1, 1, new Money(1000, SEK), SweBank, "Lars");
		SweBank.tick(); // Simulate time passing
		assertEquals(Integer.valueOf(99000), SweBank.getBalance("Elsa"));
		assertEquals(Integer.valueOf(0), Nordea.getBalance("Lars"));
	}
}
