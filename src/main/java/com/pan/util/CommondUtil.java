package com.pan.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 命令执行工具类
 */
public class CommondUtil {
    private final Logger logger = LoggerFactory.getLogger(CommondUtil.class);
    public int exc(String cmd, String...args) throws Exception {
        String exeSendCmd = this.replaceToken(cmd, args);
        int res;
        if ((res=this.executeCommand(exeSendCmd)) != 0) {
            String errorMes = "命令执行失败!，执行的命令为:"+exeSendCmd;
            throw new Exception(errorMes);
        }
        return res;
    }

    private int executeCommand(String command) throws Exception {
        return this.executeCommand(command, System.getenv());
    }

    private int executeCommand(String command, Map<String, String> environment) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(new String[0]);
        Process p = null;
        BufferedReader br = null;
        boolean var6 = false;

        try {
            if (environment != null) {
                pb.environment().putAll(environment);
            }

            this.logger.info("exc command:" + command);
            List<String> execCommand = Arrays.asList(command.split("\\s"));
            p = pb.redirectErrorStream(true).command(execCommand).start();
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            this.logger.info("exc log：");

            while((line = br.readLine()) != null) {
                this.logger.info(line);
            }

            int exitCode = p.waitFor();
            this.logger.info("exc completed...");
            return exitCode;
        } catch (Exception var16) {
            this.logger.error("exc fail：" + var16.getMessage(), var16);
            throw new Exception("exc fail：" + var16.getMessage(), var16);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException var15) {
                var15.printStackTrace();
            }

        }
    }

    private String replaceToken(String str, String... values) {
        Pattern pattern = Pattern.compile("(\\?)");
        Matcher matcher = pattern.matcher(str);
        if (matcher.groupCount() > values.length) {
            throw new RuntimeException("命令占位符数量大于替换数据个数...");
        } else {
            StringBuffer buf = new StringBuffer();
            int end = 0;

            for(int i = 0; matcher.find(); ++i) {
                buf.append(str.substring(end, matcher.start(1)));
                buf.append(values[i]);
                end = matcher.end(1);
            }

            buf.append(str.substring(end));
            return buf.toString();
        }
    }
}
