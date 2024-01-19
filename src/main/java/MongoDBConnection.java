import com.mongodb.client.*;
import org.bson.Document;
import com.mongodb.client.model.*;

import java.time.LocalDate;

import java.time.temporal.ChronoUnit;

import java.util.*;

public class MongoDBConnection {

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> studentCollection;
    private MongoCollection<Document> adminCollection;
    private MongoCollection<Document> bookCollection;

    public MongoDBConnection(String connectionString, String databaseName,
                             String studentCollectionName, String adminCollectionName, String bookCollectionName) {
        this.mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase(databaseName);
        this.studentCollection = database.getCollection(studentCollectionName);
        this.adminCollection = database.getCollection(adminCollectionName);
        this.bookCollection = database.getCollection(bookCollectionName);
    }

    public void addBook(BookModel book) {
        bookCollection.insertOne(book.toDocument());
        System.out.println("Book added: " + book.getBookName());
    }

    public void deleteBook(int bookId) {
        bookCollection.deleteOne(Filters.eq("bookId", bookId));
        System.out.println("Book with ID " + bookId + " deleted.");
    }

    public void updateBook(int bookId, String newBookName, String newAuthorName) {
        bookCollection.updateOne(
                Filters.eq("bookId", bookId),
                Updates.combine(
                        Updates.set("bookName", newBookName),
                        Updates.set("authorName", newAuthorName)
                )
        );
        System.out.println("Book with ID " + bookId + " updated.");
    }

    public void editBookStatus(int bookId, String newStatus, String studentId, String dueDate) {
        bookCollection.updateOne(
                Filters.eq("bookId", bookId),
                Updates.combine(
                        Updates.set("issueStatus", newStatus),
                        Updates.set("studentId", studentId),
                        Updates.set("dueDate", dueDate)
                )
        );
        System.out.println("Book with ID " + bookId + " status edited.");
    }

    public void viewBooksIssuedToStudent(String studentId) {
        FindIterable<Document> documents = bookCollection.find(Filters.eq("studentId", studentId));
        System.out.println("Books issued to student " + studentId + ":");
        for (Document document : documents) {
            System.out.println(document.toJson());
        }
    }

    public Document searchBookByUniqueId(int bookId) {
        return bookCollection.find(Filters.eq("bookId", bookId)).first();
    }


    public void checkStatusOfBooksIssuedByUser(String studentId) {
        Document student = studentCollection.find(Filters.eq("studentId", studentId)).first();

        if (student != null) {
            Set<String> issuedBooks = new HashSet<>(student.getList("issuedBooks", String.class));

            if (!issuedBooks.isEmpty()) {
                System.out.println("Books issued by student " + studentId + ":");

                for (String bookId : issuedBooks) {
                    Document book = bookCollection.find(Filters.eq("bookId", Integer.parseInt(bookId))).first();

                    if (book != null) {
                        System.out.println("Book ID: " + bookId);
                        System.out.println("Book Name: " + book.getString("bookName"));
                        System.out.println("Author Name: " + book.getString("authorName"));
                        System.out.println("Issue Date: " + student.getString("issueDate"));
                        System.out.println("Due Date: " + student.getString("dueDate"));
                        System.out.println("Fine: Rs. " + student.getDouble("fine"));
                        System.out.println();
                    }
                }



            } else {
                System.out.println("No books issued by student " + studentId + ".");
            }
        } else {
            System.out.println("Student not found.");
        }
    }



    public void insertStudent(StudentModel student) {
        studentCollection.insertOne(student.toDocument());
        System.out.println("Student added: " + student.getStudentName());
    }

    public void insertAdmin(AdminModel admin) {
        adminCollection.insertOne(admin.toDocument());
        System.out.println("Admin added: " + admin.getAdminName());
    }

    public void insertBook(BookModel book) {
        bookCollection.insertOne(book.toDocument());
        System.out.println("Book added: " + book.getBookName());
    }

    // Methods for other operations on students, admins, and books...

    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    public void displayAllDocuments(String collectionName) {
        MongoCollection<Document> collection;

        // Determine the correct collection based on the provided collectionName
        switch (collectionName.toLowerCase()) {
            case "students":
                collection = studentCollection;
                break;
            case "admins":
                collection = adminCollection;
                break;
            case "books":
                collection = bookCollection;
                break;
            default:
                System.out.println("Invalid collection name: " + collectionName);
                return;
        }

        // Fetch all documents from the selected collection
        FindIterable<Document> documents = collection.find();

        // Print each document
        System.out.println("All Documents in " + collectionName + " collection:");
        for (Document document : documents) {
            System.out.println(document.toJson());
        }
    }

