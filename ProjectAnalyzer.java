import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ProjectAnalyzer {

    private static final Set<String> IGNORED_DIRS = new HashSet<>(Arrays.asList(
        ".git", ".idea", ".vscode", "target", "build", "node_modules", ".mvn", "wrapper"
    ));

    private static final Set<String> TRACKED_EXTENSIONS = new HashSet<>(Arrays.asList(
        "java", "xml", "yml", "yaml", "properties", "sql", "html", "css", "js", "md"
    ));

    public static void main(String[] args) {
        Path rootPath = Paths.get(".").toAbsolutePath().normalize();
        System.out.println("Analyzing project at: " + rootPath);
        System.out.println("Scanning... This may take a moment.\n");

        ProjectFileVisitor visitor = new ProjectFileVisitor();

        try {
            Files.walkFileTree(rootPath, visitor);
            printReport(visitor.getStats(), visitor.getTotalFiles(), visitor.getTotalLines(), visitor.getTotalSize());
        } catch (IOException e) {
            System.err.println("An error occurred while scanning the project: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printReport(Map<String, FileStats> stats, long totalFiles, long totalLines, long totalSize) {
        System.out.println("==========================================================================================");
        System.out.println(String.format("%-15s | %-12s | %-15s | %-15s | %-15s", 
            "File Type", "Count", "Lines of Code", "Size (KB)", "Avg LOC/File"));
        System.out.println("------------------------------------------------------------------------------------------");

        NumberFormat nf = NumberFormat.getInstance();
        
        stats.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue().lines, e1.getValue().lines))
            .forEach(entry -> {
                String type = entry.getKey().toUpperCase();
                FileStats s = entry.getValue();
                double sizeKb = s.size / 1024.0;
                long avgLoc = s.count > 0 ? s.lines / s.count : 0;

                System.out.println(String.format("%-15s | %-12s | %-15s | %-15.2f | %-15s", 
                    type, 
                    nf.format(s.count), 
                    nf.format(s.lines), 
                    sizeKb,
                    nf.format(avgLoc)
                ));
            });

        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println(String.format("%-15s | %-12s | %-15s | %-15.2f | %-15s", 
            "TOTAL", 
            nf.format(totalFiles), 
            nf.format(totalLines), 
            totalSize / 1024.0, 
            totalFiles > 0 ? nf.format(totalLines / totalFiles) : "0"
        ));
        System.out.println("==========================================================================================");
    }

    private static class ProjectFileVisitor extends SimpleFileVisitor<Path> {
        private final Map<String, FileStats> stats = new TreeMap<>();
        private long totalFiles = 0;
        private long totalLines = 0;
        private long totalSize = 0;

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (Files.isHidden(dir) || IGNORED_DIRS.contains(dir.getFileName().toString())) {
                return FileVisitResult.SKIP_SUBTREE;
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            String fileName = file.getFileName().toString();
            String extension = getExtension(fileName);

            if (TRACKED_EXTENSIONS.contains(extension)) {
                long lines = countLines(file);
                long size = attrs.size();

                stats.putIfAbsent(extension, new FileStats());
                FileStats s = stats.get(extension);
                s.count++;
                s.lines += lines;
                s.size += size;

                totalFiles++;
                totalLines += lines;
                totalSize += size;
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            System.err.println("Failed to access file: " + file + " (" + exc.getMessage() + ")");
            return FileVisitResult.CONTINUE;
        }

        public Map<String, FileStats> getStats() { return stats; }
        public long getTotalFiles() { return totalFiles; }
        public long getTotalLines() { return totalLines; }
        public long getTotalSize() { return totalSize; }

        private String getExtension(String fileName) {
            int i = fileName.lastIndexOf('.');
            return (i > 0) ? fileName.substring(i + 1).toLowerCase() : "";
        }

        private long countLines(Path file) {
            try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                return reader.lines().count();
            } catch (Exception e) {
                return 0;
            }
        }
    }
    
    private static class FileStats {
        long count = 0;
        long lines = 0;
        long size = 0;
    }
}