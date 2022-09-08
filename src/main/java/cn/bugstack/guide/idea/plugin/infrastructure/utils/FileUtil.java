package cn.bugstack.guide.idea.plugin.infrastructure.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/7 17:55
 * @Version 1.0
 */
public class FileUtil {

    /**
     * 构造函数
     */
    public FileUtil() {
    }

    /**
     * queryData
     * @param fileName
     * @return
     */
    public static List<String> queryData(String fileName) {
        File file = new File(fileName);
        if(!file.exists()){
            return null;
        }
        List<String> list = new ArrayList<String>();
        BufferedReader br = null;;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String s = "";
            while((s = br.readLine()) != null){
                list.add(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(br != null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * replaceContext
     * @param fileName
     */
    public static void replaceContext(String fileName,String content) {
        // 创建文件写流
        FileWriter writer = null;
        try {
            // 创建文件写流
            writer = new FileWriter(fileName);
            // 将替换后的字符串写入到文件
            writer.write(content.toCharArray());
            writer.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    // 关闭文件写流
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 将文件复制到指定目录
     *
     * @param srcFilePath
     * @param desFilePath
     * @throws IOException
     */
    public static int copyFile(String srcFilePath, String desFilePath) throws IOException {
        int iRet = 0;
        FileChannel fileChannelSrc = null;
        FileChannel fileChannelDes = null;
        try {
            File srcFile = new File(srcFilePath);
            File desFile = new File(desFilePath);

            fileChannelSrc = new FileInputStream(srcFile).getChannel();
            fileChannelDes = new FileOutputStream(desFile).getChannel();
            fileChannelDes.transferFrom(fileChannelSrc, 0, fileChannelSrc.size());
        } catch (IOException e) {
            iRet = -1;
            throw e;
        } finally {
            fileChannelSrc.close();
            fileChannelDes.close();
        }
        return iRet;
    }

    /**
     * 生成存放文件的目录
     *
     * @param strUploadDir
     * @return int
     */
    public static int makeUploadDir(String strUploadDir) {
        int iRet = 0;
        try {
            // 生成路径
            File hasDir = new File(strUploadDir);
            // 目录不存在时，创建目录
            if (!hasDir.exists()) {
                boolean isDir = hasDir.mkdirs();
                if (!isDir) {
                    iRet = -1;
                    throw new IOException("生成路径时出错!");
                }
            }
        } catch (NullPointerException ex) {
            iRet = -1;
        } catch (Exception ex) {
            iRet = -1;
        }
        return iRet;
    }
}