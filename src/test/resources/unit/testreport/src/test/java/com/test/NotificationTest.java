package com.test;

import org.junit.Test;

public class NotificationTest {
	public void notATestMethod() {
	}

	/**
	 * Tickets: ZSA-5
	 * <p>Generates 2 claims with their respective claim notes:
	 * <ul>
	 * <li>First claim note should be visible and has claimant contact</li>
	 * <li>Second claim note shouldn't be visible</li>
	 * <li>Check that email has been received only for the first claim</li>
	 * </ul>
	 * </p>
	 *	<p>
	 * Check that there exists a timeline entry for the first claim
	 * containing claim note
	 * </p>
	 */
	@Test
	public void claimNoteNotificationTest() throws Exception {
	}

	/**
	 * Tickets: ZSA-4, ZSA-1
	 * Create claim
	 * Go to claim detail page
	 * Upload claim document to it
	 * Check that email has been sent to the customer
	 * @see Something
	 */
	@Test
	public void testUpload() {
	}

	@Test
	public void testWithoutComment() {
	}
}
