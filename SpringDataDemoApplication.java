package com.rk.springdata;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import lombok.Data;

@SpringBootApplication
public class SpringDataDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataDemoApplication.class, args);
		
	}
	
	@Bean
	CommandLineRunner initData(EmployeeRepository empRepository,ManagerRepository mRepository) {
		return args -> {
			Manager m = mRepository.save(new Manager("Lali"));
			Manager m2 = mRepository.save(new Manager("Maharjan"));
			empRepository.save(new Employee ("Ram","Maharjan","Carefirst",m));
			empRepository.save(new Employee("Nilesh","Shrestha","TRowe",m2));
			empRepository.save(new Employee("Dinesh","Maharjan","Columbia",m));
		};
	}
}	

	@Data
	@Entity
	class Manager {
		
		@Id
		@GeneratedValue
		private Long id;
		
		private String name;
		
		@OneToMany(mappedBy="manager")
		private List<Employee> employee;
		
		public Manager() {
			
		}
		public Manager(String name) {
			this.setName(name);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	@Data
	@Entity
	class Employee {
		
		@Id @GeneratedValue
		private Long id;
		
		
		private String firstName;
		private String lastName;
		private String role;
		
		@ManyToOne
		private Manager manager;
		
		public Employee() {
			//Default Constructor
		}
		
		public Employee(String firstName,String lastname,String role,Manager manager) {
			this.firstName = firstName;
			this.lastName = lastname;
			this.role=role;
			this.setManager(manager);
		}
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public Manager getManager() {
			return manager;
		}

		public void setManager(Manager manager) {
			this.manager = manager;
		}

	}
	
	@RepositoryRestResource
	interface EmployeeRepository extends CrudRepository<Employee,Long> {
		
		List<Employee> findByLastName(@Param("l") String lastName);
		List<Employee> findByLastNameContains(@Param("x") String lastName);
		List<Employee> findByRole(@Param("r") String roleName);
	}

	@RepositoryRestResource
	interface ManagerRepository extends CrudRepository<Manager,Long> {
		
		List<Manager> findByEmployeeRoleContains(@Param("r") String role);
	}
