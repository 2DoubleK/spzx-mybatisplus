package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.Utils.UploadAddr;
import com.atguigu.spzx.manager.service.UploadFileService;
import com.atguigu.spzx.utils.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadServiceImpl implements UploadFileService {
    @Autowired
    private MinioUtils minioUtils;

    @Override
    public String uploadService(MultipartFile file, Integer type) {

        if (type == 1) { //用户图片上传
            return minioUtils.uploadFile(file, UploadAddr.USER_IMG.getPrefix());
        } else if (type == 2) {//品牌图片上传
            return minioUtils.uploadFile(file, UploadAddr.BRAND_IMG.getPrefix());
        }else if(type == 3){
            return minioUtils.uploadFile(file, UploadAddr.PRODUCT_IMGS.getPrefix());
        }else if(type == 4){
            return minioUtils.uploadFile(file, UploadAddr.PRODUCT_SKU.getPrefix());
        }else if(type == 5){
            return minioUtils.uploadFile(file, UploadAddr.PRODUCT_DETAILS.getPrefix());
        }
        return UploadAddr.UNKNOWTYPE.getPrefix();
    }

}
