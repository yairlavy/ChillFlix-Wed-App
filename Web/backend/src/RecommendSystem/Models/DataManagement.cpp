#include "DataManagement.h"

// Load user-movie data from the file
void DataManagement::LoadUsersData() {
    ifstream userMoviesFile(UserMoviesFileName); // Open file with user-movie data
    if (!userMoviesFile) {
     //   cout << "Error: Could not open " << UserMoviesFileName << " for reading" << endl;
        ofstream file;
            // Creating a new file
            file.open(UserMoviesFileName);

            // Check if the file was successfully created
                if (!file.is_open())
                {
       //          cout << "Error in creating file!" << endl;
            
            // Return a non-zero value to indicate an error
                return;
                }
         //   cout << "File created successfully." << endl;
            return; 
    }

    string line;
    while (getline(userMoviesFile, line)) {
        if (line.find("UserID: ") != string::npos) {
            int currentUserID = stoi(line.substr(8)); // Extract UserID
            if (getline(userMoviesFile, line) && line.find("MovieIDs: ") != string::npos) {
                istringstream movieStream(line.substr(10));
                int movieID;
                while (movieStream >> movieID) {
                    userMovies[currentUserID].insert(movieID); // Directly update the class-level map
                }
            }
        }
    }

    userMoviesFile.close();
}

// Function to update the user-movies file
void DataManagement::updateUserMoviesFile(long int userID) {
    try {
        // Open the file for writing (overwrite mode)
        ofstream userMoviesFile(UserMoviesFileName);
        if (!userMoviesFile) {
//            cout << "Error: Could not open User-Movies.txt for writing" << endl;
            return;
        }

        // Iterate through the sorted map and write each user and their movies
        for (const auto& [userID, movies] : userMovies) {
            userMoviesFile << "UserID: " << userID << endl;
            userMoviesFile << "MovieIDs: ";
            for (const auto& movieID : movies) {
                userMoviesFile << movieID << " ";
            }
            userMoviesFile << endl << endl;
        }

        userMoviesFile.close();
    } catch (const exception& e) {
  //      cout << "Exception occurred: " << e.what() << endl;
    }

    //cout << "File updated successfully." << endl;
}

unordered_set<int> DataManagement::getUserMovies(long int userID) const {
    auto it = userMovies.find(userID);
    if (it != userMovies.end()) {
        return it->second;  // Return the set of movies for this user
    }
    return unordered_set<int>();  // Return an empty set if user not found
}

    map<int, unordered_set<int>>& DataManagement::getAllUsersMovies() const {
    return userMovies;  // Return the entire map of user-movie data
}


/* debuging function
// Function to print all users and thier movies (debuug function)
void DataManagement::PrintUser_Movies() {
    for(const auto& userId : userMovies) {
        cout << endl << endl << "Movies for User " << userId.first << ":" << endl;
        const auto MyMovies = userMovies.at(userId.first);
        for (const auto& movieID : MyMovies) {
            cout << movieID << " ";
        }
    }
}
*/