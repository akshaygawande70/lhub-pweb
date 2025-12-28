package api.ntuc.common.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.ImageResolutionException;
import com.liferay.portal.kernel.image.ImageBag;
import com.liferay.portal.kernel.image.ImageToolUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class CompressImageUtil {
	
	private CompressImageUtil() {
		// Do Nothing
	}
	
	private static Log log = LogFactoryUtil.getLog(CompressImageUtil.class);
	private static final int MAX_HEIGHT = 1920;
	private static final int MAX_WIDTH = 1920;
	private static final String TMP_DIR = System.getProperty("java.io.tmpdir") + File.separatorChar + "CP";

	public static boolean convertImage(String inputImagePath, String outputImagePath, String formatName) {

		try(FileInputStream inputStream = new FileInputStream(inputImagePath);FileOutputStream outputStream  = new FileOutputStream(outputImagePath); ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream))  {

			// reads input image from file
			BufferedImage inputImage = ImageIO.read(inputStream);

			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
			ImageWriter writer = writers.next();

			writer.setOutput(ios);

			ImageWriteParam param = writer.getDefaultWriteParam();
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			if (Validator.isNotNull(formatName) && "tiff".equalsIgnoreCase(formatName)) {
				param.setCompressionType("LZW");
				param.setCompressionQuality(0.05f);
			} else {
				// for jpeg image
				param.setCompressionQuality(0.7f);
			}
			writer.write(null, new IIOImage(inputImage, null, null), param);
			writer.dispose();

			return true;

		} catch (Exception e) {
			log.error(e);
			return false;
		} 
	}

	public static boolean convertAndResizeImage(String inputImagePath, String outputImagePath, String formatName) {

		try {
			log.debug("convertAndResizeImage - inputImagePath: " + inputImagePath + ", outputImagePath: "
					+ outputImagePath);

			File file = new File(inputImagePath);

			ImageBag imageBag = ImageToolUtil.read(file);

			RenderedImage renderedImage = imageBag.getRenderedImage();

			if (renderedImage.getHeight() > MAX_HEIGHT || renderedImage.getWidth() > MAX_WIDTH) {
				log.debug("convertAndResizeImage - resize");

				RenderedImage resize = ImageToolUtil.scale(renderedImage, MAX_HEIGHT, MAX_WIDTH);

				ImageIO.write(resize, imageBag.getType(), file);
			}
			return convertImage(inputImagePath, outputImagePath, formatName);
		} catch (ImageResolutionException e) {
			log.error("convertAndResizeImage - ImageResolutionException: " + e.getMessage(), e);
			return false;
		} catch (Exception e) {
			log.error("convertAndResizeImage - error: " + e.getMessage(), e);
			return false;
		}
	}

	public static File compressImage(File file, String fileType) {

		String ext = GetterUtil.getString(FileUtil.getExtension(file.getName())).toLowerCase();
		log.info("extension :" + ext);

		String filename = fileType + StringPool.PERIOD + ext;
		File folder = new File(TMP_DIR);
		if (!folder.exists())
			folder.mkdirs();
		String tempFileName = TMP_DIR + File.separator + filename;
		File document = new File(tempFileName);

		try {
			log.debug("convertAndResizeImage - inputImagePath: " + file.getPath());

			ImageBag imageBag = ImageToolUtil.read(file);

			RenderedImage renderedImage = imageBag.getRenderedImage();

			if (renderedImage.getHeight() > MAX_HEIGHT || renderedImage.getWidth() > MAX_WIDTH) {
				log.debug("convertAndResizeImage - resize");

				RenderedImage resize = ImageToolUtil.scale(renderedImage, MAX_HEIGHT, MAX_WIDTH);

				ImageIO.write(resize, imageBag.getType(), file);
			}
			convertImage(file.getPath(), document.getPath(), ext);
		} catch (ImageResolutionException e) {
			log.error("convertAndResizeImage - ImageResolutionException: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("convertAndResizeImage - error: " + e.getMessage(), e);
		}

		return document;
	}

	public static void deleteTempFile(File[] file) {
		try {
			for (File temp : file) {
				Files.delete(temp.toPath());
			}
		} catch (Exception e) {
			log.error("Error delete file : " + e.getLocalizedMessage());
		}

	}

}
