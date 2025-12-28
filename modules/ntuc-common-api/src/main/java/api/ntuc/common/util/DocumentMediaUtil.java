package api.ntuc.common.util;

import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFileEntryConstants;
import com.liferay.document.library.kernel.model.DLFileVersion;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.model.DLVersionNumberIncrease;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFileVersionLocalServiceUtil;
import com.liferay.document.library.util.DLURLHelperUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.service.ResourceLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.permission.ModelPermissions;
import com.liferay.portal.kernel.service.permission.ModelPermissionsFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

public class DocumentMediaUtil {
	
	private DocumentMediaUtil() {
		// Do nothing
	}

	private static Log log = LogFactoryUtil.getLog(DocumentMediaUtil.class);

	private static Long rootFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;

	/*
	 * Add file to DMS folder with permission Guest not permitted to view and each
	 * if files uploaded to same directory with same file name, library will
	 * automatically rename files by adding suffix (runningNo), example: ktp.jpg
	 * uploaded 3 times -> ktp.jpg, ktp(1).jpg, ktp(2).jpg
	 * 
	 * Directory permission applied as follow: if subfolder have parent folder, then
	 * all parent folder will have permission Guest permitted to view else
	 * destination folder will have permission Guest not permitted to view
	 */
	public static FileEntry addFileToFolder(File file, String fileName, String description, String folderPath,
			long groupId, long userId, ServiceContext context) throws Exception {
		return saveFileToFolder(file, fileName, description, folderPath, groupId, userId, context, false, true);
	}

	/*
	 * Add file to DMS folder with permission Guest cannot view and each if files
	 * uploaded to same directory with same file name, liferay will automatically
	 * create new version with same DLFileEntry, example: ktp.jpg uploaded 3 times
	 * -> ktp.jpg (ver1.0), ktp.jpg (ver1.1), ktp.jpg (ver1.2)
	 * 
	 * Directory permission applied as follow: if subfolder have parent folder, then
	 * all parent folder will have permission Guest permitted to view else
	 * destination folder will have permission Guest not permitted to view
	 */
	public static FileEntry addVersioningFileToFolder(File file, String fileName, String description, String folderPath,
			long groupId, long userId, ServiceContext context) throws Exception {
		return saveFileToFolder(file, fileName, description, folderPath, groupId, userId, context, false, false);
	}

	/*
	 * Add file to DMS folder with permission Guest permitted to view and each if
	 * files uploaded to same directory with same file name, library will
	 * automatically rename files by adding suffix (runningNo), example: ktp.jpg
	 * uploaded 3 times -> ktp.jpg, ktp(1).jpg, ktp(2).jpg
	 * 
	 * Directory permission applied as follow: all parent folder and subfolder will
	 * have permission Guest permitted to view
	 */
	public static FileEntry addPublicFileToFolder(File file, String fileName, String description, String folderPath,
			long groupId, long userId, ServiceContext context) throws Exception {
		return DocumentMediaUtil.saveFileToFolder(file, fileName, description, folderPath, groupId, userId, context,
				true, true);
	}

	/*
	 * Add file to DMS folder with permission Guest cannot view and each if files
	 * uploaded to same directory with same file name, liferay will automatically
	 * create new version with same DLFileEntry, example: ktp.jpg uploaded 3 times
	 * -> ktp.jpg (ver1.0), ktp.jpg (ver1.1), ktp.jpg (ver1.2)
	 * 
	 * Directory permission applied as follow: all parent folder and subfolder will
	 * have permission Guest permitted to view
	 */
	public static FileEntry addPublicVersioningFileToFolder(File file, String fileName, String description,
			String folderPath, long groupId, long userId, ServiceContext context) throws Exception {
		return DocumentMediaUtil.saveFileToFolder(file, fileName, description, folderPath, groupId, userId, context,
				true, false);
	}

