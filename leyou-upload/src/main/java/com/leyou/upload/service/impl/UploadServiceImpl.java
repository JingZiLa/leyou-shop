package com.leyou.upload.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.upload.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/3/18.
 *
 * 上传文件业务处理接口实现
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private FastFileStorageClient storageClient;

    //定义文件类型白名单
    private static final List<String> CONTENT_TYPES = Arrays.asList("image/png","image/jpeg","image/jpg");

    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceImpl.class);

    /**
     * 处理上传文件
     * @param file
     * @return
     */
    @Override
    public String uploadImage(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        try {
        //校验文件类型
        //String last = StringUtils.substringAfterLast(originalFilename, ",");
        String contentType = file.getContentType();
        if (!CONTENT_TYPES.contains(contentType)){
            LOGGER.info("文件类型不合法:  {}",originalFilename);
            return null;
        }
        //校验文件内容
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if(bufferedImage == null){
            LOGGER.info("文件内容不合法: {}",originalFilename);
            return null;
        }
        //保存到服务器
//        file.transferTo(new File("E:\\IDEA_Staroge\\leyou\\leyou-upload\\image\\" + originalFilename));
        String ext = StringUtils.substringAfterLast(originalFilename, ".");
        StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);
            //返回url，进行回显
//            return "http://image.leyou.com/"+originalFilename;
            return "http://image.leyou.com/"+storePath.getFullPath();
        } catch (IOException e) {
            LOGGER.info("服务器内部错误: {}",originalFilename);
            e.printStackTrace();
        }
        return null;
    }
}
