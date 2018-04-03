package com.cmcc.vrp.boss.heilongjiang.fee;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestCheckParamNotNull {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Ignore
    @Test
    public void testSingle() {
        exception.expect(RuntimeException.class);
        CheckParamNotNull.check((Object[]) null);
    }

    @Test
    public void testArray() {
        exception.expect(RuntimeException.class);
        Object[] param = new Object[2];
        param[0] = (Object) null;
        param[1] = (Object) null;
        CheckParamNotNull.check(param);
    }
}
