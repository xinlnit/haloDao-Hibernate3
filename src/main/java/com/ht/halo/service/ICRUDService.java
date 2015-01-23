package com.ht.halo.service;

import java.io.Serializable;

public interface ICRUDService <T, PK extends Serializable>{
	    public T findById(PK id);
}
