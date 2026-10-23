public class Magazine extends LibraryItem {
    private String issue;

    public Magazine(String id, String title, String issue) {
        super(id, title);
        if (issue == null || issue.trim().isEmpty()) {
            throw new IllegalArgumentException("Issue is required.");
        }
        this.issue = issue.trim();
    }

    public String getIssue() {
        return issue;
    }

    @Override
    public double calculateLateFee(int daysLate) {
        return daysLate <= 0 ? 0 : daysLate * 2.00;
    }

    @Override
    public String getItemType() {
        return "Magazine";
    }

    @Override
    public int getLoanDays() {
        return 3;
    }
}