#ifndef __STOPDUMPINGTASK__
#define __STOPDUMPINGTASK__

#include "Task.h"
#include "State.h"
#include "ServoMotor.h"
#include "Led.h"

class StopDumpingTask: public Task {

private:
  ServoMotor* servoMotor;
  Led* ledA;
  Led* ledB;
  Led* ledC;
  virtual void closeHatch(State* state);

public:
  StopDumpingTask(ServoMotor* servoMotor, Led* ledA, Led* ledB, Led* ledC);
  virtual void init();
  virtual void tick(State* state);

};

#endif
