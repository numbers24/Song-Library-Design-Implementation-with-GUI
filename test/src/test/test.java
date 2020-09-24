package test;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class test{

    public void add(song new_song, ArrayList<song> playlist)
    {
        for(song s : playlist) {
            if (s.name.compareTo(new_song.name) == 0) {

                if(s.artist.compareTo(new_song.artist) == 0) {
                    System.out.println("Error: This song already exists in your playlist.");
                    break;
                }
                if(s.artist.compareTo(new_song.artist) < 0) {
                    playlist.add(playlist.indexOf(s),new_song);
                    break;
                }
            }
            if (s.name.compareTo(new_song.name) < 0) {
                playlist.add(playlist.indexOf(s),new_song);
                break;
            }
        }
    }

    public song delete(song target, ArrayList<song> playlist)
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
    public static void main(String args[]) {

        ArrayList<song> playlist = new ArrayList<song>();
    }
        public static void writePlaylist(ArrayList<song> playlist) {
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
}
