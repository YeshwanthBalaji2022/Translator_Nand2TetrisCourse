import java.util.List;
public class Translator {
    List<String> FileList;
    private String infunction="";
    Translator(List<String> FileList) {
        this.FileList = FileList;
        this.labelGenerator = new LabelGenerator();

    }


    private LabelGenerator labelGenerator;
    public void writeInit() {
        this.FileList.add("@256\nD=A\n@SP\nM=D");
        WriteCall("Sys.init", "0");
    }
    public void bootstrap() {
        writeInit();
    }

    public void add() {
        this.FileList.add("@SP\nAM=M-1\nD=M\nA=A-1\nM=D+M");
    }

    public void sub() {
        this.FileList.add("@SP\nAM=M-1\nD=M\nA=A-1\nM=M-D");
    }

    public void neg() {
        this.FileList.add("@SP\nA=M-1\nD=M\nM=-D");
    }

    public void and() {
        this.FileList.add("@SP\nAM=M-1\nD=M\nA=A-1\nM=M&D");
    }

    public void or() {
        this.FileList.add("@SP\nAM=M-1\nD=M\nA=A-1\nM=M|D");
    }

    public void not() {
        this.FileList.add("@SP\nA=M-1\nM=!M");
    }

    public void eq() {
        String s = labelGenerator.getNextLabel();
        this.FileList.add(
                "@SP\nA=M-1\nD=M\n@SP\nM=M-1\nA=M-1\nD=M-D\n@" + s + "\nD;JEQ\n@SP\nA=M-1\nM=0\n@END" + s
                        + "\n0;JMP\n(" + s
                        + ")\n@SP\nA=M-1\nM=-1\n(END" + s + ")");
    }

    public void gt() {
        String s = labelGenerator.getNextLabel();
        this.FileList.add(
                "@SP\nA=M-1\nD=M\n@SP\nM=M-1\nA=M-1\nD=M-D\n@" + s + "\nD;JGT\n@SP\nA=M-1\nM=0\n@END" + s
                        + "\n0;JMP\n(" + s
                        + ")\n@SP\nA=M-1\nM=-1\n(END" + s + ")");

    }

    public void lt() {
        String s = labelGenerator.getNextLabel();
        this.FileList.add(
                "@SP\nA=M-1\nD=M\n@SP\nM=M-1\nA=M-1\nD=M-D\n@" + s + "\nD;JLT\n@SP\nA=M-1\nM=0\n@END" + s
                        + "\n0;JMP\n(" + s
                        + ")\n@SP\nA=M-1\nM=-1\n(END" + s + ")");
    }

    public void Push_Argument(String str) {
        this.FileList.add("@ARG\nD=M\n@" + str + "\nD=A+D\n@13\nAM=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1");
    }

    public void Push_Temp(String str) {
        this.FileList.add("@5\nD=A\n@" + str + "\nD=A+D\n@13\nAM=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1");
    }

    public void Push_Local(String str) {
        this.FileList.add("@" + str + "\nD=A\n@LCL\nD=D+M\n@13\nAM=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1");
    }

    public void Push_Static(String str,String filename) {
        if (Integer.parseInt(str) < 256 && Integer.parseInt(str) > -1) {
            this.FileList.add("@"+filename+"." + str + "\nD=M\n@SP\nM=M+1\nA=M-1\nM=D");
        }
    }

    public void Push_This(String str) {
        this.FileList.add("@" + str + "\nD=A\n@THIS\nD=M+D\n@13\nAM=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1");
    }

    public void Push_That(String str) {
        this.FileList.add("@" + str + "\nD=A\n@THAT\nD=D+M\n@13\nAM=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1");
    }

    public void Pop_This(String str) {
        this.FileList.add("@" + str + "\nD=A\n@THIS\nD=D+M\n@13\nM=D\n@SP\nAM=M-1\nD=M\n@13\nA=M\nM=D");
    }

    public void Pop_That(String str) {
        this.FileList.add("@" + str + "\nD=A\n@THAT\nD=D+M\n@13\nM=D\n@SP\nAM=M-1\nD=M\n@13\nA=M\nM=D");
    }

