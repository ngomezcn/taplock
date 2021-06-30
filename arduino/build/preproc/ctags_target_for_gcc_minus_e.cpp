# 1 "d:\\TapLock\\arduino\\main.ino"

# 3 "d:\\TapLock\\arduino\\main.ino" 2
# 4 "d:\\TapLock\\arduino\\main.ino" 2





SoftwareSerial miBT(5, 6); // pin 10 como RX, pin 11 como TX




void startAT(SoftwareSerial BT)
{
  digitalWrite(8, 0x0);
  delay(500);
  digitalWrite(7, 0x1);
  delay(500);
  digitalWrite(8, 0x1);
  delay(2000);
  BT.println("AT+VERSION?\n");
}

void stopAT(SoftwareSerial BT)
{
  digitalWrite(7, 0x0);
  delay(500);
  BT.println("AT+RESET\n");
}

void setup(){
  Serial.begin(9600);
  miBT.begin(38400);
  pinMode(7, 0x1);
  pinMode(8, 0x1);

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
