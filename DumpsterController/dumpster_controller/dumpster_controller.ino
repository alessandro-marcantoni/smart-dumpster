#include "Scheduler.h"
#include "DetectingCommandsTask.h"
#include "StartDumpingTask.h"
#include "DumpingTask.h"
#include "StopDumpingTask.h"
#include "Led.h"
#include "ServoMotor.h"

#define RxD 3
#define TxD 2
#define SERVO_PIN 5
#define LED_PIN_A 7
#define LED_PIN_B 8
#define LED_PIN_C 9

Scheduler scheduler;
Led* ledA;
Led* ledB;
Led* ledC;
ServoMotor* motor;
State state = State::DETECTING_COMMANDS;

void setup() {
  Serial.begin(9600);
  while (!Serial) {}

  ledA = new Led(LED_PIN_A);
  ledB = new Led(LED_PIN_B);
  ledC = new Led(LED_PIN_C);
  motor = new ServoMotor(SERVO_PIN);

  long timeToDump;
  const int basePeriod = 100;
  scheduler.init(basePeriod);

  Task* t0 = new DetectingCommandsTask(RxD, TxD);
  t0->init();
  scheduler.addTask(t0);

  Task* t1 = new StartDumpingTask(motor, ledA, ledB, ledC, &timeToDump);
  t1->init();
  scheduler.addTask(t1);

  Task* t2 = new DumpingTask(&timeToDump);
  t2->init();
  scheduler.addTask(t2);

  Task* t3 = new StopDumpingTask(motor, ledA, ledB, ledC);
  t3->init();
  scheduler.addTask(t3);

  Serial.println("Ready to go.\n");
}

void loop() {
  scheduler.schedule(&state);
}
