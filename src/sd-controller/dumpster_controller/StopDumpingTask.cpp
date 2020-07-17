#include "StopDumpingTask.h"
#include "Arduino.h"

StopDumpingTask::StopDumpingTask(SoftwareSerial* softwareSerial, ServoMotor* servoMotor, Led* ledA, Led* ledB, Led* ledC) {
  this->softwareSerial = softwareSerial;
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
  int byteSent = this->softwareSerial->println(D);
  Serial.println(byteSent);
  *state = State::DETECTING_COMMANDS;
}
