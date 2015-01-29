package com.ht.halo.service;

import java.io.Serializable;

public interface ICRUDService <T, PK extends Serializable>{
	    public T findById(PK id);
	    public T checkById(PK id);
	    public void add(T entity);
	    public void modify(T entity);
	    public void removeById(PK id);
	    public void remove(T entity);
	    
}
