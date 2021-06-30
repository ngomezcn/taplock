
#include <SoftwareSerial.h>	

SoftwareSerial miBT(5, 5); 	// pin 10 como RX, pin 11 como TX
void setup(){
  Serial.begin(9600);		
  miBT.begin(38400);		

   pinMode(SD_STATUS, OUTPUT);
}

void loop(){
Serial.write("miBT.read()\n");
delay(1000);
if (miBT.available())
{
   Serial.write(miBT.read()); 	
}     	

if (Serial.available())   	
{
   miBT.write(Serial.read()); 	
}

}