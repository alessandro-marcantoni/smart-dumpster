#ifndef __DETECTINGCOMMANDSTASK__
#define __DETECTINGCOMMANDSTASK__

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
  DetectingCommandsTask(int RxPin, int TxPin, long* timeToDump);
  virtual void init();
  virtual void tick(State* state);

};

#endif
