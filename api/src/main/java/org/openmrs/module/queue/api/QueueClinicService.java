/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.queue.api;

import javax.validation.constraints.NotNull;

import java.util.Collection;
import java.util.Optional;

import org.openmrs.api.APIException;
import org.openmrs.module.queue.model.QueueClinic;

public interface QueueClinicService {
	
	/**
	 * Gets a queue clinic given UUID.
	 *
	 * @param uuid uuid of the queue clinic to be returned.
	 * @return {@link org.openmrs.module.queue.model.QueueClinic}
	 */
	Optional<QueueClinic> getQueueClinicByUuid(@NotNull String uuid);
	
	/**
	 * Gets a queue clinic by id.
	 *
	 * @param id queueClinicId - the id of the queue clinic to retrieve.
	 * @return {@link org.openmrs.module.queue.model.QueueClinic}
	 */
	Optional<QueueClinic> getQueueClinicById(@NotNull Integer id);
	
	/**
	 * Saves a queue clinic
	 *
	 * @param queue clinic the queue clinic to be saved
	 * @return saved {@link org.openmrs.module.queue.model.QueueClinic}
	 */
	QueueClinic createQueueClinic(@NotNull QueueClinic queueClinic);
	
	/**
	 * Gets all queue clinics
	 *
	 * @return all queue clinics
	 */
	Collection<QueueClinic> getAllQueueClinics();
	
	/**
	 * Voids a queue clinic
	 *
	 * @param queueClinicUuid uuid of the queue clinic to be voided
	 * @param voidReason the reason for voiding the queue
	 */
	void voidQueueClinic(@NotNull String queueClinicUuid, String voidReason);
	
	/**
	 * Completely remove a queue clinic from the database
	 *
	 * @param queue clinic queue clinic to be deleted
	 * @throws APIException <strong>Should</strong> delete the given queue clinic from the database
	 */
	void purgeQueueClinic(@NotNull QueueClinic queueClinic) throws APIException;
	
}
