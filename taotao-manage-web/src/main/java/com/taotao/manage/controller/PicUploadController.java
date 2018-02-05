package com.taotao.manage.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.manage.vo.PicUploadResult;

/**
 * 用于文件上传
 *
 */

@Controller
@RequestMapping("/pic")
public class PicUploadController {
	// 这5中就是支持上传的图片后缀名格式
	private static final String[] IMAGE_TYPES = { ".jpg", ".png", ".bmp", ".jpeg", ".gif" };
	
	private static final ObjectMapper MAPPER = new ObjectMapper();

	// 文件上传,当浏览器需要的是text类型，就需要在produces添加类型,也可以返回json格式
	@RequestMapping(value = "/upload", method = RequestMethod.POST,produces=MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String fileUpload(@RequestParam("uploadFile") MultipartFile multipartFile) throws JsonProcessingException {
		// 创建一个返回对象
		PicUploadResult picUploadResult = new PicUploadResult();
		// 设置成功与否的判断,默认不成功
		picUploadResult.setError(1);

		// 判断文件是否合法
		Boolean isLegal = false;

		for (String type : IMAGE_TYPES) {
			// 获取上传文件的后缀名,因为kindeditor的批量上传文件,它也是拆成一个个上传
			if (multipartFile.getOriginalFilename().lastIndexOf(type) > 0) {
				isLegal = true;
				break;
			}
		}
		// 优化：对文件可执行进行校验
		if (isLegal) {
			//文件上传
			FastDFSFileUpload(multipartFile, picUploadResult);
		}

		return MAPPER.writeValueAsString(picUploadResult);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void FastDFSFileUpload(MultipartFile multipartFile, PicUploadResult picUploadResult) {
		// 校验图片的内容是否为图片
		try {
			// 关于ImageIO去读取图片,如果是一个可执行的图片就不会报错，否则就会报异常
			BufferedImage image = ImageIO.read(multipartFile.getInputStream());

			// 读取FastDFS文件
			String tranckerConfig = this.getClass().getClassLoader().getResource("tracker.conf").getPath();
			// 加载文件
			ClientGlobal.init(tranckerConfig);
			TrackerClient trackerClient = new TrackerClient();
			// 通过trackerClient,创建trackerServer
			TrackerServer trackerServer = trackerClient.getConnection();

			StorageServer storageServer = null;
			// 创建StorageClient,需要传入一个
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);

			// 第二种截取文件后缀名的方法
			String file_ext_name = StringUtils.substringAfter(multipartFile.getOriginalFilename(), ".");
			// 文件上传,需要使用字节传输了,会返回一个字符串数组，里面有文件两个信息，第一个是组名，另外一个是相对路径
			String[] fileInfos = storageClient.upload_appender_file(multipartFile.getBytes(), file_ext_name, null);
			// 创建一个url，用来拼装一个可以访问虚拟机中,在FastDFS的当前上传文件的路径
			String url = "";
			if (fileInfos.length > 1 && fileInfos != null) {
				String groupName = fileInfos[0];
				String filename = fileInfos[1];
				// 使用追踪器,传入文件组,并且名称获取文件信息
				ServerInfo[] serverInfos = trackerClient.getFetchStorages(trackerServer, groupName, filename);
				// serverInfos里面有两个信息,第一个是可访问的地址,第二个是可访问的端口
				// System.out.println("IP地址:"+serverInfo.getIpAddr()+",端口port="+serverInfo.getPort());
				// 开始拼接可访问的地址
				url = "http://" + serverInfos[0].getIpAddr() + "/" + fileInfos[0] + "/" + fileInfos[1];

				// 到达这里说明是上传成功
				picUploadResult.setUrl(url);
				picUploadResult.setError(0);
				// 使用ImageIo读取的图片中可以获取长宽
				picUploadResult.setHeight(image.getHeight() + "");
				picUploadResult.setWidth(image.getWidth() + "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
