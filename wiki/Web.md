# ChillFlix - Web Application

ChillFlix is a streaming platform that allows users to watch movies and TV shows anytime, anywhere. 

---

##  Home Page For Non Registers Users 

The home page provides a modern interface with a background collage of trending movies. Users can enter their email to start the registration process or log in to an existing account. The navigation includes options for signing up, logging in, and exploring content.

## Screenshot

![1](https://github.com/user-attachments/assets/000126c8-3593-42c8-a131-d69be55bc950)

---
#  ChillFlix Web - Sign Up Page

The sign-up page allows new users to create an account by entering their email, username, password, and display name. Users must choose an avatar and ensure their credentials meet validation rules. If any errors occur, such as an invalid email or mismatched passwords, a notification appears. Upon successful registration, users are redirected to the login page.

## Screenshots
### Sign-Up Page:
![3](https://github.com/user-attachments/assets/6be40512-bda3-4931-9624-43a0268e8b94)


### Register Success:
![4](https://github.com/user-attachments/assets/25aee285-ec44-4f2d-a25d-3d4bef7ca528)


---
# ChillFlix Web - Sign In Page

The sign-in page allows users to log into their accounts by entering their username and password. If the credentials are correct, a secure JWT token is generated, granting access to the platform. In case of incorrect credentials or missing input, an error message appears. Upon successful login, users are redirected to the home page.


---

## Screenshots
###  Sign-In Page:
![5](https://github.com/user-attachments/assets/630b0e95-b722-454b-b432-bb2461e795c0)


###  Invalid Credentials:
![7](https://github.com/user-attachments/assets/e7490292-d5e1-4f57-9c8e-382c4f66359b)


###  Login Success:
![8](https://github.com/user-attachments/assets/1fc14e6e-b03e-43ab-8683-f09bf5b6daf8)


---

# ChillFlix Web - Home Page

The home page serves as the main hub for browsing and discovering movies, categorized by genres. It features a top navigation bar for accessing the home page, categories, search, and user settings. A featured movie is displayed at the top, followed by different genre sections where users can explore available films. Additionally, users can save movies to their personal watchlist for later viewing.

---

## Screenshots
### Home Page:
![9](https://github.com/user-attachments/assets/d7c7ca43-9f1f-43fc-8e14-bf4419012e1b)


### Movie Categories:
![11](https://github.com/user-attachments/assets/9d74f88e-4819-4713-8667-8850244040ce)


### Navigation Bar:
![10](https://github.com/user-attachments/assets/ea631a65-6fe4-44e4-881d-d3be2b081fa0)


---
# ChillFlix Web - Category Page

The category page allows users to browse movies sorted by genre. After selecting a category from the navigation bar, a filtered list of relevant movies is displayed. Each movie appears as a clickable card, leading to its detailed information page. Users can explore different categories and add movies to their watchlist.

---

## Screenshots
### Category Page:
![14](https://github.com/user-attachments/assets/5dc58246-7f1c-4f0d-adcb-a1d951771f94)


---
# ChillFlix Web - Movie Details Page

The movie details page provides users with essential information about a selected movie, including its title, genre, runtime, and description. Users can choose to watch the movie or add it to their watchlist for later viewing. Below the main details, recommended movies based on genre and user preferences are displayed.

---

## Screenshots
### Movie Details Page:
![12](https://github.com/user-attachments/assets/0e70f9c6-372d-4ee9-988e-7b1453ec8155)
---
# ChillFlix Web - Video Playback Feature

The video playback feature allows users to stream movies with full control over playback. Users can play, pause, skip forward or backward, and adjust playback speed. The player supports full-screen mode and minimizing, and some content may require unlocking before viewing.

---

## Screenshots
### Video Playback:
![15](https://github.com/user-attachments/assets/78c5bab5-2949-404b-9ab0-6cd16d5460fa)


---
# ChillFlix Web - Watchlist Feature

The watchlist feature allows users to save movies for later viewing. Movies can be added from the movie details page and will appear in a dedicated watchlist section on the homepage. The watchlist persists across sessions, and users can remove movies at any time.


---

## Screenshots
### Movie Added to Watchlist:
![18](https://github.com/user-attachments/assets/ef313fc6-8530-4e8e-9d96-60fb0471cbb1)


---
# ChillFlix Web - User Profile & Settings

The profile and settings feature allows users to update their personal details, change their password, and modify their profile picture. Users can choose from default avatars or upload a custom image, which updates instantly. The profile menu also provides access to settings and the logout option.

---

## Screenshots
### Profile Menu:
![19](https://github.com/user-attachments/assets/6ac3ee36-ea92-40c9-8b93-6b557136151c)


### Edit Profile Page:
![20](https://github.com/user-attachments/assets/3d7faf09-af70-4a7b-9ac0-c129e1960516)


### Profile Update Success:
![21](https://github.com/user-attachments/assets/798a3b87-c580-452c-98a1-f056fc25c4cf)

### New Profile Picture:
![23](https://github.com/user-attachments/assets/6e3a9372-793c-407a-bd7c-73a7c7694781)


---
# ChillFlix Web - Search Feature

The search feature allows users to quickly find movies by entering keywords related to the title, genre, or description. Matching results appear dynamically, and users can click on a movie to view its details. If no results are found, an appropriate message is displayed.

---

## Screenshots
### Search Results Page:
![24](https://github.com/user-attachments/assets/b9fa2e06-701c-4e0c-a2ef-c241cf91b83d)


---
# ChillFlix Web - Admin Panel

The admin panel is restricted to authorized users and provides key management tools for the platform.  
It includes insights on user activity, total movies in the system, and category management.

### Features:
- View system analytics (user count, movie count)
- Manage categories
- Add, edit, and delete movies

Only admins can access this panel via the navigation bar.

to create an admin user you could use Thunder Client like the picture below.

## Screenshots
### Create Admin User:
<img width="772" alt="Admin" src="https://github.com/user-attachments/assets/aa5e1e46-6e68-456a-8ef3-263d34de307d" />

### Admin Panel Page:
![צילום מסך 2025-03-02 215402](https://github.com/user-attachments/assets/6d52da7f-8e62-4d23-950b-de3f181c2cc4)



---
# ChillFlix Web - Category Management

In the admin panel, new categories can be added to organize movies efficiently.  
Admins enter the category name, select if it should be promoted, and confirm the addition.  
Once added, the category appears on the web platform for users to browse.
After adding a category, it can be seen in the categories screen. If no movies are assigned to it yet, a message will indicate that the category is empty. 

## Screenshots

### Adding a Category  
![צילום מסך 2025-03-02 215415](https://github.com/user-attachments/assets/7ab3966c-a814-42a7-823a-166e037811ac)


### Confirmation Message  
 
![צילום מסך 2025-03-02 215420](https://github.com/user-attachments/assets/5a86ed05-40f0-4304-bf79-02ee8f4bd8bc)


### Viewing the Category  

 
![צילום מסך 2025-03-02 221243](https://github.com/user-attachments/assets/cf441262-19b7-40b0-8e05-0a33a3fd3fa5)

---
# ChillFlix Web - Movie Management

In the admin panel, movies can be added with detailed information.  
Admins can input the following fields:

- **Title**: The name of the movie  
- **Genres**: Comma-separated list of genres  
- **Runtime**: Movie duration  
- **Overview**: A short description of the movie  
- **Poster Path**: Upload a poster image  
- **Backdrop Path**: (Optional) Upload a background image  
- **Trailer**: (Optional) Upload a trailer file  

Once a movie is added, it becomes available in the system and can be searched by users.

## Screenshots

### Adding a Movie  
![צילום מסך 2025-03-02 215900](https://github.com/user-attachments/assets/277465e2-98b0-49f4-b436-95153989a7b9)


### Confirmation Message  
After adding a movie, a success message confirms the addition.  
![צילום מסך 2025-03-02 215852](https://github.com/user-attachments/assets/6c5e85ce-ea70-4ae3-ba6d-a9eeeabd90b4)


### Searching for the Movie  
The newly added movie appears in search results.  
![צילום מסך 2025-03-02 215945](https://github.com/user-attachments/assets/26895a2f-d64e-4ac3-b14f-63390b968f50)

---
# ChillFlix Web - Movie Deletion

In the admin panel, movies can be deleted easily.  
Admins select the movie from a dropdown list and confirm the deletion.  
Once deleted, the movie is removed from the system and no longer appears in searches or categories.

## Screenshots

### Deleting a Movie  
![צילום מסך 2025-03-02 215958](https://github.com/user-attachments/assets/260f0b86-9fb0-4182-9c4b-f01c159eb249)


### Confirmation Message  
After deletion, a confirmation message is displayed.  
![צילום מסך 2025-03-02 220007](https://github.com/user-attachments/assets/522a755c-b9e5-4547-979c-0efafde49a86)


### Checking the Movie
 
![צילום מסך 2025-03-02 222101](https://github.com/user-attachments/assets/5d20fd5e-679b-4712-a9f9-abeb38b9f041)


---
# ChillFlix Web - Multi-Client Synchronization

ChillFlix supports real-time updates across multiple clients.  
Users can access the platform simultaneously, ensuring that changes such as adding or deleting movies are immediately reflected across all sessions.

## Features:
- Multiple clients can be logged in at the same time.
- Changes made by one user (like adding or deleting a movie) update instantly for all users.
- The search function dynamically reflects updates in real time.

## Screenshots

### Searching for a Movie on Different Clients   

![צילום מסך 2025-03-02 224443](https://github.com/user-attachments/assets/c7815f27-59b9-4f72-a288-a8009cf4bc4a)


### After Deleting a Movie  
 
![צילום מסך 2025-03-02 225022](https://github.com/user-attachments/assets/857ffb88-5f84-4f58-9f5d-bf3e1ef5d85e)


---
