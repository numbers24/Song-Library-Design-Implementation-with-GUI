package songlib.view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import songlib.app.song;

public class SonglibController {
	
	@FXML TextField name;
	@FXML TextField artist;
	@FXML TextField album;
	@FXML TextField year;
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	@FXML ListView<String> listView;
	
	private ObservableList<song> songList;
	
	public void start() {
		
	}

}
