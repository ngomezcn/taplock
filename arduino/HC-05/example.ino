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

SoftwareSerial miBT(5, 5); 	// pin 10 como RX, pin 11 como TX
void setup(){
  Serial.begin(9600);		
  Serial.println("Listo");
  miBT.begin(38400);		
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