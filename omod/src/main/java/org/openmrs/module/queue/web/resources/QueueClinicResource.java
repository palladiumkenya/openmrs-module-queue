/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.queue.web.resources;

import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Optional;

import org.openmrs.api.context.Context;
import org.openmrs.module.queue.api.QueueClinicService;
import org.openmrs.module.queue.model.QueueClinic;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.CustomRepresentation;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ObjectNotFoundException;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1 + "/queue-clinic", supportedClass = QueueClinic.class, supportedOpenmrsVersions = {
        "2.0 - 2.*" })
public class QueueClinicResource extends DelegatingCrudResource<QueueClinic> {

	private final QueueClinicService queueClinicService;

	public QueueClinicResource() {
		this.queueClinicService = Context.getService(QueueClinicService.class);
	}

	@Override
	public NeedsPaging<QueueClinic> doGetAll(RequestContext requestContext) throws ResponseException {
		return new NeedsPaging<QueueClinic>(
		        new ArrayList<QueueClinic>(Context.getService(QueueClinicService.class).getAllQueueClinics()),
		        requestContext);
	}

	@Override
	public QueueClinic getByUniqueId(@NotNull String uuid) {
		Optional<QueueClinic> optionalQueueClinic = queueClinicService.getQueueClinicByUuid(uuid);
		if (!optionalQueueClinic.isPresent()) {
			throw new ObjectNotFoundException("Could not find queue clinic with UUID " + uuid);
		}
		return optionalQueueClinic.get();
	}

	@Override
	protected void delete(QueueClinic queueClinic, String retireReason, RequestContext requestContext)
	        throws ResponseException {
		if (!this.queueClinicService.getQueueClinicByUuid(queueClinic.getUuid()).isPresent()) {
			throw new ObjectNotFoundException("Could not find queue clinic with uuid " + queueClinic.getUuid());
		}
		this.queueClinicService.voidQueueClinic(queueClinic.getUuid(), retireReason);
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
		DelegatingResourceDescription resourceDescription = new DelegatingResourceDescription();
		if (representation instanceof RefRepresentation) {
			this.addSharedResourceDescriptionProperties(resourceDescription);
			resourceDescription.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		} else if (representation instanceof DefaultRepresentation) {
			this.addSharedResourceDescriptionProperties(resourceDescription);
			resourceDescription.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		} else if (representation instanceof FullRepresentation) {
			this.addSharedResourceDescriptionProperties(resourceDescription);
			resourceDescription.addProperty("auditInfo");
		} else if (representation instanceof CustomRepresentation) {
			//For custom representation, must be null
			// - let the user decide which properties should be included in the response
			resourceDescription = null;
		}
		return resourceDescription;
	}

	@Override
	public QueueClinic newDelegate() {
		return new QueueClinic();
	}

	@Override
	public QueueClinic save(QueueClinic queueClinic) {
		return this.queueClinicService.createQueueClinic(queueClinic);
	}

	@Override
	public void purge(QueueClinic queueClinic, RequestContext requestContext) throws ResponseException {
		this.queueClinicService.purgeQueueClinic(queueClinic);
	}

	private void addSharedResourceDescriptionProperties(DelegatingResourceDescription resourceDescription) {
		resourceDescription.addSelfLink();
		resourceDescription.addProperty("uuid");
		resourceDescription.addProperty("name");
		resourceDescription.addProperty("description");
	}

	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription resourceDescription = new DelegatingResourceDescription();
		resourceDescription.addProperty("name");
		resourceDescription.addProperty("description");
		return resourceDescription;
	}

	@Override
	public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
		return this.getCreatableProperties();
	}

	@Override
	public String getResourceVersion() {
		//What determines the resource version? is it the target platform version or just 1.8
		return "2.3";
	}

}