package com.changgou.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * seckillActivity实体类
 * @author 黑马架构师2.5
 *
 */
@Table(name="tb_seckill_activity")
public class SeckillActivity implements Serializable {

	@Id
	private Long id;//id


	
	private String title;//秒杀活动标题
	private String status;//状态
	private java.util.Date startDate;//开始日期
	private java.util.Date endDate;//截至日期

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public java.util.Date getStartDate() {
		return startDate;
	}
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}



}
