
package com.atakmap.android.video;

import com.atakmap.android.androidtest.ATAKInstrumentedTest;

import org.junit.Test;
import static org.junit.Assert.*;

public class StreamManagementUtilsTest extends ATAKInstrumentedTest {

    @Test
    public void testUdp() {
        final ConnectionEntry ce = StreamManagementUtils
                .createConnectionEntryFromUrl("test1", "udp://239.1.1.1:4001");
        assertNotNull(ce);
        assertEquals(ConnectionEntry.Protocol.UDP, ce.getProtocol());
        assertEquals(4001, ce.getPort());
        assertEquals("test1", ce.getAlias());
        assertEquals("239.1.1.1", ce.getAddress());
    }

    @Test
    public void testUdpWithoutPort() {
        final ConnectionEntry ce = StreamManagementUtils
                .createConnectionEntryFromUrl("test1", "udp://239.1.1.1");
        assertNotNull(ce);
        assertEquals(ConnectionEntry.Protocol.UDP, ce.getProtocol());
        assertEquals(1234, ce.getPort());
        assertEquals("test1", ce.getAlias());
        assertEquals("239.1.1.1", ce.getAddress());
    }

    @Test
    public void testUrlEmpty() {
        final ConnectionEntry ce = StreamManagementUtils
                .createConnectionEntryFromUrl("test1", "");
        assertEquals(ce, null);
    }

    @Test
    public void testRtspWithoutPort() {
        final ConnectionEntry ce = StreamManagementUtils
                .createConnectionEntryFromUrl("test1",
                        "rtsp://192.168.1.1:3001/pathname");
        assertNotNull(ce);
        assertEquals(ConnectionEntry.Protocol.RTSP, ce.getProtocol());
        assertEquals(3001, ce.getPort());
        assertEquals("test1", ce.getAlias());
        assertEquals("192.168.1.1", ce.getAddress());
        assertEquals("/pathname", ce.getPath());
    }

    @Test
    public void deepTestUdp() {
        String[] examples = new String[] {
                "udp://231.1.1.1:3500",
                "udp://231.1.1.1:3500/",
                "udp://@231.1.1.1:3500",
                // not technically correct but a robust parser should be able to make these work
                //"udp:/231.1.1.1:3500",
                //"udp:////231.1.1.1:3500",
                "udp://231.1.1.1:3500//",
                //"udp://231.1.1.1:3500:",
                //"udp://231.1.1.1:3500:/"
        };

        for (String s : examples) {
            final ConnectionEntry ce = StreamManagementUtils
                    .createConnectionEntryFromUrl("test1", s);
            assertEquals("Test: " + s, ConnectionEntry.Protocol.UDP,
                    ce.getProtocol());
            assertEquals("Test: " + s, 3500, ce.getPort());
            assertEquals("Test: " + s, "test1", ce.getAlias());
            assertEquals("Test: " + s, "231.1.1.1", ce.getAddress());
        }

    }

    @Test
    public void deepTestUdp2() {
        String[] examples = new String[] {
                "udp://231.1.1.1/",
                "udp://231.1.1.1:/"
        };
        for (String s : examples) {
            final ConnectionEntry ce = StreamManagementUtils
                    .createConnectionEntryFromUrl("test1", s);
            assertEquals("Test: " + s, ConnectionEntry.Protocol.UDP,
                    ce.getProtocol());
            assertEquals("Test: " + s, 1234, ce.getPort());
            assertEquals("Test: " + s, "test1", ce.getAlias());
            assertEquals("Test: " + s, "231.1.1.1", ce.getAddress());
        }
    }

    @Test
    public void deepTestRtsp() {
        String[] examples = new String[] {
                "rtsp://192.168.1.1:8500/some/path-to/file",
                "rtsp://192.168.1.1:8500/some/path-to/file?quality=high&source=local",
                "rtsp://192.168.1.1/some/path-to/file",
                //"rtsp://192.168.1.1?",
                "rtsp://192.168.1.1:?",
                //"rtsp://user:pass@192.168.1.1/some/path-to/file"
        };

        for (String s : examples) {
            final ConnectionEntry ce = StreamManagementUtils
                    .createConnectionEntryFromUrl("test1", s);
            assertEquals("Test: " + s, ConnectionEntry.Protocol.RTSP,
                    ce.getProtocol());
            assertEquals("Test: " + s, "192.168.1.1", ce.getAddress());
        }
    }
}
