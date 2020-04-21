#ifndef __STARTDUMPINGTASK__
#define __STARTDUMPINGTASK__

#include "Task.h"
#include "State.h"
#include "ServoMotor.h"
#include "Led.h"

/**
 * The task that starts the dumping transaction.
 */
class StartDumpingTask: public Task {

private:
  ServoMotor* servoMotor;
  Led* ledA;
  Led* ledB;
  Led* ledC;
  long* timeToDump;
  virtual void openHatch(State* state);

public:
  StartDumpingTask(ServoMotor* servoMotor, Led* ledA, Led* ledB, Led* ledC, long* timeToDump);
  virtual void init();
  virtual void tick(State* state);

};

#endif
