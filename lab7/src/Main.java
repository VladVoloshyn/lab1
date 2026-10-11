// ============================
// IMusicFacade.java
// Інтерфейс фасаду
// ============================

interface IMusicFacade {

    void playTrack(String trackName);

    void pausePlayback();

    void loadPlaylist(String name);

    void savePlayerState();

    void restorePlayerState();
}


// ============================
// MusicPlayer.java
// Клас підсистеми
// ============================

class MusicPlayer {

    private String currentTrack;
    private boolean isPlaying;

    public MusicPlayer() {
        this.currentTrack = "Немає";
        this.isPlaying = false;
    }

    public void playTrack(String trackName) {

        this.currentTrack = trackName;
        this.isPlaying = true;

        System.out.println(
                "[MusicPlayer] Відтворюється трек: "
                        + trackName
        );
    }

    public void pause() {

        if (isPlaying) {

            isPlaying = false;

            System.out.println(
                    "[MusicPlayer] Відтворення призупинено."
            );

        } else {

            System.out.println(
                    "[MusicPlayer] Програвач уже на паузі."
            );
        }
    }

    public void stop() {

        isPlaying = false;

        System.out.println(
                "[MusicPlayer] Відтворення зупинено."
        );
    }

    public String getCurrentTrack() {
        return currentTrack;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}


// ============================
// PlaylistService.java
// Клас підсистеми
// ============================

class PlaylistService {

    private String activePlaylist;

    public PlaylistService() {
        this.activePlaylist = "Default";
    }

    public void loadPlaylist(String name) {

        this.activePlaylist = name;

        System.out.println(
                "[PlaylistService] Завантажено плейлист: "
                        + name
        );
    }

    public String getPlaylist() {
        return activePlaylist;
    }
}


// ============================
// PlayerStateManager.java
// Клас підсистеми
// ============================

class PlayerStateManager {

    private String savedTrack;
    private String savedPlaylist;

    public void saveState(
            String track,
            String playlist) {

        this.savedTrack = track;
        this.savedPlaylist = playlist;

        System.out.println(
                "[PlayerStateManager] Стан програвача збережено."
        );
    }

    public String restoreTrack() {
        return savedTrack;
    }

    public String restorePlaylist() {
        return savedPlaylist;
    }

    public boolean hasSavedState() {

        return savedTrack != null
                || savedPlaylist != null;
    }
}


// ============================
// MusicFacade.java
// Facade / Фасад
// ============================

class MusicFacade implements IMusicFacade {

    private MusicPlayer player;
    private PlaylistService playlistService;
    private PlayerStateManager stateManager;

    public MusicFacade(
            MusicPlayer player,
            PlaylistService playlistService,
            PlayerStateManager stateManager) {

        this.player = player;
        this.playlistService = playlistService;
        this.stateManager = stateManager;
    }

    @Override
    public void playTrack(String trackName) {

        System.out.println(
                "\n[MusicFacade] Запит на відтворення треку."
        );

        player.playTrack(trackName);
    }

    @Override
    public void pausePlayback() {

        System.out.println(
                "\n[MusicFacade] Запит на паузу."
        );

        player.pause();
    }

    @Override
    public void loadPlaylist(String name) {

        System.out.println(
                "\n[MusicFacade] Завантаження плейлиста."
        );

        playlistService.loadPlaylist(name);
    }

    @Override
    public void savePlayerState() {

        System.out.println(
                "\n[MusicFacade] Збереження стану."
        );

        stateManager.saveState(
                player.getCurrentTrack(),
                playlistService.getPlaylist()
        );
    }

    @Override
    public void restorePlayerState() {

        System.out.println(
                "\n[MusicFacade] Відновлення стану."
        );

        if (!stateManager.hasSavedState()) {

            System.out.println(
                    "Немає збереженого стану."
            );

            return;
        }

        String savedPlaylist =
                stateManager.restorePlaylist();

        String savedTrack =
                stateManager.restoreTrack();

        if (savedPlaylist != null) {

            playlistService.loadPlaylist(
                    savedPlaylist
            );
        }

        if (savedTrack != null) {

            player.playTrack(
                    savedTrack
            );
        }

        System.out.println(
                "[MusicFacade] Попередній стан відновлено."
        );
    }

    public void showCurrentState() {

        System.out.println(
                "\n--- Поточний стан програвача ---"
        );

        System.out.println(
                "Трек: "
                        + player.getCurrentTrack()
        );

        System.out.println(
                "Плейлист: "
                        + playlistService.getPlaylist()
        );

        System.out.println(
                "Відтворення: "
                        + (player.isPlaying()
                        ? "увімкнено"
                        : "призупинено")
        );

        System.out.println(
                "--------------------------------"
        );
    }
}


// ============================
// UserInterface.java
// Client / Клієнт
// ============================

class UserInterface {

    private IMusicFacade facade;

    public UserInterface(
            IMusicFacade facade) {

        this.facade = facade;
    }

    public void onPlayButton(
            String trackName) {

        System.out.println(
                "\n[UI] Натиснуто кнопку Play"
        );

        facade.playTrack(
                trackName
        );
    }

    public void onPauseButton() {

        System.out.println(
                "\n[UI] Натиснуто кнопку Pause"
        );

        facade.pausePlayback();
    }

    public void onLoadPlaylist(
            String name) {

        System.out.println(
                "\n[UI] Обрано плейлист: "
                        + name
        );

        facade.loadPlaylist(
                name
        );
    }

    public void onSaveState() {

        System.out.println(
                "\n[UI] Збереження стану"
        );

        facade.savePlayerState();
    }

    public void onRestoreState() {

        System.out.println(
                "\n[UI] Відновлення стану"
        );

        facade.restorePlayerState();
    }
}


// ============================
// Main.java
// Точка входу в програму
// ============================

public class Main {

    public static void main(String[] args) {

        System.out.println(
                "=== Лабораторна робота №7 ==="
        );

        System.out.println(
                "Музичний програвач"
        );

        System.out.println(
                "Патерн Facade"
        );

        System.out.println(
                "============================"
        );


        // Створення компонентів підсистеми
        MusicPlayer player =
                new MusicPlayer();

        PlaylistService playlistService =
                new PlaylistService();

        PlayerStateManager stateManager =
                new PlayerStateManager();


        // Створення фасаду
        MusicFacade musicFacade =
                new MusicFacade(
                        player,
                        playlistService,
                        stateManager
                );


        // Створення клієнта
        UserInterface ui =
                new UserInterface(
                        musicFacade
                );


        // ============================
        // Робота через фасад
        // ============================

        ui.onLoadPlaylist(
                "Favorites"
        );

        ui.onPlayButton(
                "Blinding Lights - The Weeknd"
        );

        musicFacade.showCurrentState();


        // Збереження стану
        ui.onSaveState();


        // Зміна стану
        ui.onLoadPlaylist(
                "Workout"
        );

        ui.onPlayButton(
                "Believer - Imagine Dragons"
        );

        musicFacade.showCurrentState();


        // Пауза
        ui.onPauseButton();

        musicFacade.showCurrentState();


        // Відновлення попереднього стану
        ui.onRestoreState();

        musicFacade.showCurrentState();


        System.out.println(
                "\nРоботу програми завершено."
        );
    }
}