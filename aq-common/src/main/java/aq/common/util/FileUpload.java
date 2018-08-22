package aq.common.util;

import aq.common.constants.APPConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUpload {
    private long allowSize = 2L;
    private String fileName;
    private String[] fileNames;

    public long getAllowSize() {
        return allowSize * 1024 * 1024;
    }

    public void setAllowSize(long allowSize) {
        this.allowSize = allowSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String[] getFileNames() {
        return fileNames;
    }

    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }

    private String getFileNameNew() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return fmt.format(new Date());
    }

    /**
     * 单文件上传
     * @param file
     * @param destDir
     * @param request
     * @throws Exception
     */
    public String upload(MultipartFile file,String name, String suffix, String destDir, HttpServletRequest request) throws Exception {
        try {
            suffix = StringUtil.isEmpty(suffix)?file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1):suffix;
            if (file.getSize() > getAllowSize()) {
                throw new Exception("您上传的文件大小已经超出范围");
            }
            File destFile = new File(APPConstants.FILE_SERVICE_PLACE + destDir);
            if (!destFile.exists()) {
                destFile.mkdirs();
            }
            String fileNameNew = "/" + (StringUtil.isEmpty(name)?getFileNameNew():name) + "." + suffix;
            File f = new File(destFile.getAbsoluteFile() + fileNameNew);
            file.transferTo(f);
            f.createNewFile();
            fileName = APPConstants.FILE_SERVICE_URL + destDir + fileNameNew;
            return fileName;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 单文件读取
     * @throws Exception
     */
    public FileInputStream write(String url) throws Exception {
        String path = APPConstants.FILE_SERVICE_PLACE+url;
        path = path.replace("\\", "/");
        FileInputStream inputStream = null;
        try {
            File file = new File(path);
            inputStream = new FileInputStream(file);
            return inputStream;
        }catch (Exception ex) {
            inputStream.close();
            throw ex;
        }
    }

    /**
     * 多文件上传
     * @param files
     * @param destDir
     * @param request
     * @throws Exception
     */
    public String[] uploads(MultipartFile[] files, String destDir, HttpServletRequest request) throws Exception {
        try {
            fileNames = new String[files.length];
            int index = 0;
            for (MultipartFile file : files) {
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                if (file.getSize() > getAllowSize()) {
                    throw new Exception("您上传的文件大小已经超出范围");
                }
                File destFile = new File(APPConstants.FILE_SERVICE_PLACE +  "/" + destDir);
                if (!destFile.exists()) {
                    destFile.mkdirs();
                }
                String fileNameNew = getFileNameNew() + "." + suffix;//
                File f = new File(destFile.getAbsoluteFile() + fileNameNew);
                file.transferTo(f);
                f.createNewFile();
                fileNames[index++] = APPConstants.FILE_SERVICE_URL + "/" + destDir + "/" + fileNameNew;
            }
            return fileNames;
        } catch (Exception e) {
            throw e;
        }
    }

}
