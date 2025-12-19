package com.atguigu.spzx.utils;


import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.SetBucketPolicyArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class MinioUtils {

    @Autowired
    private MinioClient minioClient;

    // 从配置文件注入桶名
    @Value("${minio.bucket-name}")
    private String bucketName;

    // 从配置文件注入MinIO服务地址
    @Value("${minio.endpoint}")
    private String endpoint;

    // 初始化桶，不存在则创建
    public void initBuckets() {
        try {
            // 检查桶是否存在
            boolean isBucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!isBucketExists) {
                // 创建桶
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
                log.info("桶{}创建成功，开始设置公共读权限", bucketName);
                // 设置桶为公共读
                setBucketPublic();
            }
        } catch (Exception e) {
            log.error("初始化桶失败：" + e.getMessage(), e);
            throw new RuntimeException("初始化桶失败：" + e.getMessage(), e);
        }
    }

    // 设置桶为公共读权限（兼容MinIO社区版的简化策略）
    public void setBucketPublic() {
        try {
            // 构造MinIO社区版兼容的公共读策略（直接用字符串，避免JSON序列化问题）
            String policyJson = String.format("{\n" +
                    "  \"Version\": \"2012-10-17\",\n" +
                    "  \"Statement\": [\n" +
                    "    {\n" +
                    "      \"Effect\": \"Allow\",\n" +
                    "      \"Principal\": \"*\",\n" +
                    "      \"Action\": \"s3:GetObject\",\n" +
                    "      \"Resource\": \"arn:aws:s3:::%s/*\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}", bucketName);

            // 设置桶策略
            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                            .bucket(bucketName)
                            .config(policyJson)
                            .build()
            );
            log.info("桶{}的公共读策略设置成功", bucketName);
        } catch (Exception e) {
            log.error("设置桶{}的公共读策略失败：", bucketName, e);
            throw new RuntimeException("设置桶为Public权限失败：" + e.getMessage(), e);
        }
    }

    /**
     * 上传文件到MinIO（按用户ID分文件夹存储）
     *
     * @param file 前端上传的文件
     * @return 文件的公共访问URL
     */
    public String uploadFile(MultipartFile file,String prefix) {
        // 1. 校验文件是否为空
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        try {
            // 2. 初始化桶（确保桶存在且为公共读）
            initBuckets();

//            // 3. 获取当前登录用户ID（非空校验）
//            String userId = AuthContextUtil.get().getId().toString();
//            if (userId == null || userId.trim().isEmpty()) {
//                userId = "default"; // 给默认文件夹，避免路径异常
//                log.warn("当前用户ID为空，使用默认文件夹{}", userId);
//            }

            // 4. 处理文件名：避免重复，安全处理后缀
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                originalFilename = "unknown-file";
            }

            // 安全获取文件后缀（转小写，避免大小写问题）
            String suffix = "";
            int lastDotIndex = originalFilename.lastIndexOf(".");
            if (lastDotIndex > 0 && lastDotIndex < originalFilename.length() - 1) {
                suffix = originalFilename.substring(lastDotIndex).toLowerCase();
            }

            // 5. 拼接完整的文件路径：用户ID/随机文件名（关键！确保上传路径和URL路径一致）
            String fileObjectName = prefix + "/" + UUID.randomUUID().toString() + suffix;
            log.info("文件上传路径：{}", fileObjectName);

            // 6. 上传文件到MinIO（使用完整的文件路径）
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileObjectName) // 这里传完整的路径（用户ID/文件名）
                            .stream(file.getInputStream(), file.getSize(), 10 * 1024 * 1024) // 10MB分片
                            .contentType(file.getContentType())
                            .build()
            );

            // 7. 拼接公共访问URL（路径和上传路径一致）
            String fileUrl = endpoint + "/" + bucketName + "/" + fileObjectName;
            log.info("文件访问URL：{}", fileUrl);
            return fileUrl;

        } catch (Exception e) {
            log.error("文件上传失败：", e);
            throw new RuntimeException("文件上传失败：" + e.getMessage(), e);
        }
    }

    // 可选：添加删除文件的方法，方便后续扩展
    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(
                    io.minio.RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            log.info("文件{}删除成功", fileName);
        } catch (Exception e) {
            log.error("删除文件失败：", e);
            throw new RuntimeException("删除文件失败：" + e.getMessage(), e);
        }
    }
}