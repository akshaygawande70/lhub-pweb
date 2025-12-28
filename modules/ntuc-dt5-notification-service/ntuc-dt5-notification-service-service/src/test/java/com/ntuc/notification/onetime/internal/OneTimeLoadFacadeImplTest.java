package com.ntuc.notification.onetime.internal;

import com.liferay.portal.kernel.exception.PortalException;
import com.ntuc.notification.onetime.OneTimeS3LoadService;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OneTimeLoadFacadeImplTest {

    @Mock
    private OneTimeS3LoadService oneTimeS3LoadService;

    @Test
    public void executeS3Path_valid_delegatesToService() throws Exception {
        OneTimeLoadFacadeImpl impl =
                new OneTimeLoadFacadeImpl(oneTimeS3LoadService, new S3PathParser());

        impl.executeS3Path("s3://bucket-a/a/b/c");

        verify(oneTimeS3LoadService, times(1)).execute("bucket-a", "a/b/c");
        verifyNoMoreInteractions(oneTimeS3LoadService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeS3Path_invalidScheme_throws() {
        OneTimeLoadFacadeImpl impl =
                new OneTimeLoadFacadeImpl(oneTimeS3LoadService, new S3PathParser());

        impl.executeS3Path("http://bucket-a/a/b");
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeS3Path_blank_throws() {
        OneTimeLoadFacadeImpl impl =
                new OneTimeLoadFacadeImpl(oneTimeS3LoadService, new S3PathParser());

        impl.executeS3Path("   ");
    }

    @Test(expected = RuntimeException.class)
    public void executeS3Path_portalException_wrapped() throws Exception {
        doThrow(new PortalException("boom"))
                .when(oneTimeS3LoadService).execute("bucket-a", "a");

        OneTimeLoadFacadeImpl impl =
                new OneTimeLoadFacadeImpl(oneTimeS3LoadService, new S3PathParser());

        impl.executeS3Path("s3://bucket-a/a");
    }
}
