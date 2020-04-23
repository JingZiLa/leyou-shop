package com.leyou.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Mirror
 * @CreateDate 2020/3/18.
 * 上传文件业务处理接口
 */
public interface UploadService {
    String uploadImage(MultipartFile file);
}
