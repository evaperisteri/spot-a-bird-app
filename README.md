**Spot-A-Bird Application Setup Guide**

## Complete instructions to set up the backend and frontend 

This project has two parts:

•	Backend: https://github.com/evaperisteri/spot-a-bird-app

•	Frontend: https://github.com/evaperisteri/spot-a-bird-react
________________________________________
 ️🔸 **Tools & Frameworks**

**Backend:**
- Java 17, Spring Boot, Gradle
- MySQL

**Frontend:**
- React + Vite
- TailwindCSS (utility-first styling)
- shadcn/ui (prebuilt components)
- Lucide React (icons)


Follow the steps below to get everything running locally.
________________________________________
🔸 **Prerequisites**

Make sure you have these installed on your system:

🔹 Java JDK 17

🔹 MySQL Server

🔹 Node.js 18+ & npm

🔹 Git
________________________________________
🔸 **Database Setup**
1.	Open MySQL and run the following commands:

        ```sql
        CREATE DATABASE spot_a_bird_db;
        
        CREATE USER 'spotter'@'localhost' IDENTIFIED BY '12345';
        
        GRANT ALL PRIVILEGES ON spot_a_bird_db.* TO 'spotter'@'localhost';
        
        FLUSH PRIVILEGES;
        ```


   (Alternatively, you can use a MySQL client such as MySQL Workbench.)

2.	Verify that the backend’s src/main/resources/application.properties file is pointing to the same database name, user, and password.

      ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/spot_a_bird_db?serverTimezone=UTC
    
        spring.datasource.username=spotter
    
        spring.datasource.password=12345
        ```
________________________________________
🔸 **Backend Setup**

1.	Clone the backend repository:

        ```bash
        git clone https://github.com/evaperisteri/spot-a-bird-app
                    
        cd spot-a-bird-app
      ```


2.	First build (creates the tables):

   - Open src/main/resources/application.properties
     -	Uncomment the lines under:
       ===== HIBERNATE (Initial, comment out after first build) =====
     -	Comment out the lines under:
        ==== HIBERNATE (Ongoing, tables are already created and filled) ====
     - Then run:

       # Gitbash/Linux/Mac
        ```bash
        ./gradlew clean build
       
        ./gradlew bootRun
        ```
       
       # Windows Command Prompt
        ```cmd
        gradlew.bat clean build
       
        gradlew.bat bootRun
        ```
        
        # PowerShell
        ```powershell
        .\gradlew.bat clean build
       
        .\gradlew.bat bootRun
        ```

     3.	Subsequent builds (when tables already exist):
           -	Reverse the above comments:
      
                •	Comment out the Initial Hibernate lines
      
                •	Uncomment the Ongoing Hibernate lines
      
           -	Run again:

           ```bash
             ./gradlew bootRun
           ``` 
           or 
           ```cmd
             gradlew.bat bootRun
           ```

✅ The backend should now be running on http://localhost:8080.
________________________________________
🔸 **Frontend Setup**
1.	Clone the frontend repository:

        ```bash
            git clone https://github.com/evaperisteri/spot-a-bird-react
        
            cd spot-a-bird-react
      ```
2.	Install dependencies:

        ```bash
        npm install
      ```
3.	Run the development server:

        ```bash
        npm run dev
      ```

✅ The frontend will be available at http://localhost:5173.
________________________________________
🔸 **Usage**

•	Open http://localhost:5173 in your browser.

   (The frontend will communicate with the backend running on http://localhost:8080.)
________________________________________
🔸 **Additional Docs**

•	Postman Collection Import Guide:

https://github.com/evaperisteri/spot-a-bird-app/blob/79c203129fab4fb7435f9b61076fbb313fbf4d59/docs/postman/README.md
