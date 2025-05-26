# CSCI 205 - Password Manager
Bucknell University  
Lewisburg, PA

### Course Info
- **Instructor:** Professor Romano
- **Semester:** Spring 2025

## Team Information

### Team Members
- **Bennett McKeon** (Product Owner) - Sophomore, Computer Science
    - Lead for authentication and encryption systems
    - Designed core security architecture
    - Implemented user management features

- **Andrew Bond** (Scrum Master) - Sophomore, Computer Science and Engineering
    - Developed encryption/decryption mechanisms
    - Implemented password generation algorithms
    - Managed Gradle build system configuration

- **Aiden Kim** (Developer) - Sophomore, Computer Science
    - Created the JavaFX user interface
    - Developed file storage and persistence
    - Implemented password health analysis features

## Project Overview

### Background
In today's digital world, the average person maintains 70-80 online accounts, each requiring unique credentials. Managing these passwords securely has become an overwhelming challenge, leading to risky practices such as password reuse, weak passwords, or insecure storage methods. Our Password Manager application addresses this critical security problem by providing a secure, encrypted vault that requires remembering only a single master password.

### Motivation
Password breaches and account compromises have become increasingly common, with millions of credentials exposed annually. Most security experts recommend unique, complex passwords for each account, but this is impractical without proper tools. Our Password Manager enables users to maintain strong security practices without sacrificing convenience, helping protect sensitive personal and financial information from unauthorized access.

### Key User Personas

**LeBron James - Parent of 3**  
As a parent, LeBron needs to keep track of his children's passwords for school accounts and apps while teaching them about digital safety. He values family sharing features with appropriate controls and educational components that help teach password safety.

**Marcus Smart - Security Developer**  
As a security professional, Marcus needs to know exactly how his passwords are being stored and encrypted. He won't use anything that might compromise security and prefers applications where he can verify the security practices implemented.

**Sarah Guillot - Business Professional**  
As a busy professional, Sarah needs a password manager that's both secure and efficient. She values quick access across multiple devices and an intuitive interface that doesn't slow her down in her fast-paced work environment.

## Design

### Object-Oriented Architecture
Our password manager implements a modular architecture following key object-oriented principles to ensure security, maintainability, and extensibility.

#### Key Classes and Relationships

![Class Diagram](design/ClassDiagram.png)

The core architecture revolves around these main components:

- **User**: Central entity that manages the collection of password entries and organizes them into categories
- **PasswordEntry**: Encapsulates credential information including website, username, and encrypted password
- **AuthenticationManager**: Handles user login/logout and session management
- **EncryptionManager**: Provides encryption services using AES algorithm
- **FileStorageManager**: Manages persistent storage of encrypted data
- **PasswordManager**: Coordinates password operations between the UI and data layer

#### State and Sequence Diagrams

The application follows several well-defined workflows, illustrated in our UML diagrams:

The state diagram shows the application's lifecycle through different states:
- **InitialState**: Application startup and encryption key management
- **LoginState**: User authentication and registration
- **LoggedInState**: Password management operations
- **LoggingOutState**: Secure session termination

The sequence diagram illustrates key interactions between components, including:
- Authentication flow
- Password storage process
- Password retrieval and decryption
- Secure logout procedure

## Implementation

### Security Implementation
- **AES Encryption**: All sensitive data is encrypted using the industry-standard AES algorithm with 128-bit keys
- **Secure Storage**: Passwords are never stored in plaintext, even in memory
- **Master Password**: Used for authentication and as a seed for encryption key generation
- **Auto-Logout**: Session automatically terminates after period of inactivity

### Data Structures
- **HashMap**: Used for efficient storage and retrieval of user data and password entries
- **ArrayList**: Used for maintaining collections of password entries
- **Observer Pattern**: Implemented for UI updates when underlying data changes

### Tools and Libraries
- **Gradle**: Build automation and dependency management
- **JavaFX**: User interface framework
- **Java Cryptography Extension (JCE)**: For implementing AES encryption
- **Java NIO**: For efficient file I/O operations

Java Libraries

