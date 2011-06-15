package ttp.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import ttp.model.TTPInstance;

public class TTPProblemInstanceReader {

	public static TTPInstance readProblemInstance(File file) throws Exception {

		int noTeams = 0;
		int row = 0;
		int[][] distances = null;

		BufferedReader rd = new BufferedReader(new FileReader(file));
		String line;
		while ((line = rd.readLine()) != null) {
			line = line.trim().replaceAll(" +", " ");
			if (!line.isEmpty()) {
				String[] dStrings = line.split(" ");
				if (noTeams == 0) {
					noTeams = dStrings.length;
					distances = new int[noTeams][noTeams];
				}

				for (int i = 0; i < noTeams; i++) {
					distances[row][i] = Integer.parseInt(dStrings[i]);
				}

				row++;
			}
		}
		rd.close();

		TTPInstance result = new TTPInstance();
		result.setD(distances);
		result.setNoTeams(noTeams);
		result.setL(1);
		result.setU(3);

		return result;
	}
}
