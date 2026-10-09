import java.util.ArrayList;
import java.util.List;

// Track.java
class Track {

    private String title;
    private String artist;
    private int duration;

    public Track(String title, String artist, int duration) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return artist + " - " + title
                + " (" + duration + " сек.)";
    }
}


// IIterator.java
interface IIterator {

    boolean hasNext();

    Track next();
}


// IPlaylistCollection.java
interface IPlaylistCollection {

    IIterator createIterator();
}


// PlaylistIterator.java
class PlaylistIterator implements IIterator {

    private List<Track> tracks;
    private int currentIndex = 0;

    public PlaylistIterator(List<Track> tracks) {
        this.tracks = tracks;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < tracks.size();
    }

    @Override
    public Track next() {

        if (hasNext()) {
            Track track = tracks.get(currentIndex);
            currentIndex++;
            return track;
        }

        return null;
    }
}


// Playlist.java
class Playlist implements IPlaylistCollection {

    private List<Track> tracks = new ArrayList<>();

    public void addTrack(Track track) {
        tracks.add(track);
    }

    @Override
    public IIterator createIterator() {
        return new PlaylistIterator(tracks);
    }
}


// MusicPlayer.java
class MusicPlayer {

    public void playPlaylist(
            IPlaylistCollection playlist) {

        System.out.println(
                "Відтворення плейлиста:"
        );

        System.out.println(
                "----------------------------------"
        );

        IIterator iterator =
                playlist.createIterator();

        while (iterator.hasNext()) {

            Track track = iterator.next();

            System.out.println(
                    "Відтворюється: " + track
            );
        }

        System.out.println(
                "----------------------------------"
        );
    }
}


// Main.java
public class Main {

    public static void main(String[] args) {

        Playlist playlist =
                new Playlist();

        playlist.addTrack(
                new Track(
                        "Blinding Lights",
                        "The Weeknd",
                        200
                )
        );

        playlist.addTrack(
                new Track(
                        "Believer",
                        "Imagine Dragons",
                        204
                )
        );

        playlist.addTrack(
                new Track(
                        "Counting Stars",
                        "OneRepublic",
                        257
                )
        );

        playlist.addTrack(
                new Track(
                        "Shape of You",
                        "Ed Sheeran",
                        233
                )
        );

        MusicPlayer player =
                new MusicPlayer();

        player.playPlaylist(playlist);
    }
}