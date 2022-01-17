package cc.eabour.file.server.controller;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/file-server")
public class FileUploadController {

    private Logger LOG = LoggerFactory.getLogger(FileUploadController.class);

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> upload(@RequestParam MultipartFile file) {
        Map<String, String> map = new HashMap<>();
        LOG.info("get file {} with size {}", file.getOriginalFilename(), file.getSize());
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("file-upload", ".test");
            LOG.info("create tmp file {}", tmpFile);
            OutputStream out = new FileOutputStream(tmpFile);
            InputStream in = file.getInputStream();
            IOUtils.copy(in, out);
            map.put("success", "true");
            map.put("size", String.valueOf(file.getSize()));
        } catch (IOException e) {
            LOG.error(e.getMessage());
        } finally {
            if (tmpFile.exists()) {
                tmpFile.delete();
                LOG.info("delete tmp file {}", tmpFile);
            }
        }

        return map;
    }
}
