package com.changgou.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * seckillTime实体类
 * @author 黑马架构师2.5
 *
 */
@Table(name="tb_seckill_time")
public class SeckillTime implements Serializable {

	@Id
	private Integer id;//id


	
	private String name;//时间段名称
	private String startTime;//开始时间
	private String endTime;//截至时间
	private String status;//状态

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}



}
