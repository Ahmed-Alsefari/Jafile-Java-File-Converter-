import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class FileConverter {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println("Enter input with the path (C:\\Users\\faisa\\input.md)");
        String input_file = in.nextLine();

        System.out.println("Enter output with the path (C:\\Users\\faisa\\output.pdf)");
        String output_file = in.nextLine();


        Set<String> officeFormats = Set.of("pdf", "doc", "odt", "rtf", "epub", "fodt", "xml", "xls", "docx");
        Set<String> imageFormats = Set.of("jpg", "jpeg", "png", "bmp", "gif", "tiff", "webp", "heic", "avif");
        Set<String> audioFormats = Set.of("mp3", "wav", "ogg", "flac", "aac", "m4a", "wma", "alac", "opus", "amr", "aiff");
        Set<String> videoFormats = Set.of("mp4", "mkv", "avi", "mov", "flv", "wmv", "webm", "mpeg", "3gp", "ts", "m4v");

        String input_extension = input_file.substring(input_file.lastIndexOf('.') + 1).toLowerCase();
        String output_extension = output_file.substring(output_file.lastIndexOf('.') + 1).toLowerCase();

        String file_name  = output_file.substring(output_file.lastIndexOf("\\") + 1, output_file.lastIndexOf("."));
        String output_path = output_file.substring(0, output_file.lastIndexOf("\\"));
        String temp = output_path + "\\" + file_name + ".docx";
        String ffmpeg_path = "C:\\Users\\faisa\\all\\جامعة\\CPIT305\\ffmpeg-master-latest-win64-gpl-shared\\bin\\ffmpeg.exe";

        boolean check = false;

        try {
            String command = "";

            if (audioFormats.contains(input_extension) || audioFormats.contains(output_extension) || videoFormats.contains(input_extension) || videoFormats.contains(output_extension)) {
                command = String.format("%s -i \"%s\" \"%s\"", ffmpeg_path, input_file, output_file);
            } else if (imageFormats.contains(input_extension) || imageFormats.contains(output_extension)) {
                command = String.format("magick \"%s\" \"%s\"", input_file, output_file);

            } else if (officeFormats.contains(input_extension) || officeFormats.contains(output_extension)) {
                if (officeFormats.contains(input_extension) == false) {
                    command(String.format("pandoc -s %s -o %s", input_file, temp));
                    check = true;
                }
                command = String.format("\"C:\\Program Files\\LibreOffice\\program\\soffice.exe\" --headless --convert-to %s \"%s\" --outdir \"%s\"", output_extension, temp, output_path);
            } else
                command = String.format("pandoc -s %s -o %s", input_file, output_file);

            command(command);

            System.out.println("done");
        } catch (Exception e) {
            System.out.println("GG  " + e.getMessage());
        } finally {
            if (check && new File(temp).exists())
                new File(temp).delete();
        }
    }

    private static void command(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor(120, TimeUnit.SECONDS);
            //process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("can not do the command" + e);
        }finally {
            killProcessIfRunning(process);
        }
    }

    public static void killProcessIfRunning(Process process) {
        if (process != null && process.isAlive()) {
            process.destroy();
            try {
                if (!process.waitFor(3, java.util.concurrent.TimeUnit.SECONDS)) {
                    process.destroyForcibly();
                }
            } catch (InterruptedException e) {
                System.err.println("Error while waiting for process to end: " + e.getMessage());
            }
        }
    }
}
