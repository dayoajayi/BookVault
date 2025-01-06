# **BookVault**

## **Overview**
BookVault is a robust book management application designed to keep track of books within a digital library. The application supports a variety of functionalities, including adding, updating, deleting, and retrieving book records. In the future, it will also allow users to borrow books from the collection.

## **Features**
- **Add Books**: Store book details, including title, author, and unique identifier.
- **Update Book Information**: Modify existing book records with new data.
- **Delete Books**: Remove books from the collection.
- **Retrieve Books**: Get details of all books or a specific book by its ID.
- **Book Borrowing (Future)**: Allow users to borrow books from the collection.

## **Getting Started**

### **Prerequisites**
- **Java Development Kit (JDK) 21**: Ensure you have Temurin 21 installed.
- **Gradle** (or the Gradle Wrapper provided) for building the project.
- **Spring Boot**: The framework used for building the application.
- **Lombok**: For reducing boilerplate code in Java.

### **Installation**
1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/bookvault.git
   cd bookvault
   ```

2. **Build the project**:
   ```bash
   ./gradlew build
   ```

3. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```

### **Testing**
To run the test suite, execute the following command:
```bash
./gradlew test
```

## **Usage**

### **Adding a Book**
You can add a new book by making a POST request to the `/books` endpoint with the book details.

### **Updating a Book**
To update an existing book, send a PUT request to `/books/{id}` with the updated book information.

### **Deleting a Book**
To delete a book, send a DELETE request to `/books/{id}`.

### **Retrieving Books**
To retrieve a list of all books, send a GET request to `/books`. To retrieve a specific book by its ID, send a GET request to `/books/{id}`.

## **API Endpoints**

| Method | Endpoint         | Description                      |
|--------|------------------|----------------------------------|
| POST   | `/books`         | Add a new book                   |
| GET    | `/books`         | Retrieve all books               |
| GET    | `/books/{id}`    | Retrieve a book by its ID        |
| PUT    | `/books/{id}`    | Update a book                    |
| DELETE | `/books/{id}`    | Delete a book                    |

## **API Documentation (Swagger UI)**

If you’ve added **Springdoc OpenAPI** (or a similar library) for automatic API documentation, you can access the **Swagger UI** once the application is running:

1. **Run the application locally**:
   ```bash
   ./gradlew bootRun
   ```
   or use Docker (after building an image):
   ```bash
   docker run -p 8080:8080 your-image-name:latest
   ```

2. **Open your browser** and navigate to:
   ```
   http://localhost:8080/swagger-ui.html
   ```
   Depending on your setup, you may also find it at:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```
   You’ll see a user-friendly interface for exploring and testing all available endpoints.

3. **Explore and Test Endpoints** from the Swagger UI by expanding each endpoint, filling in parameters, and clicking **Try it out**.

> **Note**: If you don’t see Swagger UI, ensure you’ve added the relevant dependency in your build file, for example:
> ```kotlin
> implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
> ```
> Then rebuild and rerun the application.

## **Contributing**
Contributions are welcome! Please submit a pull request or open an issue to suggest improvements or report bugs.

## **License**
This project is licensed under the **[MIT License](LICENSE)**. See the `LICENSE` file for more details.

## **Contact**
- **Author**: Dayo Ajayi
- **Email**: dayo.ajayi@gmail.com
- **GitHub**: [dayoajayi](https://github.com/dayoajayi)
```

