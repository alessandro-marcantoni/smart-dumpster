#include "ServoMotor.h"

ServoMotor::ServoMotor(int pin) {
  this->pin = pin;
}

void ServoMotor::openHatch() {
  this->motor.write(2250);
}

void ServoMotor::closeHatch() {
  this->motor.write(900);
}

void ServoMotor::on() {
  this->motor.attach(this->pin);
  this->motor.write(900);
}

void ServoMotor::off() {
  this->motor.detach();
}
