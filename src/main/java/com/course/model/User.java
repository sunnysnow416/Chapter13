package com.course.model;

import lombok.Data;

@Data
public class User {
	private int id;
	private String userNameString;
	private String passwordString;
	private String ageString;
	private String sexString;
	private String permissionString;
	private String isDelete;
}
