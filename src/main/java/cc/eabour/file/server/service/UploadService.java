package cc.eabour.file.server.service;

import cc.eabour.file.server.feign.FeignSpringFormEncoder;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Service
public class UploadService {

    private Feign.Builder  builder = null;

    @Autowired
    private Environment env;

    @Autowired
    public UploadService() {
        builder =  Feign.builder().decoder(new JacksonDecoder()).encoder(new FeignSpringFormEncoder());
    }

    public Map<String, String> uploadJar(String path) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "file";
        FileItem item = factory.createItem(textFieldName, "multipart/form-data", true, path);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(path);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MultipartFile multipartFile = new CommonsMultipartFile(item);
        String target = env.getProperty("server.target");
        return builder.target(UploadClient.class, target).uploadFile(multipartFile);
    }
}
