package DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Database {
    private static String db_user = "postgres";
    private static String db_password = "changeme";
    private static String db_name = "history_db";
    private static String table_name = "file_history";
    private static String db_url = "jdbc:postgresql://localhost:5432/" + db_name;

    private static final ReentrantLock lock = new ReentrantLock();

    public static void initialize_database() {
        lock.lock();
        try (Connection connection = get_connection();
             Statement stmt = connection.createStatement()) {

            String create_table_sql = "CREATE TABLE IF NOT EXISTS " + table_name + " (" +
                    "id SERIAL PRIMARY KEY, " +
                    "file_name VARCHAR(255) NOT NULL, " +
                    "file_path TEXT NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(create_table_sql);
            System.out.println("File history database has been initialized");
        } catch (SQLException e) {
            System.err.println("Unable to initialize the database: " + e.getMessage());
            System.exit(2);
        } finally {
            lock.unlock();
        }
    }

    public static void add_file_history(FileHistory file_history) {
        lock.lock();
        String sql = "INSERT INTO " + table_name + " (file_name, file_path) VALUES (?, ?)";
        try (Connection connection = get_connection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, file_history.getFileName());
            stmt.setString(2, file_history.getFilePath());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to add file history: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public static List<FileHistory> get_all_file_history() {
        List<FileHistory> file_history_list = new ArrayList<>();
        lock.lock();
        try {
            String sql = "SELECT * FROM " + table_name + " ORDER BY created_at DESC";
            try (Connection connection = get_connection();
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    FileHistory file_history = new FileHistory(
                            rs.getString("file_name"),
                            rs.getString("file_path")
                    );
                    file_history.setId(rs.getInt("id"));
                    file_history.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    file_history_list.add(file_history);
                }
            } catch (SQLException e) {
                System.err.println("Failed to get file history: " + e.getMessage());
            }
        } finally {
            lock.unlock();
        }
        return file_history_list;
    }

    public static void delete_all_history() {
        lock.lock();
        try {
            String sql = "DELETE FROM " + table_name;
            try (Connection connection = get_connection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.executeUpdate();
                System.out.println("All history records have been deleted");
            } catch (SQLException e) {
                System.err.println("Failed to delete history: " + e.getMessage());
            }
        } finally {
            lock.unlock();
        }
    }

    public static Connection get_connection() throws SQLException {
        return DriverManager.getConnection(db_url, db_user, db_password);
    }
}