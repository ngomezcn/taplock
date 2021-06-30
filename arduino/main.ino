
#include <SoftwareSerial.h>  
#include "utils/bluetooth.h"
#include "utils/config.cpp"

SoftwareSerial miBT(BT_RX, BT_TX);  // pin 10 como RX, pin 11 como TX



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