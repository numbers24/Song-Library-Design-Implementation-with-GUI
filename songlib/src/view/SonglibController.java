//James Aikins & Christopher DeAngelis
package view;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


public class SonglibController {
	
	@FXML TextField name;
	@FXML TextField artist;
	@FXML TextField album;
	@FXML TextField year;
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	@FXML ListView<String> listView;
	
	
	private static ObservableList<String> songList;
	private static ArrayList<song> songs;
	
	private Stage s;
	
	public void start(Stage mainStage) {
		s = mainStage;
		songs = readPlaylist();
		songList = FXCollections.observableArrayList();
		
		//Creates a list of the string representation of the songs
		refreshList();
		
		//FXCollections.reverse(songList);
		listView.setItems(songList);
		mainStage.setResizable(false);
		mainStage.show();
		
		listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> showDetails(mainStage));
		listView.getSelectionModel().select(0);
		
		
	}

	//Displays the details of the selected song in the text boxes
	private void showDetails(Stage mainStage) {
		// TODO Auto-generated method stub
		int index = songs.size()-(listView.getSelectionModel().getSelectedIndex()+1) ;

		try {
			name.setText(songs.get(index).getName());
			artist.setText(songs.get(index).getArtist());
			album.setText(songs.get(index).getAlbum());
			year.setText(songs.get(index).getYear());
		} catch (IndexOutOfBoundsException e){}
	}
	
	public void guiAdd(ActionEvent e) {
		
		//popup asks if you want to continue
		if(!confirm(s,"Add"))
			return;
		//Throws an error if the name or artist section is blank
		if(name.getText().equals("") || artist.getText().equals("")) {
			throwError(s,1);
			return;
		}
		song nSong = new song(name.getText(),artist.getText(),album.getText(),year.getText());
		
		int index = add(nSong);
		if(index<0) {
			throwError(s,2);
			return;
		}
		
		//songList.add(songs.size()-(index+1), (nSong.getName()+", "+nSong.getArtist()));
		
		refreshList();
		
		if(songs.size()>0) {
			listView.getSelectionModel().select(index);
		}
		writePlaylist(songs);
	}
	
	public void guiEdit(ActionEvent e) {
		//Throws an error if the name or artist section is blank
		if(!confirm(s,"Edit"))
			return;
		try {
			if (name.getText().equals("") || artist.getText().equals("")) {
				throwError(s, 3);
				return;
			}
			int index = listView.getSelectionModel().getSelectedIndex();

			song nSong = new song(name.getText(), artist.getText(), album.getText(), year.getText());

			if (edit(nSong, songs.get(songs.size()-(index+1)), songs) < 0) {
				throwError(s, 4);
				return;
			}
			//songList.set(songs.size()-(index+1), (nSong.getName() + ", " + nSong.getArtist()));
			refreshList();

			writePlaylist(songs);
		} catch (IndexOutOfBoundsException err) {
			//throw error to the user that they have no song selected to edit
			throwError(s,6);
		}

	}
	
	public void guiDelete(ActionEvent e) {
		try {
			//Throws an error if the name or artist section is blank
			if (!confirm(s, "Delete"))
				return;
			int index = listView.getSelectionModel().getSelectedIndex();

			delete(songs.get(songs.size() - (index + 1)));
			//songList.remove(index);
			refreshList();
			writePlaylist(songs);
		} catch (IndexOutOfBoundsException err) {throwError(s,5);}
	}
	public boolean confirm(Stage mainStage, String option){
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.initOwner(mainStage);
		alert.setTitle("Songlib");
		alert.setHeaderText("Confirmation");
		alert.setContentText("Are You Sure You Wish to " + option + "?");
		ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
			return true;
		else
			return false;
	}
	public void throwError(Stage mainStage, int err) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(mainStage);
		alert.setTitle("Songlib");
		
		switch (err) {
			case 1:
				alert.setHeaderText("Add Error");
				alert.setContentText("You must include name and artist when adding new song");
				break;
			case 2:
				alert.setHeaderText("Add Error");
				alert.setContentText("The song you are attempting to add already exists");
				break;
			case 3:
				alert.setHeaderText("Edit Error");
				alert.setContentText("The song must include a name and artist");
				break;
			case 4:
				alert.setHeaderText("Edit Error");
				alert.setContentText("You must include name and artist when adding new song");
				break;
			case 5:
				alert.setHeaderText("Edit Error");
				alert.setContentText("This song does not contain an album or year");
				break;
			case 6:
				alert.setHeaderText("Edit Error");
				alert.setContentText("You have no selected a song to edit");
				break;
		}
		alert.showAndWait();
	}
	
	public static int add(song new_song) //adds a new song to the playlist alphabetically
    {
		int cnt = 0;
        for(song s : songs) {
            if (s.getName().toLowerCase().compareTo(new_song.getName().toLowerCase()) == 0) { //if the name already exists

                if(s.getArtist().toLowerCase().compareTo(new_song.getArtist().toLowerCase()) == 0) { //if the artist already exists
                    return -1;
                }
                if(s.getArtist().toLowerCase().compareTo(new_song.getArtist().toLowerCase()) < 0) { //if the artists are not the same we will now add it in alphabetically
                    songs.add(songs.indexOf(s),new_song);
                    return cnt;
                }
            }
            if (s.getName().toLowerCase().compareTo(new_song.getName().toLowerCase()) < 0) { //if the names are not the same then we will add it in alphabetically (insertion sort)
            	songs.add(songs.indexOf(s),new_song);
                return cnt;
            }
            cnt++;
        }
        songs.add(songs.size(),new_song);
        return cnt;
    }
	public static int delete(song target) //delete removes the targeted song in the playlist and will return the closest song where the next song takes precedence
    {
        for (song curr : songs)
            if(curr.getName().equalsIgnoreCase(target.getName()) && curr.getArtist().equalsIgnoreCase(target.getArtist()))
            {
                int i = songs.indexOf(curr);
                songs.remove(i);
                return 0;
            }
        return -1;
    }
	
    public static int edit(song e, song curr, ArrayList<song> playlist) //edit edits the fields of the current song
    {
    	int err;
		delete(curr); //delete the current song
		err = add(e);
    	if(err < 0){//add the edited song, add detects conflicts with other songs
			add(curr); //add curr back if e fails
    		return err+1;
		}
		return 0;
    }
    
    public static void writePlaylist(ArrayList<song> playlist) { //this function writes the playlist into a txt file using | as a divider
        try{
            PrintStream p = new PrintStream(new File("playlist.txt"));
            for (int i=playlist.size()-1;i>=0;i--)
                p.println("|" + playlist.get(i).getName() + "|" + playlist.get(i).getArtist() + "|" + playlist.get(i).getAlbum() + "|" + playlist.get(i).getYear() + "|");
            p.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
	
	public static String[] parse(String s){//gets the content between |...| and updates the string upon completion
        s = s.substring(1);
        int next = s.indexOf('|');
        if(next==0)
            return new String[] {s.substring(0),""}; //returns null if ||
        else
            return new String[] {s.substring(next), s.substring(0,next)}; //returns |...|
    }
	
	 public static ArrayList<song> readPlaylist() {
	        ArrayList<song> playlist = new ArrayList<song>();
	        try {
	            Scanner scanner = new Scanner(new File("playlist.txt"));
	            while(scanner.hasNextLine()) {
	                String[] p = parse(scanner.nextLine());
	                String name = p[1];
	                p = parse(p[0]);
	                String artist = p[1];
	                p = parse(p[0]);
	                String album = p[1];
	                p = parse(p[0]);
	                String year = p[1];
	                playlist.add(0,new song(name,artist,album,year));
	            }
	        } catch (IOException e) {}
	        return playlist;
	  }
	 
	 public static void refreshList() {
		 songList.clear();
		 for (song s : songs) {
				songList.add(0,s.toString());
		}
	 }
}