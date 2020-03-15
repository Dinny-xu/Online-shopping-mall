package com.changgou.file.util;

import com.changgou.file.pojo.FastDFSFile;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件上传的工具类
 */
public class FastDFSClient {

    private static Logger logger = LoggerFactory.getLogger(FastDFSClient.class);

    private static TrackerClient trackerClient = null;

    private static StorageClient1 storageClient1 = null;

    private static TrackerServer trackerServer = null;

    private static StorageServer storageServer = null;



    //静态代码块   为了上传文件  初始化加载文件的上传配置文件  (文件中包含  上传文件的地址 连接时间 读取时间 编码集 等
    static {
        try {
            String absolutePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init(absolutePath);

            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            storageClient1 = new StorageClient1(trackerServer, storageServer);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ClientGlobal is fail:{}",e.getMessage());
        }

    }

    /**
     * 上传文件
     *
     * @param file
     * @return 文件的路径
     */
    public static String uploadFile(FastDFSFile file){
        System.out.println("上传文件");
        NameValuePair[] nameValuePairs = new NameValuePair[1];
        nameValuePairs[0] = new NameValuePair(file.getAuthor());
        try {
            return storageClient1.upload_file1(file.getContent(),file.getExt(),nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("upload file is fail: {} ",e.getMessage());
        }
        return null;

    }

    /**
     * 下载文件
     * @param path
     * @return  文件对象
     */
    public static InputStream downFile(String path){

        try {
            byte[] bytes = storageClient1.download_file1(path);
            return new ByteArrayInputStream(bytes);
        } catch (Exception e){
            e.getStackTrace();
            logger.error("download file is fail : {}" ,e.getMessage());
        }


        return null;
    }


    /**
     *  删除文件
     * @param path
     *
     */
    public static void deleteFile(String path){

        try {
            storageClient1.delete_file1(path);
        } catch (Exception e)  {
            e.printStackTrace();
            logger.error("delete file is fail : {}",e.getMessage());
        }
    }
    /***
     * 获取Tracker服务地址
     * @return
     * @throws IOException
     */
    public static String getTrackerUrl() throws IOException {
        return "http://"+trackerServer.getInetSocketAddress().getHostString()+":"+ClientGlobal.getG_tracker_http_port()+"/";
    }

}
