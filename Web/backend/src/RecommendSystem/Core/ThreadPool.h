#ifndef THREADPOOL_H
#define THREADPOOL_H

#include <vector>
#include <thread>
#include <queue>
#include <mutex>
#include <condition_variable>
#include <atomic>
#include <functional>
#include <iostream>

using namespace std;
class ThreadPool {
private:
    vector<thread> workers;
    queue<function<void()>> tasks;
    mutex queueMutex;
    condition_variable condition;
    atomic<bool> stop;

    void workerThread();

public:
    ThreadPool(size_t maxThreads);
    ~ThreadPool();

    void addTask(int clientSocket,function<void(int)> task);
};

#endif // THREADPOOL_H
