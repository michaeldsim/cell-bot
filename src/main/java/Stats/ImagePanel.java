package Stats;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	
	Font font;
    private BufferedImage image;

    public ImagePanel(String url) {
       try {                
          image = ImageIO.read(new File(url));
       } catch (IOException ex) {
            
       }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0,this.getWidth(),this.getHeight(), this); 
        
    }
    
   
    public BufferedImage createImage() {
        int w = this.getWidth();
        int h = this.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        this.paint(g);
        g.dispose();
        return bi;
    }
    
	public  Font font(){

		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("libs/Burbank.ttf"));
			font = font.deriveFont(100f);
			return font;

		
		} catch (FontFormatException | IOException e) {
			System.out.println(e.getMessage());
		}
		return null;   
	}
	



}