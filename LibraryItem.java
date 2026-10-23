public abstract class LibraryItem implements Borrowable {
    private final String id;
    private String title;
    private boolean borrowed;

    public LibraryItem(String id, String title) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID is required.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }
        this.id = id.trim();
        this.title = title.trim();
        this.borrowed = false;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }
        this.title = title.trim();
    }

    @Override
    public boolean isAvailable() {
        return !borrowed;
    }

    @Override
    public void borrowItem() {
        if (borrowed) {
            throw new IllegalStateException(title + " is already borrowed.");
        }
        borrowed = true;
    }

    @Override
    public void returnItem() {
        if (!borrowed) {
            throw new IllegalStateException(title + " was not borrowed.");
        }
        borrowed = false;
    }

    public abstract double calculateLateFee(int daysLate);

    public abstract String getItemType();

    public abstract int getLoanDays();

    @Override
    public String toString() {
        return getItemType() + " | " + id + " | " + title + " | "
                + (borrowed ? "Borrowed" : "Available");
    }
}