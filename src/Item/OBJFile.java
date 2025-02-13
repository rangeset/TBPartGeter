package Item;

import java.io.*;
import java.util.LinkedList;

public class OBJFile {
    public LinkedList<Part> partList = new LinkedList<>();
    public LinkedList<TBPoint> points = new LinkedList<>();
    public OBJFile(String path) throws IOException {
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            String[] str = line.split(" ");
            if (str[0].equals("v")) {
                points.add(new TBPoint(str));
            }else if (str[0].equals("f")) {
                int[] face = new int[str.length - 1];
                for (int i = 1; i < str.length; i++) {
                    face[i - 1] = Integer.parseInt(str[i].split("/")[0]);
                }
                TBPoint[] partPoint = new TBPoint[8];
                for (int i = 0; i < 8; i++) {
                    if (i < face.length) {
                        partPoint[i] = points.get(face[i] - 1);
                    }else {
                        partPoint[i] = points.get(face[0] - 1);
                    }
                }
                partList.add(new Part(partPoint, new int[]{1, 1, 1}));
            }
        }
    }
    public void getPart() throws IOException {
        Model model = new Model();
        model.parts = partList;
        model.getModel();
    }
}
