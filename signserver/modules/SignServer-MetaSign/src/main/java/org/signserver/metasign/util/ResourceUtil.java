package org.signserver.metasign.util;

import java.io.*;

public class ResourceUtil {

    public String getConfiguration(String path, String commentPrefix)
            throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(path);
        if (inputStream == null)
            throw new FileNotFoundException("Properties file: " + path + " not found" );

        StringBuilder result = new StringBuilder();

        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);
        String line;

        while ((line = br.readLine()) != null) {
            if (!commentPrefix.isEmpty() || commentPrefix != null)
                if (line.startsWith(commentPrefix))
                    continue;
            result.append(line).append('\n');
        }

        br.close();
        isr.close();
        return result.toString();
    }
}