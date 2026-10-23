public class Dvd extends LibraryItem {
    private int durationMinutes;

    public Dvd(String id, String title, int durationMinutes) {
        super(id, title);
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0.");
        }
        this.durationMinutes = durationMinutes;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public double calculateLateFee(int daysLate) {
        return daysLate <= 0 ? 0 : daysLate * 10.00;
    }

    @Override
    public String getItemType() {
        return "DVD";
    }

    @Override
    public int getLoanDays() {
        return 2;
    }
}