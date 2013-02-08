package com.hjg.pngj.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.hjg.pngj.FilterType;
import com.hjg.pngj.ImageInfo;
import com.hjg.pngj.ImageLine;
import com.hjg.pngj.ImageLineHelper;
import com.hjg.pngj.PngWriter;

/**
 * Creates a huge image
 * <p>
 * This is mainly for profiling
 */
public class CreateHuge {

	/**
	 * if filename==null, the image is writen to a black hole (like a /dev/null
	 */
	public static void createHuge(String filename, final int cols, final int rows) throws Exception {
		OutputStream os = filename == null ? TestsHelper.createNullOutputStream() : new FileOutputStream(new File(
				filename));
		PngWriter png = new PngWriter(os, new ImageInfo(cols, rows, 8, false));
		png.setFilterType(FilterType.FILTER_AVERAGE);
		png.setIdatMaxSize(0x10000);
		png.setCompLevel(6);
		ImageLine iline1 = new ImageLine(png.imgInfo);
		ImageLine iline2 = new ImageLine(png.imgInfo);
		ImageLine iline = iline1;
		for (int j = 0; j < cols; j++) {
			ImageLineHelper.setPixelRGB8(iline1, j, ((j & 0xFF) << 16) | (((j * 3) & 0xFF) << 8) | (j * 2) & 0xFF);
			ImageLineHelper.setPixelRGB8(iline2, j, (j * 13) & 0xFFFFFF);
		}
		long t0 = System.currentTimeMillis();
		for (int row = 0; row < rows; row++) {
			iline = row % 4 == 0 ? iline2 : iline1;
			png.writeRow(iline, row);
		}
		png.end();
		int dt = (int) (System.currentTimeMillis() - t0);
		System.out.println("Created: " + png.imgInfo.toString());
		System.out.printf("%d msecs, %.1f msecs/MPixel \n", dt, dt * 1000000.0 / (cols * rows));
	}

	public static void run3(int cols, int rows) throws Exception {
		createHuge(null, cols, rows);
		createHuge(null, cols, rows);
		createHuge(null, cols, rows);
	}

	public static void main(String[] args) throws Exception {
		run3(5000, 5000);
	}

}
