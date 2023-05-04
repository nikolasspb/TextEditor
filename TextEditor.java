package editor;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {

    List<Integer> start = new ArrayList<>();
    List<Integer> end = new ArrayList<>();
    JTextArea textArea = new JTextArea();
    JTextField textField = new JTextField();
    JFileChooser fileChooser = new JFileChooser();
    JCheckBox useRegExCheckbox = new JCheckBox("Use regex");

    public TextEditor() {
        super("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 330);
        String imagePath = "Text Editor\\task\\src\\editor\\";
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu searchMenu = new JMenu("Search");

        fileMenu.setName("MenuFile");
        searchMenu.setName("MenuSearch");
        JMenuItem loadMenuItem = new JMenuItem("Open");
        loadMenuItem.setName("MenuOpen");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setName("MenuSave");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setName("MenuExit");
        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        JMenuItem menuStartSearch = new JMenuItem("Start search");
        menuStartSearch.setName("MenuStartSearch");
        menuStartSearch.addActionListener(a -> startSearch());

        JMenuItem menuPreviousMatch = new JMenuItem("Previous match");
        menuPreviousMatch.setName("MenuPreviousMatch");
        menuPreviousMatch.addActionListener(a -> previousMatch());

        JMenuItem menuNextMatch = new JMenuItem("Next match");
        menuNextMatch.setName("MenuNextMatch");
        menuNextMatch.addActionListener(a -> nextMatch());

        JMenuItem menuUseRegExp = new JMenuItem("Use regular expression");
        menuUseRegExp.setName("MenuUseRegExp");
        menuUseRegExp.addActionListener(a -> useRegExp());

        searchMenu.add(menuStartSearch);
        searchMenu.add(menuPreviousMatch);
        searchMenu.add(menuNextMatch);
        searchMenu.add(menuUseRegExp);
        menuBar.add(searchMenu);

        loadMenuItem.addActionListener(a -> openFile());
        saveMenuItem.addActionListener(a -> saveFile());

        exitMenuItem.addActionListener(a -> System.exit(0));
        textArea.setName("TextArea");
        int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        JScrollPane js = new JScrollPane(textArea, v, h);
        js.setPreferredSize(new Dimension(395, 200));
        js.setName("ScrollPane");
        textField.setName("SearchField");
        textField.setPreferredSize(new Dimension(140, 25));
        ImageIcon loadIcon = new ImageIcon(new ImageIcon(imagePath + "Open.png").getImage().getScaledInstance(23, 23, Image.SCALE_DEFAULT));
        JButton loadButton = new JButton();
        loadButton.setName("OpenButton");
        loadButton.setIcon(loadIcon);
        loadButton.setPreferredSize(new Dimension(25, 25));
        loadButton.addActionListener(a -> openFile());

        fileChooser.setName("FileChooser");

        ImageIcon saveIcon = new ImageIcon(new ImageIcon(imagePath + "Save.png").getImage().getScaledInstance(23, 23, Image.SCALE_DEFAULT));
        JButton saveButton = new JButton();
        saveButton.setName("SaveButton");
        saveButton.setIcon(saveIcon);
        saveButton.setPreferredSize(new Dimension(25, 25));
        saveButton.addActionListener(a -> saveFile());

        ImageIcon searchIcon = new ImageIcon(new ImageIcon(imagePath + "Search.png").getImage().getScaledInstance(23, 23, Image.SCALE_DEFAULT));
        JButton startSearchButton = new JButton();
        startSearchButton.setName("StartSearchButton");
        startSearchButton.setIcon(searchIcon);
        startSearchButton.setPreferredSize(new Dimension(25, 25));
        startSearchButton.addActionListener(a -> startSearch());

        ImageIcon previousIcon = new ImageIcon(new ImageIcon(imagePath + "Previous.png").getImage().getScaledInstance(23, 23, Image.SCALE_DEFAULT));
        JButton previousMatchButton = new JButton();
        previousMatchButton.setName("PreviousMatchButton");
        previousMatchButton.setIcon(previousIcon);
        previousMatchButton.setPreferredSize(new Dimension(25, 25));
        previousMatchButton.addActionListener(a -> previousMatch());

        ImageIcon nextIcon = new ImageIcon(new ImageIcon(imagePath + "Next.png").getImage().getScaledInstance(23, 23, Image.SCALE_DEFAULT));
        JButton nextMatchButton = new JButton();
        nextMatchButton.setName("NextMatchButton");
        nextMatchButton.setIcon(nextIcon);
        nextMatchButton.setPreferredSize(new Dimension(25, 25));
        nextMatchButton.addActionListener(a -> nextMatch());

        useRegExCheckbox.setName("UseRegExCheckbox");

        setJMenuBar(menuBar);
        add(fileChooser);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(loadButton);
        panel.add(saveButton);
        panel.add(textField);
        panel.add(startSearchButton);
        panel.add(previousMatchButton);
        panel.add(nextMatchButton);
        panel.add(useRegExCheckbox);
        add(panel, BorderLayout.NORTH);
        add(js, BorderLayout.CENTER);
        setVisible(true);
    }

    void saveFile() {
        String text = textArea.getText();
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                FileOutputStream fos = new FileOutputStream(selectedFile.getAbsolutePath());
                byte[] b = text.getBytes();
                fos.write(b);
                fos.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void openFile() {
        var returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            textArea.setText("");
            try {
                InputStream stream = Files.newInputStream(Paths.get(selectedFile.getAbsolutePath()));
                String contents = new String(stream.readAllBytes());
                textArea.setText(contents);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void startSearch() {
        String foundText = textField.getText();
        Pattern javaPattern = Pattern.compile(foundText, useRegExCheckbox.isSelected() ? Pattern.CASE_INSENSITIVE : Pattern.LITERAL);
        Matcher matcher = javaPattern.matcher(textArea.getText());
        start.clear();
        end.clear();
        while (matcher.find()) {
            int indexRegex = matcher.start();
            start.add(indexRegex);
            end.add(indexRegex + matcher.group().length());
        }
        if (start.size() > 0) {
            selectWord(0);
        }
    }

    void previousMatch() {
        if (start.size() > 0) {
            selectWord(start.size() - 1);
        }
    }

    void nextMatch() {
        if (start.size() > 0) {
            selectWord(0);
        }
    }

    void useRegExp() {
        useRegExCheckbox.setSelected(!useRegExCheckbox.isSelected());
    }

    void selectWord(int ind) {
        textArea.setCaretPosition(end.get(ind));
        textArea.select(start.get(ind), end.get(ind));
        textArea.grabFocus();
        start.remove(ind);
        end.remove(ind);
    }
}