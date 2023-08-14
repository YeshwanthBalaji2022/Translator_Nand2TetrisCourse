import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Code {
    static int r3Counter = 0;
    static int sysinitcounter = 0;

    public static void translateCommands(List<String> commandList1, List<String> commandList2, Translator outputFile,
                                         List<String> functionNames, String projectPath) {
        String[] command;
        for (int i = 0; i < commandList1.size(); i++) {
            command = commandList1.get(i).split(" ");
            for (int j = 0; j < commandList2.size(); j++) {
                if (commandList2.get(j).contains(command[0])) {
                    if (command[0].equals("add")) {
                        outputFile.add();
                    } else if (command[0].equals("sub")) {
                        outputFile.sub();
                    } else if (command[0].equals("and")) {
                        outputFile.and();
                    } else if (command[0].equals("call")) {
                        outputFile.WriteCall(command[1], command[2]);
                    } else if (command[0].equals("return")) {
                        r3Counter = r3Counter - 1;
                        outputFile.WriteReturn();
                    } else if (command[0].equals("function")) {
                        r3Counter = r3Counter + 1;
                        outputFile.writeFunction(command[1], command[2]);
                    } else if (command[0].equals("not")) {
                        outputFile.not();
                    } else if (command[0].equals("or")) {
                        outputFile.or();
                    } else if (command[0].equals("neg")) {
                        outputFile.neg();
                    } else if (command[0].equals("eq")) {
                        outputFile.eq();
                    } else if (command[0].equals("gt")) {
                        outputFile.gt();
                    } else if (command[0].equals("lt")) {
                        outputFile.lt();
                    } else if (command[0].equals("if-goto")) {
                        outputFile.IF_GOTO(functionNames.get(r3Counter), command[1]);
                    } else if (command[0].equals("goto")) {
                        outputFile.GOTO(command[1]);
                    } else if (command[0].equals("label")) {
                        outputFile.Label(command[1]);
                    } else if (command[0].equals("push")) {
                        if (command[1].equals("argument")) {
                            outputFile.Push_Argument(command[2]);
                        } else if (command[1].equals("static")) {
                            outputFile.Push_Static(command[2], projectPath);
                        } else if (command[1].equals("local")) {
                            outputFile.Push_Local(command[2]);
                        } else if (command[1].equals("temp")) {
                            outputFile.Push_Temp(command[2]);
                        } else if (command[1].equals("this")) {
                            outputFile.Push_This(command[2]);
                        } else if (command[1].equals("that")) {
                            outputFile.Push_That(command[2]);
                        } else if (command[1].equals("pointer")) {
                            if (command[2].equals("0")) {
                                outputFile.Push_Pointer_This();
                            } else {
                                outputFile.Push_Pointer_That();
                            }
                        } else {
                            outputFile.Push_Constant(command[2]);
                        }

                    } else if (command[0].equals("pop")) {
                        if (command[1].equals("argument")) {
                            outputFile.Pop_Argument(command[2]);
                        } else if (command[1].equals("static")) {
                            outputFile.Pop_Static(command[2], projectPath);
                        } else if (command[1].equals("local")) {
                            outputFile.Pop_Local(command[2]);
                        } else if (command[1].equals("temp")) {
                            outputFile.Pop_Temp(command[2]);
                        } else if (command[1].equals("this")) {
                            outputFile.Pop_This(command[2]);
                        } else if (command[1].equals("that")) {
                            outputFile.Pop_That(command[2]);
                        } else if (command[1].equals("pointer")) {
                            if (command[2].equals("0")) {
                                outputFile.Pop_Pointer_This();
                            } else {
                                outputFile.Pop_Pointer_That();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void translateFile(String inputFilePath) {
        try {
            File[] listOfFiles;
            List<String> outputList = new ArrayList<String>();
            Translator outputFile = new Translator(outputList);
            String filePath = inputFilePath;
            filePath = filePath.replace("\\", "/");
            String[] filePathParts = filePath.split("/");

            String intermediatePath = "";
            String finalPath = "";
            for (int i = 0; i < filePathParts.length - 1; i++) {
                intermediatePath = intermediatePath + filePathParts[i] + "/";
            }
            File inputDirectory = new File(intermediatePath);

            if (filePath.contains(".vm")) {
                RandomAccessFile file = new RandomAccessFile(filePath, "r");
                String str;
                List<String> commandList1 = new ArrayList<String>();
                List<String> commandList2 = new ArrayList<String>();
                List<String> tempList = new ArrayList<String>();
                String[] fileNameParts = filePath.split(".vm");

                commandList2.add("add");
                commandList2.add("sub");
                commandList2.add("neg");
                commandList2.add("push");
                commandList2.add("pop");
                commandList2.add("not");
                commandList2.add("or");
                commandList2.add("and");
                commandList2.add("eq");
                commandList2.add("lt");
                commandList2.add("gt");
                commandList2.add("goto");
                commandList2.add("label");
                commandList2.add("if-goto");
                commandList2.add("call");
                commandList2.add("function");
                commandList2.add("return");
                String[] command;
                List<String> functionNames = new ArrayList<String>();
                while ((str = file.readLine()) != null) {
                    if (!str.isEmpty()) {
                        command = str.split("//");
                        if (command.length > 0) {
                            if (!command[0].isEmpty()) {
                                str = command[0];
                                str = str.trim();
                                commandList1.add(str);
                            }
                        }
                    }
                }
                for (int lo = 0; lo < commandList1.size(); lo++) {
                    command = commandList1.get(lo).split(" ");
                    if (command[0].contains("function")) {

                        functionNames.add(command[1]);
                    }
                }
                listOfFiles = inputDirectory.listFiles();
                for (File file1 : listOfFiles) {
                    if (file1.isFile() && file1.getName().contains(".vm")) {
                        String fileName = file1.getPath();
                        fileName = fileName.replace("\\", "/");
                        String[] filePathParts1 = fileName.split("/");
                        filePathParts1 = filePathParts1[filePathParts1.length - 1].split(".vm");
                        String finalFileName = filePathParts1[0];

                        RandomAccessFile filex = new RandomAccessFile(fileName, "r");
                        while ((str = filex.readLine()) != null) {
                            if (!str.isEmpty()) {
                                command = str.split("//");
                                if (command.length > 0) {
                                    if (!command[0].isEmpty()) {
                                        str = command[0];
                                        str = str.trim();
                                        tempList.add(str);
                                    }
                                }
                            }
                        }
                        for (int lo = 0; lo < tempList.size(); lo++) {
                            command = tempList.get(lo).split(" ");
                            if (command[0].contains("function")) {
                                functionNames.add(command[1]);
                            }
                        }

                        if (functionNames.size() > 1 && sysinitcounter == 0) {
                            sysinitcounter = sysinitcounter + 1;
                            outputFile.bootstrap();
                        }

                        translateCommands(tempList, commandList2, outputFile, functionNames, finalFileName);
                        tempList.clear();
                    }
                }
                Path outputFilePath = Path.of(intermediatePath +"/"+ filePathParts[filePathParts.length - 2] + ".asm");
                Files.write(outputFilePath, outputList);
                file.close();
                System.out.println("VM code is translated Successfully");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to build asm code, Errors");
        }

    }

    public static void main(String[] args) {
        String filePath = "A:/OneDrive - Amrita Vishwa Vidyapeetham/S2/EOC - 2/nand2tetris/nand2tetris/projects/08/FunctionCalls/StaticsTest";
        if (filePath.contains(".vm")) {
            translateFile(filePath);
        } else {
            File[] listOfFiles;
            File directory = new File(filePath);
            listOfFiles = directory.listFiles();
            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getPath().contains("Sys.vm")) {
                        String path = file.getPath();
                        translateFile(path);
                        break;
                    }
                }
            }
            else {
                System.out.println("No such directory is found. Recheck the directory path");
            }
        }
    }
}

