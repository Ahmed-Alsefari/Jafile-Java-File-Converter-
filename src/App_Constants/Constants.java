package App_Constants;

import java.util.Set;

public class Constants {
    public static final Set<String> officeFormats = Set.of("pdf", "doc", "odt", "rtf", "epub", "fodt", "xml", "xls", "docx");
    public static final Set<String> imageFormats = Set.of("jpg", "jpeg", "png", "bmp", "gif", "tiff", "webp", "heic", "avif");
    public static final Set<String> audioFormats = Set.of("mp3", "wav", "ogg", "flac", "aac", "m4a", "wma", "alac", "opus", "amr", "aiff");
    public static final Set<String> videoFormats = Set.of("mp4", "mkv", "avi", "mov", "flv", "wmv", "webm", "mpeg", "3gp", "ts", "m4v");

    public static final String libre_office_path  = "tools/libreoffice/program/soffice.exe";
    public static final String ffmpeg_path = "tools/ffmpeg/bin/ffmpeg.exe";
    public static final String image_magick_path = "tools/imagemagick/magick.exe";
    public static final String pandoc_path = "tools/pandoc/pandoc.exe";

}
