package com.blog.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import lombok.NoArgsConstructor;

import lombok.Setter;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
	private int id;
	@NotEmpty
	@Size(min= 4,message="Username must be min of 4 characters!!")
	private String name;
	@Email
	@NotEmpty(message="Email is required!!")
	private String email;
	@NotEmpty
	@Size(min=3,max=10,message="password must be min of 3 charcters and max 10 characters")
	private String password;
	@NotNull
	private String about;

	private Set<RoleDto> roles = new HashSet<>();

	@JsonIgnore
	public String getPassword() {
		return this.password;
	}
	
	@JsonProperty
	public void setPassword(String password) {
		this.password=password;
	}

}

