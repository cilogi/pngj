package com.hjg.pngj.test;

import java.io.File;

import com.hjg.pngj.FileHelper;
import com.hjg.pngj.ImageInfo;
import com.hjg.pngj.ImageLine;
import com.hjg.pngj.ImageLineHelper;
import com.hjg.pngj.PngWriter;
import com.hjg.pngj.chunks.PngChunkTextVar;

/**
 * grayscale image - distorted diagonal stripes
 */
public class SampleCreateStripes {

	public static void makeTestImage(PngWriter png) {
		int cols = png.imgInfo.cols;
		int rows = png.imgInfo.rows;
		png.getMetadata().setDpi(123.0);
		png.getMetadata().setTimeNow(0);
		png.getMetadata().setText(PngChunkTextVar.KEY_Software, "pngj test");
		double t1 = (cols + rows) / 16.0; // typical period
		ImageLine iline = new ImageLine(png.imgInfo);
		for (int i = 0; i < rows; i++) {
			double fase = Math.sin(1.3 * i / t1);
			for (int j = 0; j < cols; j++) {
				double sin = Math.sin((i + j) * Math.PI / t1 + fase);
				iline.scanline[j] = ImageLineHelper.clampTo_0_255((int) ((sin + 1) * 127 + 0.5));
			}
			png.writeRow(iline, i);
		}
		png.end();
	}

	public static void createTest(String name, int cols, int rows) {
		PngWriter i2 = FileHelper.createPngWriter(new File(name), new ImageInfo(cols, rows, 8, false, true, false),
				true);
		makeTestImage(i2);
		System.out.println("Done: " + name);
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 3) {
			System.err.println("Arguments: [pngdest] [cols] [rows]");
			System.exit(1);
		}
		int cols = Integer.parseInt(args[1]);
		int rows = Integer.parseInt(args[2]);
		createTest(args[0], cols, rows);
	}
}
