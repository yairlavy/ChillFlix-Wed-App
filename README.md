# ChillFlix

## Overview

In this phase, we expanded our platform by developing both a web application and an Android app, both fully integrated with the backend and MongoDB database we built in the previous phase. This allows users to interact seamlessly with the platform, providing a rich and dynamic experience across devices.

## Key Features Implemented in Part 4:

### User Authentication & Profile Management
Sign-up and login screens available on both the website and mobile app.
Session persistence to keep users logged in across sessions.
Profile settings screen where users can update their personal details, including the ability to upload a profile picture from their phone.

### Content Browsing & Streaming
A dedicated movie viewing screen where users can watch movies directly from the app or website.
Category browsing screen, displaying all movies under each category.
Search functionality that redirects users to a list of relevant movies based on their query.

### Admin Dashboard & Content Management
Full management system for adding, updating, deleting, and organizing movies and categories.
Movie and category control panel, allowing administrators to modify, delete, or add new content dynamically.

### Recommendation & Personalized Experience
Personalized movie recommendations based on viewing history.
Trending and featured movies updated dynamically from the database.
User-specific watchlist and “Continue Watching” sections for easy content access.

### Enhanced UI/UX for Seamless Navigation
Website: A structured interface with an intuitive top navigation bar, category-based movie browsing, and an interactive search feature.
Android app: A smooth, responsive design featuring sliders, lists, and grids for an optimized browsing experience.
Advanced search functionality that immediately redirects users to a page displaying all related movies.

#### This phase of the project brings together frontend and backend integration, ensuring a fully functional, immersive, and user-friendly platform while leveraging the MongoDB-powered backend developed in the previous phase. 



## Installation and Setup

### Prerequisites

1. Install [npm](https://www.npmjs.com/):
   ```bash
   cd src/MongoServer
   npm install
   ```

2. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/).
3. Install [MongoDB Compass](https://www.mongodb.com/products/compass) for managing the database.
4. Install the [Thunder Client extension](https://www.thunderclient.com/) for testing RESTful APIs conveniently in VS Code.

### Environment Variables

Ensure the following environment variables are set in the `.env.local` file located in `./src/MongoServer/config/`:
(You can modify the PORT variable to a different value if you'd like to change the port on which the server runs.)
```env
CONNECTION_STRING="mongodb://mongo:27017/api"
PORT=8181

RECOMMEND_PORT=8081
RECOMMEND_HOST="host.docker.internal"
```

### Running the Project

1. Start Docker Desktop and MongoDB Compass.

2. To start the server and database using Docker Compose, navigate to the `web` directory first and then run the following command:

   ```sh
   cd web
   ```
   ```bash
   docker compose -f docker-compose.yml --env-file ./backend/src/MongoServer/config/.env.local up --build
   ```

   This command will build the Docker images and start the server along with the MongoDB instance.

3. To run it again after stop use:
   ```bash
   docker compose -f docker-compose.yml --env-file ./backend/src/MongoServer/config/.env.local up
   ```

4. (Optional) Import Default Netflix Movies & Categories
   If you want to add the pre-built Netflix movies and categories to the database, open a new 
   terminal and run:
      ```bash
      docker exec -it MongoDB mongoimport --jsonArray --db api --collection movies --file/movies.json
      docker exec -it MongoDB mongoimport --jsonArray --db api --collection categories --file/ categories.json
      ```
5. To access the web application, open your browser and go to:  
   [http://localhost:3000/](http://localhost:3000/)

   This will launch the Netflix web application in your browser.


   # ChillFlix - Project Development Process

   ## Overview  
   The goal of this project was to take the server-side code from **Exercise 3** and integrate it with a newly developed client-side interface. This required building both a **web application**
   and an **Android application** for ChillFlix.
   
   ## Development Process  
   
   ### Web Application  
   We started by developing the **web version** of the application, with each team member assigned a specific role:  
   - One member focused on **user authentication**, registration, and token management.  
   - Another was responsible for **the homepage and category pages**.  
   - The third handled **the admin panel and settings page**.  
   
   Once the web application was completed, it provided a clear structure for the Android development phase.
   
   ### Android Application  
   With the experience gained from the web development stage, transitioning to Android was smoother. The same division of responsibilities was applied, ensuring a structured and efficient
   workflow. We successfully integrated the **server-side** with the mobile client, following the same logic as the web implementation.
   
   ## Teamwork & Collaboration  
   Throughout the project, the team maintained **strong communication and collaboration**. Whenever a team member faced a challenge, there was always someone available to assist. This mutual        support and structured division of tasks allowed us to efficiently complete the project while ensuring high-quality implementation.
   
   ---
   This project was a great learning experience, combining backend integration, web development, and mobile application development into a single cohesive system.
