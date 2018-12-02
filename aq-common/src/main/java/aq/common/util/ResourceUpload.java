package aq.common.util;

import aq.common.oss.Oss;
import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import java.io.File;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * @author admin
 * @since2017/10/13.
 */
public class ResourceUpload {
    //log日志
    private static Logger logger = Logger.getLogger(ResourceUpload.class);

    private OSSClient ossClient;

    public ResourceUpload(String endpoint,String accessKeyId,String accessKeySecret) {
        ossClient = new OSSClient(endpoint, accessKeyId,accessKeySecret);
    }

    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }

    /**
     * 创建模拟文件夹
     * @param folder   模拟文件夹名如"qj_nanjing/"
     * @return  文件夹名
     */
    public String createFolder(String folder,String backetName){
        //文件夹名
        final String keySuffixWithSlash =folder;
        //判断文件夹是否存在，不存在则创建
        if(!ossClient.doesObjectExist(backetName, keySuffixWithSlash)){
            //创建文件夹
            ossClient.putObject(backetName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            logger.info("创建文件夹成功");
            //得到文件夹名
            OSSObject object = ossClient.getObject(backetName, keySuffixWithSlash);
            String fileDir=object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    /**
     * 删除一个Bucket和其中的Objects
     * @param bucketName  Bucket名
     * @throws OSSException
     * @throws ClientException
     */
    public void deleteBucket(String bucketName)throws OSSException, ClientException{
        ObjectListing ObjectListing = ossClient.listObjects(bucketName);
        List<OSSObjectSummary> listDeletes = ObjectListing.getObjectSummaries();
        for(int i = 0; i < listDeletes.size(); i++){
            String objectName = listDeletes.get(i).getKey();
            System.out.println("objectName = " + objectName);
            //如果不为空，先删除bucket下的文件
            ossClient.deleteObject(bucketName, objectName);
        }
        ossClient.deleteBucket(bucketName);
    }

    /**
     * 删除文件
     * @param bucketName
     * @param key
     */
    public void deleteFileToOSS(String bucketName, String path,String towPath, String key) {
        String towBucketName = StringUtil.isEmpty(towPath)?"":towPath+"/";
        ObjectListing ObjectListing = ossClient.listObjects(bucketName);
        List<OSSObjectSummary> listDeletes = ObjectListing.getObjectSummaries();
        ossClient.deleteObject(bucketName, towBucketName+path+"/"+key);
    }

    /**
     * 上传文件
     * @param file  文件名
     * @throws OSSException
     * @throws ClientException
     * @throws FileNotFoundException
     */
    public Oss uploadFile(MultipartFile file, String path,String towPath, String name,String bucketName){
        String towBucketName = StringUtil.isEmpty(towPath)?"":towPath+"/";
        Oss oss = new Oss();
        Map map = new HashMap();
        if(file == null && file.getSize() <= 0) {
            oss.setCode("error");
            map.clear();
            map.put("MSG","文件不能为空");
            oss.setResult(map);
            return oss;
        }
        String resultStr = null;
        //文件名
        String fileName = name+"."+file.getOriginalFilename().split("\\.")[1];
        //文件大小
        Long fileSize = file.getSize();
        String key = towBucketName+path+"/"+fileName;

        try {
            InputStream is = file.getInputStream();
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();

            /*******************设置参数**************************/
            metadata.setContentLength(is.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");

            //上传文件   (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName,  key, is, metadata);
            //解析结果
            resultStr = putResult.getETag();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        oss.setCode("success");
        map.clear();
        map.put("MSG",resultStr);
        map.put("FILEURL",key);
        oss.setResult(map);
        return oss;
    }

    /**
     * 断点续传
     * @param file
     * @return
     */
    public Oss resumeVideo(MultipartFile file,String path,String name,String bucketName) {
        Oss oss = new Oss();
        Map map = new HashMap();
        if(file == null && file.getSize() <= 0) {
            oss.setCode("error");
            map.clear();
            map.put("MSG","文件不能为空");
            oss.setResult(map);
            return oss;
        }
        int j=0;
        String uploadid = null;
        //文件名
        String fileName = name+"."+file.getOriginalFilename().split("\\.")[1];
        //文件大小
        Long fileSize = file.getSize();
        //路径
        String key = path+fileName;

        ListMultipartUploadsRequest lmur = new ListMultipartUploadsRequest(bucketName);
        // 获取Bucket内所有上传事件
        MultipartUploadListing listing = ossClient.listMultipartUploads(lmur);
        // 新建一个List保存每个分块上传后的ETag和PartNumber
        List<PartETag> partETags = new ArrayList<PartETag>();
        // 遍历所有上传事件  设置UploadId
        for (MultipartUpload multipartUpload : listing.getMultipartUploads()) {
            if (multipartUpload.getKey().equals(key)) {
                uploadid=multipartUpload.getUploadId();
                break;
            }
        }

        if(StringUtil.isEmpty(uploadid)){
            // 开始Multipart Upload,InitiateMultipartUploadRequest 来指定上传Object的名字和所属Bucke
            InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);
            InitiateMultipartUploadResult initiateMultipartUploadResult = ossClient.initiateMultipartUpload(initiateMultipartUploadRequest);
            uploadid=initiateMultipartUploadResult.getUploadId();
        }else{
            ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName,key, uploadid);
            //listParts 方法获取某个上传事件所有已上传的块
            PartListing partListing = ossClient.listParts(listPartsRequest);
            // 遍历所有Part
            for (PartSummary part : partListing.getParts()) {
                partETags.add(new PartETag(part.getPartNumber(),part.getETag()));
                j++;
            }
        }

        // 设置每块为 5M（最小支持5M）
        final int partSize = 1024 * 1024 * 5;
        int partCount = (int) (fileSize / partSize);
        if (fileSize % partSize != 0) {
            partCount++;
        }

        try {

            for (int i=j ; i < partCount; i++) {
                // 获取文件流
                InputStream is = file.getInputStream();

                // 跳到每个分块的开头
                long skipBytes = partSize * i;
                is.skip(skipBytes);

                // 计算每个分块的大小
                long size = partSize < fileSize - skipBytes ? partSize: fileSize - skipBytes;

                // 创建UploadPartRequest，上传分块
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(key);
                uploadPartRequest.setUploadId(uploadid);
                uploadPartRequest.setInputStream(is);
                uploadPartRequest.setPartSize(size);
                uploadPartRequest.setPartNumber(i + 1);
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);

                // 将返回的PartETag保存到List中。
                partETags.add(uploadPartResult.getPartETag());

                // 关闭文件
                is.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, key, uploadid, partETags);
        // 完成分块上传
        CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        // 打印Object的ETag（返回的ETag不是md5.具体是什么不详）
        String tag = completeMultipartUploadResult.getETag();
        oss.setCode("success");
        map.clear();
        map.put("MSG",tag);
        map.put("FILEURL",key);
        oss.setResult(map);
        return oss;
    }

    /**
     * 文件类型
     * @param fileName
     * @return
     */
    public String getContentType(String fileName) {
        //文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));

        switch (fileExtension) {
            case "bmp":
                return "image/bmp";
            case "gif":
                return "image/gif";
            case "png":
            case "jpeg":
            case "jpg":
                return "image/jpeg";
            case "html":
                return "text/html";
            case "txt":
                return "text/plain";
            case "vsd":
                return "application/vnd.visio";
            case "ppt":
            case "pptx":
                return "application/vnd.ms-powerpoint";
            case "doc":
            case "docx":
                return "application/msword";
            case "xml":
                return "text/xml";
            case "mp4":
                return "video/mp4";
            case "wma":
            case "mp3":
                return "audio/mp3";
            default:
                return "application/octet-stream";
        }
    }

    public String getFileUrl(String fileUrl,String bucketName) {
        // 设置URL过期时间为10年 3600l* 1000*24*365*10

        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, fileUrl, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    /**
     * MultipartFile 转换成File
     *
     * @param multfile 原文件类型
     * @return File
     * @throws IOException
     */
    public static File multipartToFile(MultipartFile multfile) throws IOException {
        CommonsMultipartFile cf = (CommonsMultipartFile)multfile;
        //这个myfile是MultipartFile的
        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
        File file = fi.getStoreLocation();
        //手动创建临时文件
        if(file.length() < 2048){
            File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") +
                    file.getName());
            multfile.transferTo(tmpFile);
            return tmpFile;
        }
        return file;
    }

}
