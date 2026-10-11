import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


// ======================================================
// DOMAIN
// ======================================================

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


// Playlist.java
class Playlist {

    private String name;
    private List<Track> tracks;

    public Playlist(String name, List<Track> tracks) {
        this.name = name;
        this.tracks = new ArrayList<>(tracks);
    }

    public String getName() {
        return name;
    }

    public List<Track> getTracks() {
        return new ArrayList<>(tracks);
    }

    @Override
    public String toString() {
        return name + " (" + tracks.size() + " треків)";
    }
}


// ======================================================
// SERVICE CONTRACTS / MIDDLEWARE
// ======================================================

// IMusicLibraryService.java
interface IMusicLibraryService {

    List<Track> getAllTracks();

    Track findTrack(String title);
}


// IPlaylistService.java
interface IPlaylistService {

    void savePlaylist(String name, List<Track> tracks);

    Playlist getPlaylist(String name);
}


// IPlaybackService.java
interface IPlaybackService {

    void playTrack(Track track);

    void pausePlayback();

    void stopPlayback();
}


// ======================================================
// DATA ACCESS
// ======================================================

// TrackRepository.java
class TrackRepository {

    private final List<Track> tracks =
            new ArrayList<>();

    public TrackRepository() {

        tracks.add(
                new Track(
                        "Blinding Lights",
                        "The Weeknd",
                        200
                )
        );

        tracks.add(
                new Track(
                        "Believer",
                        "Imagine Dragons",
                        204
                )
        );

        tracks.add(
                new Track(
                        "Counting Stars",
                        "OneRepublic",
                        257
                )
        );

        tracks.add(
                new Track(
                        "Shape of You",
                        "Ed Sheeran",
                        233
                )
        );
    }

    public List<Track> findAll() {
        return new ArrayList<>(tracks);
    }

    public Track findByTitle(String title) {

        for (Track track : tracks) {

            if (track.getTitle()
                    .equalsIgnoreCase(title)) {

                return track;
            }
        }

        return null;
    }
}


// ======================================================
// SERVER SERVICE IMPLEMENTATIONS
// ======================================================

// MusicLibraryServiceImpl.java
class MusicLibraryServiceImpl
        implements IMusicLibraryService {

    private TrackRepository repository;

    public MusicLibraryServiceImpl(
            TrackRepository repository) {

        this.repository = repository;
    }

    @Override
    public List<Track> getAllTracks() {

        System.out.println(
                "[MusicLibraryService] Отримання списку треків"
        );

        return repository.findAll();
    }

    @Override
    public Track findTrack(String title) {

        System.out.println(
                "[MusicLibraryService] Пошук треку: "
                        + title
        );

        return repository.findByTitle(title);
    }
}


// PlaylistServiceImpl.java
class PlaylistServiceImpl
        implements IPlaylistService {

    private IMusicLibraryService libraryService;

    private final List<Playlist> playlists =
            new ArrayList<>();

    public PlaylistServiceImpl(
            IMusicLibraryService libraryService) {

        this.libraryService = libraryService;
    }

    @Override
    public void savePlaylist(
            String name,
            List<Track> tracks) {

        Playlist playlist =
                new Playlist(name, tracks);

        playlists.add(playlist);

        System.out.println(
                "[PlaylistService] Плейлист збережено: "
                        + name
        );
    }

    @Override
    public Playlist getPlaylist(String name) {

        for (Playlist playlist : playlists) {

            if (playlist.getName()
                    .equalsIgnoreCase(name)) {

                return playlist;
            }
        }

        return null;
    }
}


// ======================================================
// FACADE
// ======================================================

// MusicFacade.java
class MusicFacade {

    private Track currentTrack;
    private boolean isPlaying;

    public void play(Track track) {

        currentTrack = track;
        isPlaying = true;

        System.out.println(
                "[MusicFacade] Відтворюється: "
                        + track
        );
    }

    public void pause() {

        if (currentTrack == null) {

            System.out.println(
                    "[MusicFacade] Немає активного треку"
            );

            return;
        }

        isPlaying = false;

        System.out.println(
                "[MusicFacade] Відтворення призупинено"
        );
    }

    public void stop() {

        if (currentTrack == null) {

            System.out.println(
                    "[MusicFacade] Немає активного треку"
            );

            return;
        }

        isPlaying = false;
        currentTrack = null;

        System.out.println(
                "[MusicFacade] Відтворення зупинено"
        );
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}


// PlaybackServiceImpl.java
class PlaybackServiceImpl
        implements IPlaybackService {

    private IMusicLibraryService libraryService;
    private MusicFacade facade;

    public PlaybackServiceImpl(
            IMusicLibraryService libraryService,
            MusicFacade facade) {

        this.libraryService = libraryService;
        this.facade = facade;
    }

    @Override
    public void playTrack(Track track) {

        if (track == null) {

            System.out.println(
                    "[PlaybackService] Трек не знайдено"
            );

            return;
        }

        System.out.println(
                "[PlaybackService] Запуск треку"
        );

        facade.play(track);
    }

    public void playTrackByTitle(String title) {

        Track track =
                libraryService.findTrack(title);

        playTrack(track);
    }

    @Override
    public void pausePlayback() {

        System.out.println(
                "[PlaybackService] Пауза"
        );

        facade.pause();
    }

    @Override
    public void stopPlayback() {

        System.out.println(
                "[PlaybackService] Зупинка"
        );

        facade.stop();
    }
}


// ======================================================
// SERVER
// ======================================================

// MusicServer.java
class MusicServer implements Runnable {

