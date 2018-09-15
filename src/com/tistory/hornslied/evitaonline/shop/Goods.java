package com.tistory.hornslied.evitaonline.shop;

import org.bukkit.inventory.ItemStack;

public class Goods {

	private ItemStack item;
	private GoodsType type;
	
	private int buyPrice;
	private int sellPrice;
	
	public Goods(ItemStack item, GoodsType type, int buyPrice, int sellPrice) {
		this.item = item;
		this.type = type;
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public GoodsType getType() {
		return type;
	}
	
	public enum GoodsType {
		BUY,
		SELL,
		BOTH
	}
}
