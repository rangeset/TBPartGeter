package Item;

public class TBPoint {
    public TBVector vector = new TBVector();
    public TBPoint(String[] str) {
        if (str[0].equals("v")) {
            vector.x = Float.parseFloat(str[str.length - 3]);
            vector.y = Float.parseFloat(str[str.length - 2]);
            vector.z = Float.parseFloat(str[str.length - 1]);
        }
    }
    public TBPoint(float x, float y, float z) {
        vector.x = x;
        vector.y = y;
        vector.z = z;
    }
    public TBPoint(TBVector vector, int i, int[] dimensions) {
        this.vector.x = ((i + 1) % 4 < 2)?(-(vector.x)):(vector.x + dimensions[0]);
        this.vector.y = (i < 4)?(-(vector.y)):(vector.y + dimensions[1]);
        this.vector.z = (!((i + 2) % 4 < 2))?(-(vector.z)):(vector.z + dimensions[2]);
    }
}
