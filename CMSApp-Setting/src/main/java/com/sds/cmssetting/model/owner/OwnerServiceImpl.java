package com.sds.cmssetting.model.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sds.cmssetting.domain.Owner;
import com.sds.cmssetting.exception.OwnerException;

@Service
public class OwnerServiceImpl implements OwnerService {

	@Autowired
	private OwnerDAO ownerDAO;
	
	@Override
	public void update(Owner owner) throws OwnerException {
		// TODO Auto-generated method stub
		ownerDAO.update(owner);
	}

}
