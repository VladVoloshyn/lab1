// ============================
// ICommand.java
// ============================

interface ICommand {
    void execute();
}


// ============================
// PlaybackController.java
// Receiver / Отримувач
// ============================

class PlaybackController {

    private String currentTrack = "Blinding Lights - The Weeknd";
    private boolean isPlaying = false;

    public void play() {
        isPlaying = true;
        System.out.println(
                "[PlaybackController] Відтворення: "
                        + currentTrack
        );
    }

    public void pause() {

        if (isPlaying) {
            isPlaying = false;

            System.out.println(
                    "[PlaybackController] Відтворення призупинено."
            );
        } else {
            System.out.println(
                    "[PlaybackController] Музика вже на паузі."
            );
        }
    }

    public void stop() {

        isPlaying = false;

        System.out.println(
                "[PlaybackController] Відтворення зупинено."
        );
    }

    public void next() {

        currentTrack = "Believer - Imagine Dragons";

        System.out.println(
                "[PlaybackController] Наступна композиція: "
                        + currentTrack
        );

        if (isPlaying) {
            System.out.println(
                    "[PlaybackController] Відтворення продовжено."
            );
        }
    }

    public void previous() {

        currentTrack = "Blinding Lights - The Weeknd";

        System.out.println(
                "[PlaybackController] Попередня композиція: "
                        + currentTrack
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
// PlayCommand.java
// Concrete Command
// ============================

class PlayCommand implements ICommand {

    private PlaybackController controller;

    public PlayCommand(
            PlaybackController controller) {

        this.controller = controller;
    }

    @Override
    public void execute() {
        controller.play();
    }
}


// ============================
// PauseCommand.java
// Concrete Command
// ============================

class PauseCommand implements ICommand {

    private PlaybackController controller;

    public PauseCommand(
            PlaybackController controller) {

        this.controller = controller;
    }

    @Override
    public void execute() {
        controller.pause();
    }
}


// ============================
// StopCommand.java
// Concrete Command
// ============================

class StopCommand implements ICommand {

    private PlaybackController controller;

    public StopCommand(
            PlaybackController controller) {

        this.controller = controller;
    }

    @Override
    public void execute() {
        controller.stop();
    }
}


// ============================
// NextTrackCommand.java
// Concrete Command
// ============================

class NextTrackCommand implements ICommand {

    private PlaybackController controller;

    public NextTrackCommand(
            PlaybackController controller) {

        this.controller = controller;
    }

    @Override
    public void execute() {
        controller.next();
    }
}


// ============================
// PreviousTrackCommand.java
// Concrete Command
// ============================

class PreviousTrackCommand implements ICommand {

    private PlaybackController controller;

    public PreviousTrackCommand(
            PlaybackController controller) {

        this.controller = controller;
    }

    @Override
    public void execute() {
        controller.previous();
    }
}


// ============================
// MusicPlayerUI.java
// Invoker / Ініціатор
// ============================

class MusicPlayerUI {

    private ICommand playCommand;
    private ICommand pauseCommand;
    private ICommand stopCommand;
    private ICommand nextCommand;
    private ICommand previousCommand;

    public void setPlayCommand(
            ICommand command) {

        this.playCommand = command;
    }

    public void setPauseCommand(
            ICommand command) {

        this.pauseCommand = command;
    }

    public void setStopCommand(
            ICommand command) {

        this.stopCommand = command;
    }

    public void setNextCommand(
            ICommand command) {

        this.nextCommand = command;
    }

    public void setPreviousCommand(
            ICommand command) {

        this.previousCommand = command;
    }

    public void onPlayButtonClick() {

        System.out.println(
                "\n[UI] Натиснуто кнопку Play"
        );

        if (playCommand != null) {
            playCommand.execute();
        }
    }

    public void onPauseButtonClick() {

        System.out.println(
                "\n[UI] Натиснуто кнопку Pause"
        );

        if (pauseCommand != null) {
            pauseCommand.execute();
        }
    }

    public void onStopButtonClick() {

        System.out.println(
                "\n[UI] Натиснуто кнопку Stop"
        );

        if (stopCommand != null) {
            stopCommand.execute();
        }
    }

    public void onNextButtonClick() {

        System.out.println(
                "\n[UI] Натиснуто кнопку Next"
        );

        if (nextCommand != null) {
            nextCommand.execute();
        }
    }

    public void onPreviousButtonClick() {

        System.out.println(
                "\n[UI] Натиснуто кнопку Previous"
        );

        if (previousCommand != null) {
            previousCommand.execute();
        }
    }
}


// ============================
// Main.java
// Client / Клієнт
// ============================

public class Main {

    public static void main(String[] args) {

        System.out.println(
                "=== Лабораторна робота №5 ==="
        );

        System.out.println(
                "Музичний програвач"
        );

        System.out.println(
                "Патерн Command"
        );

        System.out.println(
                "============================"
        );


        // Створення Receiver
        PlaybackController controller =
                new PlaybackController();


        // Створення конкретних команд
        ICommand playCommand =
                new PlayCommand(controller);

        ICommand pauseCommand =
                new PauseCommand(controller);

        ICommand stopCommand =
                new StopCommand(controller);

        ICommand nextCommand =
                new NextTrackCommand(controller);

        ICommand previousCommand =
                new PreviousTrackCommand(controller);


        // Створення Invoker
        MusicPlayerUI ui =
                new MusicPlayerUI();


        // Прив'язка команд до кнопок
        ui.setPlayCommand(
                playCommand
        );

        ui.setPauseCommand(
                pauseCommand
        );

        ui.setStopCommand(
                stopCommand
        );

        ui.setNextCommand(
                nextCommand
        );

        ui.setPreviousCommand(
                previousCommand
        );


        // Демонстрація роботи програми
        ui.onPlayButtonClick();

        ui.onNextButtonClick();

        ui.onPauseButtonClick();

        ui.onPlayButtonClick();

        ui.onPreviousButtonClick();

        ui.onStopButtonClick();


        // Підсумковий стан
        System.out.println(
                "\n--- Поточний стан програвача ---"
        );

        System.out.println(
                "Поточний трек: "
                        + controller.getCurrentTrack()
        );

        System.out.println(
                "Відтворюється: "
                        + controller.isPlaying()
        );

        System.out.println(
                "\nРоботу програми завершено."
        );
    }
}