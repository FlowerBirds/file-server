package cc.eabour.file.server.controller;

import cc.eabour.file.server.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/file-test")
public class FileTestController {
    private Logger LOG = LoggerFactory.getLogger(FileTestController.class);

    @Autowired
    private UploadService uploadService;

    @Autowired
    private Environment env;

    @RequestMapping("/upload")
    @ResponseBody
    public Map<String, String> testUpload(@RequestParam(required = false) String path) {
        String location = env.getProperty("file.location");
        if (location == null || location.isEmpty()) {
            return Collections.singletonMap("message", "location is null");
        }
        File file = new File(location);
        if (!file.exists() || file.isFile()) {
            return Collections.singletonMap("message", "location does not exist or is not directory");
        }
        long c = 0;
        long start = System.currentTimeMillis();
        long size = 0;
        for (File f : file.listFiles()) {
            LOG.info("start to upload {}", f.getPath());
            Map<String, String> map = uploadService.uploadJar(f.getPath());
            LOG.info("upload {} successful: {}", f.getPath(), map);
            c++;
            if (map.containsKey("size")) {
                size += Long.parseLong(map.get("size"));
            }
        }
        LOG.info("upload over");
        long end = System.currentTimeMillis() - start;
        Map<String, String> map = new HashMap<>();
        map.put("count", String.valueOf(c));
        map.put("time", String.valueOf(end / 1000.0));

        return map;
    }

}
