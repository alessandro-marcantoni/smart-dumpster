#include "Led.h"
#include "Arduino.h"

Led::Led(int pin) {
  this-> pin = pin;
  pinMode(this->pin, OUTPUT);
  this->isOn = false;
}

void Led::switchOn() {
  if (!this->isOn) {
    digitalWrite(this->pin, HIGH);
    this->isOn = true;
  }
}

void Led::switchOff() {
  if (this->isOn) {
    digitalWrite(this->pin, LOW);
    this->isOn = false;
  }
}
