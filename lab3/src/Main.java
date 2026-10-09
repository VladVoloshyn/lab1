import java.util.ArrayList;
import java.util.List;

// Track.java
class Track {
    private int id;
    private String title;
    private String artist;
    private String format;
    private int duration;

    public Track(int id, String title, String artist,
                 String format, int duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.format = format;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getFormat() {
        return format;
    }

    public int getDuration() {
        return duration;
    }

    public void accept(IMusicVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return artist + " - " + title;
    }
}


// Playlist.java
class Playlist {
    private String name;
    private List<Track> tracks = new ArrayList<>();

    public Playlist(String name) {
        this.name = name;
    }

    public void addTrack(Track track) {
        tracks.add(track);
    }

    public void removeTrack(Track track) {
        tracks.remove(track);
    }

    public String getName() {
        return name;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public PlaylistIterator iterator() {
        return new PlaylistIterator(tracks);
    }

    public void accept(IMusicVisitor visitor) {
        visitor.visit(this);
    }
}


// Iterator
class PlaylistIterator {
    private List<Track> tracks;
    private int position = 0;

    public PlaylistIterator(List<Track> tracks) {
        this.tracks = tracks;
    }

    public boolean hasNext() {
        return position < tracks.size();
    }

    public Track next() {
        if (!hasNext()) {
            return null;
        }

        return tracks.get(position++);
    }

    public void reset() {
        position = 0;
    }
}


// TrackRepository.java
class TrackRepository {
    private List<Track> tracks = new ArrayList<>();

    public void addTrack(Track track) {
        tracks.add(track);

        System.out.println(
                "[Repository] Збережено композицію: "
                        + track.getTitle()
        );
    }

    public Track getTrack(int id) {
        for (Track track : tracks) {
            if (track.getId() == id) {
                return track;
            }
        }

        return null;
    }

    public List<Track> getAllTracks() {
        return new ArrayList<>(tracks);
    }
}


// PlaylistRepository.java
class PlaylistRepository {
    private List<Playlist> playlists = new ArrayList<>();

    public void savePlaylist(Playlist playlist) {
        playlists.add(playlist);

        System.out.println(
                "[Repository] Плейлист збережено: "
                        + playlist.getName()
        );
    }

    public List<Playlist> getPlaylists() {
        return new ArrayList<>(playlists);
    }
}


// PlaybackService.java
class PlaybackService {
    private TrackRepository repository;
    private Track currentTrack;

    private boolean playing = false;
    private int volume = 50;

    public PlaybackService(TrackRepository repository) {
        this.repository = repository;
    }

    public void play(int trackId) {
        Track track = repository.getTrack(trackId);

        if (track == null) {
            System.out.println(
                    "[PlaybackService] Композицію не знайдено."
            );
            return;
        }

        currentTrack = track;
        playing = true;

        System.out.println(
                "[PlaybackService] Відтворення: "
                        + currentTrack
        );
    }

    public void pause() {
        if (currentTrack != null && playing) {
            playing = false;

            System.out.println(
                    "[PlaybackService] Пауза: "
                            + currentTrack
            );
        }
    }

    public void stop() {
        playing = false;
        System.out.println(
                "[PlaybackService] Відтворення зупинено."
        );
    }

    public void setVolume(int volume) {
        this.volume = volume;

        System.out.println(
                "[PlaybackService] Гучність: "
                        + volume
        );
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public int getVolume() {
        return volume;
    }

    public boolean isPlaying() {
        return playing;
    }
}


// Command
interface ICommand {
    void execute();
}

class PlayCommand implements ICommand {
    private PlaybackService player;
    private int trackId;

    public PlayCommand(PlaybackService player, int trackId) {
        this.player = player;
        this.trackId = trackId;
    }

    @Override
    public void execute() {
        player.play(trackId);
    }
}

class PauseCommand implements ICommand {
    private PlaybackService player;

    public PauseCommand(PlaybackService player) {
        this.player = player;
    }

