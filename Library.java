import java.util.ArrayList;
import java.util.List;

public class Library {
    private final List<LibraryItem> items = new ArrayList<>();
    private final List<Person> members = new ArrayList<>();

    public void addItem(LibraryItem item) {
        if (findItem(item.getId()) != null) {
            throw new IllegalArgumentException("An item with ID " + item.getId() + " already exists.");
        }
        items.add(item);
    }

    public void removeItem(String id) {
        LibraryItem item = findItem(id);
        if (item == null) {
            throw new IllegalArgumentException("Item not found.");
        }
        if (!item.isAvailable()) {
            throw new IllegalStateException("Cannot delete an item that is currently borrowed.");
        }
        items.remove(item);
    }

    public LibraryItem findItem(String id) {
        if (id == null) return null;
        for (LibraryItem item : items) {
            if (item.getId().equalsIgnoreCase(id.trim())) {
                return item;
            }
        }
        return null;
    }

    public List<LibraryItem> searchItems(String keyword) {
        List<LibraryItem> results = new ArrayList<>();
        String key = keyword == null ? "" : keyword.trim().toLowerCase();
        for (LibraryItem item : items) {
            if (item.getTitle().toLowerCase().contains(key)
                    || item.getId().toLowerCase().contains(key)) {
                results.add(item);
            }
        }
        return results;
    }

    public List<LibraryItem> getItems() {
        return new ArrayList<>(items);
    }

    public void addMember(Person member) {
        if (findMember(member.getId()) != null) {
            throw new IllegalArgumentException("A member with ID " + member.getId() + " already exists.");
        }
        members.add(member);
    }

    public Person findMember(String id) {
        if (id == null) return null;
        for (Person member : members) {
            if (member.getId().equalsIgnoreCase(id.trim())) {
                return member;
            }
        }
        return null;
    }

    public List<Person> getMembers() {
        return new ArrayList<>(members);
    }

    public void borrowItem(String memberId, String itemId) {
        Person member = findMember(memberId);
        if (member == null) {
            throw new IllegalArgumentException("Member not found.");
        }
        LibraryItem item = findItem(itemId);
        if (item == null) {
            throw new IllegalArgumentException("Item not found.");
        }
        if (!member.canBorrow()) {
            throw new IllegalStateException(member.getName() + " reached the borrow limit of "
                    + member.getMaxBorrowLimit() + ".");
        }
        item.borrowItem();
        member.addBorrowed(item);
    }

    public double returnItem(String memberId, String itemId, int daysLate) {
        if (daysLate < 0) {
            throw new IllegalArgumentException("Days late cannot be negative.");
        }
        Person member = findMember(memberId);
        if (member == null) {
            throw new IllegalArgumentException("Member not found.");
        }
        LibraryItem item = findItem(itemId);
        if (item == null) {
            throw new IllegalArgumentException("Item not found.");
        }
        if (!member.getBorrowedItems().contains(item)) {
            throw new IllegalStateException(member.getName() + " did not borrow this item.");
        }
        item.returnItem();
        member.removeBorrowed(item);
        return item.calculateLateFee(daysLate);
    }
}