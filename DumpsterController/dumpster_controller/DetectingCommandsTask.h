#ifndef __DETECTINGCOMMANDSTASK__
#define __DETECTINGCOMMANDSTASK__

#include "Task.h"
#include "SoftwareSerial.h"
#include "State.h"

/**
 * The task that detects commands.
 */
class DetectingCommandsTask: public Task {

private:
  SoftwareSerial* serial;
  String command;
  virtual void handleCommand(String command, State* state);

public:
  DetectingCommandsTask(int RxPin, int TxPin);
  virtual void init();
  virtual void tick(State* state);

};

#endif
