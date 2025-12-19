package com.atguigu.spzx.manager.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
    String uploadService(MultipartFile file, Integer type);


}
