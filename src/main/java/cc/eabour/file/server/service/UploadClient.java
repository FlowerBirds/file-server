package cc.eabour.file.server.service;

import feign.Param;
import feign.RequestLine;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UploadClient {
    @RequestLine(value = "POST /api/file-server/upload")
    Map<String, String> uploadFile(@Param("file") MultipartFile file);
}
