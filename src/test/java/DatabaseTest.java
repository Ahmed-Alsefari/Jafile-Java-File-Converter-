package com.jafile.tests;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import DB.Database;
import DB.FileHistory;

import java.util.List;

public class DatabaseTest {

    @Test
    void testInsertAndRetrieveHistory() {
        FileHistory history = new FileHistory("input_sample.txt", "src/test/resources/input_sample.txt");
        boolean inserted = Database.add_file_history(history);
        assertTrue(inserted);

        List<FileHistory> all = Database.get_all_file_history();
        assertNotNull(all);
        assertTrue(all.size() > 0);
    }
}
