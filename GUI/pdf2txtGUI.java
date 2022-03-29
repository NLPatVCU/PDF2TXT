import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.IOException;
import java.nio.file.*;

public class pdf2txtGUI extends JFrame{
    //define instance variable

    // this actually does the GUI program itself
    public static void main(String[] args){
        // check if pdf2txt is already installed, install if it isn't
        String userName = System.getProperty("user.name");
        String filePath = "/home/" + userName + "/Packages/pdf2txt_venv_gui";
        Path path = Paths.get(filePath);
        if (!Files.exists(path)){Install();}

        // create the window itself
        JFrame window = new JFrame("pdf2txt graphical");
        window.setSize(650, 360);
        window.setLayout(null);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create the entry fields for the input and output file paths
        //infile
        JTextField infile = new JTextField("input file path");
        infile.setBounds(20, 50, 400, 20);
        infile.setSize(infile.getWidth(), infile.getHeight() * 2);
        infile.setFont(new Font(infile.getFont().getFontName(), Font.PLAIN, 18));
        window.add(infile);

        //outfile
        JTextField outfile = new JTextField("output file path");
        outfile.setBounds(20, 125, 400, 20);
        outfile.setSize(outfile.getWidth(), outfile.getHeight() * 2);
        outfile.setFont(new Font(outfile.getFont().getFontName(), Font.PLAIN, 18));
        window.add(outfile);

        //in addition to the text fields, create buttons that let the user call the file explorer tab
        JButton addFileIn = new JButton("Browse infile");
        addFileIn.setBounds(430, 50, 200, 40);
        addFileIn.setFont(new Font(addFileIn.getFont().getFontName(), Font.PLAIN, 18));
        window.add(addFileIn);
        addFileIn.addActionListener(e -> infile.setText(FileExplorer(false)));


        JButton addFileOut = new JButton("Browse outfile");
        addFileOut.setBounds(430, 125, 200, 40);
        addFileOut.setFont(new Font(addFileOut.getFont().getFontName(), Font.PLAIN, 18));
        window.add(addFileOut);
        addFileOut.addActionListener(e -> outfile.setText(FileExplorer(true)));


        //create a checkbox for whole file or partial file conversion
        JCheckBox wholeFile = new JCheckBox("Convert whole directory?");
        wholeFile.setBounds(168, 180, 350, 45);
        wholeFile.setFont(new Font(wholeFile.getFont().getFontName(), Font.PLAIN, 20));
        window.add(wholeFile);

        // the run button to execute the program
        JButton run = new JButton("Convert");
        run.setBounds(240, 255, 150, 40);
        run.setFont(new Font(run.getFont().getFontName(), Font.PLAIN, 20));
        window.add(run);


        // this will happen when the run button is pressed.
        //TODO: this is all one lambda expression. this is huge and hard to read here, make this into its own method
        run.addActionListener(e -> {
            //gets the args
            String inPath = infile.getText();
            String outPath = outfile.getText();
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
            if(wholeFile.isSelected()) {
                try {
                    hasFiles = Files.list(Paths.get(inPath)).findAny().isPresent();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (!hasFiles) {
                popupMessage.setText("Conversion error: input filepath is empty.");
                popup.add(popupMessage);
            }

            //make sure "whole directory" isn't selected while trying to convert only a single file
            else if (wholeFile.isSelected() && inPath.contains(".pdf")){
                popupMessage.setText("conversion error: whole directory selected, but file path refers to single file");
                popup.add(popupMessage);
            }


            //this else will run if no errors are found prior to the run attempt
            else {
                //define the args
                if (wholeFile.isSelected()) {
                    convertDirectory = "-d";
                }
                String runScript = ("bash ./gui_wrapper " + convertDirectory + " " + inPath + " -o " + outPath);
                System.out.println(runScript);

                //this calls the wrapper to run it
                try {
                    Process p = Runtime.getRuntime().exec(runScript);
                    p.waitFor();
                    p.destroy();

                    //this part tells the user it worked and shows them where the output is
                    popupMessage.setText("Conversion complete! converted file(s) located at" + outPath);
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
        });

        // make the completed window visible
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
                Process p;
                p = Runtime.getRuntime().exec("bash ./gui_wrapper i");
                p.waitFor();
                p.destroy();
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }

            install_window.setVisible(false);
        }
}