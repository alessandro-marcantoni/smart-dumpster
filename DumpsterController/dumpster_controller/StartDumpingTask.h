#ifndef __STARTDUMPINGTASK__
#define __STARTDUMPINGTASK__

#include "Task.h"
#include "State.h"
#include "ServoMotor.h"
#include "Led.h"

class StartDumpingTask: public Task {

private:
  ServoMotor* servoMotor;
  Led* led_a;
  Led* led_b;
  Led* led_c;
  virtual void openHatch(State* state);

public:
  StartDumpingTask(ServoMotor* servoMotor, Led* led_a, Led* led_b, Led* led_c);
  virtual void init();
  virtual void tick(State* state);

};

#endif
