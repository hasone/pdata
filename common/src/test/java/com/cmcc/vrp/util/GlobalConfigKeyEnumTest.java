package com.cmcc.vrp.util;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Formatter;

/**
 * Created by sunyiwei on 2016/7/26.
 */
public class GlobalConfigKeyEnumTest {

    @Ignore
    @Test
    public void testGetKey() throws Exception {
        final String filename = "C:\\Users\\Lenovo\\Desktop\\global_config.sql";

        StringBuilder stringBuilder = new StringBuilder();
        for (GlobalConfigKeyEnum globalConfigKeyEnum : GlobalConfigKeyEnum.values()) {
            Formatter formatter = new Formatter();
            formatter.format("INSERT INTO `global_config` (`name`, `description`, `config_key`, `config_value`, `create_time`, `update_time`, `creator_id`, `updater_id`, `delete_flag`, `config_update`) VALUES ('%s', '%s', '%s', '%s', NOW(), NOW(), 1, 1, 0, 0);",
                globalConfigKeyEnum.getDescription(),
                globalConfigKeyEnum.getDescription(),
                globalConfigKeyEnum.getKey(),
                "");
            stringBuilder.append(formatter.toString());
            stringBuilder.append(System.lineSeparator());
        }

        FileUtils.writeStringToFile(new File(filename), stringBuilder.toString(), Charsets.UTF_8);
    }
}