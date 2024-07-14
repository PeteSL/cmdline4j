import java.io.*;
import java.util.*;

public class cmdline
{
    /**
     * Parse OS command line into individual command arg... strings. Uses
     * java.io.StreamTokenizer with number and comment parsing turned off.
     * 
     * @param fullcommand command line as it would appear in a batch/shell file.
     * @return List with command, arg1, arg2, ... in order for ProcessBuilder
     */
    public static List<String> parseCommandLine(String fullcommand)
    {
        try (Reader str = new StringReader(fullcommand))
        {
            StreamTokenizer stok = new StreamTokenizer(str);
            // Disable number parsing
            stok.ordinaryChars('0', '9');
            stok.ordinaryChar('.');
            stok.ordinaryChar('-');
            // Disable comment parsing
            stok.ordinaryChar('/');
            // Call all characters Alpha but still does quote parsing
            stok.wordChars(0x21, 0xfe);
            // Take care of parsing inbetween quotes. Since quotes are now alpha, we can use them in the string.
            String quote = null;
            StringJoiner quotedstr = null;
            List<String> retlist = new ArrayList<>();
            while (stok.nextToken() != StreamTokenizer.TT_EOF)
                if (quote == null)
                {
                    int tq = stok.sval.charAt(0);
                    if (tq == '\'' || tq == '\"')
                    {
                        quote = stok.sval.substring(0, 1);
                        if (stok.sval.length() == 1)
                            quotedstr = new StringJoiner(" ", " ", "");
                        else if (stok.sval.endsWith(quote))
                        {
                            retlist.add(stok.sval.substring(1, stok.sval.length() -1));
                            quote = null;
                        }
                        else
                        {
                            quotedstr = new StringJoiner(" ");
                            quotedstr.add(stok.sval.substring(1));
                        }
                    }
                    else
                        retlist.add(stok.sval);
                }
                else if (stok.sval.endsWith(quote))
                {
                    if (stok.sval.length() == 1)
                        quotedstr.add("");
                    else
                        quotedstr.add(stok.sval.substring(0, stok.sval.length() - 1));
                    retlist.add(quotedstr.toString());
                    quote = null;
                }
                else
                    quotedstr.add(stok.sval);
            return retlist;
        }
        catch (IOException e)
        {
            classlogger.log(Level.WARNING, fullcommand, e);
        }
        return Collections.emptyList();
    }
}