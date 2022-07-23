package com.assignment.smallcase.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="test")
public class TestModel {
	  @Id
	  private String id;
	  private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "TestModel [id=" + id + ", name=" + name + "]";
	}
	  
	
}