	public static FileEntry saveFileToFolder(File file, String fileName, String description, String folderPath,
			long groupId, long userId, ServiceContext context, boolean isPublic, boolean isUnique) throws Exception {

		Folder destinationFolder = null;
		// handle folderName using path: /BRI/KPRS/idCard or only one path KPRS
		long parentFolderId = rootFolderId;
		String[] folderNames = folderPath.split("/");
		if (ArrayUtil.isNotEmpty(folderNames)) {
			for (int i = 0; i < folderNames.length; i++) {
				if (Validator.isNotNull(folderNames[i])) {
					String sanitizedFolderName = folderNames[i].replaceAll("\\\\|/|:|\\?|\\*|\"|<|>|\\|", "");
					boolean isLastChildFolder = (i == (folderNames.length - 1));

					// decide permission before create folder
					// Context using default liferay folder context
					ServiceContext folderContext = setDefaultPermission(context, DLFolder.class.getName());
					if (isLastChildFolder && !isPublic) {
						// update folder permission based on isPublic flag

						// Permitted only for administrator, call updateFolderPermission if need to
						// update specific permission
						folderContext = setDefaultPrivatePermission(context, DLFolder.class.getName());

					}

					Folder pathFolder = checkFolder(parentFolderId, sanitizedFolderName, groupId, userId,
							folderContext);

					// get last child folder id
					if (pathFolder != null && isLastChildFolder) {
						destinationFolder = pathFolder;
					}

					// decide parent folder for next iterate child folder
					if (pathFolder != null && i < (folderNames.length - 1)) {
						parentFolderId = pathFolder.getFolderId();
					}
				}
			}
		}

		// update file permission
		ServiceContext fileContext = (ServiceContext) context.clone();
		if (isPublic) {
			fileContext = setDefaultPermission(context, DLFileEntry.class.getName());
		} else {
			// Permitted only for administrator, call updateFilePermission if need to update
			// specific permission
			fileContext = setDefaultPrivatePermission(context, DLFileEntry.class.getName());
		}

		// add file to folder
		String mimeType = MimeTypesUtil.getContentType(file);
		String title = Validator.isNull(fileName) ? file.getName() : fileName;
		String desc = Validator.isNotNull(description) ? description : ("File signature " + title);
		

		log.info("create file " + title);
		FileEntry fileEntry = null;
		try(InputStream is = new FileInputStream(file)) {
			boolean isExist = true;
			String newTitle = title;
			int suffixNum = 0;// if unique, rename file by add suffix (1)
			FileEntry existingFe = null;

			// both unique or versioned check existing file if exist
			while (isExist) {
				try {
					existingFe = DLAppLocalServiceUtil.getFileEntry(groupId, destinationFolder.getFolderId(), newTitle);
				} catch (Exception e) {
					log.error("get existingFe error: " + e.getMessage());

					existingFe = null;
				}

				if (existingFe != null) {
					suffixNum++;

					if (isUnique) {
						// if unique, if exist name added suffix (1)
						int dotIdx = title.lastIndexOf(".");
						String titleFile = title.substring(0, dotIdx);
						String extensionFile = title.substring(dotIdx);
						newTitle = titleFile.concat("(").concat(String.valueOf(suffixNum)).concat(")")
								.concat(extensionFile);

						isExist = true;
					} else {
						// if versioned, title value remain, there nothing to do

						isExist = false;
					}
				} else {
					isExist = false;
				}
			}
			// out if this while, newtitle is not exist in db

			if (!isUnique && !isExist && existingFe != null) {

				fileEntry = DLAppLocalServiceUtil.updateFileEntry(userId, existingFe.getFileEntryId(), newTitle,
						mimeType, newTitle, desc, null, DLVersionNumberIncrease.MINOR, is, file.length(), fileContext);

			} else {
				fileEntry = DLAppLocalServiceUtil.addFileEntry(userId, groupId, destinationFolder.getFolderId(),
						newTitle, mimeType, newTitle, desc, null, is, file.length(), fileContext);

				AssetEntryLocalServiceUtil.updateVisible(DLFileEntryConstants.getClassName(),
						fileEntry.getFileEntryId(), true);

				DLFileVersion dlFileVersion = DLFileVersionLocalServiceUtil
						.getDLFileVersion(fileEntry.getFileVersion().getFileVersionId());
				DLFileEntry dlFileEntry = DLFileEntryLocalServiceUtil.getDLFileEntry(dlFileVersion.getFileEntryId());
				Indexer<DLFileEntry> indexer = IndexerRegistryUtil.nullSafeGetIndexer(DLFileEntry.class);
				indexer.reindex(dlFileEntry);
			}
		} catch (Exception e) {
			log.error("saveFileToFolder error: " + e.getMessage(), e);
		}

		return fileEntry;
	}

