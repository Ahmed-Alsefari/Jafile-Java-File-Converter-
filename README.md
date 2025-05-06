# 📁 Jafile

Welcome to our project 🎉 

<img src="logo.png" alt="Jafile Logo" width="300"/>

This project was developed using **Java** along with **Swing** to create a simple and user-friendly graphical interface.

---

## 📝 Description
The **Jafile(Java File Converter)** is a versatile tool designed to easily transform files between a wide range of formats.  
It provides a user-friendly interface that supports the conversion of:

- 📄 **Document Formats:**  
  `pdf`, `doc`, `docx`, `odt`, `fodt`, `rtf`, `txt`, `xml`
  
- 📚 **E‑book Formats:**  
  `epub`, `fb2`
  
- 📊 **Spreadsheet Formats:**  
  `xls`, `xlsx`, `ods`, `csv`
  
- 🖼️ **Image Formats:**  
  `jpg`, `jpeg`, `png`, `bmp`, `gif`, `tiff`, `webp`, `heic`, `avif`
  
- 🎵 **Audio Formats:**  
  `mp3`, `wav`, `ogg`, `flac`, `aac`, `m4a`, `wma`, `alac`, `opus`, `amr`, `aiff`
  
- 🎬 **Video Formats:**  
  `mp4`, `mkv`, `avi`, `mov`, `flv`, `wmv`, `webm`, `mpeg`, `3gp`, `ts`, `m4v`
  
- ⚡ **Other Formats:**  
  `json`, `html`, `latex`, `markdown`

---

## 🛠 Requirements

To ensure proper file conversion across various formats, the application uses the following essential tools:

-  📄 **Pandoc** - For document and markdown file conversions. [Get Pandoc](https://github.com/jgm/pandoc/releases/tag/3.6.4)
-  🖇️ **LibreOffice** - For advanced document, spreadsheet, and presentation conversions. [Get libreoffice](https://www.libreoffice.org/download/download-libreoffice/)
-  🖼️ **ImageMagick** - For image format conversions and optimizations. [Get imagemagick](https://imagemagick.org/script/download.php)
-  🎞️ **FFmpeg** - For audio and video file format conversions. [Get ffmpeg](https://www.ffmpeg.org/download.html)
-  🐳 Docker & Docker Compose – Required to run the PostgreSQL database and manage pgAdmin easily via containers. [Get Docker Desktop](https://www.docker.com/products/docker-desktop/)

Get Docker Desktop (includes Docker Compose
### 📦 Included Tools

 All required tools are already packaged inside the project under the `tools/` directory.  
 When you run the application for the first time, the program automatically detects and configures these tools without requiring any additional installation or setup.

> ⚠️ **Important Note**  
> You’ll need to manually install **Docker & Docker Compose** on your system to enable database support.  
> 👉 [Get Docker Desktop](https://www.docker.com/products/docker-desktop/)

---

## 📥 Installation

*Follow these simple steps to set up the project:*

1. **Choose a folder** where you want to clone the project.

2. **Clone the repository**
    ```bash
    git clone https://github.com/Ahmed-Alsefari/Jafile-Java-File-Converter-.git
    ```

3. **Make sure Java is installed**
    ```bash
    java -version
    ```
    > Java 17 or higher is recommended.

4. **Open the project** using **IntelliJ IDEA** or any Java-supported IDE.

5. Run the GUI

Locate the JavaConverter class and run it to launch the main graphical interface of the application.

---

## 👥 Team Members

| Name               |
|--------------------|
| Faisal Aljuaid     |
| Ahmed Alsefari     |
| Ibrahem Alshikh    |
