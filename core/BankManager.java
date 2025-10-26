package bank.core;

import java.util.ArrayList;
import java.util.List;

public class BankManager {
    private final List<Account> accounts;

    public BankManager() {
        accounts = new ArrayList<>();
    }

    // Add a new customer (or reuse existing account)
    public void addCustomer(String name, String accNum) {
        Account acc = findAccount(accNum);
        if(acc == null) {
            acc = new Account(accNum, name);
            accounts.add(acc);
        } else {
            // Optional: update holder name
            // acc.setHolderName(name);
        }
    }

    // Create an account separately (reuse existing account number)
    public void addAccount(String name, String accNum) {
        Account acc = findAccount(accNum);
        if(acc == null) {
            acc = new Account(accNum, name);
            accounts.add(acc);
        }
    }

    // Deposit amount
public void deposit(String accNum, double amount) throws IllegalArgumentException {
    Account acc = findAccount(accNum);
    if (acc == null) {
        throw new IllegalArgumentException("Account " + accNum + " does not exist!");
    }
    acc.deposit(amount); // may throw IllegalArgumentException from Account
}
    //withdraw
public boolean withdraw(String accNum, double amount) throws IllegalArgumentException {
    Account acc = findAccount(accNum);
    if (acc == null) {
        throw new IllegalArgumentException("Account " + accNum + " does not exist!");
    }
    return acc.withdraw(amount); // may throw IllegalArgumentException from Account
}


    // Undo last transaction for account
    public void undo(String accNum) {
        Account acc = findAccount(accNum);
        if(acc != null) {
            acc.undo();
        }
    }

    // Clear pending & undo history for account
    public void clearPending(String accNum) {
        Account acc = findAccount(accNum);
        if(acc != null) {
            acc.clearPending();
        }
    }

    // Show pending transactions for account
    public String showPending(String accNum) {
        Account acc = findAccount(accNum);
        if(acc != null) {
            List<Transaction> pending = acc.getPendingTransactions();
            if(pending.isEmpty()) return "No pending transactions.";
            StringBuilder sb = new StringBuilder("🧾 Pending Transactions:\n");
            for(Transaction t : pending) {
                sb.append("• ").append(t.getType())
                  .append(" ₹").append(String.format("%,.2f", t.getAmount()))
                  .append("\n");
            }
            return sb.toString();
        }
        return "Account not found.";
    }

    // Show all accounts (unique only)
    public String showAllAccounts() {
        if(accounts.isEmpty()) return "No accounts found.";
        StringBuilder sb = new StringBuilder();
        List<String> seenAccNums = new ArrayList<>();
        for(Account acc : accounts) {
            if(seenAccNums.contains(acc.getAccountNumber())) continue;
            seenAccNums.add(acc.getAccountNumber());
            sb.append("🏦 ").append(acc.getHolderName())
              .append(" (").append(acc.getAccountNumber()).append(") : ₹")
              .append(String.format("%,.2f", acc.getBalance()))
              .append("\n");
        }
        return sb.toString();
    }

    // Find account by account number
    public Account findAccount(String accNum) {
        for(Account acc : accounts) {
            if(acc.getAccountNumber().equals(accNum)) return acc;
        }
        return null;
    }
}

