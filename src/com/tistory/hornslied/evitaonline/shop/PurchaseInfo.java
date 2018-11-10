package com.tistory.hornslied.evitaonline.shop;

public class PurchaseInfo {

	private Goods goods;
	private int amount;
	private boolean buy;
	
	public PurchaseInfo(Goods goods, boolean buy) {
		this.goods = goods;
		amount = 0;
		this.buy = buy;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public Goods getGoods() {
		return goods;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public boolean isBuy() {
		return buy;
	}
}
