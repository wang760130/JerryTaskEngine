package com.jerry.taskengine.log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;

/**
 * @author Jerry Wang 
 * @Email  jerry002@126.com
 * @Web	   http://jerry.wang
 * @date   2015年11月3日
 */
public class SystemPrintStream extends PrintStream {
	private ILog log = LogFactory.getLogger(SystemPrintStream.class);

    private static PrintStream outInstance = new SystemPrintStream(System.out);
    private static PrintStream errInstance = new SystemPrintStream(System.err);

    private SystemPrintStream(OutputStream out) {
        super(out);
    }

    public static void redirectToLog4j() {
        System.setOut(outInstance);
        System.setErr(errInstance);
    }

    public PrintStream append(char c) {
        // Ignore
        return this;
    }

    public PrintStream append(CharSequence csq, int start, int end) {
        // Ignore
        return this;
    }

    public PrintStream append(CharSequence csq) {
        // Ignore
        return this;
    }

    public boolean checkError() {
        // Ignore
        return false;
    }

    protected void clearError() {
        // Ignore
    }

    public void close() {
        // Ignore
    }

    public void flush() {
        // Ignore
    }

    public PrintStream format(Locale l, String format, Object... args) {
        // Ignore
        return this;
    }

    public PrintStream format(String format, Object... args) {
        // Ignore
        return this;
    }

    public void print(boolean b) {
        println(b);
    }

    public void print(char c) {
        println(c);
    }

    public void print(char[] s) {
        println(s);
    }

    public void print(double d) {
        println(d);
    }

    public void print(float f) {
        println(f);
    }

    public void print(int i) {
        println(i);
    }

    public void print(long l) {
        println(l);
    }

    public void print(Object obj) {
        println(obj);
    }

    public void print(String s) {
        println(s);
    }

    public PrintStream printf(Locale l, String format, Object... args) {
        // Ignore
        return this;
    }

    public PrintStream printf(String format, Object... args) {
        // Ignore
        return this;
    }

    public void println() {
        // Ignore
    }

    public void println(boolean x) {
        if (this == errInstance) {
            log.error(String.valueOf(x));
        } else {
            log.info(String.valueOf(x));
        }
    }

    public void println(char x) {
        if (this == errInstance) {
            log.error(String.valueOf(x));
        } else {
            log.info(String.valueOf(x));
        }
    }

    public void println(char[] x) {
        if (this == errInstance) {
            log.error(x == null ? null : new String(x));
        } else {
            log.info(x == null ? null : new String(x));
        }
    }

    public void println(double x) {
        if (this == errInstance) {
            log.error(String.valueOf(x));
        } else {
            log.info(String.valueOf(x));
        }
    }

    public void println(float x) {
        if (this == errInstance) {
            log.error(String.valueOf(x));
        } else {
            log.info(String.valueOf(x));
        }
    }

    public void println(int x) {
        if (this == errInstance) {
            log.error(String.valueOf(x));
        } else {
            log.info(String.valueOf(x));
        }
    }

    public void println(long x) {
        if (this == errInstance) {
            log.error(String.valueOf(x));
        } else {
            log.info(String.valueOf(x));
        }
    }

    public void println(Object x) {
        if (this == errInstance) {
            log.error(String.valueOf(x));
        } else {
            log.info(String.valueOf(x));
        }
    }

    public void println(String x) {
        if (this == errInstance) {
            log.error(x);
        } else {
            log.info(x);
        }
    }

    protected void setError() {
        // Ignore
    }

    public void write(byte[] buf, int off, int len) {
        // Ignore
    }

    public void write(int b) {
        // Ignore
    }

    public void write(byte[] b) throws IOException {
        // Ignore
    }
}
