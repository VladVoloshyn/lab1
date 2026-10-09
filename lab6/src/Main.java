import java.util.Stack;


// ============================
// PlayerMemento.java
// Memento / Знімок
// ============================

class PlayerMemento {

    private final String currentTrack;
    private final int playbackPosition;
    private final int volumeLevel;
    private final boolean isPlaying;
    private final String activePlaylist;

    public PlayerMemento(
            String currentTrack,
            int playbackPosition,
            int volumeLevel,
            boolean isPlaying,
            String activePlaylist) {

        this.currentTrack = currentTrack;
        this.playbackPosition = playbackPosition;
        this.volumeLevel = volumeLevel;
        this.isPlaying = isPlaying;
        this.activePlaylist = activePlaylist;
    }

    public String getCurrentTrack() {
        return currentTrack;
    }

    public int getPlaybackPosition() {
        return playbackPosition;
    }

    public int getVolumeLevel() {
        return volumeLevel;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public String getActivePlaylist() {
        return activePlaylist;
    }
}


// ============================
// MusicPlayer.java
// Originator / Творець
// ============================

class MusicPlayer {

    private String currentTrack;
    private int playbackPosition;
    private int volumeLevel;
    private boolean isPlaying;
    private String activePlaylist;

    public MusicPlayer() {
        currentTrack = "Немає";
        playbackPosition = 0;
        volumeLevel = 50;
        isPlaying = false;
        activePlaylist = "Default";
    }

    public void play() {
        isPlaying = true;
        System.out.println("Відтворення розпочато.");
    }

    public void pause() {
        isPlaying = false;
        System.out.println("Відтворення призупинено.");
    }

    public void stop() {
        isPlaying = false;
        playbackPosition = 0;
        System.out.println("Відтворення зупинено.");
    }

    public void setCurrentTrack(String currentTrack) {
        this.currentTrack = currentTrack;
    }

    public void setPlaybackPosition(int playbackPosition) {
        this.playbackPosition = playbackPosition;
    }

    public void setVolumeLevel(int volumeLevel) {

        if (volumeLevel < 0) {
            this.volumeLevel = 0;
        } else if (volumeLevel > 100) {
            this.volumeLevel = 100;
        } else {
            this.volumeLevel = volumeLevel;
        }
    }

    public void setActivePlaylist(String activePlaylist) {
        this.activePlaylist = activePlaylist;
    }

    public PlayerMemento createMemento() {

        System.out.println(
                "\n[MusicPlayer] Збереження поточного стану..."
        );

        return new PlayerMemento(
                currentTrack,
                playbackPosition,
                volumeLevel,
                isPlaying,
                activePlaylist
        );
    }

    public void restoreFromMemento(PlayerMemento memento) {

        if (memento == null) {
            System.out.println(
                    "[MusicPlayer] Немає стану для відновлення."
            );
            return;
        }

        currentTrack = memento.getCurrentTrack();
        playbackPosition = memento.getPlaybackPosition();
        volumeLevel = memento.getVolumeLevel();
        isPlaying = memento.isPlaying();
        activePlaylist = memento.getActivePlaylist();

        System.out.println(
                "[MusicPlayer] Попередній стан відновлено."
        );
    }

    public void showState() {

        System.out.println(
                "------------------------------------"
        );

        System.out.println(
                "Поточний трек: " + currentTrack
        );

        System.out.println(
                "Позиція відтворення: "
                        + playbackPosition
                        + " сек."
        );

        System.out.println(
                "Гучність: "
                        + volumeLevel
                        + "%"
        );

        System.out.println(
                "Стан: "
                        + (isPlaying
                        ? "відтворюється"
                        : "призупинено")
        );

        System.out.println(
                "Активний плейлист: "
                        + activePlaylist
        );

        System.out.println(
                "------------------------------------"
        );
    }
}


// ============================
// PlayerStateManager.java
// Caretaker / Опікун
// ============================

class PlayerStateManager {

    private final Stack<PlayerMemento> history =
            new Stack<>();

    public void addMemento(
            PlayerMemento memento) {

        history.push(memento);

        System.out.println(
                "[PlayerStateManager] Стан додано до історії."
        );
    }

    public PlayerMemento getMemento() {

        if (history.isEmpty()) {

            System.out.println(
                    "[PlayerStateManager] Історія станів порожня."
            );

            return null;
        }

        System.out.println(
                "[PlayerStateManager] Отримано попередній стан."
        );

        return history.pop();
    }

    public int getHistorySize() {
        return history.size();
    }
}


// ============================
// Main.java
// Client / Клієнт
// ============================

public class Main {

    public static void main(String[] args) {

        System.out.println(
                "=== Лабораторна робота №6 ==="
        );

        System.out.println(
                "Музичний програвач"
        );

        System.out.println(
                "Патерн Memento"
        );

        System.out.println(
                "============================\n"
        );


        // Створення музичного програвача
        MusicPlayer player =
                new MusicPlayer();


        // Створення менеджера станів
        PlayerStateManager stateManager =
                new PlayerStateManager();


        // ============================
        // Перший стан
        // ============================

        System.out.println(
                "=== ПОЧАТКОВИЙ СТАН ==="
        );

        player.setCurrentTrack(
                "Blinding Lights - The Weeknd"
        );

        player.setPlaybackPosition(45);
        player.setVolumeLevel(70);
        player.setActivePlaylist("Favorites");

        player.play();

        player.showState();


        // Зберігаємо перший стан
        stateManager.addMemento(
                player.createMemento()
        );


        // ============================
        // Другий стан
        // ============================

        System.out.println(
                "\n=== СТАН ПІСЛЯ ЗМІН ==="
        );

        player.setCurrentTrack(
                "Believer - Imagine Dragons"
        );

        player.setPlaybackPosition(120);
        player.setVolumeLevel(40);
        player.setActivePlaylist("Workout");

        player.pause();

        player.showState();


        // Зберігаємо другий стан
        stateManager.addMemento(
                player.createMemento()
        );


        // ============================
        // Третій стан
        // ============================

        System.out.println(
                "\n=== НОВИЙ СТАН ==="
        );

        player.setCurrentTrack(
                "Counting Stars - OneRepublic"
        );

        player.setPlaybackPosition(200);
        player.setVolumeLevel(90);
        player.setActivePlaylist("Road Trip");

        player.play();

        player.showState();


        // ============================
        // Відновлення
        // ============================

        System.out.println(
                "\n=== ВІДНОВЛЕННЯ ПОПЕРЕДНЬОГО СТАНУ ==="
        );

        PlayerMemento previousState =
                stateManager.getMemento();

        player.restoreFromMemento(
                previousState
        );

        player.showState();


        // Ще одне відновлення
        System.out.println(
                "\n=== ВІДНОВЛЕННЯ ЩЕ ОДНОГО СТАНУ ==="
        );

        previousState =
                stateManager.getMemento();

        player.restoreFromMemento(
                previousState
        );

        player.showState();


        System.out.println(
                "\nКількість станів в історії: "
                        + stateManager.getHistorySize()
        );

        System.out.println(
                "\nРоботу програми завершено."
        );
    }
}