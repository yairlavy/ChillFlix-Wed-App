#include "ThreadPool.h"

ThreadPool::ThreadPool(size_t maxThreads) : stop(false) {
    for (size_t i = 0; i < maxThreads; ++i) {
        workers.emplace_back(&ThreadPool::workerThread, this);
    }
}

ThreadPool::~ThreadPool() {
    stop.store(true);
    condition.notify_all();
    for (std::thread &worker : workers) {
        if (worker.joinable()) {
            worker.join();
        }
    }
}

void ThreadPool::addTask(int clientSocket, std::function<void(int)> task) {
    {
        std::lock_guard<std::mutex> lock(queueMutex);
        tasks.push([clientSocket, task]() { task(clientSocket); });
    }
    condition.notify_one();
}

void ThreadPool::workerThread() {
    while (!stop.load()) {
        std::function<void()> task;
        {
            std::unique_lock<std::mutex> lock(queueMutex);
            condition.wait(lock, [this] { return stop.load() || !tasks.empty(); });
            if (stop.load() && tasks.empty()) {
                return;
            }
            task = std::move(tasks.front());
            tasks.pop();
        }
        task();
    }
}
