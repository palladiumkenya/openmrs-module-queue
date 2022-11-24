/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.queue.api.impl;

import javax.validation.constraints.NotNull;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.queue.api.QueueClinicService;
import org.openmrs.module.queue.api.dao.QueueClinicDao;
import org.openmrs.module.queue.model.QueueClinic;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Setter(AccessLevel.MODULE)
public class QueueClinicServiceImpl extends BaseOpenmrsService implements QueueClinicService {
	
	private QueueClinicDao<QueueClinic> dao;
	
	public void setDao(QueueClinicDao<QueueClinic> dao) {
		this.dao = dao;
	}
	
	/**
	 * @see org.openmrs.module.queue.api.QueueClinicService#getQueueClinicByUuid(String)
	 */
	@Override
	@Transactional(readOnly = true)
	public Optional<QueueClinic> getQueueClinicByUuid(@NotNull String queueClinicUuid) {
		return this.dao.get(queueClinicUuid);
	}
	
	/**
	 * @see org.openmrs.module.queue.api.QueueClinicService#getQueueClinicById(Integer)
	 */
	@Override
	@Transactional(readOnly = true)
	public Optional<QueueClinic> getQueueClinicById(@NotNull Integer queueClinicId) {
		return this.dao.get(queueClinicId);
	}
	
	/**
	 * @see org.openmrs.module.queue.api.QueueClinicService#createQueueClinic(org.openmrs.module.queue.model.QueueClinic)
	 */
	@Override
	public QueueClinic createQueueClinic(@NotNull QueueClinic queueClinic) {
		return this.dao.createOrUpdate(queueClinic);
	}
	
	/**
	 * @see QueueClinicService#getAllQueueClinics()
	 */
	@Override
	@Transactional(readOnly = true)
	public Collection<QueueClinic> getAllQueueClinics() {
		return this.dao.findAll();
	}
	
	/**
	 * @see org.openmrs.module.queue.api.QueueService#voidQueue(String, String)
	 */
	@Override
	public void voidQueueClinic(@NotNull String queueClinicUuid, String voidReason) {
		this.dao.get(queueClinicUuid).ifPresent(queueClinic -> {
			queueClinic.setRetired(true);
			queueClinic.setDateRetired(new Date());
			queueClinic.setRetireReason(voidReason);
			queueClinic.setRetiredBy(Context.getAuthenticatedUser());
			//Effect the change
			this.dao.createOrUpdate(queueClinic);
		});
	}
	
	/**
	 * @see org.openmrs.module.queue.api.QueueClinicService#purgeQueueClinic(org.openmrs.module.queue.model.QueueClinic)
	 */
	@Override
	public void purgeQueueClinic(QueueClinic queueClinic) throws APIException {
		this.dao.delete(queueClinic);
	}
	
}
