package com.cmcc.vrp.util;

/**
 * 产品单位的转换
 * <p>
 * Created by sunyiwei on 2016/6/15.
 */
public enum SizeUnits {
    TB {
        public long toKB(long d) {
            return d * C1 * C1 * C1;
        }

        public long toKB(Double d) {
            return Math.round(d * C1 * C1 * C1);
        }

        public long toMB(long d) {
            return d * C1 * C1;
        }

        public long toGB(long d) {
            return d * C1;
        }
    },

    GB {
        public long toKB(long d) {
            return d * C1 * C1;
        }

        public long toKB(Double d) {
            return Math.round(d * C1 * C1);
        }

        public long toMB(long d) {
            return d * C1;
        }

        public long toGB(long d) {
            return d;
        }
    },

    MB {
        public long toKB(long d) {
            return d * C1;
        }

        public long toKB(Double d) {
            return Math.round(d * C1);
        }

        public long toMB(long d) {
            return d;
        }

        public long toGB(long d) {
            return d / C1;
        }
    },

    KB {
        public long toKB(long d) {
            return d;
        }

        public long toKB(Double d) {
            return Math.round(d);
        }

        public long toMB(long d) {
            return d / C1;
        }

        public long toGB(long d) {
            return d / C1 / C1;
        }
    };

    private static final int C1 = 1024;

    public long toKB(long d) {
        throw new AbstractMethodError();
    }

    public long toKB(Double d) {
        throw new AbstractMethodError();
    }

    public long toMB(long d) {
        throw new AbstractMethodError();
    }

    public long toGB(long d) {
        throw new AbstractMethodError();
    }

}
