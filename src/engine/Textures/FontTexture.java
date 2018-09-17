package engine.Textures;

import engine.Interface.InputProperty;
import engine.Util.Error;
import engine.Util.Raw;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

public class FontTexture extends TextureAtlas {

    private static final String IMAGE_FORMAT = "png";

    Font font;
    public String charSetName;
    public Map<Byte, CharInfo> charMap;

    public FontTexture(InputProperty<Raw> input)  {
        super(input);
    }

    @Override
    public void create() {
        font = raw.get("font");
        charSetName = raw.get("charSetName");
        charMap = new HashMap<>();

        // Get the font metrics for each character for the selected font by using image
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = img.createGraphics();
        g2D.setFont(font);
        FontMetrics fontMetrics = g2D.getFontMetrics();

        String allChars = getAllAvailableChars(charSetName);
        this.width = 0;
        this.height = 0;
        char[] chars = allChars.toCharArray();
        byte[] bytes = allChars.getBytes();
        for (int i = 0; i < allChars.length(); i++) {

            char c = chars[i];
            byte b = bytes[i];
            CharInfo charInfo = new CharInfo(c, width, fontMetrics.charWidth(c));
            charMap.put(b, charInfo);
            width += charInfo.width;
            height = Math.max(height, fontMetrics.getHeight());
        }

        g2D.dispose();

        // Create the image associated to the charset
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2D = img.createGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(font);
        fontMetrics = g2D.getFontMetrics();
        g2D.setColor(Color.WHITE);
        g2D.drawString(allChars, 0, fontMetrics.getAscent());
        g2D.dispose();

        InputStream is = null;
        try {
            ImageIO.write(img, IMAGE_FORMAT, new java.io.File("generateTextureTemp.png"));
            // Dump image to a byte buffer

            try (
                    ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                ImageIO.write(img, IMAGE_FORMAT, out);
                out.flush();
                is = new ByteArrayInputStream(out.toByteArray());
            }
        } catch (Exception e) {
            Error.fatalError(e, "error create font texture");
        }


        col = charMap.size();
        row = 1;
        buildTexture(is);
    }

    private String getAllAvailableChars(String charsetName) {
        CharsetEncoder ce = Charset.forName(charsetName).newEncoder();
        StringBuilder result = new StringBuilder();
        for (char c = 0; c < Character.MAX_VALUE; c++) {
            if (ce.canEncode(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }


}