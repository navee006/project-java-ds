package bank.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// Custom exceptions for clarity (optional, you can also use IllegalArgumentException)
class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }
}

class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

public class Account {
    private final String accountNumber;
    private final String holderName;
    private double balance;

    private final Stack<Transaction> undoStack;
    private final Stack<Transaction> pendingStack;

    public Account(String accountNumber, String holderName) {
        if (accountNumber == null || accountNumber.isEmpty())
            throw new IllegalArgumentException("Account number cannot be null or empty.");
        if (holderName == null || holderName.isEmpty())
            throw new IllegalArgumentException("Holder name cannot be null or empty.");

        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = 0.0;
        this.undoStack = new Stack<>();
        this.pendingStack = new Stack<>();
    }

    // Deposit money
    public void deposit(double amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        balance += amount;
        Transaction t = new Transaction("Deposit", amount);
        pendingStack.push(t);
        undoStack.push(t);
    }

    // Withdraw money
    public boolean withdraw(double amount) throws IllegalArgumentException {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        if (amount > balance) throw new IllegalArgumentException("Insufficient balance for withdrawal.");
        
        balance -= amount;
        Transaction t = new Transaction("Withdraw", amount);
        pendingStack.push(t);
        undoStack.push(t);
        return true;
    }

    // Undo last transaction
    public void undo() throws IllegalStateException {
        if (undoStack.isEmpty()) {
            throw new IllegalStateException("No transaction to undo.");
        }

        Transaction last = undoStack.pop();
        if (last.getType().equals("Deposit")) {
            balance -= last.getAmount();
        } else if (last.getType().equals("Withdraw")) {
            balance += last.getAmount();
        }

        pendingStack.push(new Transaction("Undo " + last.getType(), last.getAmount()));
    }

    // Clear pending and undo history
    public void clearPending() {
        pendingStack.clear();
        undoStack.clear();
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }
    public List<Transaction> getPendingTransactions() { return new ArrayList<>(pendingStack); }
}

