package frogger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JFrame;


public class ConnectionToServer {
    private final int PORT ;//= 5555;
    private final String IP;// = "127.0.0.1";// "192.168.1.105";
    private static Socket socket;
    private int i=0;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private String name;
    private boolean win=false;
    ArrayList<DataBaseObject> db = new ArrayList<DataBaseObject>();
	private JFrame frame;
    public ConnectionToServer(String playerName,String IP,int PORT) {
        this.IP=IP;
        this.PORT=PORT;
        name = playerName;
        
        System.out.println("Trying to connect to server");
        StartConnectionToServer();
        System.out.println("Connect to server");
        try {
            
    		 
            SendingDataToServer(playerName);
            DisplayClient Game = new DisplayClient();
         
                	 
                    
                	 frame= new JFrame(name); 		
            		frame.add(Game);	
            		frame.setSize(Globals.WIDTH,Globals.HEIGHT);
                  	frame.setResizable(false);
            		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            		frame.setVisible(true);
            		
             
            StartReceivingData2(Game);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (socket != null) {
                    System.out.println("closing the socket.");
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        

    }

    public static void SendingDataToServer(String data) {
    	
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    private void StartReceivingData2( DisplayClient Game)  throws IOException {
    	
    
        try {
         
        	 InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
            	String dataFromServer = "";
            	dataFromServer  = bufferedReader.readLine();
            	if(dataFromServer==null) {
            		break;
            		
            	}
          
            	if(dataFromServer.contains("TIME")) {
            		LocalTime time = LocalTime.parse(dataFromServer.substring(4));
            		while(time.isAfter(LocalTime.now())) {
            		}
            		Game.StartGame("start");
            		
            	}
            	 if(dataFromServer.equals("Red")) {
            		Game.SetColor("Red");
            	}
            	else if(dataFromServer.equals("Blue")) {
            		Game.SetColor("Blue");
            	}
            	else if(dataFromServer.equals("WON")) {
            		this.win = true;
            		socket.close();
            	}
            	else if(dataFromServer.equals("LOST")) {
            		this.win = false;
            		socket.close();
            	}
            
            	else if(dataFromServer.equals("right")) {
            		Game.MoveOtherFrog(KeyEvent.VK_RIGHT);
            		//Game.MoveFrog(KeyEvent.VK_D);
            	}
            	else if(dataFromServer.equals("left")) {
            		Game.MoveOtherFrog(KeyEvent.VK_LEFT);
            	}
            	else if(dataFromServer.equals("down")) {
            		Game.MoveOtherFrog(KeyEvent.VK_DOWN);
            	}
            	else if(dataFromServer.equals("up")) {
            		Game.MoveOtherFrog(KeyEvent.VK_UP);
            	}
            	else if(dataFromServer.equals("DEAD")) {
            
            		Game.KillOtherFrog();
            	}
            	else if(dataFromServer.equals("NEXTLEVEL")) {
            		Game.AddScoreAndResetFrogs();
            	}
            	else if(dataFromServer.contains("DATABASE")) {
       			
       			String[] arr = dataFromServer.split("@");
       			for(int i =1;i<arr.length;i++) {
       				String [] player = arr[i].split(",");
       				System.out.println(player[0]);
       				db.add(new DataBaseObject(player[0], Integer.parseInt(player[1])));
       			}

            	}

            }
          
        } catch (IOException e) {
            System.out.println("There was an error receiving data.");
    
        }
       frame.setVisible(false);
       frame.dispose();
    
}
    public  ArrayList<DataBaseObject> GetDataBase(){
    	return db;
    }
    public boolean IsSocketClosed() {
    	System.out.println(socket.isClosed());
    	return socket.isClosed();
    }
    

    private void StartConnectionToServer() {
        try {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
            socket = new Socket(IP, PORT);
            
            try {
    			inputStream = socket.getInputStream();
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
        } catch (IOException e) {
            StartConnectionToServer();
        }
    }

	public boolean isWin() {
		return win;
	}

}
