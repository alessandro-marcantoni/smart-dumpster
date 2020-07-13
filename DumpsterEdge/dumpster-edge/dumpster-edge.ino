#include "Led.h"
#include "Pot.h"
#include "HttpManager.h"
#include "Utils.h"

Led* greenLed;
Led* redLed;
Pot* pot;
HttpManager* httpManager;

void setup() {
  Serial.begin(115200);
  while (!Serial) {}

  greenLed = new Led(GREEN_LED_PIN);
  redLed = new Led(RED_LED_PIN);
  pot = new Pot(POT_PIN);
  httpManager = new HttpManager();

  pinMode(POT_PIN, INPUT);
}

void loop() {
  if (httpManager->isEdgeConnected() && httpManager->isDumpsterAvailable()) {
    greenLed->switchOn();
    redLed->switchOff();
    if (pot->hasChanged()) {
      httpManager->sendWeight(pot->getValue());
    }
  } else {
    greenLed->switchOff();
    redLed->switchOn();
  }
  delay(1000);
}
