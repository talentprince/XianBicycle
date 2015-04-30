package org.weyoung.xianbicycle.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtil {

    public static String readRawTextFile(Context context, int id) {
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = context.getResources().openRawResource(id);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
}
