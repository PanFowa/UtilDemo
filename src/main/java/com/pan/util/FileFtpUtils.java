package com.pan.util;

import com.pan.domain.FtpConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class FileFtpUtils {
    private static Logger logger = LoggerFactory.getLogger(FileFtpUtils.class);


    public static boolean putFile(FtpConfig config,int bufSize,String encoding,Integer fileType){
        FTPClient ftpClient=new FTPClient();
        FileInputStream fis=null;
        if (StringUtils.isEmpty(encoding)){
            encoding=System.getProperty("sun.jnu.encodiing");
        }
        try{
            ftpClient.setDefaultPort(config.getPort());
            ftpClient.connect(config.getIp());
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setControlEncoding(encoding);
            ftpClient.login(config.getUsrNme(),config.getPsw());
            ftpClient.setBufferSize(bufSize);
            if (fileType==null){
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            }else {
                ftpClient.setFileType(fileType);
            }

            if (!existFile(ftpClient,config.getDstDir())){
                createDir(config.getDstDir(),encoding,ftpClient);
            }else {
                ftpClient.changeWorkingDirectory(config.getDstDir());
            }

            //文件上传到服务器
            File srcFile=new File(config.getLocDir()+File.separator+config.getLocFle());
            fis=new FileInputStream(srcFile);
            ftpClient.storeFile(new String(config.getLocFle().getBytes(encoding), "iso-8859-1"), fis);//传输协议要求编码是iso-8859-1,避免中文传到目标变为乱码

        }catch (Exception e){
            logger.error("Ftp传输文件出错",e);
        }finally {
            IOUtils.closeQuietly(fis);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                logger.error("关闭Ftp客户端时出差",e);
            }
        }

        return true;

    }

    private static boolean existFile(FTPClient ftpClient, String dstDir) throws IOException {
        FTPFile[] files=ftpClient.listFiles(dstDir);
        if (ArrayUtils.isEmpty(files)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 创建目录
     *
     * @param remoteBaseDir 远程目录
     * @param encoding 编码 ,UTF-8,GBK
     * @param ftpClient
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static boolean createDir(String remoteBaseDir, String encoding, FTPClient ftpClient) throws UnsupportedEncodingException, IOException {
        if(StringUtils.isBlank(remoteBaseDir)){
           throw new NullArgumentException("远程目录不能为空");
        }
        String directory = remoteBaseDir.substring(0, remoteBaseDir.lastIndexOf(File.separator) + 1);
        if (!directory.equalsIgnoreCase(File.separator) && !ftpClient.changeWorkingDirectory(
                new String(directory.getBytes(encoding), "iso-8859-1"))) {
            //如果远程目录不存在，则递归创建远程服务器目录
            int start = 0;
            int end = 0;
            if (directory.startsWith(File.separator)) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf(File.separator, start);
            String path="";
            while (true) {
                String subDirectory = new String(remoteBaseDir.substring(start, end).getBytes(encoding),
                        "iso-8859-1");
                path=path+File.separator+subDirectory;
                if (!existFile(ftpClient,path)&&!ftpClient.changeWorkingDirectory(path)) {
                    if (!ftpClient.makeDirectory(subDirectory)) {
                        return false;
                    }
                }
                ftpClient.changeWorkingDirectory(path);
                start = end + 1;
                end = directory.indexOf(File.separator, start);
                //检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return true;
    }
}
