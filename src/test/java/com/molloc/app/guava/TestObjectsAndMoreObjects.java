package com.molloc.app.guava;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.molloc.app.BaseTest;
import org.junit.Test;

/**
 * Created by robot on 2015/5/6.
 */
public class TestObjectsAndMoreObjects extends BaseTest {

    @Test
    public void testObjects() {
        log(Objects.equal(null, 123));
        log(Objects.equal(123, 123));
        log(Objects.equal(123, null));
        log(Objects.equal(null, null));

        log(MoreObjects.firstNonNull(null, "you are null"));
        log(MoreObjects.firstNonNull("I am not null", "you are null"));
        log(MoreObjects.toStringHelper(this).add("name", "lcyan").add("age", null).omitNullValues().toString());

    }
}
