package com.hjg.pngj.test;

import java.io.File;

import com.hjg.pngj.FileHelper;
import com.hjg.pngj.ImageInfo;
import com.hjg.pngj.ImageLine;
import com.hjg.pngj.ImageLineHelper;
import com.hjg.pngj.PngReader;
import com.hjg.pngj.PngWriter;
import com.hjg.pngj.chunks.ChunkCopyBehaviour;
import com.hjg.pngj.chunks.PngChunkPLTE;
import com.hjg.pngj.chunks.PngChunkTRNS;

/**
 * This converts a paletted image to a True color image If the image has
 * transparency (tRNS chunk) it generates a RGBA image, elsewhere a RGB
 */
public class SampleConvPalToTrueColor {

	public static void convertToTc(File orig, File copy) {
		PngReader pngr = FileHelper.createPngReader(orig);
		if (!pngr.imgInfo.indexed)
			throw new RuntimeException("Not indexed image");
		PngChunkPLTE plte = pngr.getMetadata().getPLTE();
		PngChunkTRNS trns = pngr.getMetadata().getTRNS(); // transparency metadata, can be null
		boolean alpha = trns != null;
		ImageInfo im2 = new ImageInfo(pngr.imgInfo.cols, pngr.imgInfo.rows, 8, alpha);
		PngWriter pngw = FileHelper.createPngWriter(copy, im2, false);
		pngw.copyChunksFirst(pngr, ChunkCopyBehaviour.COPY_ALL_SAFE);
		int[] buf = null;
		for (int row = 0; row < pngr.imgInfo.rows; row++) {
			ImageLine line = pngr.readRowInt(row);
			buf = ImageLineHelper.palette2rgb(line, plte, trns, buf);
			pngw.writeRowInt(buf, row);
		}
		pngw.copyChunksLast(pngr, ChunkCopyBehaviour.COPY_ALL_SAFE);
		pngr.end();
		pngw.end();
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2 || args[0].equals(args[1])) {
			System.err.println("Arguments: [pngsrc] [pngdest]");
			System.exit(1);
		}
		convertToTc(new File(args[0]), new File(args[1]));
		System.out.println("Done: " + args[1]);
	}
}
