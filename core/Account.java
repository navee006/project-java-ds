package bank.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Account {
    private final String accountNumber;
    private final String holderName;
    private double balance;

    private final Stack<Transaction> undoStack;
    private final Stack<Transaction> pendingStack;

    public Account(String accountNumber, String holderName) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = 0.0;
        this.undoStack = new Stack<>();
        this.pendingStack = new Stack<>();
    }

    public void deposit(double amount) {
        if (amount <= 0) return;
        balance += amount;
        Transaction t = new Transaction("Deposit", amount);
        pendingStack.push(t);
        undoStack.push(t);
    }

    public boolean withdraw(double amount) {
    if (amount <= 0) return false;

    if (amount > balance) {
        // Prevent overdraft
        return false;
    }

    balance -= amount;
    Transaction t = new Transaction("Withdraw", amount);
    pendingStack.push(t);  // use your existing stack
    undoStack.push(t);     // use your existing stack
    return true;
}



    public void undo() {
        if (!undoStack.isEmpty()) {
            Transaction last = undoStack.pop();
            if (last.getType().equals("Deposit")) {
                balance -= last.getAmount();
            } else if (last.getType().equals("Withdraw")) {
                balance += last.getAmount();
            }
            pendingStack.push(new Transaction("Undo " + last.getType(), last.getAmount()));
        }
    }

    public void clearPending() {
        pendingStack.clear();
        undoStack.clear();
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
