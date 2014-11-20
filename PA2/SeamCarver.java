import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {

    private int[][] colors;
    private int width;
    private int height;

    private int[][] energy;


    private double deltaX(int x, int y) {
        Color color1 = new Color(colors[x + 1][y]);
        Color color2 = new Color(colors[x - 1][y]);

        return ((color1.getBlue() - color2.getBlue()) * (color1.getBlue() - color2.getBlue())
                + (color1.getGreen() - color2.getGreen()) * (color1.getGreen() - color2.getGreen())
                + (color1.getRed() - color2.getRed()) * (color1.getRed() - color2.getRed())
        );

    }

    private double deltaY(int x, int y) {
        Color color1 = new Color(colors[x][y + 1]);
        Color color2 = new Color(colors[x][y - 1]);

        return ((color1.getBlue() - color2.getBlue()) * (color1.getBlue() - color2.getBlue())
                + (color1.getGreen() - color2.getGreen()) * (color1.getGreen() - color2.getGreen())
                + (color1.getRed() - color2.getRed()) * (color1.getRed() - color2.getRed())
        );
    }

    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        width = picture.width();
        height = picture.height();
        energy = new int[width][height];
        colors = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                colors[i][j] = picture.get(i, j).getRGB();
            }
        }

        for (int i = 0; i < energy.length; i++) {
            for (int j = 0; j < energy[i].length; j++) {
                energy[i][j] = (int) energy(i, j);
            }
        }
    }

    public Picture picture()                          // current picture
    {

        Picture curImage = new Picture(width, height);
        for (int i = 0; i < curImage.width(); i++) {
            for (int j = 0; j < curImage.height(); j++) {
                curImage.set(i, j, new Color(colors[i][j]));
            }
        }
        return curImage;
    }

    public int width()                            // width of current picture
    {
        return width;
    }

    public int height()                           // height of current picture
    {
        return height;
    }

    public double energy(int x, int y) throws IndexOutOfBoundsException             // energy of pixel at column x and row y
    {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IndexOutOfBoundsException("energy" + x + ", " + y);

        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1)
            return 195075.0;  //255^2 + 255^2 + 255^2

        return deltaX(x, y) + deltaY(x, y);
    }

    public int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        int[] distTo = new int[height];
        int[] edgeTo = new int[width];
        int[] curDistTo = new int[distTo.length];
        int[] preDistTo = new int[distTo.length];
        int[][] curEdgeTo = new int[width - 1][height];
        Arrays.fill(distTo, Integer.MAX_VALUE);


        for (int i = 1; i < width - 1; i++) {
            int x;
            x = i;
            int y = 1;

            Arrays.fill(curDistTo, Integer.MAX_VALUE);

            while (y < height - 1) {
                for (int j = -1; j < 2; j++) {
                    if (curDistTo[y + j] > preDistTo[y] + energy[i][y + j]) {
                        curDistTo[y + j] = preDistTo[y] + energy[i][y + j];
                        curEdgeTo[x][y + j] = y;
                    }
                }
                y = y + 1;
            }

            int[] tmp = preDistTo;
            preDistTo = curDistTo;
            curDistTo = tmp;
        }

        int minIndex = Integer.MAX_VALUE;
        int minDist = Integer.MAX_VALUE;

        for (int i = 0; i < preDistTo.length; i++) {
            if (minDist > preDistTo[i]) {
                minDist = preDistTo[i];
                minIndex = i;
            }
        }
        if (height > 2) {
            for (int i = edgeTo.length - 2; i > 0; i--) {
                edgeTo[i] = minIndex;
                minIndex = curEdgeTo[i][minIndex];
            }
            edgeTo[width - 1] = edgeTo[width - 2] - 1;
            edgeTo[0] = edgeTo[1] - 1;
        } else {
            edgeTo[width - 1] = 0;
            edgeTo[0] = 0;
        }

        return edgeTo;
    }

    public int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        int[] distTo = new int[width];
        int[] edgeTo = new int[height];
        int[] curDistTo = new int[distTo.length];
        int[] preDistTo = new int[distTo.length];
        int[][] curEdgeTo = new int[width][height - 1];
        Arrays.fill(distTo, Integer.MAX_VALUE);


        for (int i = 1; i < height - 1; i++) {
            int y;
            y = i;
            int x = 1;

            Arrays.fill(curDistTo, Integer.MAX_VALUE);

            while (x < width - 1) {
                for (int j = -1; j < 2; j++) {
                    if (curDistTo[x + j] > preDistTo[x] + energy[x + j][y]) {
                        curDistTo[x + j] = preDistTo[x] + energy[x + j][y];
                        curEdgeTo[x + j][y] = x; //?????????????????
                    }
                }
                x = x + 1;
            }

            int[] tmp = preDistTo;
            preDistTo = curDistTo;
            curDistTo = tmp;

        }

        int minIndex = Integer.MAX_VALUE;
        int minDist = Integer.MAX_VALUE;

        for (int i = 0; i < preDistTo.length; i++) {
            if (minDist > preDistTo[i]) {
                minDist = preDistTo[i];
                minIndex = i;
            }
        }

        if (width > 2) {
            for (int i = edgeTo.length - 2; i > 0; i--) {
                edgeTo[i] = minIndex;
                minIndex = curEdgeTo[minIndex][i];
            }
            edgeTo[height - 1] = edgeTo[height - 2] - 1;
            edgeTo[0] = edgeTo[1] - 1;
        } else {
            edgeTo[0] = 0;
            edgeTo[height - 1] = 0;
        }

        return edgeTo;
    }

    public void removeHorizontalSeam(int[] seam) throws NullPointerException, IllegalArgumentException // remove horizontal seam from current picture
    {
        if (seam == null)
            throw new NullPointerException("remove horizontal seam");

        for (int i = 0; i < width; i++) {

            if (seam[i] > height || seam[i] < 0 || (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) || seam.length != width)
                throw new IllegalArgumentException("Horizontal < >");

            System.arraycopy(colors[i], seam[i] + 1, colors[i], seam[i], height - 1 - seam[i]);
            System.arraycopy(energy[i], seam[i] + 1, energy[i], seam[i], height - 1 - seam[i]);
        }

        for (int i = 0; i < width; i++) {
            int y = seam[i] - 1 < 0 ? 0 : seam[i] - 1;
            energy[i][y] = (int) energy(i, y);
            energy[i][seam[i]] = (int) energy(i, seam[i]);
        }

        height = height - 1;
    }

    public void removeVerticalSeam(int[] seam) throws NullPointerException   // remove vertical seam from current picture
    {
        if (seam == null)
            throw new NullPointerException("remove horizontal seam");

        for (int i = 0; i < height; i++) {

            if (seam[i] > width || seam[i] < 0 || (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) || seam.length != height)
                throw new IllegalArgumentException("Horizontal < >");

            for (int j = seam[i] + 1; j < width; j++) {
                colors[j - 1][i] = colors[j][i];
                energy[j - 1][i] = energy[j][i];
            }
        }
        for (int i = 0; i < height; i++) {
            int x = seam[i] - 1 < 0 ? 0 : seam[i] - 1;
            energy[x][i] = (int) energy(x, i);
            energy[seam[i]][i] = (int) energy(seam[i], i);
        }
        width = width - 1;
    }
}