    @Override
    public void execute() {
        player.pause();
    }
}

class StopCommand implements ICommand {
    private PlaybackService player;

    public StopCommand(PlaybackService player) {
        this.player = player;
    }

    @Override
    public void execute() {
        player.stop();
    }
}


// Memento
class PlayerMemento {
    private Track track;
    private int volume;
    private boolean playing;

    public PlayerMemento(
            Track track,
            int volume,
            boolean playing) {

        this.track = track;
        this.volume = volume;
        this.playing = playing;
    }

    public Track getTrack() {
        return track;
    }

    public int getVolume() {
        return volume;
    }

    public boolean isPlaying() {
        return playing;
    }
}

class PlayerStateManager {
    private PlayerMemento savedState;

    public void saveState(PlaybackService player) {
        savedState = new PlayerMemento(
                player.getCurrentTrack(),
                player.getVolume(),
                player.isPlaying()
        );

        System.out.println(
                "[Memento] Стан програвача збережено."
        );
    }

    public void restoreState(PlaybackService player) {
        if (savedState == null) {
            System.out.println(
                    "[Memento] Збереженого стану немає."
            );
            return;
        }

        player.setVolume(savedState.getVolume());

        if (savedState.getTrack() != null
                && savedState.isPlaying()) {

            player.play(
                    savedState.getTrack().getId()
            );
        }

        System.out.println(
                "[Memento] Стан програвача відновлено."
        );
    }
}


// Visitor
interface IMusicVisitor {
    void visit(Track track);
    void visit(Playlist playlist);
    String getResult();
}

class MusicStatisticsVisitor implements IMusicVisitor {
    private int trackCount = 0;
    private int playlistCount = 0;
    private int totalDuration = 0;

    @Override
    public void visit(Track track) {
        trackCount++;
        totalDuration += track.getDuration();
    }

    @Override
    public void visit(Playlist playlist) {
        playlistCount++;

        for (Track track : playlist.getTracks()) {
            track.accept(this);
        }
    }

    @Override
    public String getResult() {
        return "Кількість плейлистів: "
                + playlistCount
                + ", композицій: "
                + trackCount
                + ", загальна тривалість: "
                + totalDuration
                + " сек.";
    }
}


// PlaylistService.java
class PlaylistService {
    private PlaylistRepository repository;

    public PlaylistService(
            PlaylistRepository repository) {
        this.repository = repository;
    }

    public Playlist createPlaylist(String name) {
        Playlist playlist = new Playlist(name);

        repository.savePlaylist(playlist);

        System.out.println(
                "[PlaylistService] Створено плейлист: "
                        + name
        );

        return playlist;
    }

    public void addTrack(
            Playlist playlist,
            Track track) {

        playlist.addTrack(track);

        System.out.println(
                "[PlaylistService] Додано трек "
                        + track.getTitle()
                        + " до плейлиста "
                        + playlist.getName()
        );
    }
}


// Facade
class MusicPlayerFacade {
    private PlaybackService playbackService;
    private PlaylistService playlistService;

    public MusicPlayerFacade(
            PlaybackService playbackService,
            PlaylistService playlistService) {

        this.playbackService = playbackService;
        this.playlistService = playlistService;
    }

    public void playTrack(int trackId) {
        ICommand command =
                new PlayCommand(
                        playbackService,
                        trackId
                );

        command.execute();
    }

    public void pauseTrack() {
        ICommand command =
                new PauseCommand(playbackService);

        command.execute();
    }

    public void stopTrack() {
        ICommand command =
                new StopCommand(playbackService);

        command.execute();
    }

    public Playlist createPlaylist(String name) {
        return playlistService.createPlaylist(name);
    }

    public void addTrackToPlaylist(
            Playlist playlist,
            Track track) {

        playlistService.addTrack(
                playlist,
                track
        );
    }
}


// MusicPlayerUI.java
class MusicPlayerUI {
    private MusicPlayerFacade facade;

    public MusicPlayerUI(
            MusicPlayerFacade facade) {
        this.facade = facade;
    }

