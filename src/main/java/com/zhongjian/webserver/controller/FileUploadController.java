package com.zhongjian.webserver.controller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zhongjian.webserver.ExceptionHandle.BusinessException;
import com.zhongjian.webserver.common.FileUtil;
import com.zhongjian.webserver.common.LoggingUtil;
import com.zhongjian.webserver.common.Result;
import com.zhongjian.webserver.common.ResultUtil;
import com.zhongjian.webserver.common.Status;
import com.zhongjian.webserver.common.TokenManager;

/**
 * 文件上传的Controller
 * 
 * @author chen_di
 * @create 2017年12月12日
 */
@RestController
public class FileUploadController {

	/**
	 * 文件上传具体实现方法（单文件上传）
	 *
	 * @param file
	 * @return
	 * 
	 * @author chen_di
	 * @throws BusinessException
	 * @create 2017年12月12日
	 */
	@Autowired
	private TokenManager tokenManager;

	@Value("${upload.catalogue}")
	private String catalogue;

	@Value("${upload.httpCatalogue}")
	private String httpCatalogue;

	@RequestMapping(value = "/v1/upload", method = RequestMethod.POST)
	public Result<Object> upload(@RequestParam("file") MultipartFile file, @RequestParam("token") String token)
			throws BusinessException {
		BufferedOutputStream out = null;
		try {
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			if (!file.isEmpty()) {
				// 这里只是简单例子，文件直接输出到项目路径下。
				// 实际项目中，文件需要输出到指定位置，需要在增加代码处理。
				// 还有关于文件格式限制、文件大小限制，详见：中配置。
				String newFileName = FileUtil.changeFileNameToRandom(file.getOriginalFilename());
				out = new BufferedOutputStream(new FileOutputStream(catalogue + newFileName));
				out.write(file.getBytes());
				out.flush();
				return ResultUtil.success(httpCatalogue + newFileName);

			} else {
				return ResultUtil.error(Status.GeneralError.getStatenum(), "上传失败，因为文件是空的.");
			}
		} catch (Exception e) {
			LoggingUtil.e("上传失败," + e.getMessage());
			throw new BusinessException(Status.SeriousError.getStatenum(), "上传失败," + e.getMessage());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				LoggingUtil.e("BufferedOutputStream close fail , " + e.getMessage());
			}
		}
	}

	/**
	 * 多文件上传 主要是使用了MultipartHttpServletRequest和MultipartFile
	 *
	 * @param request
	 * @return
	 * 
	 * @author chen_di
	 * @throws BusinessException
	 * @create 2017年12月12日
	 */
	@RequestMapping(value = "/v1/upload/batch", method = RequestMethod.POST)
	public Result<Object> batchUpload(HttpServletRequest request) throws BusinessException {
		BufferedOutputStream stream = null;
		List<BufferedOutputStream> listStream = null;
		try {
			String token = request.getParameter("token");
			// 检查token通过
			String phoneNum = tokenManager.checkTokenGetUser(token);
			if (phoneNum == null) {
				return ResultUtil.error(Status.TokenError.getStatenum(), "账号在其他终端登录");
			}
			List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
			List<String> newfileNameList = new ArrayList<String>();
			MultipartFile file = null;
			int filesLength = files.size();
			for (int i = 0; i < filesLength; ++i) {
				file = files.get(i);
				if (!file.isEmpty()) {
					byte[] bytes = file.getBytes();
					String newFileName = FileUtil.changeFileNameToRandom(file.getOriginalFilename());
					if (listStream == null) {
						listStream = new ArrayList<BufferedOutputStream>();
					}
					stream = new BufferedOutputStream(new FileOutputStream(catalogue + newFileName));
					listStream.add(stream);
					stream.write(bytes);
					newfileNameList.add(httpCatalogue + newFileName);
					stream.flush();
				} else {
					return ResultUtil.error(Status.GeneralError.getStatenum(),
							"You failed to upload " + i + " because the file was empty.");
				}
			}
			return ResultUtil.success(newfileNameList);
		} catch (Exception e) {
			throw new BusinessException(Status.SeriousError.getStatenum(), "批量上传失败 , " + e.getMessage());
		} finally {
			try {
				int listStreamLenth = listStream.size();
				for (int i = 0; i < listStreamLenth; i++) {
					if (listStream.get(i) != null) {
						listStream.get(i).close();
					}
				}
			} catch (IOException e) {
				LoggingUtil.e("BufferedOutputStream close fail , " + e.getMessage());
			}
		}
	}
}