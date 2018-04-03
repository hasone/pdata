package com.cmcc.vrp.queue.consumer.provinces;

import com.cmcc.vrp.queue.task.Worker;
import com.cmcc.vrp.queue.task.provinces.PackageWorker;
import com.cmcc.vrp.queue.utils.PorpertiesConfigurer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by leelyn on 2016/11/18.
 */
@RunWith(PowerMockRunner.class)
@ContextConfiguration("classpath:conf/applicationContext.xml")
public class ProvinceConsumerTest {

    @Mock
    PorpertiesConfigurer propertyConfigurer;


    @Test
    public void test() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Info> infos = buildClazzs();
        for (Info info : infos) {
            testConcreteConsumer(info);
        }
    }

    private void testConcreteConsumer(Info info) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<? extends ProvinceConsumer> consumerClass = info.getConsumerClass();
        Class<? extends Worker> workerClass = info.getWorkerClass();
        int workerCount = info.getWorkerCount();

        ProvinceConsumer consumer = consumerClass.getConstructor().newInstance();
        ReflectionTestUtils.setField(consumer, "propertyConfigurer", propertyConfigurer);
        assertNotNull(consumer.getConsumeQueueName());

        assertTrue(consumer.getWorkerCount() == workerCount);

        assertNotNull(consumer.getPackageKey());

        assertEquals(PackageWorker.class, workerClass);
    }

    private List<Info> buildClazzs() throws IOException {
        List<Info> infos = new LinkedList<Info>();
        infos.add(new Info(AhConsumer.class, PackageWorker.class));
        infos.add(new Info(BjConsumer.class, PackageWorker.class));
        infos.add(new Info(CqConsumer.class, PackageWorker.class));
        infos.add(new Info(FjConsumer.class, PackageWorker.class));
        infos.add(new Info(GdConsumer.class, PackageWorker.class));
        infos.add(new Info(GsConsumer.class, PackageWorker.class));
        infos.add(new Info(GxConsumer.class, PackageWorker.class));
        infos.add(new Info(GzConsumer.class, PackageWorker.class));
        infos.add(new Info(HaConsumer.class, PackageWorker.class));
        infos.add(new Info(HbConsumer.class, PackageWorker.class));
        infos.add(new Info(HeConsumer.class, PackageWorker.class));
        infos.add(new Info(HiConsumer.class, PackageWorker.class));
        infos.add(new Info(HlConsumer.class, PackageWorker.class));
        infos.add(new Info(HnConsumer.class, PackageWorker.class));
        infos.add(new Info(JlConsumer.class, PackageWorker.class));
        infos.add(new Info(JsConsumer.class, PackageWorker.class));
        infos.add(new Info(JxConsumer.class, PackageWorker.class));
        infos.add(new Info(LnConsumer.class, PackageWorker.class));
        infos.add(new Info(NmConsumer.class, PackageWorker.class));
        infos.add(new Info(NxConsumer.class, PackageWorker.class));
        infos.add(new Info(QhConsumer.class, PackageWorker.class));
        infos.add(new Info(ScConsumer.class, PackageWorker.class));
        infos.add(new Info(SdConsumer.class, PackageWorker.class));
        infos.add(new Info(ShConsumer.class, PackageWorker.class));
        infos.add(new Info(SnConsumer.class, PackageWorker.class));
        infos.add(new Info(SxConsumer.class, PackageWorker.class));
        infos.add(new Info(TjConsumer.class, PackageWorker.class));
        infos.add(new Info(XjConsumer.class, PackageWorker.class));
        infos.add(new Info(XzConsumer.class, PackageWorker.class));
        infos.add(new Info(YnConsumer.class, PackageWorker.class));
        infos.add(new Info(ZjConsumer.class, PackageWorker.class));
        return infos;
    }


    private class Info {
        private Class<? extends ProvinceConsumer> consumerClass;
        private Class<? extends Worker> workerClass;
        private int workerCount;
        private String queueName;

        public Info(Class<? extends ProvinceConsumer> consumerClass, Class<? extends Worker> workerClass, int workerCount, String queueName) {
            this.consumerClass = consumerClass;
            this.workerClass = workerClass;
            this.workerCount = workerCount;
            this.queueName = queueName;
        }

        public Info(Class<? extends ProvinceConsumer> consumerClass, Class<? extends Worker> workerClass) {
            this(consumerClass, workerClass, 1, null);
        }

        public Class<? extends ProvinceConsumer> getConsumerClass() {
            return consumerClass;
        }

        public void setConsumerClass(Class<? extends ProvinceConsumer> consumerClass) {
            this.consumerClass = consumerClass;
        }

        public Class<? extends Worker> getWorkerClass() {
            return workerClass;
        }

        public void setWorkerClass(Class<? extends Worker> workerClass) {
            this.workerClass = workerClass;
        }

        public int getWorkerCount() {
            return workerCount;
        }

        public void setWorkerCount(int workerCount) {
            this.workerCount = workerCount;
        }

        public String getQueueName() {
            return queueName;
        }

        public void setQueueName(String queueName) {
            this.queueName = queueName;
        }
    }
}