	public static FileEntry updateFile(long fileEntryId, File file, String fileName, String description, long groupId,
			long userId, ServiceContext context) throws Exception {

		// update file in folder
		String mimeType = MimeTypesUtil.getContentType(file);
		String title = Validator.isNull(fileName) ? file.getName() : fileName;
		String desc = Validator.isNotNull(description) ? description : ("File signature " + title);

		log.info("update file " + title);
		FileEntry fileEntry = null;
		try(InputStream is = new FileInputStream(file)) {
			fileEntry = DLAppLocalServiceUtil.getFileEntry(fileEntryId);

			fileEntry = DLAppLocalServiceUtil.updateFileEntry(userId, fileEntryId, title, mimeType, title, desc, null,
					DLVersionNumberIncrease.MINOR, is, file.length(), context);

			AssetEntryLocalServiceUtil.updateVisible(DLFileEntryConstants.getClassName(), fileEntry.getFileEntryId(),
					true);

			DLFileVersion dlFileVersion = DLFileVersionLocalServiceUtil
					.getDLFileVersion(fileEntry.getFileVersion().getFileVersionId());
			DLFileEntry dlFileEntry = DLFileEntryLocalServiceUtil.getDLFileEntry(dlFileVersion.getFileEntryId());
			Indexer<DLFileEntry> indexer = IndexerRegistryUtil.nullSafeGetIndexer(DLFileEntry.class);
			indexer.reindex(dlFileEntry);
		} catch (Exception e) {
			log.error("updateFile error: " + e.getMessage(), e);
		}
		log.info("file " + title + " updated");

		return fileEntry;

	}

	public static FileEntry renameFile(long fileEntryId, String newFileName, ServiceContext context) throws Exception {
		log.info("rename file to " + newFileName);
		FileEntry fileEntry = null;
		try {
			fileEntry = DLAppLocalServiceUtil.getFileEntry(fileEntryId);

			String ext = GetterUtil.getString(FileUtil.getExtension(fileEntry.getTitle())).toLowerCase();
			String title = newFileName.concat(StringPool.PERIOD).concat(ext);

			fileEntry = DLAppLocalServiceUtil.updateFileEntry(fileEntry.getUserId(), fileEntryId, title,
					fileEntry.getMimeType(), title, fileEntry.getDescription(), null, DLVersionNumberIncrease.MINOR,
					fileEntry.getContentStream(), fileEntry.getSize(), context);

			AssetEntryLocalServiceUtil.updateVisible(DLFileEntryConstants.getClassName(), fileEntry.getFileEntryId(),
					true);

			DLFileVersion dlFileVersion = DLFileVersionLocalServiceUtil
					.getDLFileVersion(fileEntry.getFileVersion().getFileVersionId());
			DLFileEntry dlFileEntry = DLFileEntryLocalServiceUtil.getDLFileEntry(dlFileVersion.getFileEntryId());
			Indexer<DLFileEntry> indexer = IndexerRegistryUtil.nullSafeGetIndexer(DLFileEntry.class);
			indexer.reindex(dlFileEntry);
		} catch (Exception e) {
			log.error("renameFile error: " + e.getMessage(), e);
		}
		log.info("file " + newFileName + " updated");

		return fileEntry;
	}

	public static FileEntry checkFile(String title, String folderPath, long groupId, long userId,
			ServiceContext context) throws Exception {
		log.info("check file " + title);
		FileEntry fileEntry = null;
		try {
			Folder destinationFolder = null;
			// handle folderName using path: /BRI/KPRS/idCard or only one path KPRS
			long parentFolderId = rootFolderId;
			String[] folderNames = folderPath.split("/");
			if (ArrayUtil.isNotEmpty(folderNames)) {
				for (int i = 0; i < folderNames.length; i++) {
					if (Validator.isNotNull(folderNames[i])) {
						String sanitizedFolderName = folderNames[i].replaceAll("\\\\|/|:|\\?|\\*|\"|<|>|\\|", "");
						Folder pathFolder = findFolder(parentFolderId, sanitizedFolderName, groupId);

						if (pathFolder == null || pathFolder.getFolderId() == 0) {
							return null;
						} else {
							// get last child folder id
							if (pathFolder != null && i == (folderNames.length - 1)) {
								destinationFolder = pathFolder;
							}

							// decide parent folder for next iterate child folder
							if (pathFolder != null && i < (folderNames.length - 1)) {
								parentFolderId = pathFolder.getFolderId();
							}
						}
					}
				}
			}
			if(destinationFolder != null) {
				fileEntry = DLAppLocalServiceUtil.getFileEntry(groupId, destinationFolder.getFolderId(), title);
			}
		} catch (Exception e) {
			log.error("checkFile error: " + e.getMessage());
		}

		return fileEntry;
	}