    public boolean adminLogin(String username, String password) {

        Document admin = adminCollection.find(Filters.eq("username", username)).first();
        if (admin == null) {
            return false;
        }
        return admin.getString("password").equals(password);
    }

    public boolean studentLogin(String studentId, String password2) {
        Document student = studentCollection.find(Filters.eq("studentId", studentId)).first();
        if (student == null) {
            return false;
        }
        return student.getString("password").equals(password2);
    }




// ...

    public void issueBook(int bookId1, String studentId1) {
        Document book = bookCollection.find(Filters.eq("bookId", bookId1)).first();

        assert book != null;
        int count = book.getInteger("count");
        if (count > 0) {
            Document student = studentCollection.find(Filters.eq("studentId", studentId1)).first();


            assert student != null;


            int borrowedBooks = student.getInteger("borrowedBooks");



            if(borrowedBooks >= 4){
                System.out.println("You have already issued 4 books.");
                return;
            }
            int cnt = book.getInteger("count");


            // Update the BookModel document to add the student to the issued list
            if(cnt-1 >= 0){
                bookCollection.updateOne(
                        Filters.eq("bookId", bookId1),
                        Updates.combine(
                                Updates.set("issueStatus", "available"),
                                Updates.set("count", count - 1),
                                Updates.addToSet("issuedToStudents", studentId1) // Add studentId to issuedToStudents array
                        )
                );

            }
            else{
                bookCollection.updateOne(
                        Filters.eq("bookId", bookId1),
                        Updates.combine(
                                Updates.set("issueStatus", "not available"),
                                Updates.set("count", count - 1),
                                Updates.addToSet("issuedToStudents", studentId1) // Add studentId to issuedToStudents array
                        )
                );
            }

            // Update the StudentModel document to add the issued book
            studentCollection.updateOne(
                    Filters.eq("studentId", studentId1),
                    Updates.combine(
                            Updates.set("borrowedBooks", borrowedBooks + 1),
                            Updates.addToSet("issuedBooks", String.valueOf(bookId1)) // Add bookId to issuedBooks array
                    )
            );


            String issueDateStr = student.getString("issueDate");

            if (issueDateStr == null) {
                // Issue date is null, update it along with due date
                LocalDate currentDate = LocalDate.now();
                LocalDate dueDate = currentDate.plusDays(15);

                studentCollection.updateOne(
                        Filters.eq("studentId", studentId1),
                        Updates.combine(
                                Updates.set("issueDate", currentDate.toString()),
                                Updates.set("dueDate", dueDate.toString())
                        )
                );
            } else {
                // Issue date is not null, calculate fine and update
                double fine = calculateFine(LocalDate.parse(student.getString("dueDate")));
                studentCollection.updateOne(
                        Filters.eq("studentId", studentId1),
                        Updates.set("fine", fine)
                );
            }



            System.out.println("Book with ID " + bookId1 + " issued.");
        } else {
            bookCollection.updateOne(
                    Filters.eq("bookId", bookId1),
                    Updates.combine(
                            Updates.set("issueStatus", "not available")
                    )
            );
            System.out.println("Book with ID " + bookId1 + " not available.");
        }
    }

    public double calculateFine(LocalDate dueDate) {
        LocalDate today = LocalDate.now();
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);

        // Calculate the fine (assuming Rs. 5 per day after 15 days)
        return daysOverdue > 15 ? (daysOverdue - 15) * 5 : 0;
    }





    public BookModel getBookById(int bookId1) {
        Document book = bookCollection.find(Filters.eq("bookId", bookId1)).first();
        assert book != null;
        return new BookModel(book.getString("bookName"), book.getString("authorName"), book.getInteger("bookId"), book.getInteger("count"));
    }

    public StudentModel getStudentById(String studentId) {
        Document student = studentCollection.find(Filters.eq("studentId", studentId)).first();
        assert student != null;
        return new StudentModel(student.getString("studentId"), student.getString("studentName"), student.getString("password"));
    }

    public MongoCollection<Document> getBookCollection() {
        return bookCollection;
    }
}
