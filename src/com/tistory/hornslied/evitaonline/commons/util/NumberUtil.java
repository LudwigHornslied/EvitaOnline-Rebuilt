package com.tistory.hornslied.evitaonline.commons.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class NumberUtil {

	public static String formatBalance(long amount) {
		return new DecimalFormat("###,###", new DecimalFormatSymbols()).format(amount);
	}
}
