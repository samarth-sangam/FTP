/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author Samarth S Sangam
 */
public class Client {
    public DataOutputStream out;
    public DataInputStream in;
    BufferedReader bufread;
    BufferedWriter bufwrite;
    BufferedInputStream bufin;
    BufferedOutputStream bufout;
    Scanner s=new Scanner(System.in); 
    Socket sc;

    public Client(Socket sock) throws Exception
    {
        {
            try
            {
                    sc=sock;
                    out=new DataOutputStream(sc.getOutputStream());
                    in=new DataInputStream(sc.getInputStream());
                    bufread=new BufferedReader(new InputStreamReader(System.in));
            }
            catch(IOException ex)
            {
                    System.out.println("Error");
            }
        }
    }

    public void send(String name) throws Exception
    {
        out.writeUTF(name);
        File f=new File(name);
        if(!f.exists())
        {
            out.writeUTF("Requested file does not exist");
        }
        else
        {
            out.writeUTF("Sending file");
            JOptionPane.showMessageDialog(null, "Sending File"); 
            try (FileInputStream fi = new FileInputStream(f)) {
                int data;
                do
                {
                    data=fi.read();
                    out.write(data);
                }while(data!=-1);
                JOptionPane.showMessageDialog(null, "Successfully sent");
            }
            in.close();
            out.close();
        }
    }

    public void receive() throws Exception
    {
        String name=in.readUTF();
        String msg=in.readUTF();
        if(msg.compareTo("Requested file does not exist")==0)
            JOptionPane.showMessageDialog(null ,"Requested file does not exist\nIncorrect Filename", "File not Found", JOptionPane.ERROR_MESSAGE);
        else
        {
            JOptionPane.showMessageDialog(null, "Recieving File"); 
            File f=new File(name);
            if(f.exists())
                    JOptionPane.showMessageDialog(null ,"Requested file already exist", "Duplicate File", JOptionPane.ERROR_MESSAGE);
            else if(msg.compareTo("Sending file")==0)
            {
                try (FileOutputStream fo = new FileOutputStream(f)) {
                    int data;
                    do
                    {
                        data=in.read();
                        fo.write(data);
                    }while(data!=-1);
                }
                JOptionPane.showMessageDialog(null, "Successfully Recieved");
                in.close();
                out.close();
            }
        }
    }


    public void chat() throws IOException 
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
