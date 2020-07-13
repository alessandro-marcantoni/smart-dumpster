#include "HttpManager.h"
#include "Arduino.h"

HttpManager::HttpManager() {
  WiFi.begin(ssidName, pwd);
  Serial.println("Connecting...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.print("Connected: \nlocal IP: ");
  Serial.println(WiFi.localIP());
}

int HttpManager::isDumpsterAvailable() {
  this->http.begin(DUMPSTER_AVAILABLE);
  int httpCode = this->http.GET();

  if (httpCode > 0) {
    const size_t capacity = JSON_OBJECT_SIZE(1);
    DynamicJsonBuffer jsonBuffer(capacity);
    JsonObject& response = jsonBuffer.parseObject(this->http.getString());
    return areEquals(response["available"].as<char*>(), this->AVAILABLE);
  }
  this->http.end();
  return 0;
}

int HttpManager::isEdgeConnected() {
  return WiFi.status() == WL_CONNECTED;
}

void HttpManager::sendWeight(int value) {
  String msg = "{\"value\":" + (String)value + "}";

  this->http.begin(UPDATE_WEIGHT);
  this->http.addHeader("Content-Type", "application/json");
  int httpCode = this->http.POST(msg);
  this->http.end();
}

int HttpManager::areEquals(String s1, String s2) {
  return s1[0] == s2[0] && s1[1] == s2[1];
}
