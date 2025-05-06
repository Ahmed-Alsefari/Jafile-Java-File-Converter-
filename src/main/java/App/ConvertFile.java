package App;


import DB.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.List;
import java.util.ArrayList;
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


// ahmed alharbi >>>>>>>>>>>>>
    public static boolean convertMultipleFiles(List<Path> inputFiles, List<Path> outputFiles) {
        List<Thread> threads = new ArrayList<>();

        if (inputFiles.size() != outputFiles.size()) {
            System.err.println("Input and output files count mismatch.");
            return false;
        }

        for (int i = 0; i < inputFiles.size(); i++) {
            Path inputFile = inputFiles.get(i);
            Path outputFile = outputFiles.get(i);

            Thread thread = new Thread(() -> {
                convertFile(inputFile, outputFile);
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted: " + e.getMessage());
                return false;
            }
        }

        return true;
    }
//  <<<<<<<<<<<<<<

    private static boolean convertFile(Path input_file, Path output_file) {

        String input_extension = getFileExtension(input_file).toLowerCase();
        String output_extension = getFileExtension(output_file).toLowerCase();


        String file_name = input_file.getFileName().toString().replaceFirst("[.][^.]+$", "");

        Path output_path = output_file.getParent();
        Path temp = output_path.resolve(file_name + ".docx");


        Path new_output_file = output_path.resolve(file_name + "_converted." + output_extension);

        boolean check = false;

        lock.lock();


        try {
            String command = "";

            if (videoFormats.contains(input_extension) && audioFormats.contains(output_extension)) {
                String audioCodec = "";
                if (output_extension.equals("mp3")) {
                    audioCodec = "-c:a libmp3lame -q:a 2";
                } else if (output_extension.equals("aac")) {
                    audioCodec = "-c:a aac -b:a 192k";
                } else if (output_extension.equals("wav")) {
                    audioCodec = "-c:a pcm_s16le";
                } else {
                    audioCodec = "-c:a copy";
                }
                command = String.format("%s -i \"%s\" -vn %s \"%s\"", ffmpeg_path, input_file, audioCodec, new_output_file);
            } else if (audioFormats.contains(input_extension) || audioFormats.contains(output_extension) ||
                    videoFormats.contains(input_extension) || videoFormats.contains(output_extension)) {

                int threads = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
                command = String.format("%s -i \"%s\" -preset fast -threads 0 \"%s\"", ffmpeg_path, input_file, new_output_file);


            } else if (imageFormats.contains(input_extension) || imageFormats.contains(output_extension)) {
                command = String.format("\"%s\" \"%s\" \"%s\"", image_magick_path, input_file.toString(), new_output_file.toString());


            } else if (officeFormats.contains(input_extension) || officeFormats.contains(output_extension)) {
                if (!officeFormats.contains(input_extension)&&(!input_extension.equals("docx"))) {

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

                Database.initialize_database();


                String file_Name = new_output_file.getFileName().toString();
                String file_path = new_output_file.toAbsolutePath().toString();

                FileHistory file_history = new FileHistory(file_Name, file_path);

                Database.add_file_history(file_history);

                System.out.println("File added to history: " + file_Name);

            return true;


        } catch (Exception e) {

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
