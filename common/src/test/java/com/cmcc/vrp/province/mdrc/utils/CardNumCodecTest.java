package com.cmcc.vrp.province.mdrc.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by sunyiwei on 2016/5/31.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class CardNumCodecTest {

    @Test
    public void testDecode() throws Exception {
        assertNull(CardNumCodec.decode(null));

        final String cardNumStr = "100001623000100001000000001,100001623000100001000001100-100001623000100001000001200,100001623000100001000001300,100001623000100001000001400,100001623000100001000001500-100001623000100001000001700,100001623000100001000001800";

        List<String> cardNums = CardNumCodec.decode(cardNumStr);
        assertTrue(cardNums.size() == 306);
    }

    @Test
    public void testEncode() throws Exception {
        assertNull(CardNumCodec.encode(null));
        assertNull(CardNumCodec.encode(new ArrayList<String>()));

        List<String> cardNums = build();
        String cardNumStr = CardNumCodec.encode(cardNums);
        for (String cardNum : cardNums) {
            assertTrue(cardNumStr.contains(cardNum));
        }
    }

    private List<String> build() {
        List<String> cardNums = new LinkedList<String>();

        final int COUNT = 10;
        for (int i = 0; i < COUNT; i++) {
            cardNums.add(String.valueOf(1000 + i));
        }

        return cardNums;
    }
}