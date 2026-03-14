# LMS – Online Mentoring Platform

LMS is a **full-stack mentorship platform** connecting students with professional mentors. Students can discover mentors, book one-on-one sessions, write reviews, and track their learning progress. Admins can manage mentors, subjects, and session bookings through a secure dashboard.

---

## Table of Contents

- [LMS – Online Mentoring Platform](#lms--online-mentoring-platform)
  - [Table of Contents](#table-of-contents)
  - [Features](#features)
    - [1. Admin Dashboard](#1-admin-dashboard)
    - [2. Mentor Discovery \& Student Dashboard](#2-mentor-discovery--student-dashboard)
  - [Tech Stack](#tech-stack)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Frontend](#frontend)
    - [Backend](#backend)
  - [Environment Variables](#environment-variables)
    - [Frontend (\`.env\`)](#frontend-env)
    - [Backend (\`.env\`)](#backend-env)
  - [API Endpoints](#api-endpoints)
  - [Deployment](#deployment)
  - [License](#license)

---

## Features

### 1. Admin Dashboard

- **Role-Based Access Control** using Clerk metadata
- **Subjects Management:** Create, edit, delete subjects and assign mentors
- **Mentor Management:** Manage mentors with profile details, certifications, experience, and image upload
- **Booking Management:** View all sessions with filtering, sorting, pagination, and action buttons:
  - Confirm Payment
  - Mark Completed
  - Add Meeting Link
- **UI:** Clean, responsive, and professional design using ShadCN/ui components
- **Notifications:** Success and error toasts for all actions

### 2. Mentor Discovery & Student Dashboard

- **Mentor Profiles:** Detailed view with bio, experience, skills, certifications, and reviews
- **Subject Cards:** Courses taught, thumbnails, and enrollment counts
- **Session Booking Workflow:**
  - Pre-filled mentor & subject info
  - Prevent double-booking or past-date booking
  - Clear user-friendly error messages
- **Reviews & Ratings:** 5-star rating system and student reviews
- **Dashboard:** View enrolled sessions with status badges for session and payment

---

## Tech Stack

- **Frontend:** React, TypeScript, React Hook Form, Zod, ShadCN/ui, Clerk Authentication, Sonner notifications, Lucide-react icons
- **Backend:** Spring Boot, Spring Security, PostgreSQL (Neon)
- **Deployment:** Vercel (Frontend), Render (Backend)
- **Database:** Neon PostgreSQL
- **Authentication:** Clerk for JWT and role-based access control

---

## Getting Started

### Prerequisites

- Node.js >= 20
- Java 17+
- PostgreSQL / Supabase account
- Clerk account

### Frontend

```bash
cd frontend
npm install
npm run dev
```

### Backend

```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

---

## Environment Variables

### Frontend (\`.env\`)

```env
VITE_CLERK_PUBLISHABLE_KEY=<Clerk Publishable Key>
VITE_API_BASE_URL=<Backend URL>
```

### Backend (\`.env\`)

```env
# Server & Profile
PORT=8080
PROFILE=prod

# Database
DB_URL=jdbc:postgresql://<HOST>:<PORT>/<DB_NAME>
DB_USERNAME=<DB_USERNAME>
DB_PASSWORD=<DB_PASSWORD>

# Redis
REDIS_HOST=<REDIS_HOST>
REDIS_PORT=<REDIS_PORT>
REDIS_PASSWORD=<REDIS_PASSWORD>

# JWT / Auth
JWT_SECRET=<JWT_SECRET>
AUTH_VALIDATOR_TYPE=clerk

# Clerk
CLERK_JWKS_URL=<CLERK_JWKS_URL>
CLERK_API_KEY=<CLERK_API_KEY>

# CORS
PUBLIC_ENDPOINTS=/api/v1/auth/**,/api/v1/public/**
CORS_ALLOWED_ORIGINS=https://frontend.com
```

---

## API Endpoints

| Method | Endpoint                                | Auth    | Description            |
| ------ | --------------------------------------- | ------- | ---------------------- |
| GET    | `/api/v1/mentors`                       | No      | Fetch all mentors      |
| POST   | `/api/v1/mentors`                       | Admin   | Create new mentor      |
| DELETE | `/api/v1/mentors/{id}`                  | Admin   | Delete mentor          |
| GET    | `/api/v1/subjects`                      | No      | Fetch subjects         |
| POST   | `/api/v1/subjects`                      | Admin   | Create subject         |
| DELETE | `/api/v1/subjects/{id}`                 | Admin   | Delete subject         |
| POST   | `/api/v1/sessions/enrollment`           | Student | Book session           |
| GET    | `/api/v1/sessions/enrollments`          | Student | Get my sessions        |
| GET    | `/api/v1/sessions?page=&size=`          | Admin   | Get all sessions       |
| PATCH  | `/api/v1/sessions/{id}/confirm-payment` | Admin   | Confirm payment        |
| PATCH  | `/api/v1/sessions/{id}/complete`        | Admin   | Mark session completed |
| PATCH  | `/api/v1/sessions/{id}/meeting-link`    | Admin   | Add meeting link       |
| PATCH  | `/api/v1/sessions/{id}/review`          | Student | Add session review     |

---

## Deployment

- **Frontend (Vercel):** [https://lms-react-mblwzxu4f-lakindu-virajiths-projects.vercel.app/](https://lms-react-mblwzxu4f-lakindu-virajiths-projects.vercel.app/)
- **Backend (Render):** [https://lms-spring-boot.onrender.com](https://lms-spring-boot.onrender.com)

---

## License

This project is licensed under the MIT License.