	public static FileEntry checkFileByAge(String title, String folderPath, int age, long groupId, long userId,
			ServiceContext context) throws Exception {
		log.info("check file by age " + title);
		FileEntry fileEntry = null;
		try {
			FileEntry existingFe = checkFile(title, folderPath, groupId, userId, context);
			if (existingFe != null) {
				// calculate age
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.add(Calendar.DATE, (-1 * age));
				log.info("age " + cal.getTime());

				Date lastModified = Validator.isNotNull(existingFe.getModifiedDate()) ? existingFe.getModifiedDate()
						: existingFe.getCreateDate();
				if (lastModified.equals(cal.getTime()) || lastModified.after(cal.getTime())) {
					fileEntry = existingFe;
				}
			}
		} catch (Exception e) {
			log.error("checkFileByAge error: " + e.getMessage(), e);
		}

		return fileEntry;
	}

	public static FileEntry checkFileByAge(String title, String[] extensions, String folderPath, int age, long groupId,
			long userId, ServiceContext context) throws Exception {
		log.info("check file by age with extensions " + title);
		try {
			if (ArrayUtil.isNotEmpty(extensions)) {
				for (String ext : extensions) {
					// title: only name of file without extension
					String fileName = title.concat(".").concat(ext);
					FileEntry fileEntry = checkFileByAge(fileName, folderPath, age, groupId, userId, context);
					if (fileEntry != null) {
						return fileEntry;
					}
				}
			}
		} catch (Exception e) {
			log.error("checkFileByAge with extensions error: " + e.getMessage(), e);
		}

		return null;
	}

	public static Folder findFolder(long parentFolderId, String folderName, long groupId) throws Exception {
		Folder folder = null;
		try {
			log.info("check folder " + folderName);
			folder = DLAppLocalServiceUtil.getFolder(groupId, parentFolderId, folderName);
		} catch (Exception e) {
			log.error("folder " + folderName + " not exist");
		}

		return folder;
	}

	public static Folder checkFolder(long parentFolderId, String folderName, long groupId, long userId,
			ServiceContext context) throws Exception {

		Folder folder = findFolder(parentFolderId, folderName, groupId);

		if (folder == null || folder.getFolderId() == 0) {
			String desc = "Folder " + folderName;
			folder = DLAppLocalServiceUtil.addFolder(userId, groupId, parentFolderId, folderName, desc, context);
		}
		log.info(folderName + ": " + folder.getFolderId());

		return folder;
	}

	public static Folder renameFolder(String oldFolderPath, String newFolderName, ServiceContext context) {
		Folder folder = null;
		try {
			long groupId = context.getScopeGroupId();
			long userId = context.getUserId();

			Folder destinationFolder = null;
			// handle folderName using path: /BRI/KPRS/idCard or only one path KPRS
			long parentFolderId = rootFolderId;
			String[] folderNames = oldFolderPath.split("/");
			if (ArrayUtil.isNotEmpty(folderNames)) {
				for (int i = 0; i < folderNames.length; i++) {
					if (Validator.isNotNull(folderNames[i])) {
						String sanitizedFolderName = folderNames[i].replaceAll("\\\\|/|:|\\?|\\*|\"|<|>|\\|", "");
						Folder pathFolder = checkFolder(parentFolderId, sanitizedFolderName, groupId, userId, context);

						// get last child folder id
						if (pathFolder != null && i == (folderNames.length - 1)) {
							destinationFolder = pathFolder;
						}

						// decide parent folder for next iterate child folder
						if (pathFolder != null && i < (folderNames.length - 1)) {
							parentFolderId = pathFolder.getFolderId();
						}
					}
				}
			}

			// Folder Name cannot contain \ / : * ? " < > | character
			newFolderName = newFolderName.replaceAll("\\\\|/|:|\\?|\\*|\"|<|>|\\|", "");
			String desc = "Folder " + newFolderName;
			if(destinationFolder != null) {
				folder = DLAppLocalServiceUtil.updateFolder(destinationFolder.getFolderId(), parentFolderId, newFolderName,
						desc, context);
				log.info(oldFolderPath + " to " + newFolderName + ": " + folder.getFolderId());
			}
		} catch (Exception e) {
			log.error("moveFolder error: " + e.getMessage(), e);
		}

		return folder;
	}

