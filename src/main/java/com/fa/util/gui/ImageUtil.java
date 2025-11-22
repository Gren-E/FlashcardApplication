package com.fa.util.gui;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    private static final Logger LOG = Logger.getLogger(ImageUtil.class);

    public static Image readImage(File imageFile) {
        try {
            return ImageIO.read(imageFile);
        } catch (IOException e) {
            LOG.error(String.format("Could not read image: %s.", imageFile), e);
            return null;
        }
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static Image resize(Image image, int targetWidth, int targetHeight) {
        int imageHeight = image.getHeight(null);
        int imageWidth = image.getWidth(null);

        if (targetWidth <= 0) {
            targetWidth = (int) (imageWidth * ((double) targetHeight / imageHeight));
        }

        if (targetHeight <= 0) {
            targetHeight = (int) (imageHeight * ((double) targetWidth / imageWidth));
        }

        if (targetWidth == imageWidth && targetHeight == imageHeight) {
            return image;
        }

        return bilinearResize(image, targetWidth, targetHeight);
    }

    private static BufferedImage bilinearResize(Image image, int targetWidth, int targetHeight) {
        BufferedImage img = (BufferedImage) image;
        Object hint = RenderingHints.VALUE_INTERPOLATION_BILINEAR;

        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        int w = img.getWidth();
        int h = img.getHeight();

        do {
            if (w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (w < targetWidth) {
                w *= 2;
                if (w > targetWidth) {
                    w = targetWidth;
                }
            }

            if (h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            if (h < targetHeight) {
                h *= 2;
                if (h > targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(img, 0, 0, w, h, null);
            g2.dispose();

            img = tmp;
        } while (w != targetWidth || h != targetHeight);

        return img;
    }

    public static Image replaceColor(Image image, Color originalColor, Color newColor) {
        return replaceColor(image, originalColor, newColor, 0);
    }

    public static Image replaceColor(Image image, Color originalColor, Color newColor, int a) {
        BufferedImage img = deepCopy((BufferedImage) image);
        int width = img.getWidth();
        int height = img.getHeight();

        int origRed = originalColor.getRed();
        int origGreen = originalColor.getGreen();
        int origBlue = originalColor.getBlue();

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                Color xyColor = new Color(img.getRGB(x, y), true);
                if (xyColor.getRed() >= origRed - a && xyColor.getRed() <= origRed + a) {
                    if (xyColor.getGreen() >= origGreen - a && xyColor.getGreen() <= origGreen + a) {
                        if (xyColor.getBlue() >= origBlue - a && xyColor.getBlue() <= origBlue + a) {
                            img.setRGB(x, y, new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), xyColor.getAlpha()).getRGB());
                        }
                    }
                }
            }
        }

        return img;
    }

}
