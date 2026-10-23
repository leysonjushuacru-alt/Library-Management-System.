import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LibraryGUI extends JFrame {
    private final Library library = new Library();

    private final DefaultTableModel itemModel = new DefaultTableModel(
            new String[]{"Type", "ID", "Title", "Status", "Loan Days"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable itemTable = new JTable(itemModel);

    private final DefaultTableModel memberModel = new DefaultTableModel(
            new String[]{"Role", "ID", "Name", "Borrowed", "Limit"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable memberTable = new JTable(memberModel);

    private final JTextArea logArea = new JTextArea(8, 40);

    public LibraryGUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(820, 540);
        setLocationRelativeTo(null);

        library.addItem(new Book("B001", "Java Basics", "James Gosling"));
        library.addItem(new Magazine("M001", "Tech Weekly", "Issue 12"));
        library.addItem(new Dvd("D001", "Learn Coding", 90));
        library.addMember(new Student("S001", "Ana"));
        library.addMember(new Teacher("T001", "Mr. Cruz"));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Items", buildItemsPanel());
        tabs.addTab("Members", buildMembersPanel());
        tabs.addTab("Borrow / Return", buildBorrowPanel());
        add(tabs);

        refreshItems(library.getItems());
        refreshMembers();
    }

    private JPanel buildItemsPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Book", "Magazine", "DVD"});
        JTextField idField = new JTextField(6);
        JTextField titleField = new JTextField(12);
        JLabel extraLabel = new JLabel("Author:");
        JTextField extraField = new JTextField(10);
        JButton addButton = new JButton("Add Item");

        typeBox.addActionListener(e -> {
            String t = (String) typeBox.getSelectedItem();
            if ("Book".equals(t)) {
                extraLabel.setText("Author:");
            } else if ("Magazine".equals(t)) {
                extraLabel.setText("Issue:");
            } else {
                extraLabel.setText("Minutes:");
            }
        });

        addButton.addActionListener(e -> {
            try {
                String type = (String) typeBox.getSelectedItem();
                String id = idField.getText();
                String title = titleField.getText();
                String extra = extraField.getText().trim();
                LibraryItem item;
                if ("Book".equals(type)) {
                    item = new Book(id, title, extra);
                } else if ("Magazine".equals(type)) {
                    item = new Magazine(id, title, extra);
                } else {
                    int minutes;
                    try {
                        minutes = Integer.parseInt(extra);
                    } catch (NumberFormatException ex) {
                        throw new IllegalArgumentException("Minutes must be a whole number.");
                    }
                    item = new Dvd(id, title, minutes);
                }
                library.addItem(item);
                idField.setText("");
                titleField.setText("");
                extraField.setText("");
                refreshItems(library.getItems());
                showInfo("Item added.");
            } catch (IllegalArgumentException | IllegalStateException ex) {
                showError(ex.getMessage());
            }
        });

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        form.add(new JLabel("Type:"));
        form.add(typeBox);
        form.add(new JLabel("ID:"));
        form.add(idField);
        form.add(new JLabel("Title:"));
        form.add(titleField);
        form.add(extraLabel);
        form.add(extraField);
        form.add(addButton);

        JTextField searchField = new JTextField(14);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");

        searchButton.addActionListener(e -> refreshItems(library.searchItems(searchField.getText())));
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            refreshItems(library.getItems());
        });

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        searchRow.add(new JLabel("Search:"));
        searchRow.add(searchField);
        searchRow.add(searchButton);
        searchRow.add(showAllButton);

        JPanel top = new JPanel(new GridLayout(2, 1));
        top.add(form);
        top.add(searchRow);

        JButton deleteButton = new JButton("Delete Selected Item");
        deleteButton.addActionListener(e -> {
            int row = itemTable.getSelectedRow();
            if (row == -1) {
                showError("Select an item in the table first.");
                return;
            }
            String id = itemModel.getValueAt(row, 1).toString();
            int choice = JOptionPane.showConfirmDialog(this,
                    "Delete item " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
            try {
                library.removeItem(id);
                refreshItems(library.getItems());
            } catch (IllegalArgumentException | IllegalStateException ex) {
                showError(ex.getMessage());
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(deleteButton);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(itemTable), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Student", "Teacher"});
        JTextField idField = new JTextField(8);
        JTextField nameField = new JTextField(14);
        JButton addButton = new JButton("Add Member");

        addButton.addActionListener(e -> {
            try {
                String role = (String) roleBox.getSelectedItem();
                Person member;
                if ("Student".equals(role)) {
                    member = new Student(idField.getText(), nameField.getText());
                } else {
                    member = new Teacher(idField.getText(), nameField.getText());
                }
                library.addMember(member);
                idField.setText("");
                nameField.setText("");
                refreshMembers();
                showInfo("Member added.");
            } catch (IllegalArgumentException | IllegalStateException ex) {
                showError(ex.getMessage());
            }
        });

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        form.add(new JLabel("Role:"));
        form.add(roleBox);
        form.add(new JLabel("ID:"));
        form.add(idField);
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(addButton);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(memberTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildBorrowPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField memberField = new JTextField(8);
        JTextField itemField = new JTextField(8);
        JTextField daysLateField = new JTextField("0", 4);
        JButton borrowButton = new JButton("Borrow");
        JButton returnButton = new JButton("Return");

        borrowButton.addActionListener(e -> {
            try {
                library.borrowItem(memberField.getText(), itemField.getText());
                log("Borrowed: item " + itemField.getText().trim()
                        + " by member " + memberField.getText().trim());
                refreshItems(library.getItems());
                refreshMembers();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                showError(ex.getMessage());
            }
        });

        returnButton.addActionListener(e -> {
            try {
                int daysLate;
                try {
                    daysLate = Integer.parseInt(daysLateField.getText().trim());
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Days late must be a whole number.");
                }
                double fee = library.returnItem(memberField.getText(), itemField.getText(), daysLate);
                log("Returned: item " + itemField.getText().trim()
                        + " | Late fee: PHP " + String.format("%.2f", fee));
                refreshItems(library.getItems());
                refreshMembers();
            } catch (IllegalArgumentException | IllegalStateException ex) {
                showError(ex.getMessage());
            }
        });

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        form.add(new JLabel("Member ID:"));
        form.add(memberField);
        form.add(new JLabel("Item ID:"));
        form.add(itemField);
        form.add(new JLabel("Days late:"));
        form.add(daysLateField);
        form.add(borrowButton);
        form.add(returnButton);

        logArea.setEditable(false);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        return panel;
    }

    private void refreshItems(List<LibraryItem> list) {
        itemModel.setRowCount(0);
        for (LibraryItem item : list) {
            itemModel.addRow(new Object[]{
                    item.getItemType(),
                    item.getId(),
                    item.getTitle(),
                    item.isAvailable() ? "Available" : "Borrowed",
                    item.getLoanDays()
            });
        }
    }

    private void refreshMembers() {
        memberModel.setRowCount(0);
        for (Person p : library.getMembers()) {
            memberModel.addRow(new Object[]{
                    p.getRole(),
                    p.getId(),
                    p.getName(),
                    p.getBorrowedItems().size(),
                    p.getMaxBorrowLimit()
            });
        }
    }

    private void log(String text) {
        logArea.append(text + "\n");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}