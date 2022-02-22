package com.glide.sc.models;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain=true)
public class Dept implements Serializable {

	private long id;
	private String deptName;
	private String deptMgr;
	
	public static void main(String args[]) {
		System.out.print("here");
		
		
	}
}
