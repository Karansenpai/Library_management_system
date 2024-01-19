import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;

import com.mongodb.client.FindIterable;

public class Main {


    public static void main(String[] args) {

        //please don't exploit the link
        String connectionString = "mongodb+srv://root:root@cluster0.1nisbua.mongodb.net/?retryWrites=true&w=majority";

        String databaseName = "Library_management";
        String studentCollectionName = "students";
        String adminCollectionName = "admins";
        String bookCollectionName = "books";

        // Create an instance of MongoDBConnection
        MongoDBConnection mongoDBConnection = new MongoDBConnection(
                connectionString, databaseName, studentCollectionName, adminCollectionName, bookCollectionName);

        try {


            System.out.println("Welcome to the Library Management System");
            System.out.println("Please select an option:");

            Scanner input = new Scanner(System.in);

            int choice;

            do{
                System.out.println("1. Admin Login");
                System.out.println("2. Admin Register");
                System.out.println("3: Student Login");
                System.out.println("4: Student Register");
                System.out.println("5: Exit");
                choice = input.nextInt();

                switch (choice){
                    case 1:
                        System.out.println("Enter your username");
                        String username = input.next();
                        System.out.println("Enter your password");
                        String password = input.next();
                        if(mongoDBConnection.adminLogin(username, password)){
                            System.out.println("Login Successful");
                            int adminChoice;
                            do{
                                System.out.println("1. Add a book");
                                System.out.println("2. Delete a book");
                                System.out.println("3. Update a book");
                                System.out.println("4. Edit book status");
                                System.out.println("5. View books issued to a student");
                                System.out.println("6. Search a book by unique ID");
                                System.out.println("7. Check status of books issued by a user");
                                System.out.println("8. List of All books");
                                System.out.println("9. Exit");
                                adminChoice = input.nextInt();
                                switch(adminChoice) {
                                    case 1:
                                        System.out.println("Enter the book name");
                                        String bookName = input.next();
                                        System.out.println("Enter the author name");
                                        String authorName = input.next();
                                        System.out.println("Enter the book ID");
                                        int bookId = input.nextInt();
                                        System.out.println("Enter the number of books");
                                        int number = input.nextInt();
                                        BookModel book = new BookModel(bookName, authorName, bookId, number);
                                        mongoDBConnection.addBook(book);
                                        break;
                                    case 2:
                                        System.out.println("Enter the book ID");
                                        int bookId1 = input.nextInt();
                                        mongoDBConnection.deleteBook(bookId1);
                                        break;
                                    case 3:
                                        System.out.println("Enter the book ID");
                                        int bookId2 = input.nextInt();
                                        System.out.println("Enter the new book name");
                                        String newBookName = input.next();
                                        System.out.println("Enter the new author name");
                                        String newAuthorName = input.next();
                                        mongoDBConnection.updateBook(bookId2, newBookName, newAuthorName);
                                        break;
                                    case 4:
                                        System.out.println("Enter the book ID");
                                        int bookId3 = input.nextInt();
                                        System.out.println("Enter the new status");
                                        String newStatus = input.next();
                                        System.out.println("Enter the student ID");
                                        String studentId = input.next();
                                        System.out.println("Enter the due date");
                                        String dueDate = input.next();
                                        mongoDBConnection.editBookStatus(bookId3, newStatus, studentId, dueDate);
                                        break;

                                    case 5:
                                        System.out.println("Books issued and their borrowers:");
                                        FindIterable<Document> books = mongoDBConnection.getBookCollection().find();
                                        for (Document book1 : books) {
                                            int bookId0 = book1.getInteger("bookId");
                                            String bookName1 = book1.getString("bookName");
                                            List<String> issuedToStudents = book1.getList("issuedToStudents", String.class);

                                            if (issuedToStudents != null && !issuedToStudents.isEmpty()) {
                                                System.out.println("Book ID: " + bookId0 + ", Book Name: " + bookName1);
                                                System.out.println("Issued to:");
                                                for (String studentId2 : issuedToStudents) {
                                                    Document student = mongoDBConnection.getStudentById(studentId2).toDocument();
                                                    String studentName = student.getString("studentName");
                                                    System.out.println("  - Student ID: " + studentId2 + ", Student Name: " + studentName);
                                                }
                                                System.out.println("-----");
                                            }
                                        }
                                        break;



                                    case 6:
                                        System.out.println("Enter the book ID");
                                        int bookId4 = input.nextInt();
                                        Document bookDocument = mongoDBConnection.searchBookByUniqueId(bookId4);
                                        if (bookDocument != null) {
                                            JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();
                                            System.out.println(bookDocument.toJson(prettyPrint));

                                        } else {
                                            System.out.println("Book not found.");
                                        }
                                        break;

                                    case 7:
                                        System.out.println("Enter the student ID");
                                        String studentId2 = input.next();
                                        mongoDBConnection.checkStatusOfBooksIssuedByUser(studentId2);
                                        break;

                                    case 8:
                                        System.out.println("Fetching all books from the collection:");
                                        mongoDBConnection.displayAllDocuments("books");
                                        break;

                                }

                            }while (adminChoice != 9);
                        }

                        else {
                            System.out.println("Login Failed");
                        }

                        break;
                    case 2:
                        System.out.println("Enter your username");
                        String username1 = input.next();
                        System.out.println("Enter your password");
                        String password1 = input.next();

                        AdminModel admin = new AdminModel(username1, password1);
                        mongoDBConnection.insertAdmin(admin);
                        break;

                    case 3:
                        System.out.println("Enter your studentId");
                        String studentId = input.next();
                        System.out.println("Enter your password");
                        String password2 = input.next();
                        if(mongoDBConnection.studentLogin(studentId, password2)){
                            System.out.println("Login Successful");
                            int studentChoice;
                            do{
                                System.out.println("1. Search a book by unique ID");
                                System.out.println("2. Check status of books issued by a you");
                                System.out.println("3. Issue Book");
                                System.out.println("4. List of all books");
                                System.out.println("5. exit");
                                studentChoice = input.nextInt();
                                switch(studentChoice) {
                                    case 1:
                                        System.out.println("Enter the book ID");
                                        int bookId4 = input.nextInt();
                                        Document bookDocument = mongoDBConnection.searchBookByUniqueId(bookId4);
                                        if (bookDocument != null) {
                                            JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();
                                            System.out.println(bookDocument.toJson(prettyPrint));

                                        } else {
                                            System.out.println("Book not found.");
                                        }
                                        break;
                                    case 2:

                                        mongoDBConnection.checkStatusOfBooksIssuedByUser(studentId);
                                        break;


                                    case 3:
                                        System.out.println("Enter the book ID");
                                        int bookId1 = input.nextInt();

                                        // Retrieve the StudentModel by its ID
                                        StudentModel student1 = mongoDBConnection.getStudentById(studentId);

                                        if (student1 != null) {
                                            // Issue the book
                                            mongoDBConnection.issueBook(bookId1, studentId);
                                            // Add the issued book to the student's list

                                            // Retrieve the BookModel by its ID
                                            BookModel bookToIssue = mongoDBConnection.getBookById(bookId1);

                                            if (bookToIssue != null) {
                                                // Add the student to the issued list for this boo
                                                System.out.println("Book issued successfully.");
                                            } else {
                                                System.out.println("Book not found");
                                            }
                                        } else {
                                            System.out.println("Student not found");
                                        }

                                        break;

                                    case 4:
                                        System.out.println("Fetching all books from the collection:");
                                        mongoDBConnection.displayAllDocuments("books");
                                        break;

                                }
                            }while (studentChoice != 5);
                        }
                        else {
                            System.out.println("Login Failed");
                        }
                        break;

                    case 4:
                        System.out.println("Enter your studentId");
                        String studentId1 = input.next();
                        System.out.println("Enter your name");
                        String name1 = input.next();
                        System.out.println("Enter your password");
                        String password3 = input.next();
                        StudentModel student = new StudentModel(studentId1, name1, password3);
                        mongoDBConnection.insertStudent(student);
                        break;

                }


            } while( choice != 5);







//

        } finally {
            // Close the MongoDB connection
            mongoDBConnection.closeConnection();
        }
    }


}

