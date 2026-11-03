#  Health-Yatra

### A Digital Healthcare Platform for Accessible Medical Records

Health-Yatra is an Android-based healthcare application designed to make healthcare information more accessible and organized. It helps patients and doctors manage medical information digitally, making it easier to maintain and access patient health records.

---

##  About the Project

Health-Yatra aims to simplify the management of patient medical records through a digital platform.

The application provides a dedicated interface for patients and doctors, allowing medical information to be managed and accessed conveniently.

The project uses **Kotlin for Android development** and **Spring Boot for backend services**.

##  Features

###  Doctor Module
- Dedicated doctor dashboard.
- Add and access patient information.
- Search patients using Aadhaar number.
- View patient medical records.
- Create and manage medical records.

###  Patient Module
- Patient dashboard.
- View medical records.
- Access diagnosis and disease information.
- View prescribed medicines.
- View doctor and hospital information.

### 📋 Medical Records
- Display medical records using RecyclerView.
- Clickable medical record cards.
- Detailed medical record screen.
- Display symptoms, diagnosis, medicines, allergies and chronic conditions.
- Doctor information associated with medical records.

###  Authentication & Backend
- Backend API integration.
- JWT-based authorization.
- Session management.
- REST API communication using Retrofit.

## 🛠️ Tech Stack

| Category | Technologies |
|---|---|
| Android Development | Kotlin |
| UI Design | XML, Material Components |
| Architecture | Fragment-based architecture, Repository pattern |
| API Integration | Retrofit |
| JSON Parsing | Gson |
| Backend | Spring Boot |
| Database | Backend-managed database |
| Authentication | JWT |
| Version Control | Git & GitHub |

##  Project Architecture

The application follows a layered approach to separate UI, data handling and API communication.

```text
Health-Yatra
│
├── Android Application
│   │
│   ├── Activities
│   │
│   ├── Fragments
│   │   ├── Patient Dashboard
│   │   ├── Doctor Dashboard
│   │   ├── Lab Reports
│   │   └── Medical Record Details
│   │
│   ├── Adapters
│   │   └── LabReportAdapter
│   │
│   ├── Models
│   │   ├── HealthRecord
│   │   └── DoctorRecord
│   │
│   ├── Repository
│   │   └── HealthRecordRepository
│   │
│   └── Data
│       └── SessionManager
│
└── Spring Boot Backend
    └── REST APIs
```

##  Backend Integration

The Android application communicates with the Spring Boot backend through REST APIs.

**Backend Base URL:**

```text
https://health-yatra.onrender.com/
```

### Medical Record APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/record/health-record` | Create medical record |
| GET | `/record/fetch-health/{aadhar}` | Fetch patient medical records |
| PUT | `/record/set-record/{id}` | Update medical record |
| GET | `/record/fetch-all` | Fetch records for authenticated user |

All protected endpoints require JWT authorization.

##  Getting Started

### Prerequisites

- Android Studio
- Kotlin
- Android SDK
- Git
- Internet connection

### Installation

**1. Clone the repository**

```bash
git clone https://github.com/YOUR_USERNAME/Health-Yatra.git
```

**2. Open the project**

Open the cloned project in Android Studio.

**3. Sync Gradle**

Allow Android Studio to download the required dependencies.

**4. Run the application**

- Connect an Android device or start an emulator.
- Enable USB debugging if using a physical device.
- Click the Run button in Android Studio.

> Make sure the backend is deployed and accessible before testing API-dependent features.


##  Future Enhancements

- Upload and view actual laboratory report PDFs and images.
- OTP-based authentication and verification.
- Improved patient search and management.
- Online appointment booking.
- Notifications and reminders.
- AI-powered healthcare assistance.
- Enhanced medical record management.

##  Project Objective

The main objective of Health-Yatra is to develop a digital healthcare platform that improves the accessibility and management of patient medical information while simplifying interactions between doctors and patients.

##  Developer

**Adarsh Yadav**

B.Tech – Information Technology

Interested in Android Development, Kotlin, Backend Development and Full Stack Engineering.

---

 If you find this project interesting, consider giving it a star!

**Made with using Kotlin and Spring Boot.**
