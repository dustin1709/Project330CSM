import java.io.*;
import java.util.*;

/*
 * ISTE-330
 * Group Project
 * 2020-03-02
 *
 * Custom exception class
 */
final public class DLException extends Exception {
    // Next line makes the linter happy.
    private static final long serialVersionUID = -2810164612190751275L;
    private final String LOG_FILE_NAME = "error.log";

    public DLException(Exception e){
        super(e);
        writeLog(e.getMessage());
    }

    // public DLException(Exception e, String[][] s){
    //     super();
    //     StringBuilder sb = new StringBuilder();
    //     sb.append("\n\tError: " + e.getMessage() + '\n');
    //     for (int i = 0; i < s.length; i++){
    //         sb.append('\t' + s[i][0] + ": " + s[i][1] + '\n');
    //     }
    //     writeLog(sb.toString());
    // }

    public DLException(String s, Map<String, String> m){
        this(new Exception(s), m);
    }

    public DLException(Exception e, Map<String, String> m){
        super(e);
        StringBuilder sb = new StringBuilder();
        sb.append("\n\tError: " + e.getMessage() + '\n');
        Iterator<Map.Entry<String, String>> iterator = m.entrySet().iterator();
        Map.Entry<String, String> item;
        while(iterator.hasNext()){
            item = iterator.next();
            sb.append('\t' + item.getKey() + ": " + item.getValue() + '\n');
        }
        writeLog(sb.toString());
    }

    private void writeLog(String s){
        File file = new File(LOG_FILE_NAME);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){
            bw.write(new Date().toString() + '\t' + s);
        } catch (IOException ioe) {
            System.out.println("Unable to write to a file " + file.getAbsolutePath());
        }
    }
}
