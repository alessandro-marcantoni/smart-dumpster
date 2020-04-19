#ifndef __TASK__
#define __TASK__

#include "State.h"

class Task {

public:
  virtual void init() = 0;
  virtual void tick(State* state) = 0;

};

#endif