    public void Push_Constant(String str) {
        this.FileList.add("@" + str + "\nD=A\n@SP\nM=M+1\nA=M-1\nM=D");
    }

    public void Pop_Argument(String str) {
        this.FileList.add("@ARG\nD=M\n@" + str + "\nD=A+D\n@13\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@13\nA=M\nM=D");
    }

    public void Pop_Local(String str) {
        this.FileList.add("@" + str + "\nD=A\n@LCL\nD=M+D\n@13\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@13\nA=M\nM=D");
    }

    public void Pop_Static(String str,String filename) {
        if (Integer.parseInt(str) < 256 && Integer.parseInt(str) > -1) {
            this.FileList.add("@SP\nAM=M-1\nD=M\n@" + filename+"."+str + "\nM=D");
        }
    }

    public void Pop_Temp(String str) {
        this.FileList.add("@5\nD=A\n@" + str + "\nD=A+D\n@13\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@13\nA=M\nM=D");
    }

    public void IF_GOTO(String infuction,String str) {
        this.FileList.add("@SP\nA=M-1\nD=M\n@" + infuction.toUpperCase()+"$"+str + "\nD;JNE");
    }
    public void GOTO(String str) {
        this.FileList.add("@SP\nA=M-1\n@" + infunction+"$"+str.toUpperCase() + "\n0;JMP");
    }

    public void Label(String str) {
        this.FileList.add("(" + infunction+"$"+str + ")");
    }

    public void Push_Pointer_This() {
        this.FileList.add("@THIS\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1");
    }

    public void Push_Pointer_That() {
        this.FileList.add("@THAT\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1");
    }

    public void Pop_Pointer_This() {
        this.FileList.add("@SP\nM=M-1\nA=M\nD=M\n@THIS\nM=D");
    }

    public void Pop_Pointer_That() {
        this.FileList.add("@SP\nM=M-1\nA=M\nD=M\n@THAT\nM=D");
    }

    String Push_To_Stack() {
        return "@SP\nA=M\nM=D\n@SP\nM=M+1\n";
    }

    public void WriteCall(String function, String nArgs) {
        String s = labelGenerator.getNextLabel();
        this.FileList.add("@returnAddress" + s + "\nD=A\n" + Push_To_Stack() + "@LCL\nD=M\n" + Push_To_Stack() + "@ARG\nD=M\n"
                + Push_To_Stack() + "@THIS\nD=M\n" + Push_To_Stack() + "@THAT\nD=M\n" + Push_To_Stack()
                + "@SP\nD=M\n@5\nD=D-A\n@" + nArgs + "\nD=D-A\n@ARG\nM=D\n@SP\nD=M\n@LCL\nM=D\n@"
                + function.toUpperCase() + "\n0;JMP\n(returnAddress" + s + ")");
    }

    public void WriteReturn() {
        FileList.add("@LCL\nD=M\n@endFrame\nM=D\n");
        EndFrameMinus("retAddr", "5");
        Pop_Argument("0");
        FileList.add("@ARG\nD=M\nD=D+1\n@SP\nM=D\n");
        EndFrameMinus("THAT", "1");
        EndFrameMinus("THIS", "2");
        EndFrameMinus("ARG", "3");
        EndFrameMinus("LCL", "4");
        FileList.add("@retAddr\nA=M\n0;JMP");
    }

    public void EndFrameMinus(String string, String string2) {
        FileList.add("@endFrame\nD=M\n@"+string2+"\nD=D-A\nA=D\nD=M\n@"+string+"\nM=D\n");
    }

    public void writeFunction(String function,String nLocals){
        infunction = function.toUpperCase();
        FileList.add("("+function.toUpperCase()+")\n@"+nLocals+"\nD=A\n@n\nM=D\n");
        Label("LOOP");
        FileList.add("@n\nD=M\n@"+function.toUpperCase()+"$END_LOOP\nD;JLE\n");
        Push_Constant("0");
        FileList.add("@n\nM=M-1\n");
        GOTO("LOOP");
        FileList.add("\n");
        Label("END_LOOP");
    }


}


