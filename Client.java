import java.net.*;
import java.util.Scanner;
import java.io.*;

class ftpclient
{
	DataOutputStream out;
	DataInputStream in;
	BufferedReader bufread;
	BufferedWriter bufwrite;
	BufferedInputStream bufin;
	BufferedOutputStream bufout;
	Scanner s=new Scanner(System.in); 
	Socket sc;
	
	ftpclient(Socket sock) throws Exception
	{
		{
			try
			{
				sc=sock;
				out=new DataOutputStream(sc.getOutputStream());
				in=new DataInputStream(sc.getInputStream());
				bufread=new BufferedReader(new InputStreamReader(System.in));
			}
			catch(Exception ex)
			{
				System.out.println("Error");
			}
		}
	}
	
	void menu() throws Exception
	{
		System.out.println("1:Send");
		System.out.println("2:Receive");
		System.out.println("3:Chat");
		System.out.println("4:Exit");
		int opt;
		System.out.println("Enter the service required");
		opt=s.nextInt();
		switch(opt)
		{
			case 1:out.writeUTF("Send");
				   send();
				break;
			case 2:out.writeUTF("Receive");
				   receive();
				break;
			case 3:out.writeUTF("Chat");
				   chat();
				break;
			case 4:out.writeUTF("Exit");
				   System.out.println("Client closed");
				   System.exit(0);
				break;
		}
	}
	
	void send() throws Exception
	{
		String name=in.readUTF();
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

	void receive() throws Exception
	{
		System.out.println("Enter the name of the file required");
		String name=bufread.readLine();
		out.writeUTF(name);
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
				fo.close();
				System.out.println("File succesfully received");
				in.close();
				out.close();
			}
		}
	}
	
	void chat() throws IOException 
	{
		Boolean con;
		out.writeBoolean(false);
		while(true)
		{
			con=in.readBoolean();
			if(con)
			{
				String msgi=in.readUTF();
				System.out.println("Server: "+msgi);
				out.writeBoolean(false);
			}
			else
			{
				out.writeBoolean(true);
				System.out.print("Client: ");
				String msgo=bufread.readLine();
				out.writeUTF(msgo);
			}
		}
	}
}	

class Client
{
	public static void main(String args[]) throws Exception
	{
		while(true)
		{	
			Socket sock=new Socket("127.0.0.1",6003);
			ftpclient obj=new ftpclient(sock);
			obj.menu();	
		}
	}
}