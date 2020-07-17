#include "DetectingCommandsTask.h"
#include "Arduino.h"

DetectingCommandsTask::DetectingCommandsTask(SoftwareSerial* softwareSerial, long* timeToDump) {
  this->serial = softwareSerial;
  this->timeToDump = timeToDump;
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
    if (command == A) {
      *state = State::START_DUMPING_A;
    }
    if (command == B) {
      *state = State::START_DUMPING_B;
    }
    if (command == C) {
      *state = State::START_DUMPING_C;
    }
  } else if (*state == State::DUMPING) {
    if (command == T) {
      Serial.println("Request of addictional time accepted.");
      *(this->timeToDump) += ADDITIONAL_TIME;
    }
  }
}
