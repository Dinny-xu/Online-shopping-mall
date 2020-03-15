package com.changgou.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * seckillGoods实体类
 * @author 黑马架构师2.5
 *
 */
@Table(name="tb_seckill_goods")
public class SeckillGoods implements Serializable {

	@Id
	private Long id;//id


	
	private String skuId;//skuId
	private Integer seckillPrice;//秒杀价格
	private Integer seckillNum;//秒杀数量
	private Integer seckillSurplus;//剩余数量
	private Integer seckillLimit;//限购数量
	private Integer timeId;//秒杀时间段id
	private Long activityId;//秒杀活动id
	private String skuName;//sku商品名称
	private String skuSn;//sn
	private Integer skuPrice;//原价格
	private String skuImage;//秒杀商品图片
	private Integer seq;//排序

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public Integer getSeckillPrice() {
		return seckillPrice;
	}
	public void setSeckillPrice(Integer seckillPrice) {
		this.seckillPrice = seckillPrice;
	}

	public Integer getSeckillNum() {
		return seckillNum;
	}
	public void setSeckillNum(Integer seckillNum) {
		this.seckillNum = seckillNum;
	}

	public Integer getSeckillSurplus() {
		return seckillSurplus;
	}
	public void setSeckillSurplus(Integer seckillSurplus) {
		this.seckillSurplus = seckillSurplus;
	}

	public Integer getSeckillLimit() {
		return seckillLimit;
	}
	public void setSeckillLimit(Integer seckillLimit) {
		this.seckillLimit = seckillLimit;
	}

	public Integer getTimeId() {
		return timeId;
	}
	public void setTimeId(Integer timeId) {
		this.timeId = timeId;
	}

	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getSkuSn() {
		return skuSn;
	}
	public void setSkuSn(String skuSn) {
		this.skuSn = skuSn;
	}

	public Integer getSkuPrice() {
		return skuPrice;
	}
	public void setSkuPrice(Integer skuPrice) {
		this.skuPrice = skuPrice;
	}

	public String getSkuImage() {
		return skuImage;
	}
	public void setSkuImage(String skuImage) {
		this.skuImage = skuImage;
	}

	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}



}
