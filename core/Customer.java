package bank.core;

public class Customer {
    private String name;
    private String id;
    private String accountNumber;

    public Customer(String name, String id, String accountNumber) {
        this.name = name;
        this.id = id;
        this.accountNumber = accountNumber;
    }

    public String getName() { return name; }
    public String getId() { return id; }
    public String getAccountNumber() { return accountNumber; }

    @Override
    public String toString() {
        return name + " (" + accountNumber + ")";
    }
}