    public void pressPlay(int trackId) {
        System.out.println(
                "\n[UI] Натиснуто Play"
        );

        facade.playTrack(trackId);
    }

    public void pressPause() {
        System.out.println(
                "\n[UI] Натиснуто Pause"
        );

        facade.pauseTrack();
    }

    public void pressStop() {
        System.out.println(
                "\n[UI] Натиснуто Stop"
        );

        facade.stopTrack();
    }

    public Playlist createPlaylist(String name) {
        System.out.println(
                "\n[UI] Створення плейлиста: "
                        + name
        );

        return facade.createPlaylist(name);
    }

    public void addTrackToPlaylist(
            Playlist playlist,
            Track track) {

        facade.addTrackToPlaylist(
                playlist,
                track
        );
    }
}


// Client-Server
class MusicServer {
    private TrackRepository repository;

    public MusicServer(
            TrackRepository repository) {
        this.repository = repository;
    }

    public Track getTrack(int id) {
        System.out.println(
                "[Server] GET /api/tracks/"
                        + id
        );

        Track track = repository.getTrack(id);

        if (track == null) {
            System.out.println(
                    "[Server] HTTP 404 Not Found"
            );
            return null;
        }

        System.out.println(
                "[Server] HTTP 200 OK"
        );

        return track;
    }
}

class MusicClient {
    private MusicServer server;

    public MusicClient(MusicServer server) {
        this.server = server;
    }

    public Track requestTrack(int id) {
        System.out.println(
                "[Client] Запит композиції: "
                        + id
        );

        return server.getTrack(id);
    }
}


// Main.java
public class Main {
    public static void main(String[] args) {

        TrackRepository trackRepository =
                new TrackRepository();

        PlaylistRepository playlistRepository =
                new PlaylistRepository();

        Track track1 =
                new Track(
                        1,
                        "Blinding Lights",
                        "The Weeknd",
                        "MP3",
                        200
                );

        Track track2 =
                new Track(
                        2,
                        "Believer",
                        "Imagine Dragons",
                        "MP3",
                        204
                );

        Track track3 =
                new Track(
                        3,
                        "Counting Stars",
                        "OneRepublic",
                        "AAC",
                        257
                );

        trackRepository.addTrack(track1);
        trackRepository.addTrack(track2);
        trackRepository.addTrack(track3);

        PlaybackService playbackService =
                new PlaybackService(trackRepository);

        PlaylistService playlistService =
                new PlaylistService(playlistRepository);

        MusicPlayerFacade facade =
                new MusicPlayerFacade(
                        playbackService,
                        playlistService
                );

        MusicPlayerUI ui =
                new MusicPlayerUI(facade);

        ui.pressPlay(1);

        playbackService.setVolume(70);

        PlayerStateManager stateManager =
                new PlayerStateManager();

        stateManager.saveState(playbackService);

        ui.pressPause();

        stateManager.restoreState(playbackService);

        Playlist playlist =
                ui.createPlaylist("Мої улюблені");

        ui.addTrackToPlaylist(playlist, track1);
        ui.addTrackToPlaylist(playlist, track2);
        ui.addTrackToPlaylist(playlist, track3);

        System.out.println(
                "\n--- Композиції плейлиста ---"
        );

        PlaylistIterator iterator =
                playlist.iterator();

        while (iterator.hasNext()) {
            Track track = iterator.next();
            System.out.println(track);
        }

        MusicStatisticsVisitor visitor =
                new MusicStatisticsVisitor();

        playlist.accept(visitor);

        System.out.println(
                "\n--- Статистика ---"
        );

        System.out.println(
                visitor.getResult()
        );

        System.out.println(
                "\n--- Client-Server ---"
        );

        MusicServer server =
                new MusicServer(trackRepository);

        MusicClient client =
                new MusicClient(server);

        Track requestedTrack =
                client.requestTrack(2);

        if (requestedTrack != null) {
            System.out.println(
                    "[Client] Отримано: "
                            + requestedTrack
            );
        }

        ui.pressStop();
    }
}