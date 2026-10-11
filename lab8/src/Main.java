import java.util.ArrayList;
import java.util.List;


// ============================
// IMusicVisitor.java
// Visitor Interface
// ============================

interface IMusicVisitor {

    void visit(Track track);

    void visit(Playlist playlist);
}


// ============================
// IMusicElement.java
// Element Interface
// ============================

interface IMusicElement {

    void accept(IMusicVisitor visitor);
}


// ============================
// Track.java
// Concrete Element
// ============================

class Track implements IMusicElement {

    private String title;
    private int duration;

    public Track(String title, int duration) {
        this.title = title;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public void accept(IMusicVisitor visitor) {
        visitor.visit(this);
    }
}


// ============================
// Playlist.java
// Concrete Element
// ============================

class Playlist implements IMusicElement {

    private String name;

    public Playlist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(IMusicVisitor visitor) {
        visitor.visit(this);
    }
}


// ============================
// MusicStatisticsVisitor.java
// Concrete Visitor
// ============================

class MusicStatisticsVisitor implements IMusicVisitor {

    private int totalDuration = 0;
    private int trackCount = 0;
    private int playlistCount = 0;

    @Override
    public void visit(Track track) {

        totalDuration += track.getDuration();
        trackCount++;

        System.out.println(
                "[StatisticsVisitor] Оброблено трек: "
                        + track.getTitle()
        );
    }

    @Override
    public void visit(Playlist playlist) {

        playlistCount++;

        System.out.println(
                "[StatisticsVisitor] Оброблено плейлист: "
                        + playlist.getName()
        );
    }

    public double getAverageDuration() {

        if (trackCount == 0) {
            return 0;
        }

        return (double) totalDuration / trackCount;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public int getPlaylistCount() {
        return playlistCount;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void printStatistics() {

        System.out.println(
                "\n=== Статистика музичного програвача ==="
        );

        System.out.println(
                "Кількість треків: "
                        + getTrackCount()
        );

        System.out.println(
                "Кількість плейлистів: "
                        + getPlaylistCount()
        );

        System.out.println(
                "Загальна тривалість треків: "
                        + getTotalDuration()
                        + " сек."
        );

        System.out.println(
                "Середня тривалість треку: "
                        + String.format(
                        "%.2f",
                        getAverageDuration()
                )
                        + " сек."
        );
    }
}


// ============================
// MusicExportVisitor.java
// Concrete Visitor
// ============================

class MusicExportVisitor implements IMusicVisitor {

    private StringBuilder exportData =
            new StringBuilder();

    public MusicExportVisitor() {

        exportData.append(
                "=== MUSIC DATA EXPORT ===\n"
        );
    }

    @Override
    public void visit(Track track) {

        exportData
                .append("Track: ")
                .append(track.getTitle())
                .append(", duration: ")
                .append(track.getDuration())
                .append(" sec.\n");
    }

    @Override
    public void visit(Playlist playlist) {

        exportData
                .append("Playlist: ")
                .append(playlist.getName())
                .append("\n");
    }

    public String getExport() {
        return exportData.toString();
    }
}


// ============================
// MusicReportService.java
// Client / Object Structure
// ============================

class MusicReportService {

    public void generateStatistics(
            List<IMusicElement> data) {

        MusicStatisticsVisitor statisticsVisitor =
                new MusicStatisticsVisitor();

        for (IMusicElement element : data) {

            element.accept(
                    statisticsVisitor
            );
        }

        statisticsVisitor.printStatistics();
    }


    public void exportData(
            List<IMusicElement> data) {

        MusicExportVisitor exportVisitor =
                new MusicExportVisitor();

        for (IMusicElement element : data) {

            element.accept(
                    exportVisitor
            );
        }

        System.out.println(
                "\n=== Експорт музичних даних ==="
        );

        System.out.println(
                exportVisitor.getExport()
        );
    }
}


// ============================
// Main.java
// Точка входу
// ============================

public class Main {

    public static void main(String[] args) {

        System.out.println(
                "=== Лабораторна робота №8 ==="
        );

        System.out.println(
                "Музичний програвач"
        );

        System.out.println(
                "Патерн Visitor"
        );

        System.out.println(
                "============================\n"
        );


        // Створення гетерогенної колекції
        List<IMusicElement> musicData =
                new ArrayList<>();


        // Додавання треків
        musicData.add(
                new Track(
                        "Blinding Lights - The Weeknd",
                        200
                )
        );

        musicData.add(
                new Track(
                        "Believer - Imagine Dragons",
                        204
                )
        );


        // Додавання плейлиста
        musicData.add(
                new Playlist(
                        "Favorites"
                )
        );


        // Ще один трек
        musicData.add(
                new Track(
                        "Counting Stars - OneRepublic",
                        257
                )
        );


        // Ще один плейлист
        musicData.add(
                new Playlist(
                        "Workout"
                )
        );


        // Створення сервісу
        MusicReportService reportService =
                new MusicReportService();


        // ============================
        // Visitor №1 — статистика
        // ============================

        System.out.println(
                "=== VISITOR: STATISTICS ===\n"
        );

        reportService.generateStatistics(
                musicData
        );


        System.out.println(
                "\n--------------------------------\n"
        );


        // ============================
        // Visitor №2 — експорт
        // ============================

        System.out.println(
                "=== VISITOR: EXPORT ==="
        );

        reportService.exportData(
                musicData
        );


        System.out.println(
                "Роботу програми завершено."
        );
    }
}