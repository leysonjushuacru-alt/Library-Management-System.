public class Student extends Person {

    public Student(String id, String name) {
        super(id, name);
    }

    @Override
    public int getMaxBorrowLimit() {
        return 3;
    }

    @Override
    public String getRole() {
        return "Student";
    }
}