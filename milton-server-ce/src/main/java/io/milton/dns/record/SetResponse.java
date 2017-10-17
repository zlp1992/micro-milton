/*
 * Copied from the DnsJava project
 *
 * Copyright (c) 1998-2011, Brian Wellington.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package io.milton.dns.record;

import java.util.*;

/**
 * The Response from a query to Cache.lookupRecords() or Zone.findRecords()
 *
 * @see Cache
 * @see Zone
 *
 * @author Brian Wellington
 */
public class SetResponse {

	/**
	 * The Cache contains no information about the requested name/type
	 */
	static final int UNKNOWN = 0;
	/**
	 * The Zone does not contain the requested name, or the Cache has determined
	 * that the name does not exist.
	 */
	public static final int NXDOMAIN = 1;
	/**
	 * The Zone contains the name, but no data of the requested type, or the
	 * Cache has determined that the name exists and has no data of the
	 * requested type.
	 */
	public static final int NXRRSET = 2;
	/**
	 * A delegation enclosing the requested name was found.
	 */
	public static final int DELEGATION = 3;
	/**
	 * The Cache/Zone found a CNAME when looking for the name.
	 *
	 * @see CNAMERecord
	 */
	public static final int CNAME = 4;
	/**
	 * The Cache/Zone found a DNAME when looking for the name.
	 *
	 * @see DNAMERecord
	 */
	public static final int DNAME = 5;
	/**
	 * The Cache/Zone has successfully answered the question for the requested
	 * name/type/class.
	 */
	public static final int SUCCESSFUL = 6;
	private static final SetResponse unknown = new SetResponse(UNKNOWN);
	private static final SetResponse nxdomain = new SetResponse(NXDOMAIN);
	private static final SetResponse nxrrset = new SetResponse(NXRRSET);
	public int type;
	private Object data;

	private SetResponse() {
	}

	public SetResponse(int type, RRset rrset) {
		if (type < 0 || type > 6) {
			throw new IllegalArgumentException("invalid type");
		}
		this.type = type;
		this.data = rrset;
	}

	public SetResponse(int type) {
		if (type < 0 || type > 6) {
			throw new IllegalArgumentException("invalid type");
		}
		this.type = type;
		this.data = null;
	}

	public static SetResponse ofType(int type) {
		switch (type) {
			case UNKNOWN:
				return unknown;
			case NXDOMAIN:
				return nxdomain;
			case NXRRSET:
				return nxrrset;
			case DELEGATION:
			case CNAME:
			case DNAME:
			case SUCCESSFUL:
				SetResponse sr = new SetResponse();
				sr.type = type;
				sr.data = null;
				return sr;
			default:
				throw new IllegalArgumentException("invalid type");
		}
	}

	public void addRRset(RRset rrset) {
		if (data == null) {
			data = new ArrayList();
		}
		List l = (List) data;
		l.add(rrset);
	}

	/**
	 * Is the answer to the query unknown?
	 */
	public boolean isUnknown() {
		return (type == UNKNOWN);
	}

	/**
	 * Is the answer to the query that the name does not exist?
	 */
	public boolean isNXDOMAIN() {
		return (type == NXDOMAIN);
	}

	/**
	 * Is the answer to the query that the name exists, but the type does not?
	 */
	public boolean isNXRRSET() {
		return (type == NXRRSET);
	}

	/**
	 * Is the result of the lookup that the name is below a delegation?
	 */
	public boolean isDelegation() {
		return (type == DELEGATION);
	}

	/**
	 * Is the result of the lookup a CNAME?
	 */
	public boolean isCNAME() {
		return (type == CNAME);
	}

	/**
	 * Is the result of the lookup a DNAME?
	 */
	public boolean isDNAME() {
		return (type == DNAME);
	}

	/**
	 * Was the query successful?
	 */
	public boolean isSuccessful() {
		return (type == SUCCESSFUL);
	}

	/**
	 * If the query was successful, return the answers
	 */
	public RRset[] answers() {
		if (type != SUCCESSFUL) {
			return null;
		}
		List l = (List) data;
		return (RRset[]) l.toArray(new RRset[l.size()]);
	}

	/**
	 * If the query encountered a CNAME, return it.
	 */
	public CNAMERecord getCNAME() {
		return (CNAMERecord) ((RRset) data).first();
	}

	/**
	 * If the query encountered a DNAME, return it.
	 */
	public DNAMERecord getDNAME() {
		return (DNAMERecord) ((RRset) data).first();
	}

	/**
	 * If the query hit a delegation point, return the NS set.
	 */
	public RRset getNS() {
		return (RRset) data;
	}

	/**
	 * Prints the value of the SetResponse
	 */
	public String toString() {
		switch (type) {
			case UNKNOWN:
				return "unknown";
			case NXDOMAIN:
				return "NXDOMAIN";
			case NXRRSET:
				return "NXRRSET";
			case DELEGATION:
				return "delegation: " + data;
			case CNAME:
				return "CNAME: " + data;
			case DNAME:
				return "DNAME: " + data;
			case SUCCESSFUL:
				return "successful";
			default:
				throw new IllegalStateException();
		}
	}
}
