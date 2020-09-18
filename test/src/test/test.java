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
}
