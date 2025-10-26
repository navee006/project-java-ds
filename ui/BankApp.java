package bank.ui;

import bank.core.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Random;

public class BankApp extends JFrame {
    private final BankManager manager;
    private final JTextField nameField;
    private final JTextField accField;
    private final JTextField amtField;
    private final JTextArea consoleArea;
    private final JTextArea pendingArea;
    private final JTextArea accountsArea;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    private final Random rnd = new Random();

    public BankApp() {
        super("💠 Smart Bank — Dashboard");
        manager = new BankManager();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(245, 250, 255));

        // Header
        JLabel header = new JLabel("🏦 Smart Bank Console", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setOpaque(true);
        header.setBackground(new Color(30, 130, 255));
        header.setForeground(Color.WHITE);
        header.setBorder(new EmptyBorder(12, 12, 12, 12));
        add(header, BorderLayout.NORTH);

        // Left: inputs & buttons
        JPanel left = new JPanel(new BorderLayout(10, 10));
        left.setBorder(new EmptyBorder(10, 14, 10, 10));
        left.setBackground(Color.WHITE);

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Account Number:"), gbc);
        gbc.gridx = 1;
        accField = new JTextField(15);
        inputPanel.add(accField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        amtField = new JTextField(15);
        inputPanel.add(amtField, gbc);

        left.add(inputPanel, BorderLayout.NORTH);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        btnPanel.setBackground(Color.WHITE);

        Color primary = new Color(34, 139, 230);
        Color accent = new Color(76, 175, 80);

        JButton addCustBtn = makeButton("Add Customer 👤", primary);
        JButton createAccBtn = makeButton("Create Account 🏧", primary);
        JButton depositBtn = makeButton("Deposit 💰", accent);
        JButton withdrawBtn = makeButton("Withdraw 💸", new Color(220, 80, 80));
        JButton undoBtn = makeButton("Undo ⏪", new Color(120, 90, 200));
        JButton showQueueBtn = makeButton("Pending 🕓", primary);
        JButton clearBtn = makeButton("Clear 🧹", new Color(180, 80, 120));
        JButton showAccBtn = makeButton("Accounts 🧾", primary);

        btnPanel.add(addCustBtn);
        btnPanel.add(createAccBtn);
        btnPanel.add(depositBtn);
        btnPanel.add(withdrawBtn);
        btnPanel.add(undoBtn);
        btnPanel.add(showQueueBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(showAccBtn);

        left.add(btnPanel, BorderLayout.CENTER);
        add(left, BorderLayout.WEST);

        // Center + Right
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBackground(getContentPane().getBackground());
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        consoleArea.setLineWrap(true);
        consoleArea.setWrapStyleWord(true);
        JScrollPane consoleScroll = new JScrollPane(consoleArea);
        consoleScroll.setBorder(BorderFactory.createTitledBorder("Console"));
        center.add(consoleScroll, BorderLayout.CENTER);
        DefaultCaret caret = (DefaultCaret) consoleArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        add(center, BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(2, 1, 8, 8));
        right.setPreferredSize(new Dimension(360, 0));
        right.setBackground(getContentPane().getBackground());

        pendingArea = new JTextArea();
        pendingArea.setEditable(false);
        pendingArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane pScroll = new JScrollPane(pendingArea);
        pScroll.setBorder(BorderFactory.createTitledBorder("Pending Transactions"));

        accountsArea = new JTextArea();
        accountsArea.setEditable(false);
        accountsArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane aScroll = new JScrollPane(accountsArea);
        aScroll.setBorder(BorderFactory.createTitledBorder("Accounts"));

        right.add(pScroll);
        right.add(aScroll);
        add(right, BorderLayout.EAST);

        // Button actions
        addCustBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) { appendConsole("⚠️ Enter a valid customer name."); return; }
            String acc = accField.getText().trim();
            if (acc.isEmpty()) acc = "ACC" + (1000 + rnd.nextInt(9000));
            manager.addCustomer(name, acc);
            appendConsole("✅ Added customer: " + name + " (" + acc + ")");
            refreshAccounts();
            refreshPending();
        });

        createAccBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) name = "User" + (100 + rnd.nextInt(900));
            String acc = accField.getText().trim();
            if (acc.isEmpty()) acc = "ACC" + (1000 + rnd.nextInt(9000));
            manager.addAccount(name, acc);
            appendConsole("🏧 Created account: " + acc + " (Holder: " + name + ")");
            refreshAccounts();
        });

      depositBtn.addActionListener(e -> {
    String acc = accField.getText().trim();
    String amtText = amtField.getText().trim();

    if (acc.isEmpty()) {
        appendConsole("⚠️ Enter a valid account number.");
        return;
    }

    try {
        double amt = Double.parseDouble(amtText); // parse numeric input

        Account account = manager.findAccount(acc); // find account
        if (account == null) {
            appendConsole("❌ Deposit failed: Account " + acc + " does not exist!");
            return;
        }

        account.deposit(amt); // throws IllegalArgumentException if <= 0
        appendConsole("💰 Deposited ₹" + df.format(amt) + " to " + acc); // only print if successful

    } catch (NumberFormatException ex) {
        appendConsole("⚠️ Invalid amount format."); // letters, empty
    } catch (IllegalArgumentException ex) {
        appendConsole("❌ Deposit failed: " + ex.getMessage()); // negative or zero
    }

    refreshPending();
    refreshAccounts();
});
withdrawBtn.addActionListener(e -> {
    String acc = accField.getText().trim();
    String amtText = amtField.getText().trim();

    if (acc.isEmpty()) {
        appendConsole("⚠️ Enter a valid account number.");
        return;
    }

    try {
        double amt = Double.parseDouble(amtText);
        Account account = manager.findAccount(acc);

        if (account == null) {
            appendConsole("❌ Withdrawal failed: Account " + acc + " does not exist!");
            return;
        }

        boolean success = account.withdraw(amt); // handles negative/insufficient internally
        if (success) {
            appendConsole("💸 Withdrew ₹" + df.format(amt) + " from " + acc);
        }

    } catch (NumberFormatException ex) {
        appendConsole("⚠️ Invalid amount format.");
    } catch (IllegalArgumentException ex) {
        appendConsole("❌ Withdrawal failed: " + ex.getMessage());
    }

    refreshPending();
    refreshAccounts();
});






    

        undoBtn.addActionListener(e -> {
    String acc = accField.getText().trim();
    if (acc.isEmpty()) { appendConsole("⚠️ Enter account number."); return; }

    try {
        Account account = manager.findAccount(acc);
        if (account == null) {
            appendConsole("❌ Account " + acc + " does not exist!");
            return;
        }

        account.undo();  // throws IllegalStateException if no transaction
        appendConsole("⏪ Last transaction undone for " + acc);

    } catch (IllegalStateException ex) {
        appendConsole("⚠️ Undo failed: " + ex.getMessage());
    }
    
    refreshPending();
    refreshAccounts();
});


        showQueueBtn.addActionListener(e -> {
            String acc = accField.getText().trim();
            if (acc.isEmpty()) { appendConsole("⚠️ Enter account number."); return; }
            pendingArea.setText(manager.showPending(acc));
            appendConsole("🕓 Pending transactions shown.");
        });

        clearBtn.addActionListener(e -> {
            String acc = accField.getText().trim();
            if (acc.isEmpty()) { appendConsole("⚠️ Enter account number."); return; }
            manager.clearPending(acc);
            appendConsole("🧹 Cleared pending & undo history.");
            refreshPending();
        });

        showAccBtn.addActionListener(e -> {
            refreshAccounts();
            appendConsole("🧾 Accounts refreshed.");
        });

        amtField.addActionListener(e -> depositBtn.doClick());

        setVisible(true);
        refreshPending();
        refreshAccounts();
    }

    private JButton makeButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        return b;
    }

    private void appendConsole(String msg) {
        consoleArea.append(msg + "\n");
    }

    private void refreshPending() {
        String acc = accField.getText().trim();
        if (!acc.isEmpty()) pendingArea.setText(manager.showPending(acc));
    }

    private void refreshAccounts() {
        accountsArea.setText(manager.showAllAccounts());
    }

    private double parseAmountOrZero() {
        try {
            String s = amtField.getText().trim();
            if (s.isEmpty()) return 0;
            return Double.parseDouble(s);
        } catch (Exception ex) {
            appendConsole("⚠️ Invalid amount format.");
            return 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BankApp::new);
    }
}