    private final int port;

    private IMusicLibraryService libraryService;
    private IPlaylistService playlistService;
    private IPlaybackService playbackService;

    private volatile boolean running = true;

    public MusicServer(
            int port,
            IMusicLibraryService libraryService,
            IPlaylistService playlistService,
            IPlaybackService playbackService) {

        this.port = port;
        this.libraryService = libraryService;
        this.playlistService = playlistService;
        this.playbackService = playbackService;
    }

    @Override
    public void run() {

        try (ServerSocket serverSocket =
                     new ServerSocket(port)) {

            System.out.println(
                    "[MusicServer] Сервер запущено на порту "
                            + port
            );

            while (running) {

                System.out.println(
                        "[MusicServer] Очікування клієнта..."
                );

                try (Socket clientSocket =
                             serverSocket.accept()) {

                    System.out.println(
                            "[MusicServer] Клієнт підключився: "
                                    + clientSocket.getInetAddress()
                    );

                    handleClient(clientSocket);
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "[MusicServer] Помилка сервера: "
                            + e.getMessage()
            );
        }

        System.out.println(
                "[MusicServer] Сервер завершив роботу."
        );
    }


    private void handleClient(
            Socket socket) throws IOException {

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()
                        )
                );

        PrintWriter writer =
                new PrintWriter(
                        socket.getOutputStream(),
                        true
                );

        String request;

        while ((request = reader.readLine()) != null) {

            System.out.println(
                    "[MusicServer] Отримано: "
                            + request
            );

            String response =
                    processRequest(request);

            writer.println(response);

            if (request.equalsIgnoreCase(
                    "SHUTDOWN")) {

                running = false;
                break;
            }
        }
    }


    private String processRequest(
            String request) {

        // GET_TRACKS
        if (request.equalsIgnoreCase(
                "GET_TRACKS")) {

            List<Track> tracks =
                    libraryService.getAllTracks();

            StringBuilder result =
                    new StringBuilder();

            for (Track track : tracks) {

                if (result.length() > 0) {
                    result.append(" | ");
                }

                result.append(track);
            }

            return "TRACKS: " + result;
        }


        // PLAY <title>
        if (request.toUpperCase()
                .startsWith("PLAY ")) {

            String title =
                    request.substring(5).trim();

            Track track =
                    libraryService.findTrack(title);

            if (track == null) {

                return "ERROR: Трек не знайдено";
            }

            playbackService.playTrack(track);

            return "OK: Відтворюється "
                    + track;
        }


        // PAUSE
        if (request.equalsIgnoreCase(
                "PAUSE")) {

            playbackService.pausePlayback();

            return "OK: Відтворення призупинено";
        }


        // STOP
        if (request.equalsIgnoreCase(
                "STOP")) {

            playbackService.stopPlayback();

            return "OK: Відтворення зупинено";
        }


        // CREATE_PLAYLIST <name>
        if (request.toUpperCase()
                .startsWith(
                        "CREATE_PLAYLIST ")) {

            String name =
                    request.substring(
                            "CREATE_PLAYLIST ".length()
                    ).trim();

            List<Track> tracks =
                    libraryService.getAllTracks();

            playlistService.savePlaylist(
                    name,
                    tracks
            );

            return "OK: Плейлист "
                    + name
                    + " створено";
        }


        // GET_PLAYLIST <name>
        if (request.toUpperCase()
                .startsWith(
                        "GET_PLAYLIST ")) {

            String name =
                    request.substring(
                            "GET_PLAYLIST ".length()
                    ).trim();

            Playlist playlist =
                    playlistService.getPlaylist(
                            name
                    );

            if (playlist == null) {

                return "ERROR: Плейлист не знайдено";
            }

            StringBuilder result =
                    new StringBuilder();

            result.append(
                    playlist.toString()
            );

            for (Track track :
                    playlist.getTracks()) {

                result
                        .append(" | ")
                        .append(track);
            }

            return "PLAYLIST: " + result;
        }


        // SHUTDOWN
        if (request.equalsIgnoreCase(
                "SHUTDOWN")) {

            return "OK: Сервер завершує роботу";
        }


        return "ERROR: Невідома команда";
    }
}


// ======================================================
// CLIENT
// ======================================================

