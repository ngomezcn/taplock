
#include <SoftwareSerial.h>  
#include "utils/bluetooth.h"
#include "utils/commands.h"
#include "utils/config.cpp"

// HC-05 Bluetooth
SoftwareSerial miBT(BT_RX, BT_TX);  
char incomingByte;

void setup(){
  Serial.begin(9600);   
  miBT.begin(38400);     


  pinMode(AT_PIN, OUTPUT);
  pinMode(BT_POWER, OUTPUT);
  pinMode(13, OUTPUT);
  //digitalWrite(BT_POWER, HIGH);  
  Serial.println("Listo");
}

void loop(){

  //  while (Serial.available() == 0);

  /*if (Serial.available() > 0) {
    // read the incoming byte:
    incomingByte = Serial.read();

    if(incomingByte == '1')
    {
      Serial.println("UNO");
      bluetooth::stop_AT_mode(miBT);
    }
    if(incomingByte == '2')
    {
      Serial.println("DOS");
      bluetooth::start_AT_mode(miBT);
    }

    //Serial.print("I received: ");
   // Serial.println(incomingByte);
  }*/
  get_serial_string(miBT);
  if (miBT.available())
  {
    Serial.write(miBT.read());   
  }       
}