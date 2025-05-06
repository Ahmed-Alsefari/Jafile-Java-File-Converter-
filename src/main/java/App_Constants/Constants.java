package App_Constants;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class Constants {
    public static final Set<String> officeFormats = Set.of("pdf", "doc", "odt", "rtf", "epub", "fodt", "xml", "xls");
    public static final Set<String> imageFormats = Set.of("jpg", "jpeg", "png", "bmp", "gif", "tiff", "webp", "heic", "avif");
    public static final Set<String> audioFormats = Set.of("mp3", "wav", "ogg", "flac", "aac", "m4a", "wma", "alac", "opus", "amr", "aiff");
    public static final Set<String> videoFormats = Set.of("mp4", "mkv", "avi", "mov", "flv", "wmv", "webm", "mpeg", "3gp", "ts", "m4v");

    public static final Path libre_office_path = Paths.get("tools", "LibreOffice","App","libreoffice", "program", "soffice.exe");
    public static final Path ffmpeg_path = Paths.get("tools", "ffmpeg", "bin", "ffmpeg.exe").toAbsolutePath();
    public static final Path image_magick_path = Paths.get("tools", "imagemagick", "magick.exe");
    public static final Path pandoc_path = Paths.get("tools", "pandoc", "pandoc.exe");

}
