package App;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;

// ahmed alharbi >>>>>>>>>>>>>
public class ConvertMultipleFiles {
    public static void main(String[] args) {
        List<Path> inputFiles = Arrays.asList(Paths.get("file1.md"), Paths.get("file2.md"));
        List<Path> outputFiles = Arrays.asList(Paths.get("file1.pdf"), Paths.get("file2.pdf"));

        if (inputFiles.size() != outputFiles.size()) {
            System.out.println("Input and output file lists must have the same number of files.");
            return;
        }

        boolean success = ConvertFile.convertMultipleFiles(inputFiles, outputFiles);

        if (success) {
            System.out.println("All conversions completed successfully.");
        } else {
            System.out.println("Some conversions failed.");
        }
    }
}
