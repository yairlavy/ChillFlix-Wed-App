cmake_minimum_required(VERSION 3.14)
project(Netflix)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Include GoogleTest
include(FetchContent)
FetchContent_Declare(
  googletest
  URL https://github.com/google/googletest/archive/refs/tags/release-1.12.1.zip
)
set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
FetchContent_MakeAvailable(googletest)

# Include directories for header files
include_directories(
    ${CMAKE_SOURCE_DIR}/Commands
    ${CMAKE_SOURCE_DIR}/Models
    ${CMAKE_SOURCE_DIR}/Core
    ${CMAKE_SOURCE_DIR}/Utilities
    ${CMAKE_SOURCE_DIR}/Interfaces
)

# Collect all source files dynamically
file(GLOB_RECURSE SOURCES 
    ${CMAKE_SOURCE_DIR}/Commands/*.cpp
    ${CMAKE_SOURCE_DIR}/Models/*.cpp
    ${CMAKE_SOURCE_DIR}/Core/*.cpp
    ${CMAKE_SOURCE_DIR}/Utilities/*.cpp
)

# Add the main application source file
set(MAIN_SOURCE ${CMAKE_SOURCE_DIR}/Main.cpp)

# Create the main executable
add_executable(Netflix ${SOURCES} ${MAIN_SOURCE})

# Link pthread library for threading support
find_package(Threads REQUIRED)
target_link_libraries(Netflix PRIVATE Threads::Threads)

# Collect all test source files
file(GLOB_RECURSE TEST_SOURCES
    ${CMAKE_SOURCE_DIR}/Tests/*.cpp
)

# Create the test executable (exclude Main.cpp)
add_executable(CommandTests 
    ${SOURCES}        # Reuse the application sources
    ${TEST_SOURCES}   # Add test sources
)

# Remove Main.cpp from CommandTests
list(REMOVE_ITEM SOURCES ${MAIN_SOURCE})

# Include directories for tests
target_include_directories(CommandTests PRIVATE ${CMAKE_SOURCE_DIR})

# Link GoogleTest and pthread to the test executable
target_link_libraries(CommandTests gtest gtest_main Threads::Threads)