	public static FileEntry getFileById(long fileEntryId) {
		FileEntry fileEntry = null;
		try {
			fileEntry = DLAppLocalServiceUtil.getFileEntry(fileEntryId);
		} catch (Exception e) {
			log.error("file with id " + fileEntryId + " not exist");
		}
		return fileEntry;
	}

	public static String generateURL(long fileEntryId, boolean isDownload) {
		try {
			FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(fileEntryId);
			return "/documents/" + fileEntry.getGroupId() + StringPool.SLASH + fileEntry.getUuid() + "?download="
					+ isDownload;
		} catch (Exception e) {
			log.error("generateURL error: " + e.getMessage(), e);
		}

		return null;
	}

	public static String generateThumbnailURL(long fileEntryId, ThemeDisplay themeDisplay) {
		try {
			FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(fileEntryId);
			return DLURLHelperUtil.getThumbnailSrc(fileEntry, fileEntry.getFileVersion(), themeDisplay);
		} catch (Exception e) {
			log.error("generateThumbnailURL error: " + e.getMessage(), e);
		}

		return null;
	}

	/**
	 * This function used to update permission into uploaded files into document
	 * management
	 * 
	 * @param folder
	 * @param serviceContext
	 * @param repositoryId
	 */
	public static void updateFolderPermission(Folder folder, ServiceContext serviceContext, long repositoryId) {
		log.info("start register permission for folder");
		try {
			if (serviceContext.isAddGroupPermissions() || serviceContext.isAddGuestPermissions()) {
				ResourceLocalServiceUtil.addResources(folder.getCompanyId(), folder.getGroupId(), folder.getUserId(),
						DLFolder.class.getName(), folder.getFolderId(), false, serviceContext.isAddGroupPermissions(),
						serviceContext.isAddGuestPermissions());
			} else {
				if (serviceContext.isDeriveDefaultPermissions()) {
					serviceContext.deriveDefaultPermissions(repositoryId, DLFolderConstants.getClassName());
				}

				ResourceLocalServiceUtil.addModelResources(folder.getCompanyId(), folder.getGroupId(),
						folder.getUserId(), DLFolder.class.getName(), folder.getFolderId(),
						serviceContext.getModelPermissions());
			}
		} catch (PortalException e) {
			log.error("failed to register permission with portal exception", e);
		} catch (SystemException e) {
			log.error("failed to register permission with system exception", e);
		} catch (Exception e) {
			log.error("failed to register permission with exception occur", e);
		}
	}

