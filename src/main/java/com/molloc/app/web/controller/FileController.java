package com.molloc.app.web.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Lists;
import com.molloc.app.entity.FileMeta;

/**
 * @author robot
 * @Description: 文件上传下载控制器
 * @Date 2015年2月14日
 * @Version v1.0
 */
@Controller
@RequestMapping("/files")
public class FileController extends BaseController
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3908765419211838822L;

	LinkedList<FileMeta> files = Lists.newLinkedList();
	FileMeta fileMeta = null;

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String uploadUI()
	{
		return "mess/upload";
	}

	/**
	 * URL: /files/upload
	 * upload(): receives files
	 * 
	 * @param request : MultipartHttpServletRequest auto passed
	 * @param response : HttpServletResponse auto passed
	 * @return LinkedList<FileMeta> as json format
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> upload(MultipartHttpServletRequest request, HttpServletResponse response)
	{
		// 1. build an iterator
		Iterator<String> it = request.getFileNames();
		MultipartFile mf = null;

		// 2. get each file
		while (it.hasNext())
		{
			// 2.1 get next MultipartFile
			mf = request.getFile(it.next());
			logger.info("{} uploaded! ", mf.getOriginalFilename());

			// 2.2 if files > 5 remove the first from the list.
			if (files.size() > 5)
			{
				files.poll();
			}

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			fileMeta.setFileName(mf.getOriginalFilename());
			fileMeta.setFileSize(mf.getSize() / 1024 + "Kb");
			fileMeta.setFileType(mf.getContentType());

			try
			{
				//fileMeta.setBytes(mf.getBytes());
				// copy file to local disk (make sure the path exists).
				FileCopyUtils.copy(mf.getBytes(),
						FileUtils.getFile(servletContext.getRealPath(FILE_UPLOADED_PATH), mf.getOriginalFilename()));
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			// 2.4 add to files
			files.add(fileMeta);
		}
		return files;
	}

	/**
	 * URL: /files/get/{value}
	 * get(): get file as an attachment
	 * 
	 * @param response : passed by the server
	 * @param value : value from the URL
	 * @return void
	 */
	@RequestMapping(value = "/get/{value}", method = RequestMethod.GET)
	public void get(HttpServletResponse response, @PathVariable("value") String value)
	{
		FileMeta getFile = files.get(Integer.parseInt(value));
		try
		{
			response.setContentType(getFile.getFileType());
			response.setHeader("Content-disposition", "attachment; filename=\"" + getFile.getFileName() + "\"");
			FileCopyUtils.copy(getFile.getBytes(), response.getOutputStream());
		} catch (IOException e)
		{
			// TODO: handle exception
		}
	}
}
