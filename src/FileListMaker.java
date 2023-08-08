import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.PrintWriter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import javax.swing.JFileChooser;
public class FileListMaker {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<String> arrList = new ArrayList<>();
        String answer = "";
        boolean test = true;
        boolean needsToBeSaved  = false;
        String fileName = "";
        do {
            answer = printMenu(in, arrList);
            switch (answer) {
                case "A":
                    addToList(in, arrList);
                    needsToBeSaved  = true;
                    break;
                case "C":
                    clearList(arrList);
                    needsToBeSaved  = true;
                    break;
                case "D":
                    deleteFromList(in, arrList);
                    needsToBeSaved  = true;
                    break;
                case "O":
                    fileName = openListFile(in, arrList, needsToBeSaved );
                    break;
                case "S":
                    saveCurrentFile(arrList, fileName);
                    needsToBeSaved  = false;
                    break;
                case "V":
                    displayList(arrList);
                    break;
                case "Q":
                    if (SafeInput.getYNConfirm(in, "Are you sure you want to quit (YorN): ")) {
                        if (needsToBeSaved ) {
                            saveCurrentFile(arrList, fileName);
                        }
                        test = false;
                    }
                    break;
            }
        } while (test);
    }

    public static void addToList(Scanner in, ArrayList arrList) {
        String addedItem = SafeInput.getNonZeroLenString(in, "What item would you like to add: ");
        arrList.add(addedItem);
    }

    private static String printMenu(Scanner in, ArrayList arrList) {
        if (arrList.isEmpty()) {
            System.out.println("Your current list is empty");
        } else {
            System.out.println("Current list:");
            for (int i = 0; i < arrList.size(); i++) {
                System.out.printf("    %d. %s\n", i + 1, arrList.get(i));
            }
        }
        return SafeInput.getRegExString(in, "    A: Add\n    C: Clear\n    D: Delete\n    O: Open\n    S: Save\n    V: View\n    Q: Quit\n ", "[AaCcDdOoSsVvQq]").toUpperCase();
    }
    public static void displayList(ArrayList arrList) {
        for (int i = 0; i < arrList.size(); i++) {
            System.out.println(arrList.get(i));
        }
    }
    public static void clearList(ArrayList arrList) {
        arrList.clear();
    }
    public static void deleteFromList(Scanner in, ArrayList arrList) {
        int itemDeleted = SafeInput.getRangedInt(in, "What item do you want to delete", 1, arrList.size());
        arrList.remove(itemDeleted - 1);
        System.out.println(itemDeleted + " was successfully removed!");
    }
    public static void saveCurrentFile(ArrayList arrList, String fileName) {
        PrintWriter outFile;
        Path target = new File(System.getProperty("user.dir")).toPath();
        if (fileName.equals("")) {
            target = target.resolve("src\\list.txt");
        } else {
            target = target.resolve(fileName);
        }

        try {
            outFile = new PrintWriter(target.toString());
            for (int i = 0; i < arrList.size(); i++) {
                outFile.println(arrList.get(i));
            }
            outFile.close();
            System.out.printf("File \"%s\" saved!\n", target.getFileName());
        } catch (IOException e) {
            System.out.println("IOException Error");
        }
    }
    private static String openListFile(Scanner in, ArrayList arrList, boolean needsToBeSaved ) {
        if (needsToBeSaved ) {
            String prompt = "Opening a new list will result in losing your current one, are you sure you want to open a new one (Y or N): ";
            boolean YN = SafeInput.getYNConfirm(in, prompt);
            if (!YN) {
                return "";
            }
        }
        clearList(arrList);
        Scanner inFile;
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        chooser.setFileFilter(filter);
        String line;
        Path target = new File(System.getProperty("user.dir")).toPath();
        target = target.resolve("src");
        chooser.setCurrentDirectory(target.toFile());
        try {
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                target = chooser.getSelectedFile().toPath();
                inFile = new Scanner(target);
                System.out.println("Opening File: " + target.getFileName());
                while (inFile.hasNextLine()) {
                    line = inFile.nextLine();
                    arrList.add(line);
                }
                inFile.close();
            } else {
                System.out.println("You must select a file");
            }
        } catch (IOException e) {
            System.out.println("IOException Error");
        }
        return target.toFile().toString();
    }
}

