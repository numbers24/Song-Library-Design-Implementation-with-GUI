package songlib.app;
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
}
