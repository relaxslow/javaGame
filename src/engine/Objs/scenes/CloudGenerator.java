package engine.Objs.scenes;

import engine.Interface.IFrameTask;
import engine.Interface.INeedClean;
import engine.Objs.Canvas;
import engine.Util.FrameTimer;
import engine.Util.Pool;
import engine.Util.Res;
import engine.Util.Tools;

public class CloudGenerator extends Res implements INeedClean {
    @Override
    public String getName() {
        return "cloud generator";
    }

    Canvas canvas;
    FrameTimer timer;

    public CloudGenerator(Canvas canvas) {
        this.canvas = canvas;
        timer = new FrameTimer(canvas, "cloud generate timer");

    }

    static Pool<Cloud> pool = new Pool<>("Cloud_Pool");
    static int maxNum = 5;
    static int num = 0;

    Cloud create() {
        Cloud newCloud = pool.create(Cloud.class);
        if (newCloud.getCreateFrom() == Pool.CREATE_FROM_NEW)
            newCloud.initFromNew();
        else if (newCloud.getCreateFrom() == Pool.CREATE_FROM_POOL) {
            newCloud.initFromPool();
        }
        return newCloud;
    }

    float z = 0.2f;
    float maxHei = 4.0f;
    float minHei = 1.5f;

    int minInterval = 8;
    int maxInterval = 15;

    class GenerateCloudTask implements IFrameTask {

        @Override
        public void run() {

            if (num < maxNum) {
                randomGenerateAtRight();
                num++;
            }
            int random = Tools.getRandomIntNumber(maxInterval, minInterval);
            timer.restart(random);

        }
    }

    public void begin() {
        int beginNum = 2;
        for (int i = 0; i < beginNum; i++) {
            randomGenerateOnSky();
        }
        num += beginNum;

        int random = Tools.getRandomIntNumber(maxInterval, minInterval);
        timer.schedule(new GenerateCloudTask(),random);
//        new GenerateCloudTask().run();

    }

    float bornX = 7f;

    void randomGenerateAtRight() {
        float hei = Tools.getRandomNumber(maxHei, minHei);
        Cloud newCloud = create();
        newCloud.setPos(bornX, hei, z);

    }

    float maxX = 4f;
    float minX = -4f;

    void randomGenerateOnSky() {
        float hei = Tools.getRandomNumber(maxHei, minHei);
        float x = Tools.getRandomNumber(maxX, minX);
        Cloud newCloud = create();
        newCloud.setPos(x, hei, z);
    }


    @Override
    public void clean() {
        timer.cancel();//really need this?
    }
}

