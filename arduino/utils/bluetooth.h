#include "config.cpp"
#

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