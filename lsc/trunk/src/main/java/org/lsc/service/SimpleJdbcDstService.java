/*
 ****************************************************************************
 * Ldap Synchronization Connector provides tools to synchronize
 * electronic identities from a list of data sources including
 * any database with a JDBC connector, another LDAP directory,
 * flat files...
 *
 *                  ==LICENSE NOTICE==
 * 
 * Copyright (c) 2008 - 2011 LSC Project 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:

 *    * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of the LSC Project nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *                  ==LICENSE NOTICE==
 *
 *               (c) 2008 - 2011 LSC Project
 *         Sebastien Bahloul <seb@lsc-project.org>
 *         Thomas Chemineau <thomas@lsc-project.org>
 *         Jonathan Clarke <jon@lsc-project.org>
 *         Remy-Christophe Schermesser <rcs@lsc-project.org>
 ****************************************************************************
 */

package org.lsc.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.lsc.LscModifications;
import org.lsc.configuration.DatabaseConnectionType;
import org.lsc.configuration.DatabaseDestinationServiceType;
import org.lsc.configuration.TaskType;
import org.lsc.exception.LscServiceConfigurationException;
import org.lsc.exception.LscServiceException;
import org.lsc.exception.LscServiceInitializationException;
import org.lsc.persistence.DaoConfig;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMapping;

/**
 * This class is a Database Service destination service
 * 
 * @author Sebastien Bahloul &lt;seb@lsc-project.org&gt;
 */
public class SimpleJdbcDstService extends AbstractJdbcService implements IWritableService {

	private DatabaseDestinationServiceType serviceConf;

	/**
	 * Simple JDBC source service that gets SQL request names from lsc.properties
	 * and calls the appropriate SQL requests defined in sql-map-config.d
	 * 
	 * @deprecated
	 * @param props Configuration properties
	 * @throws LscServiceInitializationException 
	 */
	@Deprecated
	public SimpleJdbcDstService(Properties props, String beanClassName) throws LscServiceException {
		super(props);
		throw new LscServiceConfigurationException("Unsupported ! Please convert your configuration to XML.");
	}

	/**
	 * Simple JDBC source service that gets SQL request names from lsc.properties
	 * and calls the appropriate SQL requests defined in sql-map-config.d
	 * 
	 * @param task Initialized task containing all necessary pieces of information to initiate connection
	 * 				and load settings 
	 * @throws LscServiceInitializationException 
	 */
	public SimpleJdbcDstService(final TaskType task) throws LscServiceException {
		super((DatabaseConnectionType)task.getDatabaseDestinationService().getConnection().getReference(), task.getBean());
		serviceConf = task.getDatabaseDestinationService();
	}

	/* (non-Javadoc)
	 * @see org.lsc.service.AbstractJdbcService#getRequestNameForList()
	 */
	@Override
	public String getRequestNameForList() {
		return serviceConf.getRequestNameForList();
	}

	/* (non-Javadoc)
	 * @see org.lsc.service.AbstractJdbcService#getRequestNameForObject()
	 */
	@Override
	public String getRequestNameForObject() {
		return serviceConf.getRequestNameForObject();
	}

	/* (non-Javadoc)
	 * @see org.lsc.service.AbstractJdbcService#getRequestNameForId()
	 */
	@Override
	public String getRequestNameForNextId() {
		throw new UnsupportedOperationException("This method should never be called  - this is a software BUG !");
	}

	@Override
	public boolean apply(LscModifications lm) throws LscServiceException {
		Map<String, Object> attributeMap = getAttributesMap(lm.getLscAttributeModifications());
		try {
			sqlMapper.startTransaction();
			switch(lm.getOperation()) {
			case CHANGE_ID:
				// Silently return without doing anything
				break;
			case CREATE_OBJECT:
				for(String request: serviceConf.getRequestsNameForInsert().getString()) {
					sqlMapper.insert(request, attributeMap);
				}
				break;
			case DELETE_OBJECT:
				for(String request: serviceConf.getRequestsNameForDelete().getString()) {
					sqlMapper.delete(request, attributeMap);
				}
				break;
			case UPDATE_OBJECT:
				// Push the destination value
				attributeMap = fillAttributesMap(attributeMap, lm.getDestinationBean());
				for(String request: serviceConf.getRequestsNameForUpdate().getString()) {
					sqlMapper.update(request, attributeMap);
				}
			}
			sqlMapper.commitTransaction();
		} catch (SQLException e) {
			LOGGER.error(e.toString(), e);
			return false;
		} finally {
			try {
				sqlMapper.endTransaction();
			} catch (SQLException e) {
				LOGGER.error(e.toString(), e);
				return false;
			}
		}
		return true;
	}

	/** Fetched attributes name cache */
	private static Map<String, List<String>> attributesNameCache = new HashMap<String, List<String>>();

	@Override
	public List<String> getWriteDatasetIds() {
		String serviceName = serviceConf.getName();
		if(attributesNameCache != null && attributesNameCache.size() > 0) {
			return attributesNameCache.get(serviceName);
		}
		attributesNameCache.put(serviceName, new ArrayList<String>());
		SqlMapClient sqlMapper = null;
		try {
			sqlMapper = DaoConfig.getSqlMapClient((DatabaseConnectionType)serviceConf.getConnection().getReference());
			if(sqlMapper instanceof SqlMapClientImpl) {
				for(String request: serviceConf.getRequestsNameForInsert().getString()) {
					for(ParameterMapping pm : ((SqlMapClientImpl)sqlMapper).getDelegate().getMappedStatement(request).getParameterMap().getParameterMappings()) {
						attributesNameCache.get(serviceName).add(pm.getPropertyName());
					}
				}
			}
		} catch (LscServiceConfigurationException e) {
			LOGGER.error("Error while looking for fetched attributes through JDBC destination service: " + e.toString(), e);
			return null;
		}
		return attributesNameCache.get(serviceName);
	}
}
