#include "Scheduler.h"

void Scheduler::init(int basePeriod) {
  this->basePeriod = basePeriod;
  this->timer.setupPeriod(this->basePeriod);
  this->nTasks = 0;
  this->state = State::DETECTING_COMMANDS;
}

bool Scheduler::addTask(Task* task) {
  if (this->nTasks < MAX_TASKS) {
    this->taskList[this->nTasks] = task;
    this->nTasks++;
    return true;
  } else {
    return false;
  }
}

void Scheduler::schedule(State* state) {
  this->timer.waitForNextTick();
  for (int i=0; i<this->nTasks; i++) {
    this->taskList[i]->tick(state);
  }
}
