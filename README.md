# CampusCycle – Student Marketplace & Barter Exchange Platform

**Tagline:** *Reuse. Exchange. Save Money. Build a Sustainable Campus.*

CampusCycle is a college-exclusive marketplace where verified students can **sell**, **buy**, **donate**, or **barter** items securely. Unlike traditional marketplaces, it encourages a circular campus economy through barter exchanges, donations, and sustainability tracking.

---

## Problem Statement

Every semester, students discard usable textbooks, calculators, lab equipment, hostel furniture, and more — while new students buy the same items at full price. Today this happens through scattered WhatsApp groups with no trust, tracking, or sustainability impact.

## Solution

A verified, campus-only platform with:
- Multiple listing types: **Sell**, **Buy Request**, **Barter**, **Donate**
- **Barter Engine** for item-for-item exchanges
- **Donation Corner** for free giveaways
- **Chat Requests** (accept/reject, then reveal contact info)
- **Sustainability Dashboard** showing reuse impact
- **Admin moderation** for users, listings, and reports

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Java Servlets |
| View | JSP + JSTL |
| Database | PostgreSQL (Neon) + JDBC |
| Architecture | MVC + DAO Pattern |
| Auth | Session-based + BCrypt |
| File Upload | Apache Commons FileUpload |
| Build | Maven (WAR) |
| Server | Apache Tomcat 9+ |

### Technical Features Demonstrated
- Servlet & JSP lifecycle
- JDBC with Prepared Statements
- MVC layered architecture (Model → DAO → Service → Servlet → JSP)
- Session management & role-based access control
- File upload & image storage
- PostgreSQL-compatible schema and Neon deployment
- `/health` database readiness endpoint
- Pagination, search & filtering
- Form validation
- Transaction recording

---

## Project Structure

```
CampusCycle/
├── pom.xml
├── sql/schema.sql
└── src/main/
    ├── java/com/campuscycle/
    │   ├── model/       # Entity classes & enums
    │   ├── dao/         # Data Access Objects
    │   ├── service/     # Business logic
    │   ├── servlet/     # Controllers
    │   ├── filter/      # Auth & admin filters
    │   └── util/        # DB, password, file upload helpers
    ├── resources/db.properties
    └── webapp/
        ├── css/style.css
        ├── uploads/
        └── WEB-INF/jsp/
```

---

## Setup Instructions

### Prerequisites
- Java 17+
- Apache Maven 3.8+
- PostgreSQL 15+ or a Neon database
- Apache Tomcat 9+

### 1. Database Setup

```bash
psql "$neonDbUrl" -f sql/schema.sql
```

The same `sql/schema.sql` works in the Neon SQL Editor. Connect to the target Neon database before running it.

### 2. Configure Database

For local development, copy `src/main/resources/db.properties.example` to `db.properties`. For Render, add only the `neonDbUrl` environment variable using the connection string copied from Neon. The application accepts both Neon's `postgresql://...` format and JDBC format and adds the JDBC prefix automatically. Never commit `db.properties` or a real connection string.

```properties
db.url=jdbc:postgresql://localhost:5432/campuscycle?sslmode=disable
db.username=postgres
db.password=your_password
college.email.domains=*
```

### 3. Build

```bash
mvn clean package
```

Deploy `target/campuscycle.war` to Tomcat's `webapps/` folder.

### 4. Run

Start Tomcat and open: `http://localhost:8080/campuscycle/home`

Check database readiness at `/health`. A healthy response is:

```json
{"status":"ok","database":"ok"}
```

### End-to-end smoke test

After deploying, run:

```powershell
.\scripts\smoke-test.ps1 -BaseUrl https://your-service.onrender.com
```

This verifies the health endpoint, public pages, login page, and unauthenticated access protection.

### Render deployment

1. Push the repository to GitHub.
2. Create a Render **Web Service** from the repository.
3. Select **Docker** runtime; Render will use the root `Dockerfile`.
4. Add `neonDbUrl` as a secret environment variable using Neon's pooled connection string with SSL enabled.
5. Set the Render health check path to `/health`.
6. Deploy and run the smoke test above.

The Docker image deploys the WAR as Tomcat `ROOT.war`, so the hosted URL is `/home`, not `/campuscycle/home`.

### Default Admin Account
| Field | Value |
|-------|-------|
| Email | `admin@college.edu` |
| Password | `admin123` |

---

## User Roles

### Student
- Buy, sell, barter, donate items
- Save wishlist favorites
- Send chat requests to sellers
- Propose barter exchanges
- Report suspicious listings
- View profile & transaction history

### Admin
- Verify new student registrations
- Approve/reject listings
- Moderate fraud reports
- Suspend spam accounts
- View platform analytics

---

## Key Features

### Listing Types
| Type | Example |
|------|---------|
| Sell | "Scientific Calculator – ₹400" |
| Buy Request | "Looking for Java textbooks" |
| Barter | "Calculator for Java Books" |
| Donate | "Free hostel mattress" |

### Barter Engine ⭐
Student A offers a Java Book → Student B offers a Calculator → Both accept → Transaction complete.

### Smart Search & Filters
Search by item, course, semester, category, seller. Filter by: Free, Donation, Exchange, Under ₹500, Hostel, Recently Added.

### Sustainability Dashboard
Tracks items reused, money saved, waste prevented, donations, and barter deals.

---

## API / URL Map

| URL | Description |
|-----|-------------|
| `/home` | Landing page with recent listings |
| `/listings` | Browse & search marketplace |
| `/listing?id=` | Listing detail |
| `/listing/create` | Create new listing |
| `/register` | Student registration |
| `/login` | Login |
| `/profile` | User profile & my listings |
| `/barter` | Barter proposals |
| `/chat` | Chat requests |
| `/sustainability` | Impact dashboard |
| `/admin/dashboard` | Admin panel |

---

## Future Enhancements

- QR code verification during item exchange
- Real-time chat using WebSocket
- AI-powered item price suggestions
- AI image moderation for inappropriate uploads
- Email notifications
- Mobile application
- Digital wallet for campus credits
- Campus points for donations

---

## Why This Project Stands Out

Most marketplace projects stop at buying and selling. CampusCycle introduces **barter exchanges**, **donations**, and **sustainability metrics** — solving a genuine student problem while demonstrating strong Java EE concepts: Servlets, JSP, JDBC, MVC, session management, file handling, search, filtering, and multi-role authorization.

---

## License

Educational / Portfolio project.
