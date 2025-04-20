#ifndef GETCOMMAND_H
#define GETCOMMAND_H

#include "ICommand.h"          // Base ICommand interface
#include "Movies.h"            // For interacting with Movies
#include "Users.h"             // For interacting with Users
#include "DataManagement.h"    // For interacting with DataManagement
#include "StatusHandler.h"      // For Handle with Errors
#include <string>              // For std::string
#include <vector>              // For std::vector
using namespace std;


// Structures to store shared movies results and new movies to watch
struct SharedMoviesResult {
    int userID;
    int sharedMoviesCount;

    SharedMoviesResult(int id, int count) : userID(id), sharedMoviesCount(count) {}
};
struct MovieData {
    int NumOfRecommendation;
    int movieID;
    MovieData(int numOfRecommendation, int movieID) : NumOfRecommendation(numOfRecommendation), movieID(movieID) {}
    };

// RecommendCommand class

class GETCommand : public ICommand {
private:
    Movies& movies;               // Reference to Movies object
    Users& users;                 // Reference to Users object
    DataManagement& dataManagement; // Reference to DataManagement object

public:
    // Constructor
    GETCommand(Movies& moviesRef, Users& usersRef, DataManagement& dataManagementRef);

    // Overriding execute() method from ICommand
    string execute(const std::string& input) override;

    // Function to find shared movies between users
    vector<SharedMoviesResult> findSharedMovies(long int userID, long int TheMovieID);
    int getSharedMoviesCountByUserID(const vector<SharedMoviesResult>& results, int targetUserID);

   // Vector to store movie recommendition of the last exctute
    vector<MovieData> newMoviesToWatch;


};

#endif // GETCOMMAND_H