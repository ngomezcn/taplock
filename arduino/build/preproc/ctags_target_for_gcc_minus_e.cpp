# 1 "d:\\TapLock\\arduino\\main.ino"

# 3 "d:\\TapLock\\arduino\\main.ino" 2
# 4 "d:\\TapLock\\arduino\\main.ino" 2
# 5 "d:\\TapLock\\arduino\\main.ino" 2


// HC-05 Bluetooth
SoftwareSerial miBT(5, 6);
char incomingByte;

void setup(){
  Serial.begin(9600);
  miBT.begin(38400);


  pinMode(7, 0x1);
  pinMode(8, 0x1);
  pinMode(13, 0x1);
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
# 45 "d:\\TapLock\\arduino\\main.ino"
  get_serial_string(miBT);
  if (miBT.available())
  {
    Serial.write(miBT.read());
  }
}
