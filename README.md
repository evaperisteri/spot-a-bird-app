# Spot-A-Bird Application Setup Guide

## Complete instructions to set up the backend and frontend

This project has two parts:

- **Backend**: https://github.com/evaperisteri/spot-a-bird-app
- **Frontend**: https://github.com/evaperisteri/spot-a-bird-react

---

## 🔸 Tools & Frameworks

### Backend:
- Java 17, Spring Boot, Gradle
- MySQL

### Frontend:
- React + Vite
- TailwindCSS (utility-first styling)
- shadcn/ui (prebuilt components)
- Lucide React (icons)

Follow the steps below to get everything running locally.

---

## 🔸 Prerequisites

Make sure you have these installed on your system:

🔹 Java JDK 17
🔹 MySQL Server
🔹 Node.js 18+ & npm
🔹 Git

---

## 🔸️ Database Setup

1. Open MySQL and run the following commands:

```sql
CREATE DATABASE spot_a_bird_db;
CREATE USER 'spotter'@'localhost' IDENTIFIED BY '12345';
GRANT ALL PRIVILEGES ON spot_a_bird_db.* TO 'spotter'@'localhost';
FLUSH PRIVILEGES;
```
(Alternatively, you can use a MySQL client such as MySQL Workbench.)

2.	Verify that the backend’s src/main/resources/application.properties file is pointing to the same database name, user, and password.
```text
spring.datasource.url=jdbc:mysql://localhost:3306/spot_a_bird_db?serverTimezone=UTC    
spring.datasource.username=spotter    
spring.datasource.password=12345
```
________________________________________
## 🔸 Backend Setup

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

**Gitbash/Linux/Mac**
```bash
./gradlew clean build
./gradlew bootRun
```

**Windows Command Prompt**
```cmd
gradlew.bat clean build
gradlew.bat bootRun
```

**PowerShell**
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
## 🔸 Frontend Setup
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
## 🔸 Usage

•	Open http://localhost:5173 in your browser.

   (The frontend will communicate with the backend running on http://localhost:8080.)
________________________________________
## 🔸 API Testing with Postman

•	Postman Collection Import Guide:

https://github.com/evaperisteri/spot-a-bird-app/blob/79c203129fab4fb7435f9b61076fbb313fbf4d59/docs/postman/README.md

A complete Postman collection is included to test the backend API endpoints independently of the frontend.

# Quick Start
1.  **Import the Collection & Environment:**
    *   In Postman, click **Import** and select both files:
        *   `docs/postman/SpotABirdApp.postman_collection.json`
        *   `docs/postman/SpotABirdApp.postman_environment.json`
2.  **Select Environment:** In the top-right corner of Postman, select the **"SpotABirdApp"** environment.
3.  **Get an Auth Token:** Run the `Login User` request first to authenticate and automatically save the JWT token to your environment variables.
4.  **Test APIs:** You can now run any other request (e.g., `Get All Families`, `Create Observation`).

The collection includes automated tests for validating responses. For more details, see the [full Postman documentation](https://github.com/evaperisteri/spot-a-bird-app/blob/1f38ac0ccbd97e090a2036e776683c942c501fea/docs/postman/README.md).

________________________________________
## 🔸 Data Sources & Attribution

# Bird Species Data
The taxonomic data and species information for birds of Greece is sourced from:
> Chandrinos G. and Kastritis Th. (2009). *BIRDS*. In: Legakis, Α. & P. Maragkou (editors). *The Red Data Book of Endangered Animals of Greece*. Hellenic Zoological Society, Athens.
Found here: https://ornithologiki.gr/en/birds-of-greece/katalogos-ton-poulion-tis-elladas-2

# Images
[See Attributions](https://github.com/evaperisteri/spot-a-bird-app/blob/18e8a5de191d8621ee746489a2ed420ff4c90557/IMAGE_ATTRIBUTION.md)
Bird species images are sourced from Wikipedia and are used under the terms of their respective licenses (typically Creative Commons Attribution-ShareAlike or Public Domain).

