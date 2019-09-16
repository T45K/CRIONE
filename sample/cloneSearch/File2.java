package cloneSearch;

class File2{
    // clone
    public void sample2() {
        int g = 1;
        char h = '1';
        double i = 1d;
        float j = 1f;
        long k = 1l;
        String l = "1";
    }

    // not clone
    public void sample2() {
        char h = 1;
        int g = 1;
        double i = 1;
        float j = 1;
        long k = 1;
        String l = "1";
    }
}
