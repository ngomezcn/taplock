#include <Arduino.h>
#line 1 "d:\\TapLock\\arduino\\main.ino"

#include <SoftwareSerial.h>  
#include "utils/bluetooth.h"

#define BT_RX 5
#define BT_TX 6


SoftwareSerial miBT(BT_RX, BT_TX);  // pin 10 como RX, pin 11 como TX

#define AT_PIN 7
#define BT_POWER 8

#line 14 "d:\\TapLock\\arduino\\main.ino"
void startAT(SoftwareSerial BT);
#line 25 "d:\\TapLock\\arduino\\main.ino"
void stopAT(SoftwareSerial BT);
#line 32 "d:\\TapLock\\arduino\\main.ino"
void setup();
#line 43 "d:\\TapLock\\arduino\\main.ino"
void loop();
#line 14 "d:\\TapLock\\arduino\\main.ino"
void startAT(SoftwareSerial BT)
{
  digitalWrite(BT_POWER, LOW);
  delay(500);  
  digitalWrite(AT_PIN, HIGH);
  delay(500);  
  digitalWrite(BT_POWER, HIGH);  
  delay(2000);  
  BT.println("AT+VERSION?\n");  
}

void stopAT(SoftwareSerial BT)
{
  digitalWrite(AT_PIN, LOW);
  delay(500);  
  BT.println("AT+RESET\n");   
}

void setup(){
  Serial.begin(9600);   
  miBT.begin(38400);     
  pinMode(AT_PIN, OUTPUT);
  pinMode(BT_POWER, OUTPUT);

  startAT(miBT);
  //delay(5000);
  //stopAT(miBT);
}

void loop(){
    if (miBT.available())
    {
        Serial.write(miBT.read());   
    }       

    if (Serial.available())     
    {
        miBT.write(Serial.read());   
    }
}
