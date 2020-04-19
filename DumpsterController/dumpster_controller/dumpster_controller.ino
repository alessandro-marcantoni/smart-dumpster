#include "Scheduler.h"
#include "DetectingCommandsTask.h"
#include "StartDumpingTask.h"
#include "Led.h"
#include "ServoMotor.h"

#define RxD 3
#define TxD 2
#define SERVO_PIN 5
#define LED_PIN_A 7
#define LED_PIN_B 8
#define LED_PIN_C 9

Scheduler scheduler;
Led* led_a;
Led* led_b;
Led* led_c;
ServoMotor* motor;
State state = State::DETECTING_COMMANDS;

void setup() {
  Serial.begin(9600);
  while (!Serial) {}
  Serial.println("Ready to go.\n");
  led_a = new Led(LED_PIN_A);
  led_b = new Led(LED_PIN_B);
  led_c = new Led(LED_PIN_C);
  motor = new ServoMotor(SERVO_PIN);

  scheduler.init(50);

  Task* t0 = new DetectingCommandsTask(RxD, TxD);
  t0->init();
  scheduler.addTask(t0);

  Task* t1 = new StartDumpingTask(motor, led_a, led_b, led_c);
  t1->init();
  scheduler.addTask(t1);

}

void loop() {
  scheduler.schedule(&state);
}
