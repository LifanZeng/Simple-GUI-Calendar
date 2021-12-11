import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * To show the shape in one grid, it should show the number of the day. If the day is Sunday or Saturday, it should be red.
 */
public class GridShape {
    private int x, y, width;
    private int value;
    private Color c;
    private boolean isCircle;

    /**
     * To construct a GridShape.
     * @param x - position x.
     * @param y - position y.
     * @param width
     * @param c - the color of the shape (number).
     */
    public GridShape(int x, int y, int width, Color c){
        this.x = x;
        this.y = y;
        this.width = width;
        this.c = c;
        isCircle=false;
    }

    /**
     * To draw the shape.
     * @param g2
     */
    public void draw(Graphics2D g2){
        Rectangle2D rectangle = new Rectangle2D.Double(x, y, width, width);
        g2.draw (rectangle);
        if(getNumber()!=0){
            double Cx=rectangle.getCenterX();
            double Cy=rectangle.getCenterY();
            if (isCircle==true){
                Ellipse2D.Double ecllipse = new Ellipse2D.Double();
                ecllipse.setFrameFromCenter(Cx, Cy,
                        0.8* rectangle.getWidth(), 0.95*rectangle.getHeight());
                g2.setColor(Color.ORANGE);
                g2.setStroke(new BasicStroke(2));
                g2.draw(ecllipse);
            }
            g2.setColor(c);
            String strValue = value+"";
            Font f =new Font("Serif", Font.BOLD, 25);
            g2.setFont(f);
            FontRenderContext context = g2.getFontRenderContext();
            Rectangle2D bounds = f.getStringBounds(strValue, context);
            double strX = rectangle.getCenterX() - bounds.getWidth()/2;
            double strY = rectangle.getCenterY() - bounds.getCenterY()/2;
            g2.drawString(strValue, (int)strX, (int)strY+8);
        }
    }

    /**
     * To set the number which should be shown on the grid.
     * @param newValue - the new number which will be shown on the grid.
     */
    public void setNumber(int newValue){
        value = newValue;
    }

    /**
     * To read the number of the grid.
     * @return
     */
    public int getNumber(){
        return value;
    }

    /**
     * To set the number color of the grid.
     * @param color
     */
    public void setColor(Color color){
        c=color;
    }

    /**
     * to draw a circle on the grid.
     * @param b - boolean parameter, when b is true, a circle will be drawn on the grid.
     */
    public void isCircled(boolean b){
        isCircle=b;
    }

}
