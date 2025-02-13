package Item;

public class Part {
    public TBVector[] vectors = new TBVector[8];
    public TBVector basicPoint;
    public int[] dimensions;
    public static int index;
    public static final String basicStrStart = "Element|0|000000000|BoxAuto %d|0|Shapebox|%.2f|%.2f|%.2f|%d|%d|%d|0|0|0|0|0|0|0|0|";
    public static final String basicStrEnd = "MR_TOP|0|MR_TOP|0|0|0|0|MR_TOP|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|1|0|0|0|0|0|0|0|0|0|0|0";
    public Part(String element) {
        String[] paraStr = element.replaceAll(",", ".").split("\\|");
        basicPoint = new TBVector(Float.parseFloat(paraStr[6]), Float.parseFloat(paraStr[7]), Float.parseFloat(paraStr[8]));
        dimensions = new int[]{Integer.parseInt(paraStr[9]), Integer.parseInt(paraStr[10]), Integer.parseInt(paraStr[11])};
        for (int i = 20; i < 28; i++) {
            vectors[i - 20] = new TBVector(Float.parseFloat(paraStr[i]) + basicPoint.x, Float.parseFloat(paraStr[i + 8]) + basicPoint.y, Float.parseFloat(paraStr[i + 16]) + basicPoint.z);
        }
    }
    public Part(TBPoint[] points, int[] dimensions) {
        basicPoint = new TBVector(0, 0, 0);
        this.dimensions = dimensions;
        for (int i = 0; i < 8; i++) {
            vectors[i] = new TBVector();
            vectors[i].x = ((i + 1) % 4 < 2)?(-(points[i].vector.x)):(points[i].vector.x - dimensions[0]);
            vectors[i].y = (i < 4)?(-(points[i].vector.y)):(points[i].vector.y - dimensions[1]);
            vectors[i].z = (!((i + 2) % 4 < 2))?(-(points[i].vector.z)):(points[i].vector.z - dimensions[2]);
        }
    }
    public String getPart() {
        StringBuilder textB = new StringBuilder().append(String.format(basicStrStart, index++, basicPoint.x, basicPoint.y, basicPoint.z, dimensions[0], dimensions[1], dimensions[2]));
        StringBuilder textY = new StringBuilder();
        StringBuilder textZ = new StringBuilder();
        for (int i = 0; i < 8; i ++) {
            textB.append(String.format("%.5f", vectors[i].x)).append("|");
            textY.append(String.format("%.5f", vectors[i].y)).append("|");
            textZ.append(String.format("%.5f", vectors[i].z)).append("|");
        }
        return textB.append(textY).append(textZ).append(basicStrEnd).toString().replaceAll("\\.", ",");
    }
}
