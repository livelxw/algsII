/*
* Bug:
* In dickens.txt
* */

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] chs = new char[256];
        for (int i = 0; i < chs.length; i++) {
            chs[i] = (char) i;
        }

        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        for (char i : input) {
            char tmp = chs[0];

            for (int j = 0; j < chs.length; j++) {
                if (chs[j] == i) {
                    BinaryStdOut.write(j, 8);
                    chs[0] = i;
                    chs[j] = tmp;
                    break;
                } else {
                    char curTmp = chs[j];
                    chs[j] = tmp;
                    tmp = curTmp;
                }
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] chs = new char[256];
        for (int i = 0; i < chs.length; i++) {
            chs[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readByte();
            BinaryStdOut.write(chs[index], 8);

            char tmp = chs[index];
            System.arraycopy(chs, 0, chs, 1, index);
            chs[0] = tmp;
        }

        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
    }
}
