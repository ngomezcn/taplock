/*
  Capitulo 24 de Arduino desde cero en Español.
  Programa que permite establecer una comunicacion con el modulo Bluetooth HC-05
  y configurarlo de manera tal que pueda ser vinculado mediante un telefono
  movil o dispositivo compatible.

  Autor: bitwiseAr  

*/

#include <SoftwareSerial.h>
SoftwareSerial miBT(5, 6);  // pin 10 como RX, pin 11 como TX

void setup(){
  Serial.begin(9600);   // comunicacion de monitor serial a 9600 bps
  Serial.println("Listo");  // escribe Listo en el monitor
  miBT.begin(38400);    // comunicacion serie entre Arduino y el modulo a 38400 bps

  
}

void loop()
{
  if (miBT.available())
  {
      //const char a = miBT.read(); 

      String b = miBT.readString();
      String check = "123";

      Serial.println(b);

      if(check == b)
      {
        Serial.println("IGUAL");
      }
  }

  if (Serial.available())
  {
    String a = Serial.readString();
    //miBT.write(a);
    miBT.print(a);
    Serial.println("Enviando datos: " + a);
  }    

}