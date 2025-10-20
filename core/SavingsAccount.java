package bank.core;

public class SavingsAccount {
    private String accountNumber;
    private double balance;
    private String holderName;

    public SavingsAccount(String holderName, String accountNumber, double balance) {
        this.holderName = holderName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void deposit(double amt) { balance += amt; }
    public boolean withdraw(double amt) {
        if (amt <= balance) {
            balance -= amt;
            return true;
        }
        return false;
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }

    @Override
    public String toString() {
        return holderName + " [" + accountNumber + "] ₹" + balance;
    }
}
