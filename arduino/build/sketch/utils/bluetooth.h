#line 1 "d:\\TapLock\\arduino\\utils\\bluetooth.h"
#pragma once

#include "config.cpp"
#include <SoftwareSerial.h>

namespace bluetooth
{
  void start_AT_mode(SoftwareSerial device)
  {
    Serial.println("exec: start_AT_mode()");
    delay(500);  
    digitalWrite(BT_POWER, LOW);
    delay(500);  
    digitalWrite(AT_PIN, HIGH);
    delay(500);  
    digitalWrite(BT_POWER, HIGH);  
    delay(2000);  
    device.println("AT+ADDR?\n");  
    device.println("AT+VERSION?\r\n");  
  }

  void stop_AT_mode(SoftwareSerial device)
  {
    Serial.println("exec: stop_AT_mode()");
    delay(500);  
    digitalWrite(AT_PIN, LOW);
    delay(500);  
    device.println("AT+RESET\n");   
  }

  void reboot()
  {
    Serial.println("exec: reboot()");

    digitalWrite(BT_POWER, LOW);
    delay(500);  
    digitalWrite(BT_POWER, HIGH);
  }
}
