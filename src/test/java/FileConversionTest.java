package com.jafile.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileConversionTest {

    @Test
    void testPandocConversion() {
        Path inputPath = Paths.get("src/test/resources/input_sample.md");
        Path outputPath = Paths.get("src/test/resources/output_sample.txt");

        boolean success = ConvertFile.converFileThread(inputPath, outputPath);
        Path convertedOutput = Paths.get("src/test/resources/output_sample_converted.txt");

        assertTrue(success, "Pandoc conversion should succeed");
        assertTrue(new File(convertedOutput.toString()).exists(), "Output file should be created by Pandoc");
        try {
            Files.deleteIfExists(convertedOutput);
        } catch (IOException e) {
            System.err.println("IOException "+e);;
        }

    }

    @Test
    void testLibreOfficeConversion() {
        Path inputPath = Paths.get("src/test/resources/input_sample.docx");
        Path outputPath = Paths.get("src/test/resources/output_sample.pdf");

        boolean success = ConvertFile.converFileThread(inputPath, outputPath);
        Path convertedOutput = Paths.get("src/test/resources/output_sample_converted.pdf");

        assertTrue(success, "LibreOffice conversion should succeed");
        assertTrue(new File(convertedOutput.toString()).exists(), "Output file should be created by LibreOffice");
        try {
            Files.deleteIfExists(convertedOutput);
        } catch (IOException e) {
            System.err.println("IOException "+e);;
        }
    }

    @Test
    void testImageMagickConversion() {
        Path inputPath = Paths.get("src/test/resources/input_sample.png");
        Path outputPath = Paths.get("src/test/resources/output_sample.jpg");

        boolean success = ConvertFile.converFileThread(inputPath, outputPath);
        Path convertedOutput = Paths.get("src/test/resources/output_sample_converted.jpg");

        assertTrue(success, "ImageMagick conversion should succeed");
        assertTrue(new File(convertedOutput.toString()).exists(), "Output image should be created by ImageMagick");
        try {
            Files.deleteIfExists(convertedOutput);
        } catch (IOException e) {
            System.err.println("IOException "+e);;
        }
    }

    @Test
    void testFFmpegConversion() {
        Path inputPath = Paths.get("src/test/resources/input_sample.mp4");
        Path outputPath = Paths.get("src/test/resources/output_sample.mp3");

        boolean success = ConvertFile.converFileThread(inputPath, outputPath);
        Path convertedOutput = Paths.get("src/test/resources/output_sample_converted.mp3");

        assertTrue(success, "FFmpeg conversion should succeed");
        assertTrue(new File(convertedOutput.toString()).exists(), "Output audio should be created by FFmpeg");
        try {
            Files.deleteIfExists(convertedOutput);
        } catch (IOException e) {
            System.err.println("IOException "+e);;
        }
    }
}
