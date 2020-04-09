/* wowCapThread.java
 
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
import java.util.Date;
//Special packet capture jpcap API utilities
import jpcap.*;
import jpcap.JpcapCaptor;
import jpcap.packet.Packet;

public class wowCapThread implements Runnable
{
    public wowCapThread(JTextArea textArea, JPanel inPanel)
    {
        t = textArea;
        //panel = panel;
        packetDataCode = new byte[5];
        tempData = new byte[2];
        padding1 = new byte[8];
        padding2 = new byte[8];
        zeroPad = new byte[3];
        
        //initialize Jpcap
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();                 
        
        try
        {
            //captor=JpcapCaptor.openDevice(devices[1],2000,true,5000);
            captor=JpcapCaptor.openDevice(devices[0], 65535, false, 20);
        }
        catch (IOException e)
        {
            e.printStackTrace(); 
            //System.exit(0);
        }
    }
    
    public void run()
    {                
        //INFINITE LOOP TO CAPTURE PACKETS UNTIL THREAD DIES
        for(;;)
        {
            p = captor.getPacket();
            System.out.println("" + p.len);
            
            if (p.data.length > 10) //To make sure the race bytes exist in the first place
                                    //Less than 10 means packet is too small anyway
            {                              
                //Fill code selection container for race tell/yell by finding it
                //Uses a sliding window technique
                for (i=0; i < p.data.length-25; i++)
                {
                    packetDataCode[0] = p.data[i];
                    packetDataCode[1] = p.data[i+1];
                    packetDataCode[2] = p.data[i+2];
                    packetDataCode[3] = p.data[i+3];       
                    packetDataCode[4] = p.data[i+4];
                    
                    padding1[0] = p.data[i+5];
                    padding1[1] = p.data[i+6];
                    padding1[2] = p.data[i+7];
                    padding1[3] = p.data[i+8];
                    padding1[4] = p.data[i+9];
                    padding1[5] = p.data[i+10];
                    padding1[6] = p.data[i+11];
                    padding1[7] = p.data[i+12];
                    
                    padding2[0] = p.data[i+13];
                    padding2[1] = p.data[i+14];
                    padding2[2] = p.data[i+15];
                    padding2[3] = p.data[i+16];
                    padding2[4] = p.data[i+17];
                    padding2[5] = p.data[i+18];
                    padding2[6] = p.data[i+19];
                    padding2[7] = p.data[i+20];
                    
                    zeroPad[0] = p.data[i+22];
                    zeroPad[1] = p.data[i+23];
                    zeroPad[2] = p.data[i+24];
                    
                    
                    //This section begins the case scenarios for the packetDataCode checks which
                    //Denotes race and say/yell
                    //Alliance cases start here
                    if (Arrays.equals(packetDataCode, allianceCodes[0]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Common <say>", i);
                        break;
                    }
                    else if (Arrays.equals(packetDataCode, allianceCodes[1]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Common <yell>", i);
                        break;
                    }
                    else if (Arrays.equals(packetDataCode, allianceCodes[2]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Gnomish <say>", i);
                        break;
                    }
                    else if (Arrays.equals(packetDataCode, allianceCodes[3]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Gnomish <yell>", i);
                        break;
                    }
                    else if (Arrays.equals(packetDataCode, allianceCodes[4]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Darnassian <say>", i);
                        break;
                    }
                    else if (Arrays.equals(packetDataCode, allianceCodes[5]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Darnassian <yell>", i);
                        break;
                    }
                    else if (Arrays.equals(packetDataCode, allianceCodes[6]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Dwarvish <say>", i);
                        break;
                    }
                    else if (Arrays.equals(packetDataCode, allianceCodes[7]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Dwarvish <yell>", i);
                        break;
                    }
                    
                    //Horde cases start here
                    else if (Arrays.equals(packetDataCode, hordeCodes[0]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Orcish <say>", i);
                        break;
                    }
                    else if (Arrays.equals(packetDataCode, hordeCodes[1]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Orcish <yell>", i);
                        break;
                    }
                    else if (Arrays.equals(packetDataCode, hordeCodes[2]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Gutterspeak <say>", i);
                        break;
                    }
                    else if (Arrays.equals(packetDataCode, hordeCodes[3]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Gutterspeak <yell>", i);
                        break;
                    }
                    else if (Arrays.equals(packetDataCode, hordeCodes[4]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Troll <say>", i);
                        break;
                    }
                    else if (Arrays.equals(packetDataCode, hordeCodes[5]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Troll <yell>", i);
                        break;
                    }
                    else if (Arrays.equals(packetDataCode, hordeCodes[6]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Taurahe <say>", i);
                        break;
                    }
                    else if (Arrays.equals(packetDataCode, hordeCodes[7]) && Arrays.equals(padding1, padding2) && Arrays.equals(zeroPad, zPad))
                    {
                        textOutput("Taurahe <yell>", i);
                        break;
                    }
                }
            }
        }
    }
    
    public void textOutput(String raceString, int contextStart)
    {
        Date tStamp = new Date();
        wPacket = new wowPacket(p, contextStart);
        
        timeStamp = tStamp.toString();
    
        //One final check
        if ((int)wPacket.length == 0)
            return;
        else
        {
            t.append("<" + timeStamp + "> ");
            t.append(raceString);
            t.append(" <" + (int)wPacket.UID1[0] + (int)wPacket.UID1[1] + (int)wPacket.UID1[2] + "> ");
            t.append(wPacket.messageString.substring(0, (int)wPacket.length - 1) + "\n");
        }
    }
       
    //WoW speak variables
    byte packetDataCode[];
    byte tempData[];
    //Variables used to check whether or not it's a wow text packet
    byte padding1[];
    byte padding2[];
    byte zeroPad[];
        
    /*These are the Alliance race/speech context byte arrays
     *Pretty self explanatory--they appear in the packet's data
     *section starting at packet.data[3] and go to packet.data[7]*/
    byte allianceCodes[][] = {
        { 0x00, 0x07, 0x00, 0x00, 0x00 }, //Human - Say
        { 0x05, 0x07, 0x00, 0x00, 0x00 }, //Human - Yell
        { 0x00, 0x0D, 0x00, 0x00, 0x00 }, //Gnome - Say
        { 0x05, 0x0D, 0x00, 0x00, 0x00 }, //Gnome - Yell
        { 0x00, 0x02, 0x00, 0x00, 0x00 }, //NE - Say
        { 0x05, 0x02, 0x00, 0x00, 0x00 }, //NE - Yell
        { 0x00, 0x06, 0x00, 0x00, 0x00 }, //Dwarf - Say
        { 0x05, 0x06, 0x00, 0x00, 0x00 } //Dwarf - Yell
    };

    /*These are the Alliance race/speech context byte arrays
     *Pretty self explanatory--they appear in the packet's data
     *section starting at packet.data[3] and go to packet.data[7]*/
    byte hordeCodes[][] = {
        { 0x00, 0x01, 0x00, 0x00, 0x00 }, //Orc - Say
        { 0x05, 0x01, 0x00, 0x00, 0x00 }, //Orc - Yell
        { 0x00, 0x0E, 0x00, 0x00, 0x00 }, //Troll - Say
        { 0x05, 0x0E, 0x00, 0x00, 0x00 }, //Troll - Yell
        { 0x00, 0x21, 0x00, 0x00, 0x00 }, //UD - Say
        { 0x05, 0x21, 0x00, 0x00, 0x00 }, //UD - Yell
        { 0x00, 0x03, 0x00, 0x00, 0x00 }, //Tauren - Say
        { 0x05, 0x03, 0x00, 0x00, 0x00 } //Tauren - Yell
    };
       
    //Zero padding before the text starts
    byte zPad[] = { 0x00, 0x00, 0x00 };
        
    //Other control variables
    int i;
    JTextArea t;
    JpcapCaptor captor;
    JPanel panel;
    Packet p;
    String temp;
    String timeStamp;
    wowPacket wPacket;
   
    //Fonts
    Font fontNormal = new Font("Serif", Font.PLAIN, 12);
    Font fontBold = new Font("Serif", Font.BOLD, 12);
    Font fontItalic = new Font("Serif", Font.ITALIC, 12);
}
