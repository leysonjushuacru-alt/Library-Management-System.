public class Book extends LibraryItem {
    private String author;

    public Book(String id, String title, String author) {
        super(id, title);
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author is required.");
        }
        this.author = author.trim();
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public double calculateLateFee(int daysLate) {
        return daysLate <= 0 ? 0 : daysLate * 5.00;
    }

    @Override
    public String getItemType() {
        return "Book";
    }

    @Override
    public int getLoanDays() {
        return 7;
    }
}