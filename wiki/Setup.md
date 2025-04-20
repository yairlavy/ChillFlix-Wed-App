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
![צילום מסך 2025-03-05 000513](https://github.com/user-attachments/assets/698c88b9-2503-4fb7-a944-0faf04888727)

   This command will build the Docker images and start the server along with the MongoDB instance.

3. To run it again after stop use:
   ```bash
   docker compose -f docker-compose.yml --env-file ./backend/src/MongoServer/config/.env.local up
   ```

4. (Optional) Import Default Netflix Movies & Categories
   If you want to add the pre-built Netflix movies and categories to the database, open a new 
   terminal and run:
      ```bash
      docker exec -it MongoDB mongoimport --jsonArray --db api --collection movies --file       
      /movies.json
      docker exec -it MongoDB mongoimport --jsonArray --db api --collection categories --file 
      /categories.json
      ```
5. To access the web application, open your browser and go to:  
   [http://localhost:3000/](http://localhost:3000/)

   This will launch the Netflix web application in your browser.

