import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;

public class SanselanTest {
    public static final String NEGATIVEARRAYSIZEEXCEPTION_JPEG_FILENAME = "/Users/harpreet/Desktop/CS590-1UNIT/NegativeArraySizeException.jpg";

    public static final String OUTOFMEMORY_JPEG_FILENAME = "/Users/harpreet/Desktop/CS590-1UNIT/OutOfMemoryError.jpg";

    /**
     * Utility method to extract dimensions of an image file via javax.imageio libraries.
     * @param imageFile the image file to extract the dimension from.
     * @return the dimensions of the image.
     * @throws IOException
     */
    public static Dimension getImageDimension(File imageFile) throws IOException {
        Dimension retval = null;

        ImageInputStream in = ImageIO.createImageInputStream(imageFile);
        try {
            final Iterator readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = (ImageReader) readers.next();
                try {
                    reader.setInput(in);
                    retval = new Dimension(reader.getWidth(0), reader.getHeight(0));
                } finally {
                    reader.dispose();
                }
            }
        } finally {
            if (in != null)
                in.close();
        }
        return retval;
    }

    /**
     * main method demonstrates how two sample JPEG image files are parsable by javax ImageIO but not
     * parseable by Apache Sanselan.  The second sample JPEG image file causes sanselan to trigger an
     * OutOfMemoryError.
     *
     * @param args - not used
     */
    public static void main(String[] args) {
        File[] testfiles = { new File(NEGATIVEARRAYSIZEEXCEPTION_JPEG_FILENAME), new File(OUTOFMEMORY_JPEG_FILENAME) };

        File jpgfile = null;
        ImageInfo imginfo = null;
        Dimension dimension = null;

        for (int i = 0; i < testfiles.length; i++) {
            try {
                jpgfile = testfiles[i];
                System.out.println("Processsing file [" + jpgfile.getName() + "]");
                dimension = SanselanTest.getImageDimension(jpgfile);

                System.out.println("javax.imageio Dimensions: {width=" + dimension.getWidth() + ",height=" + dimension.getHeight() + "}");
                imginfo = Sanselan.getImageInfo(jpgfile);
                System.out.println("Apache Sanselan Dimensions: {width=" + imginfo.getWidth() + ",height=" + imginfo.getHeight() + "}");
            } catch (Throwable t) {
                System.err.println("Throwable occurred while processing file [" + jpgfile.getName() + "]");
                t.printStackTrace(System.err);
            }
        }
    }
}