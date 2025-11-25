# Examinee Management System (Swing + MySQL)

This is a simple Java Swing application that uses MySQL as its backend database. The project contains Java sources in `src/` and needs a MySQL database and the MySQL JDBC driver (Connector/J) on the classpath to run.

## Overview of changes
- DB connection is now configurable with `db.properties` at project root.
- Added `db.properties.example` to copy and configure for your environment.
- Added `create_database.sql` to create the schema and a default admin user.

## Setup — make this match your PC

1. Make sure MySQL server is installed and running on your PC.

2. Create the database and tables by running the SQL script.

   ```bash
   mysql -u <your-db-user> -p < create_database.sql
   # e.g. mysql -u root -p < create_database.sql
   ```

3. Copy `db.properties.example` to `db.properties` and update values as needed.

   ```bash
   cp db.properties.example db.properties
   # edit db.properties using your editor and fill db.user and db.password
   ```

4. Make sure you have Java 11+ JDK installed.

5. Download MySQL Connector/J (the JDBC driver) and place the JAR into a `lib/` directory inside the project.
   - Get it from: https://dev.mysql.com/downloads/connector/j/
   - Example result: `lib/mysql-connector-java-8.0.xx.jar`

6. Compile and run the app (from project root):

   ```bash
   mkdir -p bin
   # If you want to compile and run using Java 8 specifically, set JAVA_HOME to a JDK8 installation
   # and use the helper script below, or compile with source/target 1.8:
   # Example using system javac (compile for Java 8 compatibility):
   javac -source 1.8 -target 1.8 -d bin -cp "lib/*" src/*.java

   # Run the app (include jars and bin on classpath)
   java -cp "bin:lib/*" Main
   ```

### Running with JDK 8 (recommended if you have JDK 8 installed)

If you have JDK 8 installed and want to compile/run using it, set JAVA_HOME and run the helper script:

```bash
# Example (if your JDK 8 is in /usr/lib/jvm/java-8-openjdk-amd64)
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
./run-java8.sh Main

# Or run the test-checker (non-GUI) to verify DB connection
./run-java8.sh TestDBConnection
```


Notes:
- You can change default DB credentials in `db.properties` (do not commit secrets to VCS).
- The SQL script inserts a default user `admin` with password `admin` for quick login.

If you want, I can try to compile and run the app in this environment, but I will need the MySQL server running here and a Connector/J jar in `lib/` first. I can also modify `DBConnection` further to read environment variables instead of properties if that is preferable for your setup.

## How to run (quick checklist when you open the folder again)

Follow these exact steps when you reopen the project folder to run the app quickly.

1) Open the project folder in your terminal

```bash
cd /path/to/Examinee-Management-System-main
```

2) Make sure a database server is available (pick one):

- Option A: Start a MySQL container (recommended / isolated):

```bash
# use a host port that doesn't conflict (we used 3307 in the example)
docker run --name exam-mysql -e MYSQL_ROOT_PASSWORD=MyRootPass -e MYSQL_DATABASE=exam_system -p 3307:3306 -d mysql:8.0
# apply schema
chmod +x setup-db.sh
./setup-db.sh -u root -p 'MyRootPass' -h 127.0.0.1 -P 3307
```

- Option B: Use a local MySQL server (if you already installed it):

```bash
sudo systemctl start mysql      # on Ubuntu/Debian
mysql -u root -p < create_database.sql
```

3) Configure your DB connection file

```bash
cp db.properties.example db.properties
# Edit db.properties and set your DB host/port/user/password
# e.g. db.url=jdbc:mysql://127.0.0.1:3307/exam_system?allowPublicKeyRetrieval=true&useSSL=false
```

4) Ensure a JDBC driver jar is in `lib/` (we provide `lib/mariadb-java-client.jar` in this workspace). If not, download and put the MySQL Connector/J (or MariaDB client) into `lib/`.

5) Run the application using Java 8 (this repository includes a helper script):

```bash
chmod +x run-java8.sh   # needs to be done only once
# Run GUI:
./run-java8.sh Main

# Or run a quick non-GUI DB check before launching the UI:
./run-java8.sh TestDBConnection
```

6) Default login (if you used the provided SQL script without changing credentials):

- Username: `admin`
- Password: `admin`

---

Troubleshooting
- If TestDBConnection prints "Got connection: true" and a users count number but the GUI does not open on a headless server, the GUI needs a graphical display — run the app locally or use X11 forwarding/VNC to see the Swing UI.
- If you get a JDBC auth error about RSA public key, ensure `db.properties` URL includes `?allowPublicKeyRetrieval=true&useSSL=false`.
- If Docker can't bind to port 3306, either stop your host MySQL or map the container to a different host port (like 3307) and update `db.properties` accordingly.

