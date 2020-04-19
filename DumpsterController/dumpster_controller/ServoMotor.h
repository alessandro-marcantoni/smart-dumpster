#ifndef __SERVOMOTOR__
#define __SERVOMOTOR__

#include "Arduino.h"
#include "ServoTimer2.h"

class ServoMotor {

private:
  int pin;
  ServoTimer2 motor;

public:
  ServoMotor(int pin);
  void setPosition(int angle);
  void on();
  void off();

};

#endif
