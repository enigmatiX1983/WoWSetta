/* wowPacket.java
 
WoWsetta, Copyright (c) 2006 Robert Martin

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package WoWsetta;

//Normal imports
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import java.util.Arrays;
//Special packet capture jpcap API utilities
import jpcap.*;
import jpcap.JpcapCaptor;
import jpcap.packet.Packet;

/*Wowpacket is the data structure that holds all of the relevant text data part
 *of the WoW packet (does not include movement/position data that sometimes
 *appears in the packet*/
public class wowPacket
{      
    public wowPacket(Packet p, int s) 
    {
        wPacket = p;
        startValue = s;
        
        langChan = new byte[5];
        UID1 = new byte[3];
        padding1 = new byte[5];
        UID2 = new byte[3];
        padding2 = new byte[5];
        zeroPad = new byte[3];
                
        for (i=0; i<5; i++)
        {
            langChan[i] = wPacket.data[i+startValue];
        }
        for (i=0; i<3; i++)
        {
            UID1[i] = wPacket.data[i+(startValue+5)];
        }
        for (i=0; i<5; i++)
        {
            padding1[i] = wPacket.data[i+(startValue+8)];
        }
        for (i=0; i<3; i++)
        {
            UID2[i] = wPacket.data[i+(startValue+13)];
        }
        for (i=0; i<5; i++)
        {
            padding2[i] = wPacket.data[i+(startValue+16)];
        }
        
       
        if ((int)wPacket.data[(startValue+21)] > 0 || (int)wPacket.data[(startValue+21)] < 255)
        {
            length = (int)wPacket.data[(startValue+21)];
            
            /*Extract the length of the actual message and create a message byte 
             *array of the proper size; this casts as char because in java it is
             *equivalent to an unsigned int*/
            message = new byte[length];
            System.out.println("Packet Data Length: " + wPacket.data.length + "");
            System.out.println("Message Length: " + length +"\n");

            for (i=0; i<length; i++)
            {
                message[i] = wPacket.data[i+(startValue+25)];
            }

            messageString = new String(message);
            
            for (i=0; i<3; i++)
            {
                zeroPad[i] = wPacket.data[i+(startValue+22)];
            }
        }
        else            
            length = 0;        
    }
        

    private int i;
    
    public int length;
    public int startValue;
    //Data structures are in order
    public byte langChan[];
    public byte UID1[];
    public byte padding1[];
    public byte UID2[];
    public byte padding2[];
    public byte zeroPad[];
    public byte message[];
    public String messageString, UIDstring;
    Packet wPacket;
}
