package com.tistory.hornslied.evitaonline.shop;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

public class Goods implements ConfigurationSerializable {
	
	private Shop shop;

	private ItemStack item;
	private GoodsType type;
	
	private int buyPrice;
	private int sellPrice;
	
	public Goods(Shop shop, ItemStack item, GoodsType type, int buyPrice, int sellPrice) {
		this.shop = shop;
		this.item = item;
		this.type = type;
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
	}
	
	public Shop getShop() {
		return shop;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public GoodsType getType() {
		return type;
	}
	
	public int getBuyPrice() {
		return buyPrice;
	}
	
	public int getSellPrice() {
		return sellPrice;
	}
	
	public enum GoodsType {
		BUY,
		SELL,
		BOTH
	}

	@Override
	public Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("TYPE", type.toString());
        result.put("ITEM", item);
        switch(type) {
		case BOTH:
			result.put("BUYPRICE", buyPrice);
			result.put("SELLPRICE", sellPrice);
			break;
		case BUY:
			result.put("BUYPRICE", buyPrice);
			break;
		case SELL:
			result.put("SELLPRICE", sellPrice);
			break;
		default:
			break;
        }
		return result;
	}
}
