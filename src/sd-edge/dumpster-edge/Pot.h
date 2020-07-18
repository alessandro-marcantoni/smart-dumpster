#ifndef __POT__
#define __POT__

class Pot {

private:
  int pin;
  int value;

public:
  Pot(int pin);
  virtual int hasChanged();
  virtual int getValue();

};

#endif
