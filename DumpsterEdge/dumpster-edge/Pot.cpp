#include "Pot.h"
#include "Utils.h"
#include "Arduino.h"

Pot::Pot(int pin) {
  this->pin = pin;
  pinMode(this->pin, INPUT);
  this->value = analogRead(this->pin);
}

int Pot::hasChanged() {
  int newValue = analogRead(this->pin);
  if ((this->value < (newValue - 5) || this->value > (newValue + 5)) && newValue != 1024) {
    this->value = newValue;
    return 1;
  } else {
    return 0;
  }
}

int Pot::getValue() {
  return map(this->value, 0, 1023, 0, 100);
}
