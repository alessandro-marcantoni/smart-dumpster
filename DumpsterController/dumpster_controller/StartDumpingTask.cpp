#include "StartDumpingTask.h"
#include "Arduino.h"

StartDumpingTask::StartDumpingTask(ServoMotor* servoMotor, Led* led_a, Led* led_b, Led* led_c) {
  this->servoMotor = servoMotor;
  this->led_a = led_a;
  this->led_b = led_b;
  this->led_c = led_c;
}

void StartDumpingTask::init() {
  this->servoMotor->on();
}

void StartDumpingTask::tick(State* state) {
  if (*state == State::START_DUMPING_A){
    Serial.println("Opening A hatch...");
    this->led_a->switchOn();
    this->openHatch(state);
  }
  if (*state == State::START_DUMPING_B){
    Serial.println("Opening B hatch...");
    this->led_b->switchOn();
    this->openHatch(state);
  }
  if (*state == State::START_DUMPING_C){
    Serial.println("Opening C hatch...");
    this->led_c->switchOn();
    this->openHatch(state);
  }
}

void StartDumpingTask::openHatch(State* state) {
  this->servoMotor->setPosition(2250);
  *state = State::DUMPING;
}
