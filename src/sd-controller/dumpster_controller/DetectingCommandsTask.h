#ifndef __DETECTINGCOMMANDSTASK__
#define __DETECTINGCOMMANDSTASK__

#define A "W_A\n"
#define B "W_B\n"
#define C "W_C\n"
#define T "M_T\n"

#include "Task.h"
#include "SoftwareSerial.h"
#include "State.h"

/**
 * The task that detects and handles commands.
 */
class DetectingCommandsTask: public Task {

private:
  SoftwareSerial* serial;
  long* timeToDump;
  String command;
  virtual void handleCommand(String command, State* state);

public:
  DetectingCommandsTask(SoftwareSerial* softwareSerial, long* timeToDump);
  virtual void init();
  virtual void tick(State* state);

};

#endif
