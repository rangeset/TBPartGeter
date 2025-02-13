package Item;

import java.io.*;
import java.util.LinkedList;
//import java.util.Objects;
import java.util.Scanner;

public class Model {
    public LinkedList<Part> parts = new LinkedList<>();
    float[] position = new float[3];
    public Model() {}
    public Model(String defaultFile) throws IOException {
        Scanner scanner = new Scanner(System.in);
        //System.out.println("请以x y z的形式输入结果Position值（旋转中心）默认为0 0 0");
        //String positionStr = scanner.nextLine();
        //if (!Objects.equals(positionStr, "")) {
        //    int i = 0;
        //    for (String element : positionStr.split(" ")) {
        //        position[i++] = Float.parseFloat(element);
        //    }
        //} else {
        //    position = new float[]{0, 0, 0};
        //}
        System.out.println("请将从TB中Copy出的内容粘贴至此，键入Q结束添加 或输入f并输入文件路径（如f C:\\User\\Download\\out.txt 回车默认读取本次运行中上次生成的文件（若没有则out.txt）");
        String line = scanner.nextLine();
        if (line.charAt(0) == 'f') {
            File file = new File((line.split(" ").length > 1)?line.split(" ")[1]:defaultFile);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String fileR;
            while ((fileR = br.readLine()) != null) {
                parts.add(new Part(fileR));
            }
        }else {
            parts.add(new Part(line));
            while (true) {
                line = scanner.nextLine();
                if (line.equals("Q")) {
                    break;
                }
                parts.add(new Part(line));
            }
        }
    }
    public void modelTranslate() {
        Scanner scanner = new Scanner(System.in);
        LinkedList<TBPoint[]> pointLs = new LinkedList<>();
        for (Part part: parts) {
            TBPoint[] points = new TBPoint[8];
            int i = 0;
            for (TBVector vector:part.vectors) {
                points[i] = new TBPoint(vector, i++, part.dimensions);
            }
            pointLs.add(points);
        }
        System.out.println("请键入变换类型\n[0]位移\n[1]放缩\n[2]旋转\n[3]修改Dimensions");
        int type = Integer.parseInt(scanner.nextLine());
        switch (type) {
            case 0:
                System.out.println("请输入位移距离（格式:x y z）");
                String[] distanceStrM = scanner.nextLine().split(" ");
                float[] distanceM = new float[3];
                distanceM[0] = Float.parseFloat(distanceStrM[0]);
                distanceM[1] = Float.parseFloat(distanceStrM[1]);
                distanceM[2] = Float.parseFloat(distanceStrM[2]);
                for (Part part:parts) {
                    int i = 0;
                    for (TBVector vector : part.vectors) {
                        vector.x += (distanceM[0] * (((i + 1) % 4 < 2)?(-1):1));
                        vector.y += (distanceM[1] * ((i < 4)?(-1):1));
                        vector.z += (distanceM[2] * (((i++ + 2) % 4 < 2)?1:(-1)));
                    }
                }
                return;
            case 1:
                parts.clear();
                System.out.println("请输入放缩倍数（格式:x y z）");
                String[] distanceStrS = scanner.nextLine().split(" ");
                float[] distanceS = new float[3];
                distanceS[0] = Float.parseFloat(distanceStrS[0]);
                distanceS[1] = Float.parseFloat(distanceStrS[1]);
                distanceS[2] = Float.parseFloat(distanceStrS[2]);
                for (TBPoint[] points:pointLs) {
                    for (TBPoint point : points) {
                        point.vector.x *= distanceS[0];
                        point.vector.y *= distanceS[1];
                        point.vector.z *= distanceS[2];
                    }
                    parts.add(new Part(points, new int[]{1, 1, 1}));
                }
                return;
            case 2:
                parts.clear();
                System.out.println("请输入旋转轴和角度（单位°）空格隔开（如:x 30)");
                String rotating = scanner.next();
                double angle = (Float.parseFloat(scanner.next()) * Math.PI / 180);
                for (TBPoint[] points:pointLs) {
                    for (TBPoint point : points) {
                        switch (rotating) {
                            case "x":
                                double patternX = Math.sqrt(Math.pow(point.vector.y, 2) + Math.pow(point.vector.z, 2));
                                if (patternX != 0) {
                                    double thetaX = (point.vector.z > 0 ? Math.acos(point.vector.y / patternX) : 2 * Math.PI - Math.acos(point.vector.y / patternX)) + angle;
                                    point.vector.y = (float) (patternX * Math.cos(thetaX));
                                    point.vector.z = (float) (patternX * Math.sin(thetaX));
                                }
                                continue;
                            case "y":
                                double patternY = Math.sqrt(Math.pow(point.vector.z, 2) + Math.pow(point.vector.x, 2));
                                if (patternY != 0) {
                                    double thetaY = (point.vector.x > 0 ? Math.acos(point.vector.z / patternY) : 2 * Math.PI - Math.acos(point.vector.z / patternY)) + angle;
                                    point.vector.z = (float) (patternY * Math.cos(thetaY));
                                    point.vector.x = (float) (patternY * Math.sin(thetaY));
                                }
                                continue;
                            case "z":
                                double patternZ = Math.sqrt(Math.pow(point.vector.x, 2) + Math.pow(point.vector.y, 2));
                                if (patternZ != 0) {
                                    double thetaZ = (point.vector.y > 0 ? Math.acos(point.vector.x / patternZ) : 2 * Math.PI - Math.acos(point.vector.x / patternZ)) + angle;
                                    point.vector.x = (float) (patternZ * Math.cos(thetaZ));
                                    point.vector.y = (float) (patternZ * Math.sin(thetaZ));
                                }
                        }
                    }
                    parts.add(new Part(points, new int[]{1, 1, 1}));
                }
                return;
            case 3:
                parts.clear();
                System.out.println("请输入理想Dimensions值 格式为（x y z）");
                String[] dimensionsStr = scanner.nextLine().split(" ");
                int[] dimensions = new int[3];
                int i = 0;
                for (String dimensionStr: dimensionsStr) {
                    dimensions[i++] = Integer.parseInt(dimensionStr);
                }
                for (TBPoint[] points:pointLs) {
                    parts.add(new Part(points, dimensions));
                }
        }
    }
    public String getModel(String defaultFile) throws IOException {
        if (parts.size() < 100) {
            for (Part part : parts) {
                System.out.println(part.getPart());
            }
            return defaultFile;
        } else {
            System.out.println("期望输出文件名称（后缀.txt）（回车默认out.txt）");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.nextLine();
            File outFile = new File(path.isEmpty()?"out.txt":path);
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
            for (Part part: parts) {
                bw.write(part.getPart() + " ");
                bw.newLine();
            }
            bw.close();
            System.out.println("生成了" + parts.size() + "行并存入了" + (path.isEmpty()?"out.txt":path));
            return path.isEmpty()?"out.txt":path;
        }
    }
}
