package com.qf.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qf.entity.ResultData;
import com.qf.feign.UserFeign;
import com.qf.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/res")
@Slf4j
public class ResourceController {
    @Autowired
    private FastFileStorageClient fileStorageClient;
    @Autowired
    private UserFeign userFeign;
    @Value("${fdfs.serverip}")
    private String serverip;
    @RequestMapping("/img")
    public ResultData<Map> uploadImg(MultipartFile file,Integer uid){
        try {
            StorePath png = fileStorageClient.uploadImageAndCrtThumbImage(
                    file.getInputStream(), file.getSize(), "PNG", null
            );
            //获得上传路径
            String fullPath = png.getFullPath();
            log.info("图片上传路径---->"+fullPath);
            //缩略图路径
            String crmpath=fullPath.replace(".","_80x80.");
            log.info("缩略图上传路径---->"+crmpath);
            Map<String,String> map=new HashMap();
            map.put("header","http://"+serverip+"/"+fullPath);
            map.put("headerCrm","http://"+serverip+"/"+crmpath);
            System.out.println("map数据-->"+map);
            //将上传的图片保存到数据库中
            ResultData<Boolean> booleanResultData = userFeign.updateUserHeader(fullPath, crmpath, uid);
            System.out.println("booleanResultData--->"+booleanResultData.getCode());
            if (booleanResultData.getCode().equals("0000")){
                System.out.println("Code信息----->"+booleanResultData.getCode());
                //返回成功的数据
                log.info("路径----->"+map);
                return ResultData.createSuccResultData(map);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("上传失败!");
        return ResultData.createErrorResultData(Constant.UPLOAD_IMAGE_ERROR_CODE,"图片上传失败");
    }
}
