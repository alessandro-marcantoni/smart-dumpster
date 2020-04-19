#ifndef __SCHEDULER__
#define __SCHEDULER__

#include "Timer.h"
#include "Task.h"
#include "State.h"

#define MAX_TASKS 4

/**
 * The tasks' scheduler.
 */
class Scheduler {

private:
  int basePeriod;
  int nTasks;
  Task* taskList[MAX_TASKS];
  Timer timer;
  State state;

public:
  virtual void init(int basePeriod);
  virtual bool addTask(Task *task);
  virtual void schedule(State* state);

};

#endif
