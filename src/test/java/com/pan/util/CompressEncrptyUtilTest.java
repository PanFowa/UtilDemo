package com.pan.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;

public class CompressEncrptyUtilTest {
    @Test
    public void testFilePath() {
        String path = "E://///\\\\/\\\\a\\\\//b/";
        String newPath=handlePath(path);
        System.out.println(newPath);
    }

    /**
     * 处理文件路径中多余的分隔符。如/home//abd/name.txt在linux中可能导致路径错误
     * @param path
     * @return
     */
    public static String handlePath(String path) {
        File file=new File(path);
        path=file.getAbsolutePath();
        String name="";
        boolean fileFlag=false;
        if (!file.isDirectory()){
            fileFlag=true;
            name=FilenameUtils.getName(path);
            path=FilenameUtils.getFullPath(path);
        }
        String res = "";
        String[] strs = path.split("\\/");
        StringBuffer buf = new StringBuffer();

        if (ArrayUtils.isEmpty(strs)) {
            res = "";
        } else {
            if (path.startsWith("/") || path.startsWith("\\")) {
                buf.append(File.separator);
            }
            for (String str : strs) {
                if (StringUtils.isBlank(str)) {
                    continue;
                } else {
                    buf.append(str).append(File.separator);
                }
            }
        }

        if (fileFlag){
            buf.append(name);
        }

        return buf.toString();
    }
}
