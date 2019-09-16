package cloneSearch;

class File1 {
    // clone
    public void sample1() {
        int a = 0;
        char b = '0';
        double c = 0d;
        float d = 0f;
        long e = 0l;
        String f = "0";
    }

    // not clone
    public void sample2() {
        int g = 1;
        char h = 1;
        double i = 1;
        float j = 1;
        long k = 1;
        String l = "1";
    }
}
