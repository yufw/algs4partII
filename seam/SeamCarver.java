import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("argument is null");
        }
        this.picture = new Picture(picture);

        int w = picture.width();
        int h = picture.height();
        this.energy = new double[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                energy[i][j] = -1;
            }
        }
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        int w = width();
        int h = height();

        if (!check(x, y, w, h)) {
            throw new IllegalArgumentException("x or y out of range");
        }

        if (x == 0 || x == w-1 || y == 0 || y == h-1) {
            return 1000;
        }

        if (energy[x][y] > -1) {
            return energy[x][y];
        }

        Color left = picture.get(x-1, y);
        Color right = picture.get(x+1, y);
        Color up = picture.get(x, y-1);
        Color down = picture.get(x, y+1);

        double e = 0.0;
        e += gradientSquare(left, right);
        e += gradientSquare(up, down);
        energy[x][y] = Math.sqrt(e);
        return energy[x][y];
    }

    public int[] findHorizontalSeam() {
        int w = width();
        int h = height();
        int[] edgeTo = new int[w * h];
        double[] distTo = new double[w * h];

        for (int i = 0; i < w * h; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }
        for (int i = 0; i < h; i++) {
            distTo[t(0, i, w)] = energy(0, i);
        }
        for (int i = 0; i < w-1; i++) {
            for (int j = 0; j < h; j++) {
                // i+1, j-1
                if (check(i+1, j-1, w, h)) {
                    relax(i, j, i+1, j-1, w, edgeTo, distTo);
                }
                // i+1, j
                if (check(i+1, j, w, h)) {
                    relax(i, j, i+1, j, w, edgeTo, distTo);
                }
                // i+1, j+1
                if (check(i+1, j+1, w, h)) {
                    relax(i, j, i+1, j+1, w, edgeTo, distTo);
                }
            }
        }

        int[] seam = new int[w];
        double minDist = Double.POSITIVE_INFINITY;
        int min = 0;
        for (int i = 0; i < h; i++) {
            if (distTo[t(w-1, i, w)] < minDist) {
                minDist = distTo[t(w-1, i, w)];
                min = i;
            }
        }
        min = t(w-1, min, w);

        for (int i = w-1; i >= 0; i--) {
            seam[i] = (min-i) / w;
            min = edgeTo[min];
        }
        return seam;
    }

    public int[] findVerticalSeam() {
        int w = width();
        int h = height();
        int[] edgeTo = new int[w * h];
        double[] distTo = new double[w * h];

        for (int i = 0; i < w * h; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }
        for (int i = 0; i < w; i++) {
            distTo[t(i, 0, w)] = energy(i, 0);
        }
        for (int j = 0; j < h-1; j++) {
            for (int i = 0; i < w; i++) {
                // i-1, j+1
                if (check(i-1, j+1, w, h)) {
                    relax(i, j, i-1, j+1, w, edgeTo, distTo);
                }
                // i, j+1
                if (check(i, j+1, w, h)) {
                    relax(i, j, i, j+1, w, edgeTo, distTo);
                }
                // i+1, j+1
                if (check(i+1, j+1, w, h)) {
                    relax(i, j, i+1, j+1, w, edgeTo, distTo);
                }
            }
        }

        int[] seam = new int[h];
        double minDist = Double.POSITIVE_INFINITY;
        int min = 0;
        for (int i = 0; i < w; i++) {
            if (distTo[t(i, h-1, w)] < minDist) {
                minDist = distTo[t(i, h-1, w)];
                min = i;
            }
        }
        min = t(min, h-1, w);

        for (int i = h-1; i >= 0; i--) {
            seam[i] = min - i*w;
            min = edgeTo[min];
        }
        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("argument is null");
        }
        if (picture.height() <= 1) {
            throw new IllegalArgumentException("picture too small to remove horizontal seam");
        }
        if (!checkSeam(seam, picture.width(), picture.height())) {
            throw new IllegalArgumentException("invalid seam");
        }
        Picture newPicture = new Picture(picture.width(), picture.height()-1);
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < seam[i]; j++) {
                newPicture.set(i, j, picture.get(i, j));
            }
            for (int j = seam[i] + 1; j < picture.height(); j++) {
                newPicture.set(i, j-1, picture.get(i, j));
            }
        }
        picture = newPicture;
        int w = picture.width();
        int h = picture.height();
        energy = new double[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                energy[i][j] = -1;
            }
        }
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("argument is null");
        }
        if (picture.width() <= 1) {
            throw new IllegalArgumentException("picture too small to remove vertical seam");
        }
        if (!checkSeam(seam, picture.height(), picture.width())) {
            throw new IllegalArgumentException("invalid seam");
        }
        Picture newPicture = new Picture(picture.width()-1, picture.height());
        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < seam[j]; i++) {
                newPicture.set(i, j, picture.get(i, j));
            }
            for (int i = seam[j] + 1; i < picture.width(); i++) {
                newPicture.set(i-1, j, picture.get(i, j));
            }
        }
        picture = newPicture;
        int w = picture.width();
        int h = picture.height();
        energy = new double[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                energy[i][j] = -1;
            }
        }
    }

    private double gradientSquare(Color c1, Color c2) {
        int r1 = c1.getRed();
        int g1 = c1.getGreen();
        int b1 = c1.getBlue();
        int r2 = c2.getRed();
        int g2 = c2.getGreen();
        int b2 = c2.getBlue();

        double result = 0.0;
        result += (r1 - r2) * (r1 - r2);
        result += (g1 - g2) * (g1 - g2);
        result += (b1 - b2) * (b1 - b2);
        return result;
    }

    private int t(int i, int j, int w) {
        return j*w + i;
    }

    private boolean check(int i, int j, int w, int h) {
        if (i < 0 || i >= w || j < 0 || j >= h) {
            return false;
        }
        return true;
    }

    private void relax(int i, int j, int i2, int j2, int w, int[] edgeTo, double[] distTo) {
        if (distTo[t(i2, j2, w)] > distTo[t(i, j, w)] + energy(i2, j2)) {
            distTo[t(i2, j2, w)] = distTo[t(i, j, w)] + energy(i2, j2);
            edgeTo[t(i2, j2, w)] = t(i, j, w);
        }
    }

    private boolean checkSeam(int[] seam, int length, int max) {
        if (seam.length != length) {
            return false;
        }
        for (int i = 0; i < seam.length; i++) {
            if (i > 0 && Math.abs(seam[i] - seam[i-1]) > 1) {
                return false;
            }
            if (seam[i] < 0 || seam[i] >= max) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Picture pic = new Picture("HJocean.png");
        SeamCarver sc = new SeamCarver(pic);
        sc.picture().show();

        int[] vseam = sc.findVerticalSeam();
        sc.removeVerticalSeam(vseam);
        sc.picture().show();

        int[] hseam = sc.findHorizontalSeam();
        sc.removeHorizontalSeam(hseam);
        sc.picture.show();
    }
}
