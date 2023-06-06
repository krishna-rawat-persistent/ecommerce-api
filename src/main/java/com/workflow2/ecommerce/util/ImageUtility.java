package com.workflow2.ecommerce.util;
import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 *
 * This class is help us to deal with image compression and de-compression
 *
 * @author krishna_rawat
 */
public class ImageUtility {

    /**
     *
     * This method takes bytes of data and compress it
     *
     * @param data  it take array of bytes as input
     * @return it return compressed array of bytes
     */
    public static byte[] compressImage(byte[] data) throws Exception {

        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception e) {
            throw new Exception("Some Exception Occurred while Compressing!!");
        }
        return outputStream.toByteArray();
    }

    /**
     *
     * This method takes compressed bytes of data and de-compress it
     *
     * @param data  it take array of bytes as input
     * @return it return de-compressed array of bytes
     */
    public static byte[] decompressImage(byte[] data) throws Exception {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception exception) {
            throw new Exception("Exception occurred while decompressing Images!!");
        }
        return outputStream.toByteArray();
    }
}

