This project has two parts:
•	Backend (Spring Boot + Gradle): https://github.com/evaperisteri/spot-a-bird-app
•	Frontend (React + Vite): https://github.com/evaperisteri/spot-a-bird-react
Follow the steps below to get everything running locally.
________________________________________
📦 Prerequisites
Make sure you have these installed on your system:
•	MySQL (v8.x recommended)
•	Java JDK (v17 recommended)
•	Node.js & npm (Node v18+ recommended)
________________________________________
🗄️ Database Setup
1.	Open MySQL and run the following commands:
    <pre>```CREATE DATABASE spot_a_bird_db;
     CREATE USER 'spotter'@'localhost' IDENTIFIED BY '12345';
     GRANT ALL PRIVILEGES ON spot_a_bird_db.* TO 'spotter'@'localhost';
     FLUSH PRIVILEGES;```</pre>
2.	Verify that the backend’s src/main/resources/application.properties file is pointing to the same database name, user, and password.
________________________________________
🔧 Backend Setup
1.	Clone the backend repository:
2.	<pre>```git clone https://github.com/evaperisteri/spot-a-bird-app```</pre>
3.	<pre>```cd spot-a-bird-app```</pre>
4.	First build (creates the tables):
  -	Open src/main/resources/application.properties
  -	Uncomment the lines under:
    ===== HIBERNATE (Initial, comment out after first build) =====
  -	Comment out the lines under:
 	==== HIBERNATE (Ongoing, tables are already created and filled) ====
  - Then run:
    <pre>```./gradlew clean build
    ./gradlew bootRun```</pre>
5.	Subsequent builds (when tables already exist):
  -	Reverse the above comments:
  	Comment out the Initial Hibernate lines
  	Uncomment the Ongoing Hibernate lines
  -	Run again:
     <pre>```./gradlew bootRun```</pre>
✅ The backend should now be running on http://localhost:8080.
________________________________________
🎨 Frontend Setup
1.	Clone the frontend repository:
      <pre>```git clone https://github.com/evaperisteri/spot-a-bird-react```</pre>
	<pre>```cd spot-a-bird-react```</pre>
2.	Install dependencies:
      <pre>```npm install```</pre>
3.	Run the development server:
      <pre>```npm run dev```</pre>
✅ The frontend will be available at http://localhost:5173.
________________________________________
🚀 Usage
•	Open http://localhost:5173 in your browser.
•	The frontend will communicate with the backend running on http://localhost:8080.
________________________________________
📖 Additional Docs
•	Postman Collection Import Guide: https://github.com/evaperisteri/spot-a-bird-app/blob/79c203129fab4fb7435f9b61076fbb313fbf4d59/docs/postman/README.md
