#include "StopDumpingTask.h"
#include "Arduino.h"

StopDumpingTask::StopDumpingTask(ServoMotor* servoMotor, Led* ledA, Led* ledB, Led* ledC) {
  this->servoMotor = servoMotor;
  this->ledA = ledA;
  this->ledB = ledB;
  this->ledC = ledC;
}

void StopDumpingTask::init() {}

void StopDumpingTask::tick(State* state) {
  if (*state == State::STOP_DUMPING) {
    this->ledA->switchOff();
    this->ledB->switchOff();
    this->ledC->switchOff();
    this->closeHatch(state);
  }
}

void StopDumpingTask::closeHatch(State* state) {
  Serial.println("Closing the hatch...");
  this->servoMotor->closeHatch();
  *state = State::DETECTING_COMMANDS;
}
