package com.gloiot.hygoSupply.utlis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.provider.MediaStore.Images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class VideoUtils {

	private VideoUtils() {
	};

	public static boolean hasFroyo() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;

	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
	}

	public static boolean hasKitKat() {
		return Build.VERSION.SDK_INT >= 19;
	}

	public static List<Size> getResolutionList(Camera camera) {
		Parameters parameters = camera.getParameters();
		List<Size> previewSizes = parameters.getSupportedPreviewSizes();
		return previewSizes;
	}

	public static class ResolutionComparator implements Comparator<Size> {

		@Override
		public int compare(Size lhs, Size rhs) {
			if (lhs.height != rhs.height)
				return lhs.height - rhs.height;
			else
				return lhs.width - rhs.width;
		}

	}
	
	public static String getVideoThumbPath(Context context,String videoPath){
		String imagePath = videoPath.replace(".mp4", ".jpg");
		File file = new File(imagePath);
		Bitmap bitmap = null;
		FileOutputStream fos = null;
		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			bitmap = ThumbnailUtils.createVideoThumbnail(videoPath,Images.Thumbnails.MINI_KIND);
			if (bitmap == null) {
//				bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.video_panel_icon);
			}
			fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 90, fos);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fos = null;
			}
			if (bitmap != null) {
				bitmap.recycle();
				bitmap = null;
			}
		}
		return file == null ? null : file.getAbsolutePath();
	}

	public static Bitmap getVideoThumbBitmap(Context context,String videoPath){
		Bitmap bitmap = null;
		try {
			bitmap = ThumbnailUtils.createVideoThumbnail(videoPath,Images.Thumbnails.MINI_KIND);
			if (bitmap == null) {
//				bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.video_panel_icon);
			}
			return bitmap;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}
	
	public static String videoThumbIsExsit(String videoPath){
		String imagePath = videoPath.replace(".mp4", ".jpg");
		File file = new File(imagePath);
		if(file.exists()){
		    return file.getAbsolutePath();
		}
		return null;
	}
}