	public static void updateFilePermission(FileEntry fileEntry, String folderPath, String[] roleNames,
			String[] actionIds, long companyId, long groupId, long userId, ServiceContext context) {
		log.info("updateFilePermission for role " + roleNames + " with actions " + actionIds);
		try {
			if (fileEntry != null && Validator.isNotNull(folderPath) && ArrayUtil.isNotEmpty(roleNames)
					&& ArrayUtil.isNotEmpty(actionIds)) {

				// handle folderName using path: /BRI/KPRS/idCard or only one path KPRS
				String[] folderNames = folderPath.split("/");

				for (String roleName : roleNames) {
					Role role = null;
					try {
						role = RoleLocalServiceUtil.getRole(companyId, roleName);
					} catch (Exception e) {
						log.error("updateFilePermission - Error while get role with name " + roleName + ": "
								+ e.getMessage(), e);
					}

					if (role != null) {
						try {
							// Update file permission
							ResourcePermissionLocalServiceUtil.setResourcePermissions(fileEntry.getCompanyId(),
									DLFileEntry.class.getName(), ResourceConstants.SCOPE_INDIVIDUAL,
									String.valueOf(fileEntry.getPrimaryKey()), role.getRoleId(), actionIds);
						} catch (Exception e) {
							log.error("updateFilePermission - Error while set file permission " + fileEntry.getTitle()
									+ ": " + e.getMessage(), e);
						}

						long parentFolderId = rootFolderId;
						try {
							// Update permission each path folder, except root liferay
							if (ArrayUtil.isNotEmpty(folderNames)) {
								for (int i = 0; i < folderNames.length; i++) {
									if (Validator.isNotNull(folderNames[i])) {
										String sanitizedFolderName = folderNames[i]
												.replaceAll("\\\\|/|:|\\?|\\*|\"|<|>|\\|", "");
										Folder pathFolder = checkFolder(parentFolderId, sanitizedFolderName, groupId,
												userId, context);

										// get last child folder id
										if (pathFolder != null) {
											ResourcePermissionLocalServiceUtil.setResourcePermissions(
													pathFolder.getCompanyId(), DLFolder.class.getName(),
													ResourceConstants.SCOPE_INDIVIDUAL,
													String.valueOf(pathFolder.getPrimaryKey()), role.getRoleId(),
													actionIds);
										}

										// decide parent folder for next iterate child folder
										if (pathFolder != null && i < (folderNames.length - 1)) {
											parentFolderId = pathFolder.getFolderId();
										}
									}
								}
							}
						} catch (Exception e) {
							log.error("updateFilePermission - Error while set folder permission " + folderPath + ": "
									+ e.getMessage(), e);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("updateFilePermission - Error: " + e.getMessage(), e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void deleteFileEntry(long fileEntryId) throws Exception {
		FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(fileEntryId);

		// document library/EditEntryAction.java method deleteEntries
		DLAppLocalServiceUtil.deleteFileEntry(fileEntryId);

		Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(FileEntry.class);
		indexer.reindex(fileEntry);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void deleteFolder(String parentFolderPath, String folderName, long groupId, long userId,
			ServiceContext context) throws Exception {
		// Folder Name cannot contain \ / : * ? " < > | character
		folderName = folderName.replaceAll("\\\\|/|:|\\?|\\*|\"|<|>|\\|", "");

		long parentFolderId = rootFolderId;
		Folder targetFolder = null;
		if (Validator.isNotNull(parentFolderPath)) {
			if (parentFolderPath.charAt(0) == '/') {
				parentFolderPath = parentFolderPath.substring(1, parentFolderPath.length());
			}
			String[] paths = parentFolderPath.split("/");

			// loop to get child folder
			for (int i = 0; i < paths.length; i++) {
				targetFolder = checkFolder(parentFolderId, paths[i], groupId, userId, context);
			}
		} else {
			targetFolder = checkFolder(rootFolderId, folderName, groupId, userId, context);
		}

		if (targetFolder != null) {
			DLAppLocalServiceUtil.deleteFolder(targetFolder.getFolderId());
		}

		Indexer indexer = IndexerRegistryUtil.nullSafeGetIndexer(Folder.class);
		indexer.reindex(targetFolder);
	}

	public static ServiceContext setDefaultPermission(ServiceContext context, String className) {
		ServiceContext newContext = (ServiceContext) context.clone();
		try {
			ModelPermissions mp = ModelPermissionsFactory.createWithDefaultPermissions(className);
			newContext.setModelPermissions(mp);
		} catch (Exception e) {
			log.error("setDefaultPermission error: " + e.getMessage(), e);
		}

		return newContext;
	}

	public static ServiceContext setDefaultPrivatePermission(ServiceContext context, String className) {
		ServiceContext newContext = (ServiceContext) context.clone();
		try {
			ModelPermissions defaultMP = ModelPermissionsFactory.createWithDefaultPermissions(className);
			String[] groupActionIds = defaultMP.getActionIds(RoleConstants.PLACEHOLDER_DEFAULT_GROUP_ROLE);
			String[] guestActionIds = {};

			ModelPermissions mp = ModelPermissionsFactory.create(groupActionIds, guestActionIds, className);
			newContext.setModelPermissions(mp);
		} catch (Exception e) {
			log.error("setDefaultPrivatePermission error: " + e.getMessage(), e);
		}

		return newContext;
	}

	public static ServiceContext setGuestPermission(ServiceContext context, String className) {
		ServiceContext newContext = (ServiceContext) context.clone();
		try {
			newContext.setAddGuestPermissions(true);

			String[] groupActionIds = context.getModelPermissions()
					.getActionIds(RoleConstants.PLACEHOLDER_DEFAULT_GROUP_ROLE);
			String[] guestActionIds = { "VIEW" };

			ModelPermissions mp = ModelPermissionsFactory.create(groupActionIds, guestActionIds, className);
			newContext.setModelPermissions(mp);
		} catch (Exception e) {
			log.error("setGuestPermission error: " + e.getMessage(), e);
		}

		return newContext;
	}

	public static String getBase64Image(long fileEntryId) {
		String encoded = "";
		try {
			FileEntry fe = DocumentMediaUtil.getFileById(fileEntryId);
			if (fe != null) {
				InputStream is = fe.getContentStream();
				byte[] bytes = FileUtil.getBytes(is);
				encoded = Base64.encode(bytes);
			}
		} catch (Exception e) {
			log.error("getBase64Image error: " + e.getMessage(), e);
		}

		return encoded;
	}

}
