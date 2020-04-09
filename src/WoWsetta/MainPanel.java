/* MainPanel.java
 
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
import javax.swing.*;
import java.io.IOException;
//Special packet capture jpcap API utilities
import jpcap.*;
import jpcap.JpcapCaptor;
import jpcap.packet.Packet;

public class MainPanel extends JPanel
{
    public MainPanel()
    {
        //Set the Background Color
        this.setBackground(Color.WHITE);
        this.requestFocus();

        //Sets the layout for buttons and other graphics
        setLayout(new BorderLayout());
        
        //Construct the panel
        panel = new JPanel();
                 
        //Creates "Start Now"
        JButton startButton = new JButton("Start Now");
        panel.add(startButton);
        add(panel, BorderLayout.SOUTH);
        
        //Creates "Stop"
        JButton stopButton = new JButton("Stop");
        panel.add(stopButton);
        add(panel, BorderLayout.SOUTH);
        
        ButtonActionStep startAction = new ButtonActionStep(1);
        ButtonActionStep endAction = new ButtonActionStep(2);
        //Attaches action listeners to buttons
        startButton.addActionListener(startAction);
        stopButton.addActionListener(endAction);
        
        //NEW CRAP
        textArea = new JTextArea(8,40);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        
        panel.requestFocus();
        panel.repaint();
        textArea.repaint();
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
    }

     /**
     *Class is the Action listener that can be constructed to attach to 
     *Buttons
     */
    class ButtonActionStep implements ActionListener
    {
        public ButtonActionStep(int x)
        {
            perform = x;
        }
       
        public void actionPerformed(ActionEvent event)
        {
            switch (perform)
            {
                case 1:
                    try{
                        StartCapture();
                    }
                    catch (IOException e){
                        e.getStackTrace();
                        System.exit(0);
                    }
                    break;
                case 2:
                    try{
                        EndCapture();
                    }
                    catch (IOException e){
                        System.exit(0);
                    }
                    break;               
            }
        }
        private int perform;
    }
    
    public void StartCapture() throws IOException
    {
        if (isRunning == false)
        {
            r = new wowCapThread(textArea, panel);
            t = new Thread(r);
            t.start();
            isRunning = true;
        }
        else
        {
            //
        }
    }
    
    public void EndCapture() throws IOException
    {
        t.stop();
        isRunning = false;
    }
          
    //Variables
    Thread t;
    Runnable r;
    private JPanel panel;
    private JScrollPane scrollPane;
    public JTextArea textArea;
    private Packet currentPacket;
    boolean isRunning = false;
    byte tempData;
    int counter = 0, checker = 0;
}