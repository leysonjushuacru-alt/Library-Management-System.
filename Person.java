import java.util.ArrayList;
import java.util.List;

public abstract class Person {
    private final String id;
    private String name;
    private final List<LibraryItem> borrowedItems = new ArrayList<>();

    public Person(String id, String name) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Member ID is required.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Member name is required.");
        }
        this.id = id.trim();
        this.name = name.trim();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Member name is required.");
        }
        this.name = name.trim();
    }

    public List<LibraryItem> getBorrowedItems() {
        return new ArrayList<>(borrowedItems);
    }

    public boolean canBorrow() {
        return borrowedItems.size() < getMaxBorrowLimit();
    }

    public void addBorrowed(LibraryItem item) {
        borrowedItems.add(item);
    }

    public void removeBorrowed(LibraryItem item) {
        borrowedItems.remove(item);
    }

    public abstract int getMaxBorrowLimit();

    public abstract String getRole();

    @Override
    public String toString() {
        return getRole() + " | " + id + " | " + name
                + " | Borrowed: " + borrowedItems.size() + "/" + getMaxBorrowLimit();
    }
}