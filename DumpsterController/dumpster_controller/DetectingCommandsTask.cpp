#include "DetectingCommandsTask.h"
#include "Arduino.h"

DetectingCommandsTask::DetectingCommandsTask(int RxPin, int TxPin) {
  this->serial = new SoftwareSerial(TxPin, RxPin);
}

void DetectingCommandsTask::init() {
  this->serial->begin(9600);
}

void DetectingCommandsTask::tick(State* state) {
  if (serial->available()) {
    String cmd = "";
    while (serial->available()) {
      cmd += (char)serial->read();
    }
    this->command = cmd;
    this->handleCommand(this->command, state);
  }
}

void DetectingCommandsTask::handleCommand(String command, State* state) {
  if (*state == State::DETECTING_COMMANDS) {
    if (command == "WASTE_A") {
      *state = State::START_DUMPING_A;
    }
    if (command == "WASTE_B") {
      *state = State::START_DUMPING_B;
    }
    if (command == "WASTE_C") {
      *state = State::START_DUMPING_C;
    }
  } else if (*state == State::DUMPING) {
    if (command == "MORE_TIME") {
      Serial.println("Request of addictional time accepted.");
    }
  }
}
