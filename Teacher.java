public class Teacher extends Person {

    public Teacher(String id, String name) {
        super(id, name);
    }

    @Override
    public int getMaxBorrowLimit() {
        return 5;
    }

    @Override
    public String getRole() {
        return "Teacher";
    }
}