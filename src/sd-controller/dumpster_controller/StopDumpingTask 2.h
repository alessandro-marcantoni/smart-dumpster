#ifndef __STOPDUMPINGTASK__
#define __STOPDUMPINGTASK__

#define D "DONE"

#include "Task.h"
#include "State.h"
#include "ServoMotor.h"
#include "Led.h"
#include "SoftwareSerial.h"

/**
 * The task that ends the dumping transaction.
 */
class StopDumpingTask: public Task {

private:
  SoftwareSerial* softwareSerial;
  ServoMotor* servoMotor;
  Led* ledA;
  Led* ledB;
  Led* ledC;
  virtual void closeHatch(State* state);

public:
  StopDumpingTask(SoftwareSerial* softwareSerial, ServoMotor* servoMotor, Led* ledA, Led* ledB, Led* ledC);
  virtual void init();
  virtual void tick(State* state);

};

#endif
