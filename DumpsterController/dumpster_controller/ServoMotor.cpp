#include "ServoMotor.h"
#include "Arduino.h"

ServoMotor::ServoMotor(int pin) {
  this->pin = pin;
}

void ServoMotor::setPosition(int angle) {
  this->motor.write(angle);
}

void ServoMotor::on() {
  this->motor.attach(this->pin);
  this->motor.write(900);
  Serial.println("The servo is on");
}

void ServoMotor::off() {
  this->motor.detach();
}
