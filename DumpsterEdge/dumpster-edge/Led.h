#ifndef __LED__
#define __LED__

class Led {

private:
  int pin;
  bool isOn;

public:
  Led(int pin);
  virtual void switchOn();
  virtual void switchOff();

};

#endif
