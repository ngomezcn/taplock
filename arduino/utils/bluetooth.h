#pragma once

#include "config.cpp"
#include <SoftwareSerial.h>

namespace bluetooth
{
  void start_AT_mode(SoftwareSerial device)
  {
    Serial.println("start_AT_mode()");
    delay(500);  
    digitalWrite(BT_POWER, LOW);
    delay(500);  
    digitalWrite(AT_PIN, HIGH);
    delay(500);  
    digitalWrite(BT_POWER, HIGH);  
    delay(2000);  
    device.println("AT+VERSION?\n");  
  }

  void stop_AT_mode(SoftwareSerial device)
  {
    Serial.println("stop_AT_mode()");
    delay(500);  
    digitalWrite(AT_PIN, LOW);
    delay(500);  
    device.println("AT+RESET\n");   
  }

  void reboot()
  {
    digitalWrite(BT_POWER, LOW);
    delay(500);  
    digitalWrite(BT_POWER, HIGH);
  }
}
