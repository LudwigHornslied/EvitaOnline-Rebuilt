package com.tistory.hornslied.evitaonline.balance;

public interface BalanceOwner {

	abstract public void setBalance(long balance);
	abstract public long getBalance();
	abstract public boolean hasBalance(long amount);
	abstract public void deposit(long balance);
	abstract public void withdraw(long balance);
}
