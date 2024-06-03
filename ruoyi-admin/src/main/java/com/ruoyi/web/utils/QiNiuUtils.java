package com.ruoyi.web.utils;

import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.qiniu.storage.Configuration;
/**
 * @Description //TODO
 * @Date 2024/6/3 9:11
 * @Author hy
 **/
@Component
public class QiNiuUtils {
  @Value("${qiniu.accessKey}")
  private  String accessKey;      //公钥
  @Value("${qiniu.secretKey}")
  private  String accessSecretKey;   //私钥
  @Value("${qiniu.bucketName}")
  private  String bucketName;   // 存储空间
  @Value("${qiniu.path}")
  private  String path;       // 域名


  public  List<String> upload(MultipartFile file){

    List<String> result=new ArrayList<>();
    // 生成文件名
    String fileName = getRandomImgName(file.getOriginalFilename());
    result.add(fileName);
    //构造一个带指定 Region 对象的配置类
    Configuration cfg = new Configuration(Region.autoRegion());  //根据自己的对象空间的地址选（华东）
    //...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);
    //默认不指定key的情况下，以文件内容的hash值作为文件名
    try {
      byte[] uploadBytes = file.getBytes();
      Auth auth = Auth.create(accessKey, accessSecretKey);
      String upToken = auth.uploadToken(bucketName);
      Response response = uploadManager.put(uploadBytes, fileName , upToken);
      //解析上传成功的结果
      DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
      result.add( path+fileName);
      return result;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static String getRandomImgName(String fileName) {
    int index = fileName.lastIndexOf(".");

    if (fileName.isEmpty() || index == -1){
      throw new IllegalArgumentException();
    }
    // 获取文件后缀
    String suffix = fileName.substring(index).toLowerCase();
    // 生成UUID
    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    // 拼接新的名称
    return uuid + suffix;
  }


}
