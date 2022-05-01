#include <string.h>
#include "bluetooth.h"
#include <string.h>

int STRING_LENGTH = 64;

const int bufferSize = 128;
char inputBuffer[bufferSize];
int bufferPointer = 0;

bool testt = false;

void get_serial_string(SoftwareSerial BT)
{
    if (Serial.available())
    {
        char in = Serial.read();
        inputBuffer[bufferPointer++] = in;
        char check[] = "1"; 

        if(inputBuffer == check)
        {
            Serial.println("> Starting AT... ");   
            bluetooth::start_AT_mode(BT);  
        }
        if(inputBuffer ==  "1\n")
        {
            Serial.println("> Stopping AT... "); 
            bluetooth::stop_AT_mode(BT);  
        }

        if(in == '\n')
        {
            Serial.print("--[NEW LINE]--");   
            bufferPointer = 0;
            testt = true;
        }
    }
    if(testt)
    {
        Serial.println(strlen(inputBuffer));
        Serial.println(inputBuffer);   

        testt = false;
    }
}


namespace command
{

    void command()
    {
        String command = "HOOOOLA";
    }
}