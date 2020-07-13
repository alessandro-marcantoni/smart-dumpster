#include "StartDumpingTask.h"
#include "Arduino.h"

StartDumpingTask::StartDumpingTask(ServoMotor* servoMotor, Led* ledA, Led* ledB, Led* ledC, long* timeToDump) {
  this->servoMotor = servoMotor;
  this->ledA = ledA;
  this->ledB = ledB;
  this->ledC = ledC;
  this->timeToDump = timeToDump;
}

void StartDumpingTask::init() {
  this->servoMotor->on();
}

void StartDumpingTask::tick(State* state) {
  if (*state == State::START_DUMPING_A){
    Serial.println("Opening A hatch...");
    this->ledA->switchOn();
    this->openHatch(state);
  }
  if (*state == State::START_DUMPING_B){
    Serial.println("Opening B hatch...");
    this->ledB->switchOn();
    this->openHatch(state);
  }
  if (*state == State::START_DUMPING_C){
    Serial.println("Opening C hatch...");
    this->ledC->switchOn();
    this->openHatch(state);
  }
}

void StartDumpingTask::openHatch(State* state) {
  this->servoMotor->openHatch();
  *(this->timeToDump) = DUMPING_TIME;
  *state = State::DUMPING;
}
