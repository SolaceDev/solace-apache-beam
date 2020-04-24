package com.solace.connector.beam;

import com.solacesystems.jcsmp.JCSMPProperties;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class SolaceIOTest {
	@Rule public ExpectedException thrown = ExpectedException.none();

	private JCSMPProperties testJcsmpProperties;
	private List<String> testQueues;

	private static final Logger LOG = LoggerFactory.getLogger(SolaceIOTest.class);

	@Before
	public void setup() {
		testJcsmpProperties = new JCSMPProperties();
		testJcsmpProperties.setProperty(JCSMPProperties.HOST, "dummySolacePubsubHost");
		testJcsmpProperties.setProperty(JCSMPProperties.USERNAME, "dummy");
		testJcsmpProperties.setProperty(JCSMPProperties.PASSWORD, "dummy");
		testJcsmpProperties.setProperty(JCSMPProperties.VPN_NAME, "dummy");

		testQueues = new ArrayList<>();
		testQueues.add(UUID.randomUUID().toString());
		testQueues.add(UUID.randomUUID().toString());
	}

	@Test
	public void testReadBuildsCorrectly() {
		SolaceIO.Read<SolaceTestRecord> read = SolaceIO.read(testJcsmpProperties, testQueues,
				SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper());

		assertEquals(testJcsmpProperties.toString(), read.jcsmpProperties().toString());
		assertEquals(testQueues, read.queues());
		assertFalse(read.useSenderTimestamp());
		assertEquals(500, read.advanceTimeoutInMillis());
		assertEquals(Long.MAX_VALUE, read.maxNumRecords());
		assertNull(read.maxReadTime());
		assertEquals(SolaceTestRecord.getCoder(), read.coder());
		assertEquals(SolaceTestRecord.getMapper(), read.inboundMessageMapper());
	}

	@Test
	public void testNullJcsmpProperties() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("jcsmpProperties");

		SolaceIO.read(null, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper());
	}

	@Test
	public void testNullJcsmpPropertiesOverride() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("jcsmpProperties");

		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper())
				.withJcsmpProperties(null);
	}

	@Test
	public void test_jcsmpProperties_NullHost() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(JCSMPProperties.HOST + " cannot be null");

		testJcsmpProperties.setProperty(JCSMPProperties.HOST, null);
		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper());
	}

	@Test
	public void test_jcsmpProperties_NullUsername() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(JCSMPProperties.USERNAME + " cannot be null");

		testJcsmpProperties.setProperty(JCSMPProperties.USERNAME, null);
		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper());
	}

	@Test
	public void test_jcsmpProperties_NullPassword() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(JCSMPProperties.PASSWORD + " cannot be null");

		testJcsmpProperties.setProperty(JCSMPProperties.PASSWORD, null);
		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper());
	}

	@Test
	public void test_jcsmpProperties_NullVpn() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(JCSMPProperties.VPN_NAME + " cannot be null");

		testJcsmpProperties.setProperty(JCSMPProperties.VPN_NAME, null);
		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper());
	}

	@Test
	public void test_jcsmpProperties_NonNullClientName() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage(JCSMPProperties.CLIENT_NAME + " must be null");

		testJcsmpProperties.setProperty(JCSMPProperties.CLIENT_NAME, "dummy");
		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper());
	}

	@Test
	public void testNullQueues() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("queues");

		SolaceIO.read(testJcsmpProperties, null, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper());

	}

	@Test
	public void testNullQueuesOverride() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("queues");

		SolaceIO.read(testJcsmpProperties, null, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper())
				.withQueues(new ArrayList<>());
	}

	@Test
	public void testEmptyQueues() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("queues cannot be null or empty");

		SolaceIO.read(testJcsmpProperties, new ArrayList<>(), SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper());

	}

	@Test
	public void testEmptyQueuesOverride() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("queues cannot be null or empty");

		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper())
			.withQueues(new ArrayList<>());
	}

	@Test
	public void testNullCoder() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("coder");

		SolaceIO.read(testJcsmpProperties, testQueues, null, SolaceTestRecord.getMapper());
	}

	@Test
	public void testNullCoderOverride() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("coder");

		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper())
				.withCoder(null);
	}

	@Test
	public void testNullInboundMessageMapper() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("inboundMessageMapper");

		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), null);
	}

	@Test
	public void testNullInboundMessageMapperOverride() {
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("inboundMessageMapper");

		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper())
				.withInboundMessageMapper(null);
	}

	@Test
	public void testAdvanceTimeoutInMillisZero() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("advanceTimeoutInMillis must be greater than 0");

		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper())
				.withAdvanceTimeoutInMillis(0);
	}

	@Test
	public void testAdvanceTimeoutInMillisNegative() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("advanceTimeoutInMillis must be greater than 0");

		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper())
				.withAdvanceTimeoutInMillis(-1);
	}

	@Test
	public void testMaxNumRecordsZero() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("maxNumRecords must be greater than 0");

		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper())
				.withMaxNumRecords(0);
	}

	@Test
	public void testMaxNumRecordsNegative() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("maxNumRecords must be greater than 0");

		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper())
				.withMaxNumRecords(-1);
	}

	@Test
	public void testMaxReadTimeZero() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("maxReadTime must be greater than 0");

		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper())
				.withMaxReadTime(Duration.ZERO);
	}

	@Test
	public void testMaxReadTimeNegative() {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("maxReadTime must be greater than 0");

		SolaceIO.read(testJcsmpProperties, testQueues, SolaceTestRecord.getCoder(), SolaceTestRecord.getMapper())
				.withMaxReadTime(new Duration(-1));
	}
}
