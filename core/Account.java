package bank.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// Custom exceptions for clarity
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
    public void deposit(double amount) {
        try {
            if (amount <= 0) {
                throw new InvalidAmountException("Deposit amount must be greater than zero.");
            }

            balance += amount;
            Transaction t = new Transaction("Deposit", amount);
            pendingStack.push(t);
            undoStack.push(t);

        } catch (InvalidAmountException e) {
            System.err.println("❌ Deposit failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("⚠️ Unexpected error during deposit: " + e.getMessage());
        }
    }

    // Withdraw money
    public boolean withdraw(double amount) {
        try {
            if (amount <= 0)
                throw new InvalidAmountException("Withdrawal amount must be greater than zero.");

            if (amount > balance)
                throw new InsufficientBalanceException("Insufficient balance for withdrawal.");

            balance -= amount;
            Transaction t = new Transaction("Withdraw", amount);
            pendingStack.push(t);
            undoStack.push(t);
            return true;

        } catch (InvalidAmountException | InsufficientBalanceException e) {
            System.err.println("❌ Withdrawal failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("⚠️ Unexpected error during withdrawal: " + e.getMessage());
        }
        return false;
    }

    // Undo last transaction
    public void undo() {
        try {
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

        } catch (IllegalStateException e) {
            System.err.println("⚠️ Undo failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("⚠️ Unexpected error during undo: " + e.getMessage());
        }
    }

    // Clear pending and undo history
    public void clearPending() {
        try {
            pendingStack.clear();
            undoStack.clear();
        } catch (Exception e) {
            System.err.println("⚠️ Failed to clear transactions: " + e.getMessage());
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getPendingTransactions() {
        return new ArrayList<>(pendingStack);
    }
}
