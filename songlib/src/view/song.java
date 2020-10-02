//James Aikins & Christopher DeAngelis
package view;
public class song {

    String name,artist,album,year;
    public song(String name, String artist, String album, String year) {
        this.name=name;
        this.artist=artist;
        this.album = album;
        this.year = year;
    }
    public song(String name, String artist) {
        this(name,artist,"","");
    }
    
    public String getName() {
    	return name;
    }
    public String getArtist() {
    	return artist;
    }
    public String getAlbum() {
    	return album;
    }
    public String getYear() {
    	return year;
    }
    public String toString() {
    	return(name + ", " + artist);
    }
}