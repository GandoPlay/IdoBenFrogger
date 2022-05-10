package frogger;

public class ClientMain {
	public static void main(String[]args) {
		System.out.println("CLIENT SIDE");
	        ClientMenu menu = new ClientMenu();
	        menu.RunMenu();
	        //wait until the client input his data
	        while(menu.isFrameVisible()) {
	        }
	        ConnectionToServer connect =   new ConnectionToServer(menu.GetName(), menu.GetIp(), menu.GetPort());
	        while(!connect.IsSocketClosed()) {
	        	
	        }
	        System.out.println("HERE");
	        ScoreMenu endFrame = new ScoreMenu(connect.isWin(), connect.GetDataBase());
	        endFrame.RunMenu(); 
	

	     

	}
}
