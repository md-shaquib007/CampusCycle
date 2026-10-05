package com.campuscycle.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class FileUploadUtil {
    private static final int MAX_MEMORY = 1024 * 1024;

    private FileUploadUtil() {}

    public static class UploadResult {
        private final Map<String, String> fields = new HashMap<>();
        private final Map<String, String> files = new HashMap<>();

        public Map<String, String> getFields() { return fields; }
        public Map<String, String> getFiles() { return files; }
        public String getField(String name) { return fields.get(name); }
        public String getFile(String name) { return files.get(name); }
    }

    public static boolean isMultipart(HttpServletRequest request) {
        return ServletFileUpload.isMultipartContent(request);
    }

    private static final java.util.Set<String> ALLOWED_EXTENSIONS = java.util.Set.of(
            ".jpg", ".jpeg", ".png", ".webp", ".gif", ".pdf"
    );

    public static UploadResult parse(HttpServletRequest request, ServletContext context) throws Exception {
        UploadResult result = new UploadResult();
        if (!isMultipart(request)) {
            return result;
        }

        String relativeUploadPath = AppConfig.get("uploadPath", "uploads");
        String realPath = context != null ? context.getRealPath("/" + relativeUploadPath) : null;
        File dir = realPath != null ? new File(realPath) : new File(System.getProperty("java.io.tmpdir"), relativeUploadPath);
        if (!dir.exists() && !dir.mkdirs()) {
            dir = new File(relativeUploadPath);
            dir.mkdirs();
        }

        long maxSize = Long.parseLong(AppConfig.get("uploadMaxSize", "5242880"));
        DiskFileItemFactory factory = new DiskFileItemFactory(MAX_MEMORY, dir);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(maxSize);

        List<FileItem> items = upload.parseRequest(request);
        for (FileItem item : items) {
            if (item.isFormField()) {
                result.fields.put(item.getFieldName(), item.getString("UTF-8"));
            } else if (item.getSize() > 0) {
                String ext = getExtension(item.getName());
                if (!ALLOWED_EXTENSIONS.contains(ext)) {
                    throw new IllegalArgumentException("Invalid file type. Allowed types: jpg, jpeg, png, webp, gif, pdf");
                }
                String filename = UUID.randomUUID() + ext;
                File saved = new File(dir, filename);
                item.write(saved);
                result.files.put(item.getFieldName(), relativeUploadPath + "/" + filename);
            }
        }
        return result;
    }

    private static String getExtension(String name) {
        if (name == null) return ".jpg";
        int dot = name.lastIndexOf('.');
        String ext = dot >= 0 ? name.substring(dot).toLowerCase() : ".jpg";
        return ext;
    }
}
