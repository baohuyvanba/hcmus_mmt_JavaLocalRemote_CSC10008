package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;					
import javafx.stage.Stage;
import javafx.scene.Parent;
	

public class Client extends Application
{
	@Override
	public void start(Stage MainStage) throws Exception
	{
		try
		{
			//Khởi tạo Input Panel
			Parent root = FXMLLoader.load(this.getClass().getResource("/application/Remote Control.fxml"));
			MainStage.setTitle("Remote Control");
			MainStage.setScene(new Scene(root, 274, 163));
			MainStage.show();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	//MAIN
	public static void main(String args[])
	{
		launch(args);
	}
}