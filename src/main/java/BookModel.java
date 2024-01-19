import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class BookModel {

    private String bookName;
    private String authorName;
    private int bookId;
    private String issueStatus;

    private List<String> issuedToStudents;  // List to store student IDs

    private int count;

    // Constructors, getters, and setters...
    public BookModel(String bookName, String authorName, int bookId, int count) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.bookId = bookId;
        this.count = count;
        this.issueStatus = "Available";
        this.issuedToStudents = new ArrayList<>();  // Initialize the list
    }

    public Document toDocument() {
        return new Document("bookName", bookName)
                .append("authorName", authorName)
                .append("bookId", bookId)
                .append("issueStatus", issueStatus)
                .append("count", count)
                .append("issuedToStudents", issuedToStudents);  // Add the list to the document
    }

    public String getBookName() {
        return bookName;
    }



    // Add other getters and setters as needed...
}
