# StudyGenAI

A polished full-stack study companion built with React, Vite, Spring Boot, and MySQL.

## Run the preview

```powershell
cd C:\PROJECTS\StudyGenAI\frontend
npm install
npm run dev
```

Open `http://localhost:5173`. The frontend includes authentication, dashboard, subjects, AI generator, and an interactive study planner.

## Run the API

```powershell
cd C:\PROJECTS\StudyGenAI\backend
mvn spring-boot:run
```

The API runs on `http://localhost:8080`. The local `backend/.env` contains the configured MySQL connection and Gemini model settings. The app exposes `/api/health`, `/api/dashboard`, and `/api/generate`; `/api/generate` calls Gemini live and falls back to a local study pack only when the provider is temporarily unavailable.

## Verification

- Frontend build: `npm run build`
- Backend compile: `mvn test`
- MySQL connection verified against MySQL 8.0.44 and the `studygen_ai` database.
- Generated packs are persisted to the MySQL `study_packs` table.
- Sign up and sign in use the MySQL `app_users` table with BCrypt password hashing.
- Fresh users create a personal account through the Sign up screen.
- Gemini live generation verified through the browser flow using `gemini-3.1-flash-lite`.
- Keep `backend/.env` and `frontend/.env` private; they are excluded from Git by `.gitignore`.

## GitHub setup

1. Copy `backend/.env.example` to `backend/.env` and enter local MySQL/Gemini values.
2. Copy `frontend/.env` from the local setup or create it with `VITE_API_URL=http://localhost:8080/api`.
3. Review `git status --ignored` and confirm both `.env` files are ignored before pushing.
4. Add your GitHub remote and push the repository; never force-add an `.env` file.
