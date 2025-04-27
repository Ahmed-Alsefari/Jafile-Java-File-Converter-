package App;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import static App_Constants.Constants.*;
import static Utils.Tool.*;

public class ConvertFile {
    public final static ReentrantLock lock = new ReentrantLock();

    public static boolean converFileThread(String input_file, String output_file) {
        AtomicBoolean B = new AtomicBoolean(false);
        Thread thread = new Thread(() -> {
            B.set(convertFile(input_file, output_file));
        });
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted: " + e.getMessage());
            return false;
        }

        return B.get();
    }

    public static boolean convertFile(String input_file, String output_file) {
        String input_extension = input_file.substring(input_file.lastIndexOf('.') + 1).toLowerCase();
        String output_extension = output_file.substring(output_file.lastIndexOf('.') + 1).toLowerCase();

        String file_name = output_file.substring(output_file.lastIndexOf(File.separator) + 1, output_file.lastIndexOf("."));
        String output_path = output_file.substring(0, output_file.lastIndexOf(File.separator));
        String temp = output_path + File.separator + file_name + ".docx";

        boolean check = false;
        lock.lock();
        try {
            String command = "";

            if (audioFormats.contains(input_extension) || audioFormats.contains(output_extension) ||
                    videoFormats.contains(input_extension) || videoFormats.contains(output_extension)) {
                command = String.format("%s -i \"%s\" \"%s\"", ffmpeg_path, input_file, output_file);
            } else if (imageFormats.contains(input_extension) || imageFormats.contains(output_extension)) {
                command = String.format("\"%s\" \"%s\" \"%s\"", image_magick_path, input_file, output_file);
            } else if (officeFormats.contains(input_extension) || officeFormats.contains(output_extension)) {
                if (!officeFormats.contains(input_extension)) {
                    command(String.format("\"%s\" -s \"%s\" -o \"%s\"", pandoc_path, input_file, temp));
                    check = true;
                }
                command = String.format("%s --headless --convert-to %s \"%s\" --outdir \"%s\"",
                        libre_office_path, output_extension, check ? temp : input_file, output_path);

            } else {
                command = String.format("\"%s\" -s \"%s\" -o \"%s\"", pandoc_path, input_file, output_file);
            }
            command(command);
            System.out.println("Conversion completed successfully");
            return true;
        } catch (
                Exception e) {
            System.out.println("Error during conversion: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (check && new File(temp).exists())
                new File(temp).delete();
            lock.unlock();
        }
    }
}

