package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.Utils.UploadAddr;
import com.atguigu.spzx.manager.service.UploadFileService;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/system")
@Tag(name = "文件上传接口")
public class FileUploadController {
    @Autowired
    private UploadFileService uploadFileService;
    @PostMapping("/fileUpload/{type}")
    public Result uploadFile(@RequestParam("file")MultipartFile file, @PathVariable("type") Integer type){
        String url=uploadFileService.uploadService(file,type);
        if(url.equals(UploadAddr.UNKNOWTYPE.getPrefix())){
            return  Result.build(null,ResultCodeEnum.DATA_ERROR);
        }
        return  Result.build(url, ResultCodeEnum.SUCCESS);
    }
}
