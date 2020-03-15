package com.changgou.file.controller;


import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.file.pojo.FastDFSFile;
import com.changgou.file.util.FastDFSClient;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Logger;



@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {

  /*  private Logger logger = (Logger) LoggerFactory.getLogger(FileController.class);*/


    /**
     * 上传图片
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadFile")
    public Result uploadFile(@RequestParam MultipartFile[] file) throws IOException {

        List<String> urls = new ArrayList<>();


        for (MultipartFile multipartFile : file) {
            FastDFSFile fastDFSFile = new FastDFSFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(), FilenameUtils.getExtension(multipartFile.getOriginalFilename()));

            String path = FastDFSClient.uploadFile(fastDFSFile);
            urls.add(FastDFSClient.getTrackerUrl() + path);
        }


        return new Result(true, StatusCode.OK, "上传成功", urls);
    }
}