https://openjfx.io/
1. JavaFX (javafx.application.Application)
2. JavaFX (javafx.application.Platform)
3. JavaFX (javafx.scene.Scene)
4. JavaFX (javafx.scene.control.Alert)
5. JavaFX (javafx.scene.control.Button)
6. JavaFX (javafx.scene.control.ButtonBar)
7. JavaFX (javafx.scene.control.ButtonType)
8. JavaFX (javafx.scene.control.ComboBox)
9. JavaFX (javafx.scene.control.Dialog)
10. JavaFX (javafx.scene.control.Label)
11. JavaFX (javafx.scene.control.PasswordField)
12. JavaFX (javafx.scene.control.ProgressIndicator)
13. JavaFX (javafx.scene.control.Separator)
14. JavaFX (javafx.scene.control.TableCell)
15. JavaFX (javafx.scene.control.TableColumn)
16. JavaFX (javafx.scene.control.TableView)
17. JavaFX (javafx.scene.control.TextArea)
18. JavaFX (javafx.scene.control.TextField)
19. JavaFX (javafx.scene.control.ToggleButton)
20. JavaFX (javafx.scene.control.ToolBar)
21. JavaFX (javafx.scene.control.cell.PropertyValueFactory)
22. JavaFX (javafx.scene.input.Clipboard)
23. JavaFX (javafx.scene.input.ClipboardContent)
24. JavaFX (javafx.scene.layout.BorderPane)
25. JavaFX (javafx.scene.layout.ColumnConstraints)
26. JavaFX (javafx.scene.layout.GridPane)
27. JavaFX (javafx.scene.layout.HBox)
28. JavaFX (javafx.scene.layout.Priority)
29. JavaFX (javafx.scene.layout.Region)
30. JavaFX (javafx.scene.layout.StackPane)
31. JavaFX (javafx.scene.layout.VBox)
32. JavaFX (javafx.scene.paint.Color)
33. JavaFX (javafx.scene.text.Font)
34. JavaFX (javafx.scene.text.FontWeight)
35. JavaFX (javafx.scene.text.Text)
36. JavaFX (javafx.stage.Stage)
37. JavaFX (javafx.geometry.Insets)
38. JavaFX (javafx.geometry.Pos)
39. JavaFX (javafx.animation.PauseTransition)
40. JavaFX (javafx.util.Duration)
41. JavaFX (javafx.collections.FXCollections)
42. JavaFX (javafx.collections.ObservableList)
43. JavaFX (javafx.collections.transformation.FilteredList)

https://www.oracle.com/java/technologies/javase-jce-all-downloads.html
44. Java Cryptography Extension (javax.crypto.Cipher)
45. Java Cryptography Extension (javax.crypto.KeyGenerator)
46. Java Cryptography Extension (javax.crypto.SecretKey)
47. Java Cryptography Extension (javax.crypto.spec.SecretKeySpec)

## Demonstration

### Installation
1. Ensure Java 11 or higher is installed
2. Clone the repository
3. Navigate to the project directory
4. Run the application using:
   ```
   ./gradlew runPasswordManager
   ```
   (On Windows systems, use `gradlew.bat runPasswordManager`)

### Key Features
- Create an account with a master password
- Add, edit, and delete password entries
- Generate strong random passwords
- Organize passwords with categories
- Search and filter password entries
- Password health analysis for weak or duplicate passwords
- Secure clipboard operations

## Team Reflection

### Scrum Process
Our team implemented Scrum methodologies throughout the development process:
- Weekly sprints with planning, daily standups, and retrospectives
- AIECode for issue tracking and backlog management
- Rotating the Scrum Master role to gain diverse perspectives
- User stories guided our development priorities

The most challenging aspects were estimation accuracy and maintaining consistent momentum throughout sprints. We found that breaking down user stories into smaller tasks improved our velocity and estimation accuracy.

### Successes and Challenges

**Successes:**
- Strong encryption implementation with minimal performance impact
- Intuitive user interface that received positive feedback during testing
- Successful integration of various components through well-defined interfaces

**Challenges:**
- Git workflow coordination required significant learning, especially with merge conflicts
- JavaFX UI responsiveness required more effort than initially anticipated
- Balancing security best practices with user convenience

### Time Management
Our time estimation improved throughout the project as we became more familiar with the technologies and our own capabilities. Initial sprints saw significant underestimation of complex security features, but by the third sprint, our estimates became much more accurate. Breaking down tasks into smaller, more manageable pieces helped tremendously.

### Future Approach
In future projects, we would:
- Establish stronger coding standards from the beginning
- Implement more automated testing earlier in the development process
- Dedicate more time to initial architecture planning
- Create more detailed user stories before beginning implementation

## Future Enhancements
With additional time, we would implement:
- Two-factor authentication
- Cloud synchronization
- Browser integration with auto-fill capabilities
- Mobile companion application
- Password sharing with trusted contacts

## Conclusion
Our Password Manager successfully addresses the critical need for secure password management through a user-friendly interface and robust security measures. By applying object-oriented design principles, we created a modular, maintainable application that effectively balances security with usability.

The project demonstrated our team's ability to analyze complex requirements, design an effective solution, and implement it with security as a priority. The experience provided valuable lessons in team collaboration, software engineering practices, and security implementation that will inform our approach to future projects.


## Video Presentation
Here is a link to our video: https://drive.google.com/file/d/1mLYczHrnzpnb75QbiDg5LwhLDIATo9mE/view?usp=sharing

## How to Run the Project
To run this project, execute the following command in the terminal from the project root directory:
```
./gradlew runPasswordManager
```
