/*
 *
 * This program was Written by Nicolas Amselle for use by the Nano-NLP lab
 * it is a graphical wrapper program that handles running the lab's PDF to TXT converter
 * the latest changes made 4/2/22
 *
 * Version 1.2.0
 *
*/

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

public class pdf2txtGUI extends JFrame{
    // this actually does the GUI program itself
    public static void main(String[] args) {
        // check if pdf2txt is already installed, install if it isn't
        String userName = System.getProperty("user.name");
        String filePath = "/home/" + userName + "/Packages/pdf2txt_venv_gui";
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            Install();
        }

        // create the window itself
        JFrame window = new JFrame("pdf2txt graphical");
        window.setSize(590, 360);
        window.setLayout(null);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create the entry fields for the input and output file paths
        //infile
        JTextField infile = new JTextField("input file path");
        infile.setBounds(20, 50, 400, 20);
        infile.setSize(infile.getWidth(), infile.getHeight() * 2);
        infile.setFont(new Font(infile.getFont().getFontName(), Font.PLAIN, 18));
        infile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                infile.setText(null);
            }
        });
        window.add(infile);

        //outfile
        JTextField outfile = new JTextField("output file path");
        outfile.setBounds(20, 125, 400, 20);
        outfile.setSize(outfile.getWidth(), outfile.getHeight() * 2);
        outfile.setFont(new Font(outfile.getFont().getFontName(), Font.PLAIN, 18));
        outfile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                outfile.setText(null);
            }
        });
        window.add(outfile);

        /*
         *  This listener makes it so that if the user clicks on the text field and erases the prompt,
         *  they can simply click on the background and, if the field is empty, it will simply be refilled with the prompt
         */
        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (outfile.getText().equals("")) {
                    outfile.setText("output file path");
                }
                if (infile.getText().equals("")) {
                    infile.setText("input file path");
                }
            }
        });

        //in addition to the text fields, create buttons that let the user call the file explorer tab
        JButton addFileIn = new JButton("Browse");
        addFileIn.setBounds(430, 50, 120, 40);
        addFileIn.setFont(new Font(addFileIn.getFont().getFontName(), Font.PLAIN, 18));
        window.add(addFileIn);
        addFileIn.addActionListener(e -> infile.setText(FileExplorer(false)));


        JButton addFileOut = new JButton("Browse");
        addFileOut.setBounds(430, 125, 120, 40);
        addFileOut.setFont(new Font(addFileOut.getFont().getFontName(), Font.PLAIN, 18));
        window.add(addFileOut);
        addFileOut.addActionListener(e -> outfile.setText(FileExplorer(true)));


        //create a checkbox for whole file or partial file conversion
        JCheckBox wholeFile = new JCheckBox("Convert whole folder", true);
        wholeFile.setBounds(168, 180, 350, 45);
        wholeFile.setFont(new Font(wholeFile.getFont().getFontName(), Font.PLAIN, 20));
        window.add(wholeFile);

        // the run button to execute the program
        JButton run = new JButton("Convert");
        run.setBounds(200, 255, 150, 40);
        run.setFont(new Font(run.getFont().getFontName(), Font.PLAIN, 20));
        window.add(run);
        run.addActionListener(e -> run(infile.getText(), outfile.getText(), wholeFile.isSelected()));

        //show the window
        window.setVisible(true);
    }

    //this is a method that creates a file explorer window for the user to select the filepath instead of having to type it out
    public static String FileExplorer(boolean isOutput){

        JFileChooser explorer = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        //only allow whole directories to be used for the output file path
        if (isOutput){explorer.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);}
        else {explorer.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);}

        explorer.showSaveDialog(null);
        return explorer.getSelectedFile().getPath();
    }

    //this method handles the installation of pdf2txt if it is not already installed
    public static void Install(){
        // create a window to tell the user what's going on
        JFrame install_window = new JFrame("Installing...");
        install_window.setSize(550,200);
        install_window.setLayout(null);
        install_window.setLocationRelativeTo(null);

        //put text in there
        JLabel info = new JLabel("Installing pdf2txt GUI, please wait...");
        info.setBounds(60, 70, 540, 20);
        info.setFont(new Font(info.getFont().getFontName(), Font.PLAIN, 20));
        install_window.add(info);

        install_window.setVisible(true);

        //run the bash script to install what we need and build the file structure
        try {
            Process p = Runtime.getRuntime().exec("bash ./gui_wrapper i");
            p.waitFor();
            p.destroy();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

        install_window.setVisible(false);
    }

    //this method handles running the program. it is set apart from the rest for readability reasons.
    public static void run(String inPath, String outPath, boolean wholeFile){
        String convertDirectory = "-f";

        //build the message to tell the user what happened
        JFrame popup = new JFrame();
        popup.setSize(500, 300);
        popup.setLayout(null);
        popup.setLocationRelativeTo(null);
        JTextArea popupMessage = new JTextArea();
        popupMessage.setLineWrap(true);
        popupMessage.setEditable(false);
        JLabel temp = new JLabel();
        popupMessage.setBackground(temp.getBackground()); // this is incredibly stupid, unimaginably smooth brained. but it works.
        popupMessage.setBounds(25, 100, 450, 40);

        //this logic handles errors detected before attempting the conversion.
        //trying to convert an empty directory
        boolean hasFiles = true;
        if(wholeFile) {
            try {
                hasFiles = Files.list(Paths.get(inPath)).findAny().isPresent();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (!hasFiles) {
            popupMessage.setText("Conversion error: input filepath contains no convertible files.");
            popup.add(popupMessage);
        }

        //make sure "whole directory" isn't selected while trying to convert only a single file
        else if (wholeFile && inPath.contains(".pdf")){
            popupMessage.setText("conversion error: whole directory selected, but file path refers to single file");
            popup.add(popupMessage);
        }

        //make sure "whole directory" IS selected when the filepath doesn't refer to a single file
        else if (!wholeFile && !inPath.contains(".pdf")){
            popupMessage.setText("conversion error: whole directory is not selected, but the file path refers to an entire directory.");
            popup.add(popupMessage);
        }

        //verify that the output file path is valid
        else if (!Files.exists(Paths.get(outPath))){
            popupMessage.setText("conversion error: no or invalid output path selected. Please specify a valid path.");
            popup.add(popupMessage);
        }


        //this else will run if no errors are found prior to the run attempt
        else {
            //define the args
            if (wholeFile) {
                convertDirectory = "-d";
            }
            String runScript = ("bash ./gui_wrapper " + convertDirectory + " " + inPath + " -o " + outPath);
            System.out.println(runScript);

            //this calls the wrapper to run it
            try {
                Process p = Runtime.getRuntime().exec(runScript);
                p.waitFor();
                p.destroy();

                //check that there was actually something output to the output destination
                File finalDestination = new File(outPath);
                if(Objects.requireNonNull(finalDestination.list()).length>0){ //there IS something in the output destination
                    popupMessage.setText("Conversion complete! converted file(s) located at " + outPath);
                }
                else{ //there isn't and some kind of problem happened
                    popupMessage.setText("Conversion error: no files converted, reason unknown.");
                }
                popup.add(popupMessage);

            } catch (IOException | InterruptedException ex) {
                //this will happen if there's a failure with actually running the conversion
                ex.printStackTrace();
                popupMessage.setText("Conversion failed! check stack trace.");
                popup.add(popupMessage);
            }
        }

        //show the results window to the user.
        popup.setVisible(true);
    }
}
