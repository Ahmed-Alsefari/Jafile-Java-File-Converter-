package App;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static App.ConvertFile.converFileThread;
import static DB.Database.get_all_file_history;



public class FileConverter {

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)){

            System.out.println(get_all_file_history());
            System.out.println("Enter input with the path (C:\\Users\\faisa\\input.md)");
            Path input_file = Paths.get(in.nextLine());

            System.out.println("Enter output with the path (C:\\Users\\faisa\\output.pdf)");
            Path output_file = Paths.get(in.nextLine());

            boolean success = converFileThread(input_file, output_file);
            if (success) {
                System.out.println("Conversion completed successfully");
            } else {
                System.out.println("Conversion failed");
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}
