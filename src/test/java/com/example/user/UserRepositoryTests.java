package com.example.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.example.domain.Brand;
import com.example.domain.Role;
import com.example.domain.User;
import com.example.repository.BrandRepository;
import com.example.repository.UserRepository;
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
//	@Test
//	public void testCreateNewUserWithOneRole() {
//		Role roleAdmin = entityManager.find(Role.class, 1);
//		User user = new User("vuthilinh.itk12.1997@gmail.com", tEncodePassword("conyeume@123"), "Nu", "Vu Linh",true);
//		user.addRole(roleAdmin);
//		
//		User savedUser = repo.save(user);
//		
//		assertThat(savedUser.getId()).isGreaterThan(0);
//	}
//	public String tEncodePassword(String passs) {
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		String encodedPassword = passwordEncoder.encode(passs);
//		
//		System.out.println(encodedPassword);
//		return encodedPassword;
//	
//	}

//	@Autowired BrandRepository brandRepository;
//	@Test
//	public void testId()
//	{
//	     Optional<Brand> m  =brandRepository.findById(100);
//	     if(m.orElse(null)!=null)
//	     {
//	    	 System.out.println("khong ton tai");
//	     }
//	}
//	@Autowired BrandRepository brandRepository2;
//	@Test
//	public void listByPage()
//	{
//		int pageNumber=3;
//		int pageSize=5;
//		String keyWord="h";
//		String sortField ="hoa";
//		String sortDir="asc";
//		Sort sort=Sort.by(sortField);
//		sort =sortDir.equals("asc")? sort.ascending():sort.descending();
//		Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
//		Page< Brand>page=brandRepository.findAll(keyWord,pageable);
//		List<Brand>brands=page.getContent();
//		long totalElements=page.getTotalElements();
//		long TotalPages=page.getTotalPages();
//		long startCount=(pageNumber-1)*pageSize+1;
//		long endCount=startCount+pageSize-1;
//	   if(endCount>page.getTotalElements())
//	   {
//		   endCount=page.getTotalElements();
//	   }
//	}
//	
	
//	@Test
//	public void testCreateNewUserWithTwoRoles() {
//		User userRavi = new User("ravi@gmail.com", "ravi2020", "Ravi", "Kumar");
//		Role roleEditor = new Role(3);
//		Role roleAssistant = new Role(5);
//		
//		userRavi.addRole(roleEditor);
//		userRavi.addRole(roleAssistant);
//		
//		User savedUser = repo.save(userRavi);
//		
//		assertThat(savedUser.getId()).isGreaterThan(0);
//	}
//	
//	@Test
//	public void testListAllUsers() {
//		Iterable<User> listUsers = repo.findAll();
//		listUsers.forEach(user -> System.out.println(user));
//	}
//	
//	@Test
//	public void testGetUserById() {
//		User user = repo.findById(1).get();
//		System.out.println(user);
//		assertThat(user).isNotNull();
//	}
//	
//	@Test
//	public void testUpdateUserDetails() {
//		User userNam = repo.findById(1).get();
//		userNam.setEnabled(true);
//		userNam.setEmail("namjavaprogrammer@gmail.com");
//		
//		repo.save(userNam);
//	}
//	
//	@Test
//	public void testUpdateUserRoles() {
//		User userRavi = repo.findById(2).get();
//		Role roleEditor = new Role(3);
//		Role roleSalesperson = new Role(2);
//		
//		userRavi.getRoles().remove(roleEditor);
//		userRavi.addRole(roleSalesperson);
//		
//		repo.save(userRavi);
//	}
//	
//	@Test
//	public void testDeleteUser() {
//		Integer userId = 2;
//		repo.deleteById(userId);
//		
//	}
//	
//	@Test
//	public void testGetUserByEmail() {
//		String email = "ravi@gmail.com";
//		User user = repo.getUserByEmail(email);
//		
//		assertThat(user).isNotNull();
//	}
//	
//	@Test
//	public void testCountById() {
//		Integer id = 1;
//		Long countById = repo.countById(id);
//		
//		assertThat(countById).isNotNull().isGreaterThan(0);
//	}
//	
//	@Test
//	public void testDisableUser() {
//		Integer id = 1;
//		repo.updateEnabledStatus(id, false);
//		
//	}
//	
//	@Test
//	public void testEnableUser() {
//		Integer id = 3;
//		repo.updateEnabledStatus(id, true);
//		
//	}	
//	
//	@Test
//	public void testListFirstPage() {
//		int pageNumber = 0;
//		int pageSize = 4;
//		
//		Pageable pageable = PageRequest.of(pageNumber, pageSize);
//		Page<User> page = repo.findAll(pageable);
//		
//		List<User> listUsers = page.getContent();
//		
//		listUsers.forEach(user -> System.out.println(user));
//		
//		assertThat(listUsers.size()).isEqualTo(pageSize);
//	}
//	
//	@Test
//	public void testSearchUsers() {
//		String keyword = "bruce";
//	
//		int pageNumber = 0;
//		int pageSize = 4;
//		
//		Pageable pageable = PageRequest.of(pageNumber, pageSize);
//		Page<User> page = repo.findAll(keyword, pageable);
//		
//		List<User> listUsers = page.getContent();
//		
//		listUsers.forEach(user -> System.out.println(user));	
//		
//		assertThat(listUsers.size()).isGreaterThan(0);
//	}
	@Test
	public void EncodePassword() {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode("vuthilinh.itk12.1998@gmail.com");
		System.out.println("my mother love my");
		System.out.println(encodedPassword);
         boolean check=passwordEncoder.matches("vuthilinh.itk12.1998@gmail.com", encodedPassword);
         System.out.println(check? "password nhap dung":"password nhap sai");
          Object obj=   passwordEncoder.equals(encodedPassword);
         System.out.println(obj.toString());
	}
}
