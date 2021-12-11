import javax.swing.*;
import java.awt.*;

/**
 * This class defines the icon on every date-grid.
 */
public class ShapeIcon implements Icon {
    private GridShape shape;
    private int width;
    private int height;

    /**
     * To construct a ShapeIcon.
     * @param shape
     * @param width
     * @param height
     */
    public ShapeIcon(GridShape shape, int width, int height){
        this.shape = shape;
        this.width = width;
        this.height = height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        shape.draw(g2);
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}
