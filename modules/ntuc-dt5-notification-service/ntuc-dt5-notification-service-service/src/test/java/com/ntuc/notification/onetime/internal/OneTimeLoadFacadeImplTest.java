package com.ntuc.notification.onetime.internal;

import com.liferay.portal.kernel.exception.PortalException;
import com.ntuc.notification.onetime.OneTimeS3LoadService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link OneTimeLoadFacadeImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class OneTimeLoadFacadeImplTest {

    @Mock
    private OneTimeS3LoadService oneTimeS3LoadService;

    @Mock
    private S3PathParser s3PathParser;

    @Mock
    private S3PathParser.S3Location s3Location;

    @Test
    public void constructor_nullService_throwsNpe() {
        try {
            new OneTimeLoadFacadeImpl(null);
            Assert.fail("Expected NullPointerException");
        }
        catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void packageConstructor_nullParser_throwsNpe() {
        try {
            new OneTimeLoadFacadeImpl(oneTimeS3LoadService, null);
            Assert.fail("Expected NullPointerException");
        }
        catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void executeS3Path_valid_delegatesToService() throws Exception {
        when(s3PathParser.parse(anyString())).thenReturn(s3Location);
        when(s3Location.getBucket()).thenReturn("my-bucket");
        when(s3Location.getPrefix()).thenReturn("some/prefix");

        OneTimeLoadFacadeImpl facade = new OneTimeLoadFacadeImpl(oneTimeS3LoadService, s3PathParser);

        facade.executeS3Path("s3://my-bucket/some/prefix");

        verify(oneTimeS3LoadService).execute("my-bucket", "some/prefix");
    }

    @Test
    public void executeS3Path_whenServiceThrowsPortalException_wrapsRuntimeException() throws Exception {
        when(s3PathParser.parse(anyString())).thenReturn(s3Location);
        when(s3Location.getBucket()).thenReturn("my-bucket");
        when(s3Location.getPrefix()).thenReturn("some/prefix");

        doThrow(new PortalException("CLS/S3 load failure"))
                .when(oneTimeS3LoadService)
                .execute("my-bucket", "some/prefix");

        OneTimeLoadFacadeImpl facade = new OneTimeLoadFacadeImpl(oneTimeS3LoadService, s3PathParser);

        try {
            facade.executeS3Path("s3://my-bucket/some/prefix");
            Assert.fail("Expected RuntimeException");
        }
        catch (RuntimeException ex) {
            Assert.assertEquals("One-time S3 load failed", ex.getMessage());
            Assert.assertNotNull(ex.getCause());
            Assert.assertTrue(ex.getCause() instanceof PortalException);
            Assert.assertEquals("CLS/S3 load failure", ex.getCause().getMessage());
        }
    }
}
