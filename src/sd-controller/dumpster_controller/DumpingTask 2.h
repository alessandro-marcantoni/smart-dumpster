#ifndef __DUMPINGTASK__
#define __DUMPINGTASK__

#include "Task.h"
#include "State.h"

/**
 * The task that tracks the dumping time.
 */
class DumpingTask: public Task {

private:
  long* timeToDump;
  long lastTime;

public:
  DumpingTask(long* timeToDump);
  virtual void init();
  virtual void tick(State* state);

};

#endif
