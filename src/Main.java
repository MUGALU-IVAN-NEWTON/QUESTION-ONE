package src;
import java.util.ArrayList;
import java.util.List;

interface Statement {
    String generateStatement();
}

abstract class Account implements Statement {
    protected String accountNo;
    protected double balance;

    public Account(String accountNo, double opening) {
        this.accountNo = accountNo;
        this.balance = opening;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public abstract boolean withdraw(double amount);

    public double getBalance() {
        return balance;
    }

    @Override
    public String generateStatement() {
        return "Account: " + accountNo + ", Balance: UGX " + balance;
    }
}

class SavingsAccount extends Account {
    private double rate;

    public SavingsAccount(String accountNo, double opening, double rate) {
        super(accountNo, opening);
        this.rate = rate;
    }

    @Override
    public boolean withdraw(double amount) {
        if (balance - amount < 0) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public void addInterest() {
        balance += balance * (rate / 100);
    }
}

class CurrentAccount extends Account {
    private double overdraft;

    public CurrentAccount(String accountNo, double opening, double overdraft) {
        super(accountNo, opening);
        this.overdraft = overdraft;
    }

    @Override
    public boolean withdraw(double amount) {
        if (balance - amount < -overdraft) {
            return false;
        }
        balance -= amount;
        return true;
    }
}

class Customer {
    private String name;
    private List<Account> accounts;

    public Customer(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account a) {
        accounts.add(a);
    }

    public double totalWorth() {
        double sum = 0;
        for (Account acc : accounts) {
            sum += acc.getBalance();
        }
        return sum;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("=== EXECUTION VERIFICATION ===");
        
        SavingsAccount sa = new SavingsAccount("SA-101", 200000, 5.0);
        System.out.println("Initial Savings " + sa.generateStatement());
        
        sa.addInterest();
        System.out.println("After Adding 5% Interest: UGX " + sa.getBalance());
        
        boolean saWithdraw1 = sa.withdraw(250000); 
        System.out.println("Withdrawing UGX 250,000 (More than balance)? Allowed: " + saWithdraw1);
        System.out.println("Savings " + sa.generateStatement());
        
        System.out.println("------------------------------------------------");

        CurrentAccount ca = new CurrentAccount("CA-202", 50000, 100000);
        System.out.println("Initial Current " + ca.generateStatement());
        
        boolean caWithdraw1 = ca.withdraw(120000);
        System.out.println("Withdrawing UGX 120,000 (Goes negative)? Allowed: " + caWithdraw1);
        System.out.println("Current " + ca.generateStatement());
        
        boolean caWithdraw2 = ca.withdraw(40000);
        System.out.println("Withdrawing another UGX 40,000 (Exceeds Overdraft)? Allowed: " + caWithdraw2);
        System.out.println("Current " + ca.generateStatement());
        
        System.out.println("------------------------------------------------");
        
        Customer customer = new Customer("Newton");
        customer.addAccount(sa);
        customer.addAccount(ca);
        System.out.println("Customer Total Worth (Sum of all accounts): UGX " + customer.totalWorth());
    }
}