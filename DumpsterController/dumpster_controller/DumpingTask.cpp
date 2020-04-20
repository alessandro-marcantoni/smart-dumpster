#include "dumpingTask.h"
#include "Arduino.h"

DumpingTask::DumpingTask(long* timeToDump) {
  this->timeToDump = timeToDump;
  this->lastTime = 0;
}

void DumpingTask::init() {}

void DumpingTask::tick(State* state) {
  if (*state == State::STOP_DUMPING) {
    Serial.println("Still dumping");
  }

  if (*state == State::DUMPING) {
    if (this->lastTime == 0) {
      this->lastTime = millis();
    } else {
      long dt = millis() - this->lastTime;
      if (*(this->timeToDump) - dt <= 0) {
        this->lastTime = 0;
        *state = State::STOP_DUMPING;
      } else {
        *(this->timeToDump) -= dt;
        this->lastTime = millis();
      }
    }
  }
}
