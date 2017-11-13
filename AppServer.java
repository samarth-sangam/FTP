import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.*;
import java.util.Scanner;
class Server extends Frame implements ActionListener,Runnable
{
 //Declarations
 Button b1;
 TextField tf;
 TextArea ta;
 ServerSocket ss;
 Socket s;
 PrintWriter pw;
 BufferedReader br;
 Thread th;
 
 public Server()
 {
  Frame f=new Frame("Server Side Chatting");//Frame for Server
  f.setLayout(new FlowLayout());//set layout
  b1=new Button("Send");//Send Button
  b1.addActionListener(this);//Add action listener to send button.
  tf=new TextField(15);
  ta=new TextArea(12,20);
  f.addWindowListener((WindowListener) new W1());//add Window Listener to the Frame
  f.add(tf);//Add TextField to the frame
  f.add(b1);//Add send Button to the frame
  f.add(ta);//Add TextArea to the frame
  try{
   ss=new ServerSocket(12000);//Socket for server
   s=ss.accept();//accepts request from client
   System.out.println(s);
   //below line reads input from InputStreamReader
   br=new BufferedReader(new InputStreamReader(s.getInputStream()));
   //below line writes output to OutPutStream
   pw=new PrintWriter(s.getOutputStream(),true);
  }catch(Exception e)
  {
  }
  th=new Thread(this);//start a new thread
  th.setDaemon(true);//set the thread as demon
  th.start();
  setFont(new Font("Arial",Font.BOLD,20));
  f.setSize(200,200);//set the size
  f.setLocation(300,300);//set the location
  f.setVisible(true);
  f.validate();
 }

    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 //method required to close the Frame on clicking "X" icon.
 private class W1 extends WindowAdapter
 {
  public void windowClosing(WindowEvent we) 
  {
   System.exit(0);
  }
 }
 //This method will called after clicking on Send button.
 public void AactionPerformed(ActionEvent ae)
 {
  pw.println(tf.getText());//write the value of textfield into PrintWriter
  tf.setText("");//clean the textfield
 }
 //Thread running as a process in background
 public void run()
 {
  while(true)
  {
   try{
    String s=br.readLine();//reads the input from textfield
    ta.append(s+"\n");//Append to TextArea
   }catch(Exception e)
   {
   }
  } 
 }}
class ftpServer extends Thread
{
	DataOutputStream out;
	DataInputStream in;
	BufferedReader bufread;
	BufferedWriter bufwrite;
	Scanner s=new Scanner(System.in);
	Socket sc;

	ftpServer(Socket sock) 
	{
		{
			try
			{
				sc=sock;
				out=new DataOutputStream(sc.getOutputStream());
				in=new DataInputStream(sc.getInputStream());
				bufread=new BufferedReader(new InputStreamReader(System.in));
				start();
			}	
			catch(Exception ex)
			{
				System.out.println("Error");
			}
		}
	}	

	void receive() throws Exception
	{
		String name=in.readUTF();
		String msg=in.readUTF();
		if(msg.compareTo("Requested file does not exist")==0)
			System.out.println("Requested file does not exist");
		else
		{
			System.out.println("Receiving file");
			File f=new File(name);
			if(f.exists())
				System.out.println("Requested file already exists");
			else if(msg.compareTo("Sending file")==0)
			{
				FileOutputStream fo=new FileOutputStream(f);
				int data;
				do
				{
					data=in.read();
					fo.write(data);
				}while(data!=-1);
				System.out.println("File succesfully received");
				in.close();
				out.close();
				fo.close();
			}
		}
	}

	void send() throws Exception
	{
		System.out.println("Enter the name of the file to be sent");
		String name=bufread.readLine();
		out.writeUTF(name);
		File f=new File(name);
		if(!f.exists())
		{
			out.writeUTF("Requested file does not exist");
			return;
		}
		else
		{
			out.writeUTF("Sending file");
			System.out.println("Sending file");;
			FileInputStream fi=new FileInputStream(f);
			int data;
			do
			{
				data=fi.read();
				out.write(data);
			}while(data!=-1);
			System.out.println("File successfully sent");
			fi.close();
			in.close();
			out.close();
		}
	}
	public void run() 
	{
		while(true)
		{
			{
				try
				{
					String cmd;
					cmd=in.readUTF();
					if(cmd.compareTo("Send")==0)
					{	
						receive();
						continue;
					}
					else if(cmd.compareTo("Receive")==0)
					{	
						send();
						continue;
					}
					else if(cmd.compareTo("Exit")==0)
					{
						System.out.println("Server closed");
						System.exit(0);
					}
					else if(cmd.compareTo("Chat")==0)
					{
  Server server = new Server();
					}
				}	
				catch(Exception ex)
				{
				}
			}
		}
	}
 }

public class AppServer 
{
	public static void main(String args[]) throws IOException
	{
		System.out.println("Server has started");
		ServerSocket sock=new ServerSocket(6003);
		while(true)
		{	
			ftpServer obj;
                    obj = new ftpServer(sock.accept());
		}
	}
}