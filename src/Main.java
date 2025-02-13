import Item.Model;
import Item.OBJFile;
import Item.Part;
import Item.TBPoint;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String defaultFile = "out.txt";
        while (true) {
            defaultFile = run(defaultFile);
        }
    }
    public static String run(String defaultFile) {
        Part.index = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("[0]类圆\n[1]读取.obj\n[2]变换\n[q]退出");
        try {
            switch (scanner.nextLine()) {
                case "0":
                    System.out.println("请输入边数 x内径 x外径 y方向长比x方向长（圆为1） 厚度（默认0） 以及形式（默认为0 即改版） 空格隔开（如8 4.5 8.0 1.0 0.0 0）");
                    String[] paraC = scanner.nextLine().split(" ");
                    TBPoint[] inside = new TBPoint[Integer.parseInt(paraC[0])];
                    TBPoint[] outside = new TBPoint[inside.length];
                    TBPoint[] insideH = new TBPoint[inside.length];
                    TBPoint[] outsideH = new TBPoint[inside.length];
                    float insideL = Float.parseFloat(paraC[1]);
                    float outsideL = Float.parseFloat(paraC[2]);
                    float pro = Float.parseFloat(paraC[3]);
                    double angle = 2 * Math.PI / inside.length;
                    for (int i = 0; i < inside.length; i++) {
                        double tCos = Math.cos(angle * i);
                        double tSin = Math.sin(angle * i);
                        inside[i] = new TBPoint((float) (insideL * tCos), (float) (insideL * tSin * pro), 0);
                        outside[i] = new TBPoint((float) (outsideL * tCos), (float) (outsideL * tSin * pro), 0);
                        insideH[i] = new TBPoint((float) (insideL * tCos), (float) (insideL * tSin * pro), Float.parseFloat(paraC[4]));
                        outsideH[i] = new TBPoint((float) (outsideL * tCos), (float) (outsideL * tSin * pro), Float.parseFloat(paraC[4]));
                    }
                    Model modelC = new Model();
                    for (int i = 0; i < inside.length; i++) {
                        modelC.parts.add(new Part((paraC.length < 6 || paraC[5].equals("0")) ? (new TBPoint[]{outside[i], (i == 0) ? outside[inside.length - 1] : outside[i - 1], (i == 0) ? outsideH[inside.length - 1] : outsideH[i - 1], outsideH[i], inside[i], (i == 0) ? inside[inside.length - 1] : inside[i - 1], (i == 0) ? insideH[inside.length - 1] : insideH[i - 1], insideH[i]}) : (new TBPoint[]{inside[i], (i == 0) ? inside[inside.length - 1] : inside[i - 1], (i == 0) ? outsideH[inside.length - 1] : outsideH[i - 1], outsideH[i], inside[i], (i == 0) ? inside[inside.length - 1] : inside[i - 1], (i == 0) ? outsideH[inside.length - 1] : outsideH[i - 1], outsideH[i]}), new int[]{1, 1, 1}));
                    }
                    return modelC.getModel(defaultFile);
                case "1":
                    System.out.println("请输入文件路径");
                    OBJFile objFile = new OBJFile(scanner.nextLine());
                    return objFile.getPart(defaultFile);
                case "2":
                    Model modelT = new Model(defaultFile);
                    modelT.modelTranslate();
                    return modelT.getModel(defaultFile);
                case "q":
                    System.exit(0);
            }
        }catch (NumberFormatException | NullPointerException | IndexOutOfBoundsException e) {
            System.out.println("输入格式错误，请检查您的输入");
        }catch (IOException e) {
            System.out.println("文件系统错误，请检查文件是否存在或者其他文件方面问题");
        }
        return defaultFile;
    }
}