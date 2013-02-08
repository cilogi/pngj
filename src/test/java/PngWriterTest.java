// Copyright (c) 2013 Cilogi. All Rights Reserved.
//
// File:        PngWriterTest.java  (08/02/13)
// Author:      tim
//
// Copyright in the whole and every part of this source file belongs to
// Cilogi (the Author) and may not be used, sold, licenced, 
// transferred, copied or reproduced in whole or in part in 
// any manner or form or in or on any media to any person other than 
// in accordance with the terms of The Author's agreement
// or otherwise without the prior written consent of The Author.  All
// information contained in this source file is confidential information
// belonging to The Author and as such may not be disclosed other
// than in accordance with the terms of The Author's agreement, or
// otherwise, without the prior written consent of The Author.  As
// confidential information this source file must be kept fully and
// effectively secure at all times.
//


import com.hjg.pngj.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.logging.Logger;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class PngWriterTest {
    static final Logger LOG = Logger.getLogger(PngWriterTest.class.getName());


    public PngWriterTest() {
    }

    @Before
    public void setUp() {

    }

    @Test
    public void testWrite() {
        try {
            final int color = 0xeeeeee;
            final int SZ = 256;
            OutputStream os = new BufferedOutputStream(new FileOutputStream(new File("c:/tmp/out.png")));
            PngWriter png = new PngWriter(os, new ImageInfo(SZ, SZ, 8, false)); // false -> no alpha
            png.setCompLevel(6);
            ImageLine imageLine = new ImageLine(png.imgInfo);
            for (int column = 0; column < SZ; column++) {
                ImageLineHelper.setPixelRGB8(imageLine, column, color);
            }
            for (int row = 0; row < SZ; row++) {
                png.writeRow(imageLine, row);
            }
            png.end();
        } catch (IOException e) {
            fail("Can't write image: " + e.getMessage());
        }
    }
}