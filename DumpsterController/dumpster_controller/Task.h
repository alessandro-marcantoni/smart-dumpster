#ifndef __TASK__
#define __TASK__

#include "State.h"

#define DUMPING_TIME 10000

class Task {

public:
  virtual void init() = 0;
  virtual void tick(State* state) = 0;

};

#endif
