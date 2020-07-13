#ifndef __HTTPMANAGER__
#define __HTTPMANAGER__

#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>
#include <ArduinoJson.h>

/* wifi network name */
#define ssidName "2,4 ghz TIM-232 Extended"
/* WPA2 PSK password */
#define pwd "19641964"

#define DUMPSTER_AVAILABLE "http://192.168.1.201/dumpster-service/is-dumpster-available.php"
#define UPDATE_WEIGHT "http://192.168.1.201/dumpster-service/update-weight.php"

class HttpManager {

private:
  HTTPClient http;
  char* AVAILABLE = "AV";
  virtual int areEquals(String s1, String s2);

public:
  HttpManager();
  virtual int isDumpsterAvailable();
  virtual int isEdgeConnected();
  virtual void sendWeight(int value);

};

#endif
