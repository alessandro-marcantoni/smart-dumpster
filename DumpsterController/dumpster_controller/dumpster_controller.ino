//#include "MsgServiceBT.h"
#include "SoftwareSerial.h"

#define RxD 3
#define TxD 2

SoftwareSerial serial(2, 3);
String command;

void setup() {
  serial.begin(9600);
  Serial.begin(9600);
  while (!Serial) {}
  Serial.println("Ready to go.\n");
}

void loop() {
  if (serial.available()) {
    while (serial.available()) {
      command += (char)serial.read();
    }
    Serial.println(command);
    command = "";
  }
  delay(100);
}