// MusicClient.java
class MusicClient implements AutoCloseable {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public MusicClient(
            String host,
            int port) throws IOException {

        socket = new Socket(
                host,
                port
        );

        reader =
                new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()
                        )
                );

        writer =
                new PrintWriter(
                        socket.getOutputStream(),
                        true
                );

        System.out.println(
                "[MusicClient] Підключено до сервера "
                        + host + ":" + port
        );
    }


    public String sendRequest(
            String request)
            throws IOException {

        System.out.println(
                "[MusicClient] Надіслано: "
                        + request
        );

        writer.println(request);

        String response =
                reader.readLine();

        System.out.println(
                "[MusicClient] Отримано: "
                        + response
        );

        return response;
    }


    public String getTracks()
            throws IOException {

        return sendRequest(
                "GET_TRACKS"
        );
    }


    public String playTrack(
            String title)
            throws IOException {

        return sendRequest(
                "PLAY " + title
        );
    }


    public String pausePlayback()
            throws IOException {

        return sendRequest(
                "PAUSE"
        );
    }


    public String stopPlayback()
            throws IOException {

        return sendRequest(
                "STOP"
        );
    }


    public String createPlaylist(
            String name)
            throws IOException {

        return sendRequest(
                "CREATE_PLAYLIST "
                        + name
        );
    }


    public String getPlaylist(
            String name)
            throws IOException {

        return sendRequest(
                "GET_PLAYLIST "
                        + name
        );
    }


    public String shutdownServer()
            throws IOException {

        return sendRequest(
                "SHUTDOWN"
        );
    }


    @Override
    public void close()
            throws IOException {

        reader.close();
        writer.close();
        socket.close();

        System.out.println(
                "[MusicClient] З'єднання закрито"
        );
    }
}


// ======================================================
// USER INTERFACE
// ======================================================

// UserInterface.java
class UserInterface {

    private MusicClient client;

    public UserInterface(
            MusicClient client) {

        this.client = client;
    }


    public void showTracks()
            throws IOException {

        System.out.println(
                "\n=== Список композицій ==="
        );

        String response =
                client.getTracks();

        System.out.println(response);
    }


    public void playTrack(
            String title)
            throws IOException {

        System.out.println(
                "\n=== Відтворення ==="
        );

        String response =
                client.playTrack(title);

        System.out.println(response);
    }


    public void pauseTrack()
            throws IOException {

        System.out.println(
                "\n=== Пауза ==="
        );

        System.out.println(
                client.pausePlayback()
        );
    }


    public void stopTrack()
            throws IOException {

        System.out.println(
                "\n=== Зупинка ==="
        );

        System.out.println(
                client.stopPlayback()
        );
    }


    public void createPlaylist(
            String name)
            throws IOException {

        System.out.println(
                "\n=== Створення плейлиста ==="
        );

        System.out.println(
                client.createPlaylist(name)
        );
    }


    public void showPlaylist(
            String name)
            throws IOException {

        System.out.println(
                "\n=== Отримання плейлиста ==="
        );

        System.out.println(
                client.getPlaylist(name)
        );
    }
}


// ======================================================
// MAIN
// ======================================================

public class Main {

    public static void main(
            String[] args) {

        final int PORT = 5000;


        System.out.println(
                "=== Лабораторна робота №9 ==="
        );

        System.out.println(
                "Музичний програвач"
        );

        System.out.println(
                "Архітектура Client-Server"
        );

        System.out.println(
                "============================"
        );


        // ====================================
        // SERVER SIDE
        // ====================================

        TrackRepository repository =
                new TrackRepository();

        IMusicLibraryService libraryService =
                new MusicLibraryServiceImpl(
                        repository
                );

        IPlaylistService playlistService =
                new PlaylistServiceImpl(
                        libraryService
                );

        MusicFacade facade =
                new MusicFacade();

        IPlaybackService playbackService =
                new PlaybackServiceImpl(
                        libraryService,
                        facade
                );


        MusicServer server =
                new MusicServer(
                        PORT,
                        libraryService,
                        playlistService,
                        playbackService
                );


        // Сервер працює в окремому потоці
        Thread serverThread =
                new Thread(server);

        serverThread.start();


        try {

            // Даємо серверу час запуститися
            Thread.sleep(500);


            // ====================================
            // CLIENT SIDE
            // ====================================

            try (MusicClient client =
                         new MusicClient(
                                 "localhost",
                                 PORT
                         )) {

                UserInterface ui =
                        new UserInterface(
                                client
                        );


                // Отримання треків із сервера
                ui.showTracks();


                // Запуск треку
                ui.playTrack(
                        "Blinding Lights"
                );


                // Пауза
                ui.pauseTrack();


                // Створення плейлиста
                ui.createPlaylist(
                        "Favorites"
                );


                // Отримання плейлиста
                ui.showPlaylist(
                        "Favorites"
                );


                // Відтворення іншого треку
                ui.playTrack(
                        "Believer"
                );


                // Зупинка
                ui.stopTrack();


                // Завершення сервера
                System.out.println(
                        "\n=== Завершення сервера ==="
                );

                System.out.println(
                        client.shutdownServer()
                );
            }


            serverThread.join();


        } catch (IOException e) {

            System.out.println(
                    "Помилка мережевого з'єднання: "
                            + e.getMessage()
            );

        } catch (InterruptedException e) {

            Thread.currentThread()
                    .interrupt();

            System.out.println(
                    "Роботу потоку перервано."
            );
        }


        System.out.println(
                "\nРоботу програми завершено."
        );
    }
}