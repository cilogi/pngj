package com.hjg.pngj.test;

import java.io.File;

import com.hjg.pngj.FileHelper;
import com.hjg.pngj.ImageInfo;
import com.hjg.pngj.ImageLines;
import com.hjg.pngj.PngReader;
import com.hjg.pngj.PngWriter;
import com.hjg.pngj.chunks.ChunkCopyBehaviour;

/**
 * 
 */
public class SampleReadEvenLines {

	public static void convert(String origFilename, String destFilename) {
		PngReader pngr = FileHelper.createPngReader(new File(origFilename));
		pngr.setUnpackedMode(true);
		ImageInfo imr = pngr.imgInfo;

		ImageInfo imw = new ImageInfo(imr.cols, imr.rows / 2, imr.bitDepth, imr.alpha, imr.greyscale, imr.indexed);
		ImageLines imlines = pngr.readRowsInt(0, imw.rows, 2); // half of the lines
		PngWriter pngw = FileHelper.createPngWriter(new File(destFilename), imw, true);
		pngr.setUnpackedMode(true);
		pngw.copyChunksFirst(pngr, ChunkCopyBehaviour.COPY_ALL); // all chunks are queued
		pngw.writeRowsInt(imlines.scanlines);
		pngw.end();
		pngr.end();
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2 || args[0].equals(args[1])) {
			System.err.println("Arguments: [pngsrc] [pngdest]");
			System.exit(1);
		}
		convert(args[0], args[1]);
		System.out.println("Done. Result in " + args[1]);
	}
}
