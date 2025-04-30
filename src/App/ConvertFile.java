package App;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import static App_Constants.Constants.*;
import static Utils.Tool.*;



public class ConvertFile {
    public final static ReentrantLock lock = new ReentrantLock();

    public static boolean converFileThread(Path input_file, Path output_file) {

        AtomicBoolean result = new AtomicBoolean(false);

        Thread thread = new Thread(() -> {
            result.set(convertFile(input_file, output_file));
        });
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }

        return result.get();
    }

    private static boolean convertFile(Path input_file, Path output_file) {

        String input_extension = getFileExtension(input_file).toLowerCase();
        String output_extension = getFileExtension(output_file).toLowerCase();

        String file_name = output_file.getFileName().toString().replaceFirst("[.][^.]+$", "");

        Path output_path = output_file.getParent();
        Path temp = output_path.resolve(file_name + ".docx");


        // add ( "_converted" ) to make it unique
        String new_output_file = output_path + File.separator + file_name + "_converted." + output_extension;


        boolean check = false;

        lock.lock();


        try {
            String command = "";

            if (audioFormats.contains(input_extension) || audioFormats.contains(output_extension) ||
                    videoFormats.contains(input_extension) || videoFormats.contains(output_extension)) {

                int threads = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
                command = String.format("%s -i \"%s\" -preset fast -threads %d \"%s\"", ffmpeg_path, input_file, threads, new_output_file);


            } else if (imageFormats.contains(input_extension) || imageFormats.contains(output_extension)) {
                command = String.format("\"%s\" \"%s\" \"%s\"", image_magick_path, input_file.toString(), new_output_file.toString());


            } else if (officeFormats.contains(input_extension) || officeFormats.contains(output_extension)) {
                if (!officeFormats.contains(input_extension)) {

                    command(String.format("\"%s\" -s \"%s\" -o \"%s\"", pandoc_path, input_file.toString(), temp.toString()));
                    check = true;
                }


                command = String.format("%s --headless --convert-to %s \"%s\" --outdir \"%s\"",
                        libre_office_path, output_extension, check ? temp.toString() : input_file.toString(), output_path.toString());

            } else {

                command = String.format("\"%s\" -s \"%s\" -o \"%s\"", pandoc_path, input_file.toString(), new_output_file.toString());
            }


            command(command);
            System.out.println("Conversion completed successfully");
            return true;


        } catch ( Exception e) {

            System.out.println("Error during conversion: " + e.getMessage());
            e.printStackTrace();
            return false;

        } finally {
            if (check) {
                try {
                    Files.deleteIfExists(temp);
                } catch (IOException e) {
                    System.err.println("Failed to delete temporary file: " + e.getMessage());
                }
            }
            lock.unlock();

        }


    }
}

