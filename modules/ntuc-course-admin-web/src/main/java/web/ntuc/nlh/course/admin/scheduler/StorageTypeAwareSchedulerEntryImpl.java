package web.ntuc.nlh.course.admin.scheduler;

import com.liferay.portal.kernel.scheduler.SchedulerEntry;
import com.liferay.portal.kernel.scheduler.SchedulerEntryImpl;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.scheduler.StorageTypeAware;
import com.liferay.portal.kernel.scheduler.Trigger;

/**
 * @author fandifadillah
 *
 */
@SuppressWarnings("serial")
public class StorageTypeAwareSchedulerEntryImpl extends SchedulerEntryImpl implements SchedulerEntry, StorageTypeAware {
	/**
	 * StorageTypeAwareSchedulerEntryImpl: Constructor for the class.
	 * 
	 * @param schedulerEntry
	 */
	public StorageTypeAwareSchedulerEntryImpl(final SchedulerEntryImpl schedulerEntry) {
		super(schedulerEntry.getEventListenerClass(), schedulerEntry.getTrigger(), schedulerEntry.getDescription());

		schedulerEntryService = schedulerEntry;

		// use the same default that Liferay uses.
		storageTypeService = StorageType.MEMORY_CLUSTERED;
	}

	/**
	 * StorageTypeAwareSchedulerEntryImpl: Constructor for the class.
	 * 
	 * @param schedulerEntry
	 * @param storageType
	 */
	public StorageTypeAwareSchedulerEntryImpl(final SchedulerEntryImpl schedulerEntry, final StorageType storageType) {
		super(schedulerEntry.getEventListenerClass(), schedulerEntry.getTrigger(), schedulerEntry.getDescription());

		schedulerEntryService = schedulerEntry;
		storageTypeService = storageType;
	}

	@Override
	public String getDescription() {
		return schedulerEntryService.getDescription();
	}

	@Override
	public String getEventListenerClass() {
		return schedulerEntryService.getEventListenerClass();
	}

	@Override
	public StorageType getStorageType() {
		return storageTypeService;
	}

	@Override
	public Trigger getTrigger() {
		return schedulerEntryService.getTrigger();
	}

	private SchedulerEntryImpl schedulerEntryService;
	private StorageType storageTypeService;

}
