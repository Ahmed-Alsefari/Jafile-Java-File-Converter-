package GUIjava;
import App.FileConverterServer;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaConverter extends JFrame {
    // --- MAIN COMPONENTS AND FIELDS ---
    private JButton targetButton, destinationButton, showFormatsButton, convertButton;
    private JLabel pathTargetLabel, pathDestinationLabel;
    private JPanel dropPanel, mainPanel;
    private File selectedFile;
    private File destinationFile;
    private File outputFile; // Actual output file

    private JRadioButton pdfRadioButton, docxRadioButton, txtRadioButton, jpgRadioButton, mp3RadioButton, mp4RadioButton;
    private JTextField customFormatField;
    private ButtonGroup formatButtonGroup;
    private JPanel radioButtonPanel;

    ImageIcon logo = new ImageIcon("logo.png");

    // Set of valid formats for validation
    private final Set<String> validFormats = new HashSet<>(Arrays.asList(
            // Document formats
            "pdf", "doc", "docx", "odt", "fodt", "rtf", "txt", "xml", "latex", "bibtex",
            "biblatex", "commonmark", "creole", "djot", "docbook", "dokuwiki", "haddock", "jats",
            "jira", "man", "mdoc", "muse", "opml", "org", "pod", "rst", "markdown", "asciidoc",
            "context", "texinfo", "textile", "tikiwiki", "twiki", "typst", "vimwiki", "tei", "t2t",
            "icml", "markua", "xwiki", "zimwiki", "md",
            // Image formats
            "jpg", "jpeg", "png", "bmp", "gif", "tiff", "webp", "heic", "avif",
            // Audio formats
            "mp3", "wav", "ogg", "flac", "aac", "m4a", "wma", "alac", "opus", "amr", "aiff",
            // Video formats
            "mp4", "mkv", "avi", "mov", "flv", "wmv", "webm", "mpeg", "3gp", "ts", "m4v",
            // E-book formats
            "epub", "fb2",
            // Spreadsheet formats
            "xls", "xlsx", "ods", "csv", "tsv",
            // Other formats
            "json", "html", "ipynb", "native", "csljson", "ris", "endnotexml", "gfm", "opendocument",
            "plain", "pptx", "slideous", "slidy", "dzslides", "revealjs", "s5", "beamer"
    ));

    // --- CONSTRUCTOR AND INITIALIZATION ---
    public JavaConverter() {
        // Enable full Unicode support
        System.setProperty("file.encoding", "UTF-8");

        initComponents();
        setupDragAndDrop();
        setupListeners();
        configureFrame();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));

        //set icon logo
        setIconImage(logo.getImage());
        setVisible(true);

        // Set layout for main components
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(mainPanel.getBackground());

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(createFormatPanel(), BorderLayout.SOUTH);

        // Create drag and drop panel - now above manual selection
        dropPanel = createDragDropPanel();

        // Create manual selection panel
        JPanel manualPanel = createManualSelectionPanel();

        // Place drag and drop above manual selection
        centerPanel.add(dropPanel, BorderLayout.NORTH);
        centerPanel.add(manualPanel, BorderLayout.CENTER);
    }

    private void configureFrame() {
        setTitle("JaFile");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 700); // Slightly taller to accommodate the new radio buttons
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        setVisible(true);
    }

    private void setupListeners() {
        targetButton.addActionListener(e -> selectSourceFile());
        destinationButton.addActionListener(e -> selectDestinationFile());
        showFormatsButton.addActionListener(e -> showAllFormats());
        convertButton.addActionListener(e -> convertFile());
    }

    // --- UI CREATION METHODS ---
    private JPanel createHeaderPanel() {
        JLabel title = new JLabel(" [ Java File Converter ] ", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(70, 130, 180));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(mainPanel.getBackground());
        p.add(title, BorderLayout.CENTER);
        return p;
    }

    private JPanel createDragDropPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)), new EmptyBorder(40, 20, 40, 20)
        ));
        panel.setBackground(new Color(250, 250, 250));

        // Use html for better Arabic support
        JLabel dl = new JLabel("<html><div style='text-align: center;'>Drag & Drop File Here</div></html>", SwingConstants.CENTER);
        dl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        dl.setForeground(new Color(150, 150, 150));
        panel.add(dl, BorderLayout.CENTER);

        // Make minimum height for the panel
        panel.setPreferredSize(new Dimension(0, 120));
        return panel;
    }

    // In the createManualSelectionPanel() method, ensure the buttons have visible text:
    private JPanel createManualSelectionPanel() {
        JPanel manual = new JPanel();
        manual.setLayout(new BoxLayout(manual, BoxLayout.Y_AXIS));
        manual.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Manual Selection"), new EmptyBorder(10, 10, 10, 10)
        ));
        manual.setBackground(Color.WHITE);

        // Make sure text is visible by setting proper foreground color and opaque background
        targetButton = createStyledButton("Select Source File");
        targetButton.setForeground(Color.black); // Ensure text is visible
        targetButton.setOpaque(true); // Make button background visible

        pathTargetLabel = createPathLabel();
        manual.add(createFileSelectionRow(targetButton, pathTargetLabel));
        manual.add(Box.createVerticalStrut(15));

        destinationButton = createStyledButton("Select Destination Directory");
        destinationButton.setForeground(Color.black); // Ensure text is visible
        destinationButton.setOpaque(true); // Make button background visible

        pathDestinationLabel = createPathLabel();
        manual.add(createFileSelectionRow(destinationButton, pathDestinationLabel));

        return manual;
    }


    private JPanel createFormatPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Output Format"), new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);

        // radio buttons - now with 3 rows to include mp3 and mp4
        radioButtonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        radioButtonPanel.setBackground(Color.WHITE);
        pdfRadioButton = createFormatRadio(" .pdf", new Color(231, 76, 60));
        docxRadioButton = createFormatRadio(" .docx", new Color(46, 204, 113));
        txtRadioButton = createFormatRadio(" .txt", new Color(52, 152, 219));
        jpgRadioButton = createFormatRadio(" .jpg", new Color(155, 89, 182));
        mp3RadioButton = createFormatRadio(" .mp3", new Color(241, 196, 15));
        mp4RadioButton = createFormatRadio(" .mp4", new Color(230, 126, 34));

        formatButtonGroup = new ButtonGroup();
        formatButtonGroup.add(pdfRadioButton);
        formatButtonGroup.add(docxRadioButton);
        formatButtonGroup.add(txtRadioButton);
        formatButtonGroup.add(jpgRadioButton);
        formatButtonGroup.add(mp3RadioButton);
        formatButtonGroup.add(mp4RadioButton);

        radioButtonPanel.add(pdfRadioButton);
        radioButtonPanel.add(docxRadioButton);
        radioButtonPanel.add(txtRadioButton);
        radioButtonPanel.add(jpgRadioButton);
        radioButtonPanel.add(mp3RadioButton);
        radioButtonPanel.add(mp4RadioButton);

        // Set PDF as default selected
        pdfRadioButton.setSelected(true);

        // custom format & show all
        JPanel custom = new JPanel(new BorderLayout(5, 5));
        custom.setBackground(Color.WHITE);

        JLabel customLabel = new JLabel("Custom Format:");
        customLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        customFormatField = new JTextField();
        customFormatField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        customFormatField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)), new EmptyBorder(5, 5, 5, 5)
        ));

        // Add listener to handle radio button enabling/disabling
        customFormatField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateRadioButtons();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateRadioButtons();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateRadioButtons();
            }

            private void updateRadioButtons() {
                boolean isEmpty = customFormatField.getText().trim().isEmpty();
                setRadioButtonsEnabled(isEmpty);
            }
        });

        showFormatsButton = createStyledButton("Show All Formats");
        showFormatsButton.setBackground(new Color(100, 150, 200));
        showFormatsButton.setForeground(Color.black);


        custom.add(customLabel, BorderLayout.WEST);
        custom.add(customFormatField, BorderLayout.CENTER);
        custom.add(showFormatsButton, BorderLayout.EAST);

        convertButton = new JButton("Convert File");
        convertButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        convertButton.setBackground(new Color(70, 130, 180));
        convertButton.setForeground(Color.black);
        convertButton.setFocusPainted(false);
        convertButton.setBorder(new EmptyBorder(10, 30, 10, 30));

        convertButton.setContentAreaFilled(true);
        convertButton.setOpaque(true);

        JPanel btnWrap = new JPanel();
        btnWrap.setBackground(Color.WHITE);
        btnWrap.add(convertButton);

        panel.add(radioButtonPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(custom);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnWrap);

        return panel;
    }

    // --- UI HELPER METHODS ---
    private JButton createStyledButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setBackground(new Color(70, 130, 180));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(5, 15, 5, 15));
        b.setContentAreaFilled(true);
        b.setOpaque(true);

        return b;
    }

    private JLabel createPathLabel() {
        JLabel l = new JLabel("No file selected");
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(Color.DARK_GRAY);
        // Set text alignment to support Right-to-Left languages
        l.setHorizontalAlignment(SwingConstants.LEADING);
        return l;
    }

    private JPanel createFileSelectionRow(JButton btn, JLabel lbl) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(Color.WHITE);
        p.add(btn, BorderLayout.WEST);

        // Create a scroll pane for the path label with proper encoding support
        JScrollPane scrollPane = new JScrollPane(lbl);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(Color.WHITE);

        p.add(scrollPane, BorderLayout.CENTER);
        return p;
    }

    private JRadioButton createFormatRadio(String text, Color fg) {
        JRadioButton r = new JRadioButton(text);
        r.setBackground(Color.WHITE);
        r.setForeground(fg);
        r.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return r;
    }

    // Method to enable/disable and change opacity of radio buttons
    private void setRadioButtonsEnabled(boolean enabled) {
        // Make opacity 20% when disabled
        float opacity = enabled ? 1.0f : 0.2f;

        // Set all radio buttons enabled/disabled and change opacity
        for (Component c : radioButtonPanel.getComponents()) {
            if (c instanceof JRadioButton) {
                JRadioButton rb = (JRadioButton) c;
                rb.setEnabled(enabled);
                rb.setForeground(new Color(
                        rb.getForeground().getRed(),
                        rb.getForeground().getGreen(),
                        rb.getForeground().getBlue(),
                        (int) (255 * opacity)
                ));
            }
        }

        // If radio buttons are disabled, clear selection
        if (!enabled) {
            formatButtonGroup.clearSelection();
        } else if (formatButtonGroup.getSelection() == null) {
            // If enabling and no selection, select PDF by default
            pdfRadioButton.setSelected(true);
        }
    }

    // --- DRAG AND DROP FUNCTIONALITY ---
    private void setupDragAndDrop() {
        new DropTarget(dropPanel, new DropTargetAdapter() {
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    @SuppressWarnings("unchecked")
                    List<File> files = (List<File>) dtde.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);
                    if (!files.isEmpty()) {
                        selectedFile = files.get(0);
                        updatePathLabel(pathTargetLabel, selectedFile);
                        highlightDropPanel(true);
                        new Timer(800, e -> highlightDropPanel(false)).start();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(JavaConverter.this,
                            "Error processing dropped file: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            public void dragEnter(DropTargetDragEvent e) {
                highlightDropPanel(true);
            }

            public void dragExit(DropTargetEvent e) {
                highlightDropPanel(false);
            }
        });
    }

    private void highlightDropPanel(boolean hl) {
        dropPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(hl ? new Color(100, 200, 100) : new Color(200, 200, 200), 2),
                new EmptyBorder(40, 20, 40, 20)
        ));
    }

    // --- FILE SELECTION FUNCTIONALITY ---
    // Helper that sets native L&F on the chooser only while ensuring Unicode support
    private JFileChooser createNativeChooser(int dialogType, String title) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        JFileChooser chooser = new JFileChooser();
        SwingUtilities.updateComponentTreeUI(chooser);
        chooser.setDialogType(dialogType);
        chooser.setDialogTitle(title);

        // Ensure proper font for Arabic filenames in the file chooser
        updateComponentTreeFonts(chooser);

        return chooser;
    }

    // Set appropriate fonts for components to support Arabic text
    private void updateComponentTreeFonts(Container container) {
        // Use Tahoma which has good Arabic support and is available on most systems
        Font arabicFont = new Font("Tahoma", Font.PLAIN, 12);

        for (Component comp : container.getComponents()) {
            if (comp instanceof JComponent) {
                comp.setFont(arabicFont);
            }
            if (comp instanceof Container) {
                updateComponentTreeFonts((Container) comp);
            }
        }
    }

    private void selectSourceFile() {
        JFileChooser chooser = createNativeChooser(JFileChooser.OPEN_DIALOG, "Select Source File");
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            updatePathLabel(pathTargetLabel, selectedFile);
        }
    }

    private void selectDestinationFile() {
        JFileChooser chooser = createNativeChooser(JFileChooser.OPEN_DIALOG, "Choose Destination Directory");

        // Set file selection mode to directories only
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showDialog(this, "Select Directory") == JFileChooser.APPROVE_OPTION) {
            destinationFile = chooser.getSelectedFile();
            updatePathLabel(pathDestinationLabel, destinationFile);
        }
    }

    private void updatePathLabel(JLabel label, File file) {
        if (file == null) {
            label.setText("No file selected");
            label.setToolTipText(null);
            return;
        }

        try {
            // Get the canonical path to ensure proper encoding
            String path = file.getCanonicalPath();

            // Handle path display correctly to avoid encoding issues
            label.setText(path);
            label.setToolTipText(path);

            // Create a custom font that supports Arabic characters
            // We'll try several fonts that should support Arabic text
            String[] arabicFonts = {
                    "Arial Unicode MS",
                    "Simplified Arabic",
                    "Traditional Arabic",
                    "Tahoma",
                    "Lucida Sans Unicode"
            };

            // Try each font until we find one that can display the path properly
            boolean fontFound = false;
            for (String fontName : arabicFonts) {
                Font testFont = new Font(fontName, Font.PLAIN, 12);
                if (testFont.canDisplayUpTo(path) == -1) {
                    label.setFont(testFont);
                    fontFound = true;
                    break;
                }
            }

            // If no font works perfectly, use Tahoma as a fallback
            if (!fontFound) {
                label.setFont(new Font("Tahoma", Font.PLAIN, 12));
            }
        } catch (Exception e) {
            label.setText("Error displaying path");
            e.printStackTrace();
        }
    }

    // --- FORMAT SELECTION FUNCTIONALITY ---
    private String getSelectedFormat() {
        String c = customFormatField.getText().trim().toLowerCase();
        if (!c.isEmpty()) return c;
        if (pdfRadioButton.isSelected()) return "pdf";
        if (docxRadioButton.isSelected()) return "docx";
        if (txtRadioButton.isSelected()) return "txt";
        if (jpgRadioButton.isSelected()) return "jpg";
        if (mp3RadioButton.isSelected()) return "mp3";
        if (mp4RadioButton.isSelected()) return "mp4";
        return "";
    }

    private void showAllFormats() {
        JTextArea area = new JTextArea(
                "# Document Formats:\n" +
                        "\tpdf, doc, docx, odt, fodt, rtf, txt, xml, latex, bibtex, \n\tbiblatex, " +
                        "commonmark, creole, djot, docbook, dokuwiki, haddock, jats, \n\tjira, " +
                        "man, mdoc, muse, opml, org, pod, rst, markdown, asciidoc, \n\tcontext, " +
                        "texinfo, textile, tikiwiki, twiki, typst, vimwiki, tei, t2t, \n\t" +
                        "icml, markua, xwiki, zimwiki\n\n" +
                        "# Image Formats:\n" +
                        "\tjpg, jpeg, png, bmp, gif, tiff, webp, heic, avif\n\n" +
                        "# Audio Formats:\n" +
                        "\tmp3, wav, ogg, flac, aac, m4a, wma, alac, opus, amr, aiff\n\n" +
                        "# Video Formats:\n" +
                        "\tmp4, mkv, avi, mov, flv, wmv, webm, mpeg, 3gp, ts, m4v\n\n" +
                        "# E-book Formats:\n" +
                        "\tepub, fb2\n\n" +
                        "Spreadsheet Formats:\n" +
                        "\txls, xlsx, ods, csv, tsv\n\n" +
                        "# Other Formats:\n" +
                        "\tjson, html, ipynb, native, csljson, ris, endnotexml, " +
                        "gfm, opendocument, \n\tplain, pptx, slideous, slidy, " +
                        "dzslides, revealjs, s5, beamer"
        );
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(570, 450));
        JOptionPane.showMessageDialog(this, sp, "Supported Formats", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- FILE CONVERSION FUNCTIONALITY ---
    private void convertFile() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please select a source file first.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String format = getSelectedFormat();
        if (format.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please choose an output format.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the format is valid
        if (!validFormats.contains(format.toLowerCase())) {
            JOptionPane.showMessageDialog(this,
                    "'" + format + "' is not a supported format.\n" +
                            "Please choose one of the supported formats shown in 'Show All Formats'.",
                    "Invalid Format", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get source file path as Path object
        Path inputPath = selectedFile.toPath();

        // Determine output file path
        Path outputPath;
        if (destinationFile == null) {
            // If destination not selected, create default destination in same directory as source
            String srcPath = selectedFile.getAbsolutePath();
            int dotIndex = srcPath.lastIndexOf('.');
            String basePath = dotIndex > 0 ? srcPath.substring(0, dotIndex) : srcPath;
            outputPath = Paths.get(basePath + "." + format);
            outputFile = outputPath.toFile();
        } else {
            // If destination directory is selected, create output file in that directory
            String fileName = selectedFile.getName();
            int dotIndex = fileName.lastIndexOf('.');
            String baseName = dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
            outputPath = Paths.get(destinationFile.getAbsolutePath(), baseName + "." + format);
            outputFile = outputPath.toFile();
        }

        // Update the destination label
        updatePathLabel(pathDestinationLabel, outputFile);

        // Show progress dialog
        JDialog progressDialog = createProgressDialog();
        progressDialog.setVisible(true);

        // Run the conversion in a background thread
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                try (
                        Socket socket = new Socket("localhost", 9000);
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
                ) {
                    out.writeObject(inputPath.toString());
                    out.writeObject(outputPath.toString());

                    return (Boolean) in.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void done() {
                progressDialog.dispose();
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(JavaConverter.this,
                                "Conversion completed successfully!\n" +
                                        "Output file: " + outputFile.getAbsolutePath(),
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(JavaConverter.this,
                                "Conversion failed. Please check the console for details.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(JavaConverter.this,
                            "Error during conversion: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }.execute();
    }

    private JDialog createProgressDialog() {
        JDialog dialog = new JDialog(this, "Converting...", true);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel messageLabel = new JLabel("Converting file, please wait...");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        panel.add(messageLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // Make the dialog modal but allow the worker thread to close it
        dialog.setModal(false);

        return dialog;
    }

    // --- MAIN METHOD ---
    public static void main(String[] args) {
        // Set default look and feel to system
         new Thread(() -> {
            new App.FileConverterServer().main(args);
        }).start();
        try {
            // Set UTF-8 encoding for the JVM
            System.setProperty("file.encoding", "UTF-8");
            // Set the default look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(JavaConverter::new);
    }


}
