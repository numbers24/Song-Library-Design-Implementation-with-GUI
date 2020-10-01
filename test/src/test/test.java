package test;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class test{

    public void add(song new_song, ArrayList<song> playlist) //adds a new song to the playlist alphabetically
    {
        for(song s : playlist) {
            if (s.name.compareTo(new_song.name) == 0) { //if the name already exists

                if(s.artist.compareTo(new_song.artist) == 0) { //if the artist already exists
                    if(!s.album.equals(new_song.album) || !s.year.equals(new_song.year)){ //if song name and artist are the same but the optional fields are not
                        //"song already exists, but album or year is different, would you like to update?
                        //no: break song will not be added
                        //yes: edit(new_song,s,playlist);
                    }
                    System.out.println("Error: This song already exists in your playlist.");
                    break;
                }
                if(s.artist.compareTo(new_song.artist) < 0) { //if the artists are not the same we will now add it in alphabetically

                    playlist.add(playlist.indexOf(s),new_song);
                    break;
                }
            }
            if (s.name.compareTo(new_song.name) < 0) { //if the names are not the same then we will add it in alphabetically (insertion sort)
                playlist.add(playlist.indexOf(s),new_song);
                break;
            }
        }
    }
    public song delete(song target, ArrayList<song> playlist) //delete removes the targeted song in the playlist and will return the closest song where the next song takes precedence
    {
        for (song curr : playlist)
            if(curr.name.equals(target.name) && curr.artist.equals(target.artist))
            {
                int i = playlist.indexOf(curr);
                playlist.remove(i);
                curr=playlist.get(i);
                if(curr==null)
                    return playlist.get(i-1);
                return playlist.get(i);
            }
        System.out.println("There was no song to delete.");
        return playlist.get(0);
    }
    public void edit(song e, song curr, ArrayList<song> playlist) //edit edits the fields of the current song
    {
        int i = playlist.indexOf(curr);
        for (song s : playlist)
            if(playlist.indexOf(s)!=i && e.name.equals(s.name) && e.artist.equals(s.artist)) { //if the edited song has a conflict with the name and artist of another song in the list
                //the edit cannot happen
                return;
            }
        playlist.set(i,e); //the edit has been successful
    }
    public static void writePlaylist(ArrayList<song> playlist) { //this function writes the playlist into a txt file using | as a divider
        try{
            PrintStream p = new PrintStream(new File("playlist.txt"));
            for (song s : playlist)
                p.println("|" + s.name + "|" + s.artist + "|" + s.album + "|" + s.year + "|");
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
                playlist.add(new song(name,artist,album,year));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playlist;
    }
    public static void main(String args[]) {

        ArrayList<song> playlist = new ArrayList<song>();
        playlist.add(new song("Hello", "World","It's","Me"));
        playlist.add(new song("OH","MYGOSH"));
        writePlaylist(playlist);
        playlist = readPlaylist();
        writePlaylist(playlist);
    }


}
