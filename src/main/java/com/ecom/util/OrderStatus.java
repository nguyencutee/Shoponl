package com.ecom.util;

public enum OrderStatus {

	IN_PROGRESS(1, "Đang tiến hành"), ORDER_RECEIVED(2, "Đã nhận được đơn hàng"), PRODUCT_PACKED(3, "Sản phẩm đã được đóng gói"),
	OUT_FOR_DELIVERY(4, "Gửi đi để giao hàng"), DELIVERED(5, "Đã gửi"),CANCEL(6,"Đã hủy");

	private Integer id;

	private String name;

	private OrderStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

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

}
