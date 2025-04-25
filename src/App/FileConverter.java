package App;

import java.util.*;
import static App.ConvertFile.convertFile;

public class FileConverter {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter input with the path (C:\\Users\\faisa\\input.md)");
        String input_file = in.nextLine();

        System.out.println("Enter output with the path (C:\\Users\\faisa\\output.pdf)");
        String output_file = in.nextLine();

        in.close();

        boolean success = convertFile(input_file, output_file);
        if (success) {
            System.out.println("Conversion completed successfully");
        } else {
            System.out.println("Conversion failed");
        }
    }
}
