package DB;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileHistory {
    private int id;
    private String fileName;
    private String filePath;
    private LocalDateTime createdAt;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FileHistory(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.format(formatter) : "";
    }

    @Override
    public String toString() {
        return " FileHistory { " +
                " id = " + id +
                " , fileName = ' " + fileName + " ' " +
                " , filePath = ' " + filePath + " ' " +
                " , createdAt = " + createdAt +
                " } \n";
    }
}