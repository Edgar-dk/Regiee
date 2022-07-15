package com.sias.regiee.controller;

import com.sias.regiee.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * @author Edgar
 * @create 2022-07-14 9:17
 * @faction:
 */

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${regiee.path}")
    private String basePath;

    /*1.文件上传*/
    @PostMapping("/upload")
    public R<String> upload (MultipartFile file){
        /*01.传输到后台文件会暂时存储到一个位置上
        *    需要把文件存储好
        *
        *    getOriginalFilename获取原始文件的名字
        *    substring截取文件名，lastIndexOf从后面
        *    截取包括这个点之后的然后给suffix在结合uuid来使用*/
        log.info(file.toString());
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        /*02.将临时文件存储在一个位置上
        *    transferTo是存储文件到一个位置上
        *    new File存储文件的位置也文件夹
        *    所以需要去创建一个新的文件或者是文件夹
        *    ，然后把然后把file中的文件存到指定位置
        *
        *    */
        String fileName = UUID.randomUUID().toString()+suffix;

        /*03.new File是关于文件的，一切关于
        *    文件的东西，都需要去创建这个对象，
        *    basePath也是文件的东西if里面判断文件是否存在，
        *    不存在的话去创建一个*/
        File file1 = new File(basePath);
        if (!file1.exists()){
            file1.mkdir();
        }
        try {
            file.transferTo(new File(basePath+fileName));
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.success(fileName);
    }


    /*2.文件下载*/
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        /*01.想要去下载文件，先要去进入这个文件
        *    就是输入流，进哪里，地址，进去之后，就可以读取文件数据
        *    读取的大小有限制，while里面的参数是读取文件
        *    把读取多少个大小的文件交给一个变量保存，读取文件
        *    是一下子读出来，就是len的值，是一下子这么多，
        *    输出流，把读取出来的文件，按照这么大输出，有多少个，这么大的文件
        *    需要用数组0开始计算，len就是最终的，输出一个减去一个len也较少1
        *    输出这么多，也需要刷一下flush，最后管理流资源*/
        try {
            FileInputStream inputStream = new FileInputStream(new File(basePath+name));
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/ipeg");
            int len =0;
            byte[] bytes = new byte[1024];
            while ( (len = inputStream.read(bytes))!= -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
