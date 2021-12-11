import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Only use to show the head of the day-grids. That is "Su, Mo, Tu ,..., Sa".
 */
public class Weekdays_GridShape extends GridShape{
        private int x, y, width;
        private String value;
        private Color c;
        private boolean b;

        public Weekdays_GridShape(int x, int y, int width, Color c){
            super(x, y, width, c);
            this.x = x;
            this.y = y;
            this.width = width;
            this.c = c;
            b=false;
        }

        public void draw(Graphics2D g2){
            Rectangle2D rectangle = new Rectangle2D.Double(x, y, width, width);
            g2.draw (rectangle);
            double Cx=rectangle.getCenterX();
            double Cy=rectangle.getCenterY();
            g2.setColor(c);
            String strValue = value+"";
            Font f =new Font("Serif", Font.BOLD, 25);
            g2.setFont(f);
            FontRenderContext context = g2.getFontRenderContext();
            Rectangle2D bounds = f.getStringBounds(strValue, context);
            double strX = rectangle.getCenterX() - bounds.getWidth()/2;
            double strY = rectangle.getCenterY() - bounds.getCenterY()/2;
            //g2.setColor(Color.blue);
            g2.drawString(strValue, (int)strX, (int)strY+8);
        }

        public void setString(String newValue){
            value = newValue;
        }

        public String getString(){
            return value;
        }

        public void setColor(Color color){
            c=color;
        }

        public void isCircled(boolean b){
            this.b=b;
        }
    }
