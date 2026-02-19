package org.example;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

//THE TEST CLASS
@DisplayName("BankAccount - All Business Rule Tests")
class BankAccountTest {

    //FIELD - The object we're testing (System Under Test - SUT)
    private BankAccount bankAccount;

    /*
    @BeforeAll - Runs ONCE before the entire test class starts
    Must be static
    USE for : expensive setup (database connectivity, file loading)
     */
    @BeforeAll
    static void initialiseTestEnvironment(){

        System.out.println("Starting Bank Account Test Suite");
        //In production: start test db, load config files etc.

    }

    /*
    BeforeEach() - Runs before EVERY individual test
    It gives each test a fresh, clean starting point
    This is the most important lifecycle method
     */

    @BeforeEach
    void setUp(){
        //Create fresh BankAccount before each test
        // This ensure tests are INDEPENDENT of each other
        bankAccount = new BankAccount("SAVINGS");
        System.out.println("Fresh bank Account created for test");
    }

    /*
    AfterEach() - Runs after EVERY individual test
    Used for - cleanup, resetting state, closing resources
     */
    @AfterEach
    void tearDown(){
        // cleanup after each test
        bankAccount = null;
        System.out.println("Test cleanup completed");
    }

    /*
    AfterAll() - Runs once after all the tests in the class have finished execution
    Must be static
     */
    @AfterAll
    static void cleanupTestEnvironment(){
        System.out.println("Bank Account Test Suite completed");
        //in production: close database connections, cleanup test data
    }

    @Test
    @DisplayName("New account should have zero balance")
    void newAccount_shouldHaveZeroBalance(){

        //We're testing: hen we create a new account, its balance should be 0.0
       // BankAccount bankAccount = new BankAccount();
        assertEquals(0.0,bankAccount.getBalance());
    }

    //Depositing money increases the balance
    @Test
    void deposit_validAmountShouldIncreaseBalance(){

        //Performing the action
        bankAccount.deposit(5000.00);

        //Verifying the outcome
        assertEquals(5000.00,bankAccount.getBalance());

    }


    //Depositing a negative amount should throw an exception
    @Test
    void deposit_negativeAmountShouldThrowException(){

        //Expect this to throw an illegal argument exception
        assertThrows(IllegalArgumentException.class,
                () -> bankAccount.deposit(-5000.00));
    }


    @Test
    void withdraw_validAmountShouldDecreaseBalance(){
        bankAccount.deposit(1000.00);

        bankAccount.withdraw(300.00);

        assertEquals(700, bankAccount.getBalance());

    }

    @Test
    void withdraw_negativeAmountShouldThrowException(){

        assertThrows(IllegalArgumentException.class,
                () -> {
            bankAccount.withdraw(-500.00);
                });

    }

    @Test
    void withdraw_moreThanBalanceShouldThrowException(){

        bankAccount.deposit(5000.00);

        assertThrows(IllegalArgumentException.class, () ->{

            bankAccount.withdraw(10000.00);
        });
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // Covers: Branch A (savings + high balance) + Branch E (≤ 10 transactions)
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @Test
    void calculateCharge_savingsHighBalance_noCharge() {
        // ARRANGE
        BankAccount account = new BankAccount("SAVINGS");
        account.deposit(1500.0);

        // ACT
        double charge = account.calculateServiceCharge();

        // ASSERT
        assertEquals(0.0, charge, "Savings with balance >= 1000: should be free");
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // Covers: Branch B (savings + LOW balance) + Branch E (≤ 10 transactions)
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @Test
    void calculateCharge_savingsLowBalance_fiveDollarCharge() {
        // ARRANGE
        BankAccount account = new BankAccount("SAVINGS");
        account.deposit(500.0);  // Below 1000 threshold

        // ACT
        double charge = account.calculateServiceCharge();

        // ASSERT
        assertEquals(5.0, charge, "Savings with balance < 1000: $5 charge");
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // Covers: Branch C (current account) + Branch E (≤ 10 transactions)
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @Test
    void calculateCharge_currentAccount_tenDollarCharge() {
        // ARRANGE
        BankAccount account = new BankAccount("CURRENT");
        account.deposit(5000.0);

        // ACT
        double charge = account.calculateServiceCharge();

        // ASSERT
        assertEquals(10.0, charge, "Current account: flat $10 charge");
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // Covers: Branch D (excessive transactions)
    // This is the CRITICAL branch many developers miss!
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @Test
    void calculateCharge_excessiveTransactions_additionalCharge() {
        // ARRANGE
        BankAccount account = new BankAccount("SAVINGS");
        account.deposit(1500.0);  // Start with high balance

        // Simulate 11 transactions to exceed the threshold of 10
        // We'll do 11 small withdrawals
        for (int i = 0; i < 11; i++) {
            account.deposit(10.0);    // Each deposit is a transaction
        }
        // transactionCount is now 12 (1 initial + 11 loop deposits)

        // ACT
        double charge = account.calculateServiceCharge();

        // ASSERT
        // Base charge: 0.0 (high-balance savings)
        // Extra charge: +2.0 (excessive transactions)
        // Total: 2.0
        assertEquals(2.0, charge,
                "High-balance savings with > 10 transactions: $2 extra charge");
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // Boundary Condition Test — The Boundary Value (balance == 1000)
    // This is the EXACT boundary between "free" and "$5 charge"
    // Always test boundaries explicitly!
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    @Test
    void calculateCharge_savingsExactlyAtBoundary_noCharge() {
        // ARRANGE
        BankAccount account = new BankAccount("SAVINGS");
        account.deposit(1000.0);  // EXACTLY at the boundary

        // ACT
        double charge = account.calculateServiceCharge();

        // ASSERT
        // balance >= 1000 is TRUE when balance == 1000
        // So charge should be 0.0
        assertEquals(0.0, charge,
                "Savings with balance EXACTLY 1000: should be free (>= 1000 is true)");
    }
}