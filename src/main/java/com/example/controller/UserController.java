package com.example.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.domain.User;
import com.example.dto.response.ResultResponse;
import com.example.exception.CustomException;
import com.example.export.UserCsvExporter;
import com.example.export.UserPdfExporter;
import com.example.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserServiceImpl userServiceImpl;

	@GetMapping("/page/{pageNum}")
	public ResultResponse listByPage(@PathVariable(name = "pageNum") int pageNum,
			@RequestParam("pageSize") int pageSize, @RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir, @RequestParam("keyword") String keyword) {
		return userServiceImpl.listByPage(pageNum, pageSize, sortField, sortDir, keyword);
	}

	@PostMapping("")
	public ResultResponse saveUser(@RequestParam("image") MultipartFile multipartFile,
			@RequestParam("user") String user) throws CustomException {
		ObjectMapper mapper = new ObjectMapper();
		User user2 = null;
		System.out.println(user);
		try {
			user2 = mapper.readValue(user, User.class);
		} catch (JsonProcessingException e1) {
			throw new CustomException(ResultResponse.STATUS_CODE_BAD_REQUEST, user, user);
		}
		return userServiceImpl.save(multipartFile, user2);
	}

	@DeleteMapping("/{id}")
	public ResultResponse deleteUser(@PathVariable(name = "id") Integer id) throws CustomException {

		return userServiceImpl.delete(id);
	}
	@PutMapping("/{id}/enabled/{status}")
	public ResultResponse updateUserEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled) {
		System.out.println(enabled);
		return userServiceImpl.updateUserEnabledStatus(id, enabled);
		
	}
	@GetMapping("/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers =userServiceImpl.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}
	@GetMapping("/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = userServiceImpl.listAll();
		
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
	}
   
}
