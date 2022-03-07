package com.learning.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomUserReposioryImpl implements CustomUserRepository{
	
	@PersistenceContext
	EntityManager em;

	@Override
	public void customMethod() {
		// TODO Auto-generated method stub
		
		
		
	}

}
