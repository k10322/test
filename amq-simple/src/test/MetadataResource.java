package com.elsevier.vtw.metadataapi.resource;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elsevier.vtw.framework.exception.VTWException;
import com.elsevier.vtw.metadataservice.service.MetadataConstants;
import com.elsevier.vtw.metadataservice.service.MetadataService;

;

/**
 * This is a Jersey resource class for metadata CRUD API.
 * 
 * @author Sundeep_Raikhelkar
 */
@Component
@Scope("request")
@Path("/name")
public class MetadataResource extends AbstractResource {

	@Autowired
	private MetadataService metadataService;

	@Autowired
	private MetadataResourceHelper metadataResourceHelper;

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(MetadataResource.class);

	/**
	 * Error code
	 */
	final static private String NOT_FOUND = "404";
	
	/**
	 * Variables
	 */
	final static private String MODE = "mode";
	
	
	/**
	 * Creates new metadata document. Throws errors if identifier is already
	 * found. This create method is for prefix:identifier type of call. example
	 * : http://vtw.elsevier.com/metadata/pii/2222
	 * 
	 * @param httpHeaders
	 * @param identifierPrefix
	 * @param identifierValue
	 * @param inputJSON
	 * @param headerBroadcast
	 * @param queryBroadcast
	 * @return
	 */
	 
	 
	 /**
	 
	 GET/PUT/POST/DELETE
	 
Get
http://localhost:8080/employee/id
http://localhost:8080/employee/123
URL
http://localhost:8080/employee/567
Query	
http://localhost:8080/employee?id=123&location=pune
HttpHeaders:
http://localhost:8080/employee
{id = 123}
Attachment
GET  X
PUT/POST/DELETE  - available

@Consumes  only applicable for  post/put/delete
@Produces  applicable to all.


Put
http://localhost:8080/employee  {payload json}
	
JSON :
	{
	name :kartik bhatnagar,
	emp number :156503,
	location : pune
	}

XML :
<output>
   <name>kartik  bhatngar </name>
   <emp_number>  156503</emp_number>
   <location> pune </location>
</output>
employee.war

404 -
500 - exception
200  - ok
400 - bad request
403 - authentication
> 300 - all error conditions.
	
	 **/
	@POST
	@Consumes("application/xml")
@Path("/{id}")
	public Response create(
			@Context final HttpHeaders httpHeaders,
			@PathParam(MetadataConstants.PATH_PARAM_PREFIX) final String id,
			@PathParam(MetadataConstants.PATH_PARAM_ID) final String identifierValue,
			final String inputJSON,
			@HeaderParam(MetadataConstants.VTWBROADCAST) final Boolean headerBroadcast,
			@QueryParam(MetadataConstants.BROADCAST) final Boolean queryBroadcast) {

		LOG.debug(" Simple Create Called -  based on prefix:identifier ");
		final String identifier = prepareIdentifier(identifierPrefix,
				identifierValue);
		Response response = null;
		try {
			response = metadataResourceHelper.processCreateRequest(httpHeaders,
					identifier, inputJSON, headerBroadcast, queryBroadcast);
		} catch (final VTWException vtwException) {
			if (String.valueOf(HttpStatus.SC_BAD_REQUEST).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.BAD_REQUEST, null);
			}
			if (String.valueOf(HttpStatus.SC_BAD_REQUEST).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.BAD_REQUEST, null);
			}
			if (String.valueOf(HttpStatus.SC_UNAUTHORIZED).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.UNAUTHORIZED, null);
			}
			if (String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR, null);
			}

		} catch (final Exception e) {
			LOG.error("Techinical exception ", e);
			response = buildResponse(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR, null);
		}
		return response;
	}

	/**
	 * This create method is for Satellite - URL identifier type of call.
	 * example :
	 * http://vtw.elsevier.com/metadata/http%3A%2F%2Fserver1%2Fpath%3Fparam1
	 * %3Dvalue
	 * 
	 * @param httpHeaders
	 * @param identifier
	 * @param inputJSON
	 * @param headerBroadcast
	 * @param queryBroadcast
	 * @return
	 */
	@POST
	@Consumes("application/ld+json")
	@Path("/id")
	public Response create(
			@Context final HttpHeaders httpHeaders,
			@PathParam(MetadataConstants.PATH_PARAM_PREFIX) String identifier,
			final String inputJSON,
			@HeaderParam(MetadataConstants.VTWBROADCAST) final Boolean headerBroadcast,
			@QueryParam(MetadataConstants.BROADCAST) final Boolean queryBroadcast) {
		LOG.debug(" Create by URL called - for Satellite type of call ");
		LOG.debug(" httpHeaders {}     ", httpHeaders);
		LOG.debug(" identifier    {}   ", identifier);
		LOG.debug(" headerBroadcast {} ", headerBroadcast);
		LOG.debug(" queryBroadcast {}  ", queryBroadcast);

		Response response = null;
		try {
			identifier = URLDecoder.decode(identifier, "UTF-8");
			response = metadataResourceHelper.processCreateRequest(httpHeaders,
					identifier, inputJSON, headerBroadcast, queryBroadcast);
		} catch (final UnsupportedEncodingException ex) {
			LOG.error("Unsupported Encoding Exception ", ex);
		} catch (final VTWException vtwException) {
			if (String.valueOf(HttpStatus.SC_BAD_REQUEST).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.BAD_REQUEST, null);
			}
			if (String.valueOf(HttpStatus.SC_BAD_REQUEST).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.BAD_REQUEST, null);
			}
			if (String.valueOf(HttpStatus.SC_UNAUTHORIZED).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.UNAUTHORIZED, null);
			}
			if (String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR, null);
			}

		} catch (final Exception e) {
			LOG.error("Techinical exception ", e);
			response = buildResponse(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR, null);
		}
		return response;
	}

	/**
	 * This find method is for prefix:identifier type of call example :
	 * http://vtw.elsevier.com/metadata/pii/2222
	 * 
	 * @param httpHeaders
	 * @param identifierPrefix
	 * @param identifierValue
	 * @param queryMode
	 * @param headerMode
	 * @return
	 * 
	 */
	@GET
	@Path("/{id: .+}")
	@Produces("application/ld+json")
	public Response find(
			@Context final HttpHeaders httpHeaders,
			@PathParam(MetadataConstants.PATH_PARAM_PREFIX) final String identifierPrefix,
			@PathParam(MetadataConstants.PATH_PARAM_ID) final String identifierValue,
			@QueryParam(MODE) final String queryMode,
			final @HeaderParam(MetadataConstants.VTWMODE) String headerMode) {
		LOG.debug(" Find Called for prefix:identifier type of call ");
		LOG.debug(" mode : {}", queryMode);
		final String identifier = prepareIdentifier(identifierPrefix,
				identifierValue);
		Response response = null;
		try {
			response = metadataResourceHelper.processFindRequest(httpHeaders,
					identifier, queryMode, headerMode);
		} catch (final VTWException vtwException) {
			if (NOT_FOUND.equals(vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.NOT_FOUND, null);
			} else {
				LOG.error(" code -jersey layer -Error Code : ", vtwException);
			}
			if (String.valueOf(HttpStatus.SC_UNAUTHORIZED).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.UNAUTHORIZED, null);
			}
		} catch (final Exception e) {
			LOG.error(Response.Status.INTERNAL_SERVER_ERROR + e.getMessage());
			response = buildResponse(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR, null);
		}
		return response;
	}

	/**
	 * This find method is for Satellite - URL identifier type of call. example
	 * : http://vtw.elsevier.com/metadata/http%3A%2F%2Fserver1%2Fpath%3Fparam1%
	 * 3Dvalue
	 * 
	 * @param httpHeaders
	 * @param identifier
	 * @param queryMode
	 * @param headerMode
	 * @return
	 */
	@GET
	@Produces("application/ld+json")
	public Response find(@Context final HttpHeaders httpHeaders,
			@PathParam(MetadataConstants.PATH_PARAM_PREFIX) String identifier,
			@QueryParam(MODE) final String queryMode,
			final @HeaderParam(MetadataConstants.VTWMODE) String headerMode) {
		LOG.debug(" Find byURL is called ");
		LOG.debug(" identifier {} ", identifier);
		LOG.debug(" queryMode {} ", queryMode);
		LOG.debug(" headerMode {} ", headerMode);
		Response response = null;
		if (identifier.equalsIgnoreCase("context.jsonld")) {
			String FOUNDATION_JSONLD_CONTEXT = metadataService.getJSONLDContext();
			response = buildResponse(FOUNDATION_JSONLD_CONTEXT,
					Response.Status.OK, null);
			return response;
		} else {

			try {
				identifier = URLDecoder.decode(identifier, "UTF-8");
				response = metadataResourceHelper.processFindRequest(
						httpHeaders, identifier, queryMode, headerMode);

			} catch (final UnsupportedEncodingException ex) {
				LOG.error("Unsupported Encoding Exception ", ex);
			} catch (final VTWException vtwException) {
				if (NOT_FOUND.equals(vtwException.getErrorCode())) {
					response = buildResponse(vtwException.getMessage(),
							Response.Status.NOT_FOUND, null);
				} else {
					LOG.error(" code -jersey layer -Error Code : ",
							vtwException);
				}
				if (String.valueOf(HttpStatus.SC_UNAUTHORIZED).equals(
						vtwException.getErrorCode())) {
					response = buildResponse(vtwException.getMessage(),
							Response.Status.UNAUTHORIZED, null);
				}
			} catch (final Exception e) {
				LOG.error(Response.Status.INTERNAL_SERVER_ERROR
						+ e.getMessage());
				response = buildResponse(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR, null);
			}
			return response;
		}

	}

	/**
	 * Updates metadata document. Throws errors document not found. This method
	 * is for prefix:identifier based call example :
	 * http://vtw.elsevier.com/metadata/pii/2222
	 * 
	 * @param httpHeaders
	 * @param identifierPrefix
	 *            - like pii
	 * @param identifierValue
	 * @param inputJSON
	 * @param headerSeq
	 * @param querySeq
	 * @param headerBroadcast
	 * @param queryBroadcast
	 * @return
	 */
	@PUT
	@Path("/{id: .+}")
	@Consumes("application/ld+json")
	public Response update(
			@Context final HttpHeaders httpHeaders,
			@PathParam(MetadataConstants.PATH_PARAM_PREFIX) final String identifierPrefix,
			@PathParam(MetadataConstants.PATH_PARAM_ID) final String identifierValue,
			final String inputJSON,
			@HeaderParam(MetadataConstants.VTWSEQUENCE) final String headerSeq,
			@QueryParam(MetadataConstants.SEQ) final String querySeq,
			@HeaderParam(MetadataConstants.VTWBROADCAST) final Boolean headerBroadcast,
			@QueryParam(MetadataConstants.BROADCAST) final Boolean queryBroadcast) {
		Response response = null;
		LOG.debug(" update using prefix:identifier type of call ");
		try {
			final String identifier = prepareIdentifier(identifierPrefix,
					identifierValue);
			response = metadataResourceHelper.processUpdateRequest(httpHeaders,
					identifier, inputJSON, headerSeq, querySeq,
					headerBroadcast, queryBroadcast);
		} catch (final VTWException vtwException) {
			if (String.valueOf(HttpStatus.SC_UNAUTHORIZED).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.UNAUTHORIZED, null);
			}

			if (String.valueOf(HttpStatus.SC_BAD_REQUEST).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.BAD_REQUEST, null);
			}
			if (String.valueOf(HttpStatus.SC_NOT_FOUND).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.NOT_FOUND, null);
			}
			if (String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR, null);
			}
			if (String.valueOf(HttpStatus.SC_CONFLICT).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.CONFLICT, null);
			}

		} catch (final Exception e) {
			response = buildResponse(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR, null);
			LOG.error("error with stack ", e);
		}

		return response;
	}

	/**
	 * This update method is for Satellite - URL identifier type of call.
	 * example :
	 * http://vtw.elsevier.com/metadata/http%3A%2F%2Fserver1%2Fpath%3Fparam1
	 * %3Dvalue
	 * 
	 * @param httpHeaders
	 * @param identifier
	 * @param inputJSON
	 * @param headerSeq
	 * @param querySeq
	 * @param headerBroadcast
	 * @param queryBroadcast
	 * @return
	 */
	@PUT
	@Consumes("application/ld+json")
	public Response update(
			@Context final HttpHeaders httpHeaders,
			@PathParam(MetadataConstants.PATH_PARAM_PREFIX) String identifier,
			final String inputJSON,
			@HeaderParam(MetadataConstants.VTWSEQUENCE) final String headerSeq,
			@QueryParam(MetadataConstants.SEQ) final String querySeq,
			@HeaderParam(MetadataConstants.VTWBROADCAST) final Boolean headerBroadcast,
			@QueryParam(MetadataConstants.BROADCAST) final Boolean queryBroadcast) {
		Response response = null;
		LOG.debug(" update by URL is called ");
		LOG.debug(" identifier received {} ", identifier);

		try {
			identifier = URLDecoder.decode(identifier, "UTF-8");
			response = metadataResourceHelper.processUpdateRequest(httpHeaders,
					identifier, inputJSON, headerSeq, querySeq,
					headerBroadcast, queryBroadcast);
		} catch (final UnsupportedEncodingException ex) {
			LOG.error("Unsupported Encoding Exception ", ex);
		} catch (final VTWException vtwException) {
			if (String.valueOf(HttpStatus.SC_UNAUTHORIZED).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.UNAUTHORIZED, null);
			}

			if (String.valueOf(HttpStatus.SC_BAD_REQUEST).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.BAD_REQUEST, null);
			}
			if (String.valueOf(HttpStatus.SC_NOT_FOUND).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.NOT_FOUND, null);
			}
			if (String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR, null);
			}
			if (String.valueOf(HttpStatus.SC_CONFLICT).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.CONFLICT, null);
			}

		} catch (final Exception e) {
			response = buildResponse(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR, null);
			LOG.error("error with stack ", e);
		}
		return response;
	}

	/**
	 * Delete metadata document. Throws errors document not found. example :
	 * http://vtw.elsevier.com/metadata/pii/2222
	 * 
	 * @param httpHeaders
	 * @param identifierPrefix
	 * @param identiferValue
	 * @param headerSeq
	 * @param querySeq
	 * @param headerBroadcast
	 * @param queryBroadcast
	 * @return
	 */
	@DELETE
	@Path("/{id: .+}")
	public Response delete(
			@Context final HttpHeaders httpHeaders,
			@PathParam(MetadataConstants.PATH_PARAM_PREFIX) final String identifierPrefix,
			@PathParam(MetadataConstants.PATH_PARAM_ID) final String identiferValue,
			@HeaderParam(MetadataConstants.VTWSEQUENCE) final String headerSeq,
			@QueryParam(MetadataConstants.SEQ) final String querySeq,
			@HeaderParam(MetadataConstants.VTWBROADCAST) final Boolean headerBroadcast,
			@QueryParam(MetadataConstants.BROADCAST) final Boolean queryBroadcast) {
		Response response = null;
		LOG.debug(" simple Delete called for prefix:Identifier type of call ");
		try {
			final String identifier = prepareIdentifier(identifierPrefix,
					identiferValue);
			response = metadataResourceHelper.processDeleteRequest(httpHeaders,
					identifier, headerSeq, querySeq, headerBroadcast,
					queryBroadcast);
		} catch (final VTWException vtwException) {
			LOG.error("business exception ", vtwException);
			if (String.valueOf(HttpStatus.SC_UNAUTHORIZED).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.UNAUTHORIZED, null);
			} else if (Integer.toString(HttpStatus.SC_NOT_FOUND).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.NOT_FOUND, null);
			} else if (Integer.toString(HttpStatus.SC_CONFLICT).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.CONFLICT, null);
			} else if (Integer.toString(HttpStatus.SC_INTERNAL_SERVER_ERROR)
					.equals(vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR, null);
			} else {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR, null);
			}

		} catch (final Exception e) {
			LOG.error("technical exception ", e);

			response = buildResponse(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR, null);
		}
		return response;
	}

	/**
	 * This create method is for Satellite - URL identifier type of call. Delete
	 * metadata document. Throws errors document not found. example :
	 * http://vtw.
	 * elsevier.com/metadata/http%3A%2F%2Fserver1%2Fpath%3Fparam1%3Dvalue
	 * 
	 * @param httpHeaders
	 * @param identifierPrefix
	 * @param identiferValue
	 * @param headerSeq
	 * @param querySeq
	 * @param headerBroadcast
	 * @param queryBroadcast
	 * @return
	 */
	@DELETE
	public Response delete(
			@Context final HttpHeaders httpHeaders,
			@PathParam(MetadataConstants.PATH_PARAM_PREFIX) String identifier,
			@HeaderParam(MetadataConstants.VTWSEQUENCE) final String headerSeq,
			@QueryParam(MetadataConstants.SEQ) final String querySeq,
			@HeaderParam(MetadataConstants.VTWBROADCAST) final Boolean headerBroadcast,
			@QueryParam(MetadataConstants.BROADCAST) final Boolean queryBroadcast) {
		Response response = null;
		LOG.debug(" Delete by URL called ");
		LOG.debug(" identifier received {} ", identifier);

		try {
			identifier = URLDecoder.decode(identifier, "UTF-8");
			response = metadataResourceHelper.processDeleteRequest(httpHeaders,
					identifier, headerSeq, querySeq, headerBroadcast,
					queryBroadcast);
		} catch (final UnsupportedEncodingException ex) {
			LOG.error("Unsupported Encoding Exception ", ex);
		} catch (final VTWException vtwException) {
			LOG.error("business exception ", vtwException);
			if (String.valueOf(HttpStatus.SC_UNAUTHORIZED).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.UNAUTHORIZED, null);
			} else if (Integer.toString(HttpStatus.SC_NOT_FOUND).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.NOT_FOUND, null);
			} else if (Integer.toString(HttpStatus.SC_CONFLICT).equals(
					vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.CONFLICT, null);
			} else if (Integer.toString(HttpStatus.SC_INTERNAL_SERVER_ERROR)
					.equals(vtwException.getErrorCode())) {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR, null);
			} else {
				response = buildResponse(vtwException.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR, null);
			}

		} catch (final Exception e) {
			LOG.error("technical exception ", e);

			response = buildResponse(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR, null);
		}
		return response;
	}

	private String prepareIdentifier(final String prefix,
			final String identifier) {
		return prefix + ":" + identifier;
	}
}
