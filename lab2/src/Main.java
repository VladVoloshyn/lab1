import java.util.List;
import java.util.ArrayList;

// Track.java
class Track {
    public int id;
    public String title;
    public String artist;
    public String album;
    public String filePath;
    public String format;
    public int duration;

    public String getInfo() {
        return "";
    }
}

// Playlist.java
class Playlist {
    public int id;
    public String name;
    private List<Track> tracks = new ArrayList<>();

    public void addTrack(Track track) {
        // Реалізація буде додана пізніше
    }

    public void removeTrack(Track track) {
        // Реалізація буде додана пізніше
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void changeOrder(int from, int to) {
        // Реалізація буде додана пізніше
    }

    public void shuffle() {
        // Реалізація буде додана пізніше
    }

    public void clear() {
        // Реалізація буде додана пізніше
    }
}

// Repository
class PlaylistRepository {
    private List<Playlist> playlists = new ArrayList<>();

    public void addPlaylist(Playlist playlist) {
        // Реалізація буде додана пізніше
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public Playlist getPlaylistById(int id) {
        return null;
    }

    public void deletePlaylist(int id) {
        // Реалізація буде додана пізніше
    }

    public void savePlaylist(Playlist playlist) {
        // Реалізація буде додана пізніше
    }
}

// AudioDecoder.java
abstract class AudioDecoder {
    protected List<String> supportedFormats = new ArrayList<>();

    public boolean isFormatSupported(String format) {
        return false;
    }

    public Track decode(String filePath) {
        return null;
    }
}

// StreamingService.java
class StreamingService {
    private String serverUrl;
    private boolean connected;

    public void connect() {
        // ...
    }

    public void disconnect() {
        // ...
    }

    public Track getStream(String url) {
        return null;
    }

    public List<Track> search(String query) {
        return new ArrayList<>();
    }
}

// Equalizer.java
class Equalizer {
    private int bass;
    private int middle;
    private int treble;
    private String preset;

    public void setFrequency(int bass, int middle, int treble) {
        // ...
    }

    public void applyPreset(String name) {
        // ...
    }

    public void reset() {
        // ...
    }
}

// PlaybackController.java
class PlaybackController {
    private boolean isPlaying;
    private int currentIndex;
    private boolean repeatMode;
    private boolean shuffleMode;

    public void play() {
        // ...
    }

    public void pause() {
        // ...
    }

    public void stop() {
        // ...
    }

    public void next() {
        // ...
    }

    public void previous() {
        // ...
    }

    public void toggleRepeat() {
        // ...
    }

    public void toggleShuffle() {
        // ...
    }
}

// IPlayerView.java
interface IPlayerView {
    void renderTrack(Track track);
    void renderPlaylist(Playlist playlist);
    void showMessage(String message);
}

// ConsoleView.java
class ConsoleView implements IPlayerView {

    @Override
    public void renderTrack(Track track) {
        // ...
    }

    @Override
    public void renderPlaylist(Playlist playlist) {
        // ...
    }

    @Override
    public void showMessage(String message) {
        // ...
    }
}

// GuiView.java
class GuiView implements IPlayerView {

    @Override
    public void renderTrack(Track track) {
        // ...
    }

    @Override
    public void renderPlaylist(Playlist playlist) {
        // ...
    }

    @Override
    public void showMessage(String message) {
        // ...
    }
}

// MusicPlayer.java
class MusicPlayer {
    private PlaybackController controller;
    private Playlist playlist;
    private AudioDecoder decoder;
    private Equalizer equalizer;
    private StreamingService streamingService;
    private PlaylistRepository repository;
    private IPlayerView view;

    public void play() {
        // controller.play();
    }

    public void pause() {
        // controller.pause();
    }

    public void stop() {
        // controller.stop();
    }

    public void nextTrack() {
        // controller.next();
    }

    public void previousTrack() {
        // controller.previous();
    }

    public void loadTrack(String filePath) {
        // Track track = decoder.decode(filePath);
        // view.renderTrack(track);
    }

    public void playStream(String url) {
        // Track track = streamingService.getStream(url);
        // view.renderTrack(track);
        // controller.play();
    }

    public void setPlaylist(Playlist playlist) {
        // this.playlist = playlist;
        // view.renderPlaylist(playlist);
    }

    public void setEqualizerPreset(String preset) {
        // equalizer.applyPreset(preset);
    }
}

// Main.java
public class Main {
    public static void main(String[] args) {
        System.out.println("Лабораторна робота №2");
        System.out.println("Музичний програвач: структура класів спроєктована.");
    }
}