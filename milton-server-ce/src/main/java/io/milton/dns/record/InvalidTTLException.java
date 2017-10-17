// Copyright (c) 2003-2004 Brian Wellington (bwelling@xbill.org)

package io.milton.dns.record;

/**
 * An exception thrown when an invalid TTL is specified.
 *
 * @author Brian Wellington
 */

public class InvalidTTLException extends IllegalArgumentException {

public
InvalidTTLException(long ttl) {
	super("Invalid DNS TTL: " + ttl);
}

}
