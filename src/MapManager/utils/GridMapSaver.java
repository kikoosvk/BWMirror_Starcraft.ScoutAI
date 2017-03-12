package MapManager.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import MapManager.Block;
import MapManager.GridBasedMap;
import javafx.scene.paint.Color;

import static MapManager.utils.Utils.getColorForValue;
import static MapManager.utils.Utils.getIntFromColor;

/**
 * Created by Chudjak Kristián on 12.03.2017.
 */
public class GridMapSaver {


    public static void saveGridMap(String name, GridBasedMap gridMap){
        Block[][] blockMap = gridMap.getBlockMap();

        int width = gridMap.getRows();
        int height= gridMap.getColumns();

        BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        writeBlocks(img,blockMap);

        File f = new File("generatedMaps"+File.separator+name+".png");
        f.getParentFile().mkdirs();
        try {
            ImageIO.write(img,"png",f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeBlocks(BufferedImage img, Block[][] blocks){
        double max = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                if(blocks[i][j].getValue()> max)
                    max = blocks[i][j].getValue();
            }
        }
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                writePixel(img,blocks[i][j],max);
            }

        }

        System.out.println("max is : "+max);
    }

    private static void writePixel(BufferedImage img, Block block, double max) {
        Color color = getColorForValue(block.getValue(),0,max);
        int rgb = getIntFromColor(color.getRed(),color.getGreen(),color.getBlue());
        img.setRGB(block.getColumn(),block.getRow(),rgb);
    }


}
