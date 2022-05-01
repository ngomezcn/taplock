/*
	Capitulo 24 de Arduino desde cero en Español.
	Programa que permite establecer una comunicacion con el modulo Bluetooth HC-05
	y configurarlo de manera tal que pueda ser vinculado mediante un telefono
	movil o dispositivo compatible.

	Autor: bitwiseAr  

*/

#include <SoftwareSerial.h>	

// libreria que permite establecer pines digitales
// para comunicacion serie

#define AT_PIN 7
#define BT_POWER 8

#define BT_RX 5
#define BT_TX 6



SoftwareSerial miBT(5, 5); 	// pin 10 como RX, pin 11 como TX
void setup(){
  Serial.begin(9600);		
  miBT.begin(38400);		
  Serial.println("Listo");
  digitalWrite(BT_POWER, LOW);
  delay(500);  
  digitalWrite(BT_POWER, HIGH);
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
   miBT.write(Serial.read()+"ASD"); 	
}

}