package main;

import java.io.Console;
import java.util.List;
import config.ConfigLoad;
import model.ConfigRss;
import java.io.File;
import java.util.Arrays;
import java.net.URL;

public class RssReader {
    enum CommandMode {
        A_MODE, B_MODE
    }

    enum CommandOpt {
        I("-i"),
        C("-c"),
        O("-o");

        private final String text;

        CommandOpt(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    enum COptions {
        CUT("cut"),
        CONVERT("convert");

        private final String text;

        COptions(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public static void main(String[] args) {

        List<String> cliOptionList = Arrays.asList(args);
        String validMsg = CommandValidation(cliOptionList);
        if (validMsg != null) {
            System.out.println(validMsg);
            System.out.println("実行例");
            System.out.println("(1) http://tech.uzabase.com/ を読み込み A の変換だけ行い result.txt に出力する。");
            System.out.println("\t java RssReader -i http://tech.uzabase.com/ -c cut -o result.txt");
            System.out.println("(2) articles.txt を読み込んで、A と B の変換を行い標準出力に出力する。");
            System.out.println("\t java RssReader -i articles.txt -c cut,convert");
        }

        CommandMode mode = CommandMode.B_MODE;
        if (0 < cliOptionList.indexOf(CommandOpt.O.toString())) {
            mode = CommandMode.A_MODE;
        }

        String inputOpt = cliOptionList.get(cliOptionList.indexOf(CommandOpt.I.toString()) + 1);
        String[] convertOpts = cliOptionList.get(cliOptionList.indexOf(CommandOpt.C.toString()) + 1).split(",");

        try {
            ConfigRss config = new ConfigLoad().getPropValues();

            boolean isCut = Arrays.stream(convertOpts).anyMatch(COptions.CUT.toString()::equals);
            boolean isRep = Arrays.stream(convertOpts).anyMatch(COptions.CONVERT.toString()::equals);

            if (mode == CommandMode.A_MODE) {
                String outputOpt = cliOptionList.get(cliOptionList.indexOf(CommandOpt.O.toString()) + 1);
                new UrlRssReader().exec(config, inputOpt, outputOpt, isCut, isRep);
            } else {
                new FileRssReader().exec(config, inputOpt, isCut, isRep);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    protected static String CommandValidation(List<String> cliOptionList) {

        if (cliOptionList == null || cliOptionList.size() < 4) {
            return "-i or -c expects a configuration string";
        }

        if (cliOptionList.contains("-i") == false) {
            return "-i expects a configuration string";
        }
        if (cliOptionList.contains("-c") == false) {
            return "-c expects a configuration string";
        }
        int inputOptParamIndex = cliOptionList.indexOf(CommandOpt.I.toString()) + 1;
        int cutOptParamIndex = cliOptionList.indexOf(CommandOpt.C.toString()) + 1;

        String inputOptValue = cliOptionList.get(inputOptParamIndex);

        String[] cutOptValue = cliOptionList.get(cutOptParamIndex).split(",");
        boolean isCut = Arrays.stream(cutOptValue).anyMatch(COptions.CUT.toString()::equals);
        boolean isRep = Arrays.stream(cutOptValue).anyMatch(COptions.CONVERT.toString()::equals);
        if (isCut == false && isRep == false) {
            return "-c options " + COptions.CUT.toString() + "/" + COptions.CONVERT.toString();
        }

        if (isValidURL(inputOptValue)) {
            if (cliOptionList.contains(CommandOpt.O.toString()) == false) {
                return "-o expects a configuration string";
            }
            if (cliOptionList == null || cliOptionList.size() < 6) {
                return "-i or -c  or -o expects a configuration string";
            }
            int outOptParamIndex = cliOptionList.indexOf(CommandOpt.O.toString()) + 1;
            String outOptValue = cliOptionList.get(outOptParamIndex);
            if (outOptValue == null || outOptValue.isEmpty() || outOptValue.trim().isEmpty()) {
                return "-o expects a configuration string";
            }
        } else {
            File inputFile = new File(inputOptValue);
            if (inputFile.exists() == false) {
                return "-i File Not exists";
            }
        }

        return null;
    }

    public static boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
