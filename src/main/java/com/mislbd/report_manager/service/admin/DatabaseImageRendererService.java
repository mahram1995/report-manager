package com.mislbd.report_manager.service.admin;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import org.springframework.stereotype.Service;

import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;

@Service
public class DatabaseImageRendererService  {

    private final byte[] imageData;

    public DatabaseImageRendererService(byte[] imageData) {
        this.imageData = imageData;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public Image getImage() throws JRException {
        try {
            return ImageIO.read(
                    new ByteArrayInputStream(imageData)
            );
        } catch (Exception e) {
            throw new JRException("Unable to read database image", e);
        }
    }

    public Image getImage(
            int width,
            int height,
            OnErrorTypeEnum onErrorType) throws JRException {
        return getImage();
    }


    public void render(
            Graphics2D graphics,
            Rectangle2D rectangle) throws JRException {

        graphics.drawImage(
                getImage(),
                (int) rectangle.getX(),
                (int) rectangle.getY(),
                (int) rectangle.getWidth(),
                (int) rectangle.getHeight(),
                null
        );
    }




}
