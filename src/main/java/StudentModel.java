import org.bson.Document;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class StudentModel {

    private String studentId;
    private String studentName;
    private String password;
    private int borrowedBooks;
    private Set<String> issuedBooks;


    private double fine; // Fine for overdue books

    private LocalDate issueDate; // Date when the book was issued

    private LocalDate dueDate;
    public StudentModel(String studentId, String studentName, String password) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.password = password;
        this.borrowedBooks = 0;
        this.issuedBooks = new HashSet<>();
        this.fine = 0.0;
        this.issueDate = null;
        this.dueDate = null;
    }

    public String getStudentName() {
        return studentName;
    }

    public Document toDocument() {
        return new Document("studentId", studentId)
                .append("studentName", studentName)
                .append("password", password)
                .append("borrowedBooks", borrowedBooks)
                .append("issuedBooks", issuedBooks)
                .append("fine", fine)
                .append("dueDate", dueDate != null ? dueDate.toString() : null) // Convert LocalDate to string
                .append("issueDate", issueDate != null ? issueDate.toString() : null); // Convert LocalDate to string
    }

    public Set<String> getIssuedBooks() {
        return issuedBooks;
    }

    public String getIssueDate() {
        return issueDate.toString();
    }

    public String getDueDate() {
        return dueDate.toString();
    }

    public String getFine() {
        return String.valueOf(fine);
    }




    // Method to calculate fine based on issue date


    // Getters and setters (if needed)
}
