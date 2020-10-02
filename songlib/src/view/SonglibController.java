package view;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
	
	
	private ObservableList<String> songList;
	private static ArrayList<song> songs;
	
	private Stage s;
	
	public void start(Stage mainStage) {
		s = mainStage;
		songs = readPlaylist();
		songList = FXCollections.observableArrayList();
		
		for (song s : songs) {
			songList.add(s.toString());
		}
		
		FXCollections.reverse(songList);
		listView.setItems(songList);
		mainStage.setResizable(false);
		mainStage.show();
		
		listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> showDetails(mainStage));
		listView.getSelectionModel().select(0);
		
		
	}

	private void showDetails(Stage mainStage) {
		// TODO Auto-generated method stub
		int index = songs.size()-(listView.getSelectionModel().getSelectedIndex()+1) ;
		
		name.setText(songs.get(index).getName());
		artist.setText(songs.get(index).getArtist());
		album.setText(songs.get(index).getAlbum());
		year.setText(songs.get(index).getYear());
	}
	
	public void guiAdd(ActionEvent e) {
		
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
		songList.add(index, (nSong.getName()+", "+nSong.getArtist()));
		
		
		if(songs.size()>0) {
			listView.getSelectionModel().select(index);
		}
		writePlaylist(songs);
	}
	
	public void guiEdit(ActionEvent e) {
		
		if(name.getText().equals("") || artist.getText().equals("")) {
			throwError(s,3);
			return;
		}
		int index = listView.getSelectionModel().getSelectedIndex();
		
		song nSong = new song(name.getText(),artist.getText(),album.getText(),year.getText());
		
		if(edit(nSong, songs.get(index), songs)<0) {
			throwError(s,4);
			return;
		}
		songList.set(index, (nSong.getName()+", "+nSong.getArtist()));
		
		writePlaylist(songs);
		
	}
	
	public void guiDelete(ActionEvent e) {
		int index = listView.getSelectionModel().getSelectedIndex();
		
		delete(songs.get(index));
		songList.remove(index);
		
		writePlaylist(songs);
		
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
		}
		alert.showAndWait();
	}
	
	public static int add(song new_song) //adds a new song to the playlist alphabetically
    {
		int cnt = 0;
        for(song s : songs) {
            if (s.getName().compareTo(new_song.getName()) == 0) { //if the name already exists

                if(s.getArtist().compareTo(new_song.getArtist()) == 0) { //if the artist already exists
                    if(!s.getAlbum().equals(new_song.getAlbum()) || !s.getYear().equals(new_song.getYear())){ //if song name and artist are the same but the optional fields are not
                        //"song already exists, but album or year is different, would you like to update?
                        //no: break song will not be added
                        //yes: edit(new_song,s,playlist);
                    }
                    System.out.println("Error: This song already exists in your playlist.");
                    return -1;
                }
                if(s.getArtist().compareTo(new_song.getArtist()) < 0) { //if the artists are not the same we will now add it in alphabetically

                    songs.add(songs.indexOf(s),new_song);
                    return cnt;
                }
            }
            if (s.getName().compareTo(new_song.getName()) < 0) { //if the names are not the same then we will add it in alphabetically (insertion sort)
                songs.add(songs.indexOf(s),new_song);
                return cnt;
            }
            cnt++;
        }
        songs.add(0,new_song);
        return cnt;
    }
	public static int delete(song target) //delete removes the targeted song in the playlist and will return the closest song where the next song takes precedence
    {
        for (song curr : songs)
            if(curr.getName().equals(target.getName()) && curr.getArtist().equals(target.getArtist()))
            {
                int i = songs.indexOf(curr);
                songs.remove(i);
                return 0;
            }
        System.out.println("There was no song to delete.");
        return -1;
    }
	
    public static int edit(song e, song curr, ArrayList<song> playlist) //edit edits the fields of the current song
    {
        int i = playlist.indexOf(curr);
        for (song s : playlist)
            if(playlist.indexOf(s)!=i && e.getName().equals(s.getName()) && e.getArtist().equals(s.getArtist())) { //if the edited song has a conflict with the name and artist of another song in the list
                //the edit cannot happen
                return -1;
            }
        playlist.set(i,e); //the edit has been successful
        return 0;
    }
    
    public static void writePlaylist(ArrayList<song> playlist) { //this function writes the playlist into a txt file using | as a divider
        try{
            PrintStream p = new PrintStream(new File("C:\\Users\\Kweku\\eclipse-workspace\\songlib\\src\\util\\playlist.txt"));
            for (song s : playlist)
                p.println("|" + s.getName() + "|" + s.getArtist() + "|" + s.getAlbum() + "|" + s.getYear() + "|");
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
	            Scanner scanner = new Scanner(new File("C:\\Users\\Kweku\\eclipse-workspace\\songlib\\src\\util\\playlist.txt"));
	            while(scanner.hasNextLine()) {
	                String[] p = parse(scanner.nextLine());
	                String name = p[1];
	                p = parse(p[0]);
	                String artist = p[1];
	                p = parse(p[0]);
	                String album = p[1];
	                p = parse(p[0]);
	                String year = p[1];
	                playlist.add(new song(name,artist,album,year));
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return playlist;
	    }
	 

}
