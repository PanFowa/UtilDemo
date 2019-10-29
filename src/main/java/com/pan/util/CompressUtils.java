package com.pan.util;


import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * <p>Version: 1.0
 */
public class CompressUtils {
    private static final Logger log = LoggerFactory.getLogger(CompressUtils.class);

    public static final void zip(String zipFullName, String srdDir) {
        zip(zipFullName, new String[]{srdDir}, System.getProperty("sun.jnu.encoding"));
    }

    public static final void zip(String zipFullName, String srdDir, String encoding) {
        zip(zipFullName, new String[]{srdDir}, encoding);
    }


    public static final void zip(String compressPath, String[] needCompressPaths, String encoding) {
        log.info("开始启动压缩工具，目标文件为:" + compressPath + ",带压缩文件为:" + StringUtils.join(needCompressPaths, ",") + ",编码为:" + encoding);
        File compressFile = new File(compressPath);

        List<File> files = new ArrayList<File>();
        for (String needCompressPath : needCompressPaths) {
            File needCompressFile = new File(needCompressPath);
            if (!needCompressFile.exists()) {
                continue;
            }
            files.add(needCompressFile);
        }
        try {
            ZipArchiveOutputStream zaos = null;
            try {
                zaos = new ZipArchiveOutputStream(compressFile);
                zaos.setUseZip64(Zip64Mode.AsNeeded);
                if (encoding != null) {
                    zaos.setEncoding(encoding);
                }

                for (File file : files) {
                    addFilesToCompression(zaos, file, "");
                }

            } catch (IOException e) {
                throw e;
            } finally {
                IOUtils.closeQuietly(zaos);
            }
        } catch (Exception e) {
            FileUtils.deleteQuietly(compressFile);
            throw new RuntimeException("压缩失败", e);
        }
    }

    private static void addFilesToCompression(ZipArchiveOutputStream zaos, File file, String dir) throws IOException {

        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, dir + file.getName());
        zipArchiveEntry.setUnixMode(755);
        zaos.putArchiveEntry(zipArchiveEntry);

        if (file.isFile()) {
            BufferedInputStream bis = null;
            try {
                bis = new BufferedInputStream(new FileInputStream(file));
                IOUtils.copy(bis, zaos);
                zaos.closeArchiveEntry();
            } catch (IOException e) {
                throw e;
            } finally {
                IOUtils.closeQuietly(bis);
            }
        } else if (file.isDirectory()) {
            zaos.closeArchiveEntry();

            for (File childFile : file.listFiles()) {
                addFilesToCompression(zaos, childFile, dir + file.getName() + File.separator);
            }
        }
    }

    public static void unzip(String path, String descPath, boolean override, String encoding) {
        File uncompressFile = new File(path);
        File descPathFile = null;

        try {
            descPathFile = new File(descPath);
            unzipFolder(uncompressFile, descPathFile, override, encoding);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    private static void unzipFolder(File uncompressFile, File descPathFile, boolean override, String encoding) {

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(uncompressFile, encoding);

            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry zipEntry = entries.nextElement();
                String name = zipEntry.getName();
                name = name.replace("\\", "/");

                File currentFile = new File(descPathFile, name);

                //非覆盖 跳过
                if (currentFile.isFile() && currentFile.exists() && !override) {
                    continue;
                }

                if (name.endsWith("/")) {
                    currentFile.mkdirs();
                    continue;
                } else {
                    currentFile.getParentFile().mkdirs();
                }

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(currentFile);
                    InputStream is = zipFile.getInputStream(zipEntry);
                    IOUtils.copy(is, fos);
                } finally {
                    IOUtils.closeQuietly(fos);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("解压缩失败", e);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                }
            }
        }

    }


    public static void main(String[] args) {
        String encoding = System.getProperty("sun.jnu.encoding");
        System.out.println(encoding);
        String[] needCompress = new String[]{
                "F:\\文档\\简历"
        };
        if (ArrayUtils.isEmpty(args) || args.length < 2) {
            System.out.println("用法:java -jar  UtilDemo.jar \"F:\\\\文档\\\\简历.zip\" \"F:\\\\文档\\\\简历\" \"UTF-8\" ");
            System.exit(0);
        }
        if (args.length == 2) {
            zip(args[0], args[1], null);
        } else if (args.length == 3) {
            zip(args[0], args[1], args[2]);
        }

//        unzip("E:/testZip.zip", "E:\\新建文件夹",true);
    }
}