package ttp.main;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import ttp.constructionheuristics.VirtualScheduleConstructionMethod;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TenFoldFiveMinuteRun {

    @Argument(required = true, index = 0, usage = "Problem instance directory (named files named dataXX.txt)")
    private File inputDirectory;

    @Argument(required = true, index = 1,
            usage = "Directory to write statistics to.")
    private File outputDirectory = null;

    public void run() throws Exception {
        List<TravelingTournamentProblem.Neighborhood> neighborhoods =
                new ArrayList<TravelingTournamentProblem.Neighborhood>();
        neighborhoods.add(TravelingTournamentProblem.Neighborhood.TWO_OPT_SWAP_ROUNDS);
        neighborhoods.add(TravelingTournamentProblem.Neighborhood.TWO_OPT_SWAP_TEAMS);
        neighborhoods.add(TravelingTournamentProblem.Neighborhood.SWAP_HOME_VISITOR);
        neighborhoods.add(TravelingTournamentProblem.Neighborhood.SHIFT_ROUND);
        neighborhoods.add(TravelingTournamentProblem.Neighborhood.SWAP_MATCH_ROUND);

        List<String> filesToRun = new ArrayList<String>();
        filesToRun.add("data4.txt");
        filesToRun.add("data6.txt");
        filesToRun.add("data8.txt");
        filesToRun.add("data10.txt");
        filesToRun.add("data12.txt");

        for (String fileToRun : filesToRun) {
            File instanceFile = new File(inputDirectory, fileToRun);
            File instanceOut = new File(outputDirectory, fileToRun + "_statistics");

            File sumFile = new File(instanceOut, instanceFile.getName() + "_tabulistlen_sum.csv");
            File avgFile = new File(instanceOut, instanceFile.getName() + "_tabulistlen_avg.csv");
            boolean headerWritten = false;

            for (int i = 0; i < 10; i++) {
                File subOutDir = new File(instanceOut, fileToRun + "_statistics_" + (i + 1));

                TravelingTournamentProblem travelingTournamentProblem = new TravelingTournamentProblem();
                TTPParameters parameters = new TTPParameters(TravelingTournamentProblem.Method.GRASP,
                        neighborhoods, TravelingTournamentProblem.ConstructionHeuristic.GRASP,
                        VirtualScheduleConstructionMethod.FIRSTPOLYGONTHENGREEK, 50, 40, 500, 40, instanceFile,
                        subOutDir, 5L * 60L * 1000L);
                TTPResult result = travelingTournamentProblem.run(parameters);

                if (!headerWritten) {
                    writeResultHeader(sumFile, "run", result);
                    writeResultHeader(avgFile, "run", result);
                    headerWritten = true;
                }

                writeResultSum(sumFile, "" + (i+1), result);
                writeResultAvg(avgFile, "" + (i+1), result);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        TenFoldFiveMinuteRun tenFoldFiveMinuteRun = new TenFoldFiveMinuteRun();
        CmdLineParser parser = new CmdLineParser(tenFoldFiveMinuteRun);
        parser.setUsageWidth(80); // width of the error display area

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java ttp.main.TravelingTournamentProblem [options...] arguments...");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();
            System.exit(1);
        }

        tenFoldFiveMinuteRun.run();
    }


    private static void writeResultHeader(File outputFile, String prefix, TTPResult result) throws IOException {
        if (!outputFile.exists()) {
            if (!outputFile.createNewFile()) {
                System.err.println("failed to create result file " + outputFile.getCanonicalPath());
                return;
            }
        }

        PrintWriter writer = new PrintWriter(outputFile, "utf-8");

        writer.print(prefix);
        writer.print(',');

        if (result.getLocalSearchStatistics() != null) {
            result.getLocalSearchStatistics().writeInformationHeader(writer);
        } else if (result.getSearchStatistics() != null) {
            if (result.getSearchStatistics().getSolution() == null)
                writer.println();
            else
                result.getSearchStatistics().writeStatisticsHeader(writer);
        }

        writer.close();
    }

    private static void writeResultSum(File outputFile, String prefix, TTPResult result) throws FileNotFoundException {
        if (!outputFile.exists()) return;

        FileOutputStream fso = new FileOutputStream(outputFile, true);
        PrintWriter writer = new PrintWriter(fso);

        writer.print(prefix);
        writer.print(',');

        if (result.getLocalSearchStatistics() != null) {
            result.getLocalSearchStatistics().writeInformation(writer);
        } else if (result.getSearchStatistics() != null) {
            result.getSearchStatistics().writeStatisticSum(writer);
        }

        writer.close();
    }

    private static void writeResultAvg(File outputFile, String prefix, TTPResult result) throws FileNotFoundException {
        if (!outputFile.exists()) return;

        FileOutputStream fso = new FileOutputStream(outputFile, true);
        PrintWriter writer = new PrintWriter(fso);

        writer.print(prefix);
        writer.print(',');

        if (result.getLocalSearchStatistics() != null) {
            result.getLocalSearchStatistics().writeInformation(writer);
        } else if (result.getSearchStatistics() != null) {
            result.getSearchStatistics().writeStatisticAverage(writer);
        }

        writer.close();
    }
}
