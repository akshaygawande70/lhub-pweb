package web.ntuc.nlh.course.admin.scheduler;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.SchedulerEntryImpl;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.scheduler.StorageTypeAware;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactory;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import web.ntuc.nlh.course.admin.util.CourseConvertFromApi;
import web.ntuc.nlh.course.admin.util.GenerateCourseCMS;

/**
 * @author fandifadillah
 *
 */
@Component(immediate = true, property = {
		"course.admin.cron.expression=0 0 3 1/1 * ? *" }, service = CourseAdminScheduler.class)
public class CourseAdminScheduler extends BaseMessageListener {

	@Override
	protected void doReceive(Message message) throws Exception {
		log.info("start scheduler");
		long startTime = System.currentTimeMillis();
		CourseConvertFromApi convertFromApi = new CourseConvertFromApi();
		convertFromApi.converter();
		GenerateCourseCMS generateCourseCMS = new GenerateCourseCMS();
		generateCourseCMS.moveApprovedCourse();
		generateCourseCMS.generate();
		generateCourseCMS.deleteNonTMSCourse();
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		String elapsedTimeString = String.format("%02d hours, %02d min, %02d sec", 
				TimeUnit.MILLISECONDS.toHours(elapsedTime),
				TimeUnit.MILLISECONDS.toMinutes(elapsedTime) -
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedTime)),
			    TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime))
			);
		log.info("Elapsed Time = "+elapsedTimeString);
		log.info("end scheduler");
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	@Reference(unbind = "-")
	protected void setTriggerFactory(TriggerFactory triggerFactory) {
		_triggerFactory = triggerFactory;
	}

	@Reference(unbind = "-")
	protected void setSchedulerEngineHelper(SchedulerEngineHelper schedulerEngineHelper) {
		_schedulerEngineHelper = schedulerEngineHelper;
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) throws SchedulerException {

		String cronExpression = GetterUtil.getString(properties.get("course.admin.cron.expression"),
				_DEFAULT_CRON_EXPRESSION);

		String listenerClass = getClass().getName();
		Trigger jobTrigger = _triggerFactory.createTrigger(listenerClass, listenerClass, new Date(), null,
				cronExpression);
		
		log.info(listenerClass + " was actived on " + cronExpression);

		_schedulerEntryImpl = new SchedulerEntryImpl(getClass().getName(), jobTrigger, "");
		_schedulerEntryImpl = new StorageTypeAwareSchedulerEntryImpl(_schedulerEntryImpl, StorageType.PERSISTED);

//		_schedulerEntryImpl.setTrigger(jobTrigger);

		if (_initialized) {
			deactivate();
		}

		// register the scheduled task
		_schedulerEngineHelper.register(this, _schedulerEntryImpl, DestinationNames.SCHEDULER_DISPATCH);

		// set the initialized flag.
		_initialized = true;
	}

	@Deactivate
	protected void deactivate() {
		if (_initialized) {
			try {
				_schedulerEngineHelper.unschedule(_schedulerEntryImpl, getStorageType());
			} catch (SchedulerException se) {
				if (log.isWarnEnabled()) {
					log.warn("Unable to unschedule trigger", se);
				}
			}

			_schedulerEngineHelper.unregister(this);
		}

		_initialized = false;
	}

	protected StorageType getStorageType() {
		if (_schedulerEntryImpl instanceof StorageTypeAware) {
			return ((StorageTypeAware) _schedulerEntryImpl).getStorageType();
		}

		return StorageType.MEMORY_CLUSTERED;
	}

	// http://www.cronmaker.com/
	private static final String _DEFAULT_CRON_EXPRESSION = "0 0 12 1/1 * ? *";

	private static final Log log = LogFactoryUtil.getLog(CourseAdminScheduler.class);

	private volatile boolean _initialized;
	private TriggerFactory _triggerFactory;
	private SchedulerEngineHelper _schedulerEngineHelper;
	private SchedulerEntryImpl _schedulerEntryImpl = null;
}
